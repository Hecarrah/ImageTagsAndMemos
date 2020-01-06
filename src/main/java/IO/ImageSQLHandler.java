package IO;

import DataStructures.ImageWrapper;
import DataStructures.Tag;
import com.mysql.cj.jdbc.MysqlDataSource;
import Main.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import org.openimaj.io.IOUtils;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

public class ImageSQLHandler {

    private static Connection con;
    //private static Properties connectionProps = new Properties();
    static ResultSet imageQuery = null;
    static ResultSet tagQuery = null;
    static ResultSet thumbQuery = null;
    static Object[] filterArray = new Object[4];
    static int tableSize = 0;
    static ArrayList<ImageWrapper> thumbArray = new ArrayList<>();
    static ArrayList<String> imageNamesOrdered = new ArrayList<>();
    static PreparedStatement AddImageBatchStatement = null;
    static PreparedStatement AddImageTagBatchStatement = null;

    public static void main(String[] args) {
        try {
            createConnection();
            filterArray = Utils.Utils.initFilterArray();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.out.println(ex);
        }
    }

    public static void setTableSize(int size) {
        tableSize = size;
    }

    public static void createConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("java");
        dataSource.setPassword("java");
        dataSource.setDatabaseName("imagetags");
        dataSource.setPort(3307);
        dataSource.setServerTimezone("UTC");
        System.out.println(dataSource.getServerName());
        con = dataSource.getConnection();
        System.out.println(dataSource.toString());
        System.out.println("Connected");
    }

    public static Object[] getFilterArray() {
        return filterArray;
    }

    public static ResultSet getImageQuery() {
        return imageQuery;
    }

    public static void setImageQuery(ResultSet rs) {
        imageQuery = rs;
    }

    public static void processImageNamesToList() {
        ArrayList<String> list = new ArrayList<>();
        try {
            if (filterArray[0] == null) {
                filterArray = Utils.Utils.initFilterArray();
            }
            getFilteredImages(filterArray);
            imageQuery.beforeFirst();
            while (imageQuery.next()) {
                list.add(imageQuery.getString("path"));
            }
            imageQuery.beforeFirst();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        imageNamesOrdered = list;
        //return list;
    }

    public static ArrayList<String> getImageNamesList() {
        return imageNamesOrdered;
    }

    public static ResultSet getThumbQuery() {
        System.gc();
        return thumbQuery;
    }

    public static void setQueryPosition(int pos) {
        try {
            getImageQuery().absolute(pos);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static void addHistogramDatatoImage(ImageWrapper img) throws IOException {
        if (img.getPath().endsWith(".webm") || img.getPath().endsWith(".mp4") || img.getPath().endsWith(".gif")) {
            return;
        }
        try {
            PreparedStatement stmt = con.prepareStatement("UPDATE image SET Histogram = ? Where image.path = ?");
            stmt.closeOnCompletion();
            MultidimensionalHistogram histogram = Main.getImageProcessing().getHistogram(img);
            byte[] bytes = IOUtils.serialize(histogram);
            stmt.setString(2, img.getPath());
            stmt.setBytes(1, bytes);
            stmt.executeUpdate();
            bytes = null;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static MultidimensionalHistogram getHistogramDataFromImage(ImageWrapper img) {
        try {
            PreparedStatement stmt = con.prepareStatement("Select histogram FROM image where path = ?");
            stmt.closeOnCompletion();
            stmt.setString(1, img.getPath());
            ResultSet executeQuery = stmt.executeQuery();
            executeQuery.next();
            byte[] bytes = executeQuery.getBytes(1);
            MultidimensionalHistogram hist = new MultidimensionalHistogram(4);
            MultidimensionalHistogram deserialize = IOUtils.deserialize(bytes, hist);
            executeQuery.close();
            bytes = null;
            return deserialize;

        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (NullPointerException ex) {

        }
        return null;
    }

    public static void reAddHistogramsToAllImages() {
        try {
            ImageWrapper iw;
            getImages();
            imageQuery.beforeFirst();
            int count = 0;
            while (imageQuery.next()) {
                iw = new ImageWrapper(imageQuery.getString("Path"), null);
                addHistogramDatatoImage(iw);
                System.out.println("[" + count + " / " + tableSize + "] added Histogram to: " + iw.getPath());
                count++;
            }
            imageQuery.beforeFirst();

        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static ArrayList<ImageWrapper> getImagesWithSimilarHistogram(ImageWrapper img, double similarity) {
        try {
            boolean backup = false;
            //CachedRowSet backupImagesRS = RowSetProvider.newFactory().createCachedRowSet();
            ResultSet allImages = getAllImages();
            ImageWrapper iw;
            allImages.beforeFirst();
            ArrayList<ImageWrapper> images = new ArrayList<>();
            images.add(img);
            while (allImages.next()) {
                iw = new ImageWrapper(allImages.getString("Path"), null);
                double compareHistograms = Main.getImageProcessing().compareHistograms(img, iw);
                if (compareHistograms <= similarity) {
                    images.add(iw);
                }
            }
            allImages.beforeFirst();
            return images;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static void getHistogrammticallySimilarImagesToResultSet(ImageWrapper img, double similarity) {
        try {
            ArrayList<ImageWrapper> imagesWithSimilarHistogram = getImagesWithSimilarHistogram(img, similarity);
            StringBuilder base = new StringBuilder("Select distinct path, ThumbPath from image where path = ? ");
            String orImage = "OR path = ?";

            for (ImageWrapper wrap : imagesWithSimilarHistogram) {
                if (wrap.getPath() != null) {
                    base.append(orImage);
                }
            }
            PreparedStatement stmt = con.prepareStatement(base.toString());
            stmt.closeOnCompletion();
            stmt.setString(1, img.getPath());
            int index = 2;
            for (ImageWrapper wrap : imagesWithSimilarHistogram) {
                if (wrap.getPath() != null) {
                    stmt.setString(index, wrap.getPath());
                    index++;
                }
            }
            ResultSet executeQuery = stmt.executeQuery();
            imageQuery = executeQuery;
            imageQuery.first();
            int i = 1;
            while (imageQuery.next()) {
                i++;
            }
            tableSize = i;
            imageQuery.first();
            //Main.gui.getImagePanel().setCurrent(0);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public static ResultSet getHistogrammticallySimilarImagesToNewResultSet(ImageWrapper img, double similarity) {
        try {
            ArrayList<ImageWrapper> imagesWithSimilarHistogram = getImagesWithSimilarHistogram(img, similarity);
            StringBuilder base = new StringBuilder("Select distinct path, ThumbPath from image where path = ? ");
            String orImage = "OR path = ?";

            for (ImageWrapper wrap : imagesWithSimilarHistogram) {
                if (wrap.getPath() != null) {
                    base.append(orImage);
                }
            }
            PreparedStatement stmt = con.prepareStatement(base.toString());
            stmt.closeOnCompletion();
            stmt.setString(1, img.getPath());
            int index = 2;
            for (ImageWrapper wrap : imagesWithSimilarHistogram) {
                if (wrap.getPath() != null) {
                    stmt.setString(index, wrap.getPath());
                    index++;
                }
            }
            ResultSet executeQuery = stmt.executeQuery();
            return executeQuery;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static ResultSet getAllImages() {
        try {
            PreparedStatement stmt = con.prepareStatement("Select * from image");
            stmt.closeOnCompletion();
            ResultSet query = stmt.executeQuery();
            return query;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static ResultSet getTagQuery() {
        return tagQuery;
    }

    public static void addThumbnailPath(String imagePath, String thumbPath) {
        try {
            PreparedStatement stmt = con.prepareStatement("UPDATE image SET ThumbPath = ? Where image.path = ?");
            stmt.closeOnCompletion();
            stmt.setString(1, thumbPath);
            stmt.setString(2, imagePath);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static Object[] tagsAsList() {
        ArrayList a = new ArrayList<String>();
        ArrayList b = new ArrayList<Integer>();
        Object[] list = {a, b};
        int i = 0;
        try {
            tagQuery.first();
            while (tagQuery.next()) {
                a.add(tagQuery.getString(1));
                b.add(ImageSQLHandler.getTagCount(a.get(i).toString()));
                //b[i] = ImageSQLHandler.getTagCount(a[i]);
                i++;

            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    public static int getTableSize() {
        return tableSize - 1;
    }

    public static void addAll() {
        //addAllImages();
        // addAlltags();
        //addTagsToImages();
        //addAllTagtags();
    }

    public static void addAllImages(ArrayList<ImageWrapper> images) {
        for (ImageWrapper iw : images) {
            try {
                if (iw.getPath().matches("thumbs")) {
                    continue;
                }
                PreparedStatement stmt = con.prepareStatement("INSERT INTO image (name, path, filetype) VALUES (?, ?, ?)");
                stmt.closeOnCompletion();
                stmt.setString(1, Paths.get(iw.getPath()).getFileName().toString());
                stmt.setString(2, iw.getPath());
                stmt.setString(3, iw.getPath().substring(iw.getPath().lastIndexOf(".")));
                stmt.executeUpdate();
                System.out.println("added: " + iw.toString());

                addHistogramDatatoImage(iw);
            } catch (SQLException ex) {
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            }

        }
    }

    public static void addAlltags(ImageWrapper iw) {
        for (String s : iw.returnTags()) {
            try {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO tag (name) VALUES (?)");
                stmt.closeOnCompletion();
                stmt.setString(1, s);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

//    public static void addTag(String s) {
//        PreparedStatement existence;
//        try {
//            existence = con.prepareStatement("Select name from tag where name = ?");
//            existence.setString(1, s);
//            ResultSet existenceResult = existence.executeQuery();
//            if (existenceResult.next()) {
//                return;
//            }
//        } catch (SQLException ex) {
//        }
//        try {
//            PreparedStatement stmt = con.prepareStatement("INSERT INTO tag (name) VALUES (?)");
//            stmt.closeOnCompletion();
//            stmt.setString(1, s);
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            //System.out.println(ex);
//        }
//    }
    public static void removeTag(String s) {
        try {
            PreparedStatement stmt = con.prepareStatement("delete from tag where name = ? limit 1");
            stmt.closeOnCompletion();
            stmt.setString(1, s);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
//
//    public static void updateTagtags() {
//        try {
//            PreparedStatement stmt = con.prepareStatement(" INTO tag (name) VALUES (?)");
//            for (Tag t : Main.getTagList().getAllTags()) {
//                stmt.setString(1, t.getName());
//                stmt.executeUpdate();
//            }
//        } catch (SQLException ex) {
//        }
//    }

    public static void addTagsToImages(ArrayList<ImageWrapper> iwa) {
        for (ImageWrapper iw : iwa) {
            for (Tag t : iw.returnTagsArray()) {
                try {
                    System.out.println("Added: " + t + " to: " + iw.getPath());
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO imagetag (imageName,TagName) VALUES (?,?)");
                    stmt.closeOnCompletion();
                    stmt.setString(1, Paths.get(iw.getPath()).getFileName().toString());
                    stmt.setString(2, t.getName());
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println("addTagsToImages: " + ex);
                }
            }
        }
    }

    /*
    public static void addAllTagtags() {
        try {
            int weight = 0;
            double count = 0.0;
            while (tagQuery.next()) {
                while (tagQuery.next()) {
                    weight = 0;
                    try {
                        PreparedStatement countStatement = con.prepareStatement("Select count(*) from imagetag where tagName = ?");
                        countStatement.setString(1, t1.getName());
                        ResultSet countResult = countStatement.executeQuery();
                        countResult.next();
                        count = countResult.getDouble(1);
                    } catch (SQLException sQLException) {
                    }
                    try {
                        PreparedStatement weightStatement = con.prepareStatement("Select count(*) from imagetag t1 inner join imagetag t2 on t1.imageName = t2.imageName And t1.tagName = ? And t2.tagName = ?");
                        weightStatement.setString(1, t1.getName());
                        weightStatement.setString(2, t2.getName());
                        ResultSet weightResult = weightStatement.executeQuery();
                        weightResult.next();
                        weight = weightResult.getInt(1);
                    } catch (SQLException sQLException) {
                    }
                    try {
                        PreparedStatement stmt = con.prepareStatement("INSERT INTO tagtag (Tag1,tag2,Weight,WeightPerCount) VALUES (?,?,?,?)");
                        stmt.setString(1, t1.getName());
                        stmt.setString(2, t2.getName());
                        stmt.setInt(3, weight);
                        stmt.setDouble(4, ((weight / count) - 0 / (1 - 0)));
                        stmt.executeUpdate();
                    } catch (SQLException ex) {
                        //System.out.println(ex);
                    }
                    try {
                        PreparedStatement stmt = con.prepareStatement("Update tagtag Set weight = ?, WeightPerCount = ? where tag1 = ? AND tag2 = ?");
                        stmt.setInt(1, weight + 1);
                        stmt.setDouble(2, ((weight / count) - 0 / (1 - 0)));
                        stmt.setString(3, t1.getName());
                        stmt.setString(4, t2.getName());
                        stmt.executeUpdate();
                        // System.out.println("updated with" +weight+1);
                    } catch (SQLException ex) {
                        //  System.out.println("addAllTagtags: " + ex);
                    }
                }
                tagQ
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImageSQLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//*/
//    public static void addImageTags(ImageWrapper img) {
//        PreparedStatement stmt;
//        if (img == null) {
//            return;
//        }
//        for (String t : img.returnTags()) {
//            try {
//                stmt = con.prepareStatement("INSERT INTO imagetag (imageName,TagName) VALUES (?,?)");
//                stmt.closeOnCompletion();
//                stmt.setString(1, Paths.get(img.getPath()).getFileName().toString());
//                stmt.setString(2, t);
//                stmt.executeUpdate();
//                //System.out.println("added tag to sql");
//                getImageTags(img.);
//                stmt2 = con.prepareStatement(t)
//            } catch (SQLException ex) {
//                System.out.println(ex);
//            }
//        }
//    }
    public static ArrayList<String> getImageTags(String path) {
        ArrayList<String> tags = new ArrayList<>();
        PreparedStatement stmt;
        //for (Tag t : img.returnTags()) {
        try {
            stmt = con.prepareStatement("select distinct tagName from imageTag where imageName = ?");
            stmt.closeOnCompletion();
            stmt.setString(1, Paths.get(path).getFileName().toString());
            ResultSet executeQuery = stmt.executeQuery();
            while (executeQuery.next()) {
                tags.add(executeQuery.getString(1));
            }
            executeQuery.close();

        } catch (SQLException ex) {

        } catch (Exception ex) {

        }
        return tags;
    }

    public static ArrayList<Integer> getImageTagIDs(String path) {
        ArrayList<Integer> tags = new ArrayList<>();
        PreparedStatement stmt;
        //for (Tag t : img.returnTags()) {
        try {
            stmt = con.prepareStatement("select distinct ID from imageTag join tag on tagName = name where imageName = ?");
            stmt.closeOnCompletion();
            stmt.setString(1, Paths.get(path).getFileName().toString());
            ResultSet executeQuery = stmt.executeQuery();
            while (executeQuery.next()) {
                tags.add(executeQuery.getInt(1));
            }
            executeQuery.close();

        } catch (SQLException ex) {

        } catch (Exception ex) {

        }
        return tags;
    }

    public static int imageCount() {
        int count = -1;
        try {
            PreparedStatement stmt = con.prepareStatement("select count(*) from image");
            stmt.closeOnCompletion();
            ResultSet executeQuery = stmt.executeQuery();
            executeQuery.next();
            count = executeQuery.getInt(1);
            executeQuery.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return count;
    }

    public static void addImageTagExecuteBatch(PreparedStatement[] stmt) {
        if (stmt != null) {
            try {
                stmt[1].executeBatch();
            } catch (SQLException ex) {
                //System.out.println(ex);
            }
            try {
                stmt[0].executeBatch();
            } catch (SQLException ex) {

            } finally {
                try {
                    stmt[1].clearBatch();
                    stmt[0].clearBatch();

                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public static PreparedStatement[] addImageTagAddToBatch(ImageWrapper img, String t) {
        try {
            AddImageBatchStatement = con.prepareStatement("insert into tag (Name) values (?)");
            AddImageBatchStatement.closeOnCompletion();
            AddImageBatchStatement.setString(1, t);
            AddImageBatchStatement.addBatch();
        } catch (SQLException ex) {
        }
        try {
            AddImageTagBatchStatement = con.prepareStatement("insert into imagetag (imageName, tagName) values (?,?)");
            AddImageTagBatchStatement.closeOnCompletion();
            AddImageTagBatchStatement.setString(1, Paths.get(img.getPath()).getFileName().toString());
            AddImageTagBatchStatement.setString(2, t);
            AddImageTagBatchStatement.addBatch();
        } catch (SQLException ex) {
        }
        PreparedStatement[] ret = {AddImageBatchStatement, AddImageTagBatchStatement};
        return ret;
    }

    public static void RedoAllTagTags() {
        long startTime = System.currentTimeMillis();
        PreparedStatement stmt3 = null;
        PreparedStatement upstmt = null;
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> tags2 = new ArrayList<>();

        try {
            tagQuery.first();
            con.setAutoCommit(false);

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        try {
            while (tagQuery.next()) {
                tags.add(tagQuery.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        try {
            stmt3 = con.prepareStatement("insert into tagtag (tag1, tag2, Weight, WeightPerCount) values (?,?,?,?)");
            stmt3.closeOnCompletion();
            upstmt = con.prepareStatement("Update tagtag Set weight = ?, WeightPerCount = ? where tag1 = ? AND tag2 = ?");
            upstmt.closeOnCompletion();

            for (String s1 : tags) {
                tags2.clear();
                PreparedStatement secondTagGroup = con.prepareStatement("select distinct tagName from imageTag where tagName != ? AND imageName IN (\n"
                        + "select imageName from image join imageTag on name = imageName where tagName = ?)");
                secondTagGroup.closeOnCompletion();
                secondTagGroup.setString(1, s1);
                secondTagGroup.setString(2, s1);
                ResultSet executeQuery = secondTagGroup.executeQuery();
                try {
                    while (executeQuery.next()) {
                        tags2.add(executeQuery.getString(1));

                    }
                    executeQuery.close();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }

                System.out.println("TagTag s1: " + s1);
                for (String s2 : tags2) {
                    PreparedStatement countStatement = con.prepareStatement("Select count(*) from imagetag where tagName = ?");
                    countStatement.closeOnCompletion();
                    countStatement.setString(1, s1);
                    ResultSet countResult = countStatement.executeQuery();
                    countResult.next();
                    Double count = countResult.getDouble(1);

                    PreparedStatement weightStmt = con.prepareStatement("Select count(*) from imagetag t1 inner join imagetag t2 on t1.imageName = t2.imageName And t1.tagName = ? And t2.tagName = ?");
                    weightStmt.closeOnCompletion();
                    weightStmt.setString(1, s1);
                    weightStmt.setString(2, s2);

                    ResultSet weightResult = weightStmt.executeQuery();
                    weightResult.next();
                    int weight = weightResult.getInt(1);

                    PreparedStatement existence = con.prepareStatement("Select tag1,tag2 from tagtag where tag1 = ? AND tag2 = ?");
                    existence.closeOnCompletion();
                    existence.setString(1, s1);
                    existence.setString(2, s2);
                    ResultSet existenceResult = existence.executeQuery();

                    if (!existenceResult.next()) {
                        //System.out.println("does not exist: " + existenceResult.toString());
                        stmt3.clearParameters();
                        stmt3.setString(1, s1);
                        stmt3.setString(2, s2);
                        stmt3.setInt(3, weight);
                        stmt3.setDouble(4, ((weight / count) - 0.0 / (1.0 - 0.0)));
                        stmt3.addBatch();
                    } else {
                        //System.out.println("exists, updating");
                        upstmt.clearParameters();
                        upstmt.setString(3, s1);
                        upstmt.setString(4, s2);
                        upstmt.setInt(1, weight);
                        upstmt.setDouble(2, ((weight / count) - 0.0 / (1.0 - 0.0)));
                        upstmt.addBatch();
                    }
                    existenceResult.close();
                    weightResult.close();
                    countResult.close();
                }
            }
            if (stmt3 != null) {
                stmt3.executeBatch();
                stmt3.closeOnCompletion();
                System.out.println("stm3 batch");
            }
            if (upstmt != null) {
                upstmt.executeBatch();
                upstmt.closeOnCompletion();
                System.out.println("upstmt batch");
            }
            con.setAutoCommit(true);
        } catch (java.sql.SQLException ex) {
            System.out.println("shit went wrong.");
            System.out.println(ex);
        } finally {
            tags.clear();
            tags = null;
            System.out.println("Relation update finished, it took: " + (System.currentTimeMillis() - startTime) / 1000 + " seconds.");
            //System.gc();
        }
    }

    public static void removeImageTag(ImageWrapper img, String t) {
        try {
            PreparedStatement stmt = con.prepareStatement("delete from imagetag where imageName = ? and tagName = ? limit 1");
            stmt.closeOnCompletion();
            stmt.setString(1, Paths.get(img.getPath()).getFileName().toString());
            stmt.setString(2, t);
            stmt.executeUpdate();
        } catch (SQLException ex) {
        }
    }

    public static void getImages() {
        try {
            try {
                imageQuery.close();
            } catch (NullPointerException ex) {
            }
            PreparedStatement stmt = con.prepareStatement("Select name,path,thumbPath from image Order by path");
            stmt.closeOnCompletion();
            ResultSet query = stmt.executeQuery();
            query.next();
            imageQuery = query;
        } catch (SQLException ex) {
        }
    }

    public static void getThumbs() {
        try {
            if (thumbQuery != null) {
                thumbQuery.close();
            }
            PreparedStatement stmt = con.prepareStatement("Select thumbPath from image Order by name");
            stmt.closeOnCompletion();
            stmt.setFetchSize(8096);
            ResultSet query = stmt.executeQuery();
            //stmt.close();
            //query.next();
            thumbQuery = query;
        } catch (SQLException ex) {
        }
    }

    public static ArrayList<ImageWrapper> loadThumbs() {
        thumbArray.clear();
        File[] listThumbs = IO.listThumbs();
        for (File f : listThumbs) {
            String originalPath = Utils.Utils.thumbRenameBackwards(f.getName());
            ImageWrapper iw = new ImageWrapper(originalPath, f.toString());
            thumbArray.add(iw);
        }
        Collections.sort(thumbArray);
        return thumbArray;

        //thumbArray.addAll(Arrays.asList(IO.listThumbs()));
//            try {
//                //int i = 0;
//                ImageSQLHandler.getThumbs();
//                ResultSet thumbQuery = ImageSQLHandler.getThumbQuery();
//                thumbQuery.setFetchSize(8096);
//                long time = System.currentTimeMillis();
//                while (thumbQuery.next()) {
//                    thumbArray.add(ImageSQLHandler.getThumbQuery().getString("ThumbPath"));
//                    //System.out.println("loaded thumb: "+i);
//                    //i++;
//                }
//                System.out.println("Loading thumbs took: " + (System.currentTimeMillis() - time) / 1000);
//                return thumbArray;
//            } catch (SQLException ex) {
//                Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
//            }
        // }
        // return thumbArray;
    }

    public static void getTags() {
        try {
            try {
                tagQuery.close();
            } catch (NullPointerException ex) {
            }
//            PreparedStatement stmt = con.prepareStatement("Select Name from tag");
            PreparedStatement stmt = con.prepareStatement("Select distinct tag.Name, count(tagName) as tc from tag join imagetag on tag.name = imagetag.tagName group by tagName order by tc DESC");
            stmt.closeOnCompletion();
            ResultSet query = stmt.executeQuery();
            //stmt.close();
            query.next();
            tagQuery = query;
        } catch (SQLException ex) {
        }
    }

    public static void removeNonExisting() {
        try {
            imageQuery.beforeFirst();
            PreparedStatement stmt = con.prepareStatement("Delete From image WHERE path = ?");
            stmt.closeOnCompletion();
            while (imageQuery.next()) {
                File file = new File(imageQuery.getString("path"));
                String string = imageQuery.getString("path");
                if (!file.exists()) {
                    System.out.println(file.getName() + " No longer exists, deleting from database");
                    stmt.setString(1, string);
                    stmt.addBatch();
                }
            }
            stmt.executeBatch();
            imageQuery.beforeFirst();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

//    public static void loadAllImages() {
//        try {
//            ArrayList<String> names = new ArrayList<>();
//            ArrayList<String> paths = new ArrayList<>();
//            PreparedStatement stmt = con.prepareStatement("Select name,path from image");
//            ResultSet query = stmt.executeQuery();
//
//            while (query.next()) {
//                names.add(query.getString(1));
//                paths.add(query.getString(2));
//            }
//            stmt.close();
//            System.out.println(names.size());
//            System.out.println(names.get(1));
//
//            PreparedStatement stmt2 = con.prepareStatement("Select tagName from imagetag where imagename = ?");
//            for (String name : names) {
//                ArrayList<String> tags = new ArrayList<>();
//                stmt2.setString(1, name);
//                ResultSet query2 = stmt2.executeQuery();
//                while (query2.next()) {
//                    tags.add(query2.getString(1));
//                }
//                ImageWrapper wrapper = new ImageWrapper(paths.get(names.indexOf(name)), tags);
//            }
//        } catch (SQLException ex) {
//            System.out.println(ex);
//        }
//    }
    /**
     *
     * @param tags
     * @return [0] = name [1] = weight [2] = WeightPerCount
     */
    public static ArrayList[] getTagWeights(ArrayList<String> tags) {
        if (tags.isEmpty()) {
            return null;
        }
        //String base = "SELECT tag.name, tagtag.Weight, tagtag.WeightPerCount FROM tagtag right join tag on tagtag.tag1 = tag.Name OR tagtag.tag2 = tag.Name WHERE ";
        String base = "SELECT tag.name, tagtag.Weight, tagtag.WeightPerCount FROM tagtag right join tag on tagtag.tag2 = tag.Name WHERE ";
        String order = "Order by Weight DESC, WeightPerCount DESC";
        StringBuilder whereOr = new StringBuilder("(");
        StringBuilder whereNotAnd = new StringBuilder(") ");
        ArrayList<String> tagList = new ArrayList<>();
        ArrayList<Integer> weights = new ArrayList<>();
        ArrayList<Double> weights2 = new ArrayList<>();
        ArrayList[] combined = {tagList, weights, weights2};
        for (String t : tags) {
            if (whereOr.toString().equals("(")) {
                whereOr.append("tag1 = ? ");
                whereNotAnd.append(" AND tag.name != ? ");
            } else {
                whereOr.append("OR tag1 = ? ");
                whereNotAnd.append(" AND tag.name != ? ");
            }
        }
        try {
            PreparedStatement getWeights = con.prepareStatement(base + whereOr + whereNotAnd + order);
            getWeights.closeOnCompletion();
            for (int i = 0; i < tags.size(); i++) {
                getWeights.setString(i + 1, tags.get(i));
                getWeights.setString(i + 1 + tags.size(), tags.get(i));
            }
            //System.out.println(getWeights.toString());
            ResultSet executeQuery = getWeights.executeQuery();

            while (executeQuery.next()) {
                combined[0].add(executeQuery.getString("Name"));
                combined[1].add(executeQuery.getInt("Weight"));
                combined[2].add(executeQuery.getDouble("WeightPerCount"));
            }
            executeQuery.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return combined;
    }

//    public static ArrayList<String> similarBasedOnThreetags(ArrayList<String> tags) {
//        ArrayList<String> similarImages = new ArrayList<>();
//        String base = "SELECT distinct Image.path FROM imageTag left join image on imageName = name WHERE tagName = ? AND ImageName in (Select Distinct ImageName From ImageTag WHERE tagName = ?) AND ImageName in (Select Distinct ImageName From ImageTag WHERE tagName = ?) Limit 50";
//        try {
//            PreparedStatement stmt = con.prepareStatement(base);
//            stmt.setString(1, tags.get(0));
//            stmt.setString(2, tags.get(1));
//            stmt.setString(3, tags.get(2));
//            stmt.closeOnCompletion();
//            ResultSet executeQuery = stmt.executeQuery();
//
//            while (executeQuery.next()) {
//                similarImages.add(executeQuery.getString(1));
//            }
//        } catch (Exception ex) {
//        }
//        return similarImages;
//    }
    /**
     * @param tags Array of tags to be returned.
     * @return Paths of similar images.
     */
    public static ArrayList<String> getSimilarImagesBasedOnTags(ArrayList<String> tags) {
        ArrayList<String> similarImages = new ArrayList<>();
        String base = "SELECT distinct Image.path FROM imageTag left join image on imageName = name WHERE tagName = ? ";
        String additional = "AND ImageName in (Select Distinct ImageName From ImageTag WHERE tagName = ?) ";
        StringBuilder sb = new StringBuilder(base);
        int iterator = 0;
        try {
            for (int i = 0; i < tags.size() - 1; i++) {
                sb.append(additional);
            }
            PreparedStatement stmt = con.prepareStatement(sb.toString());
            stmt.closeOnCompletion();
            stmt.setString(iterator + 1, tags.get(iterator));
            for (int i = 0; i < tags.size() - 1; i++) {
                stmt.setString(iterator + 2, tags.get(iterator + 1));
                iterator++;
            }
            ResultSet executeQuery = stmt.executeQuery();
            while (executeQuery.next()) {
                similarImages.add(executeQuery.getString(1));
            }
            executeQuery.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return similarImages;
    }

    public static void addImplication(String from, String to) {
        String base = "insert into tag_Implications(Tag, ImpliedTag) values (?,?)";
        try {
            PreparedStatement getWeights = con.prepareStatement(base);
            getWeights.setString(1, from);
            getWeights.setString(2, to);
            getWeights.closeOnCompletion();
            getWeights.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static ArrayList<String> returnImplicationsForTag(String tag) {
        ArrayList<String> implications = new ArrayList<>();
        String base = "Select distinct ImpliedTag From tag_implications Where tag = ?";
        try {
            PreparedStatement getWeights = con.prepareStatement(base);
            getWeights.setString(1, tag);
            getWeights.closeOnCompletion();
            ResultSet executeQuery = getWeights.executeQuery();
            if (executeQuery.isAfterLast()) {
                return null;
            }
            while (executeQuery.next()) {
                implications.add(executeQuery.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return implications;
    }

    public static Object[] getTagWeight(String tag1, String tag2) {
        if (tag1 == null || tag2 == null) {
            return null;
        }
        String base = "SELECT tagtag.Weight, tagtag.WeightPerCount FROM tagtag WHERE tag1 = ? AND tag2 = ? ";
        Object[] ret = {0, 0.0};
        try {
            PreparedStatement getWeights = con.prepareStatement(base);
            getWeights.setString(1, tag1);
            getWeights.setString(2, tag2);
            getWeights.closeOnCompletion();
            ResultSet executeQuery = getWeights.executeQuery();
            while (executeQuery.next()) {
                ret[0] = executeQuery.getInt(1);
                ret[1] = executeQuery.getDouble(2);
            }
            executeQuery.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return ret;
    }

    public static int getTagCount(String tag1) {
        if (tag1 == null) {
            return 0;
        }
        String base = "SELECT count(imageName) from imageTag where tagName = ?";
        int ret = 0;
        try {
            PreparedStatement getWeights = con.prepareStatement(base);
            getWeights.setString(1, tag1);
            getWeights.closeOnCompletion();
            ResultSet executeQuery = getWeights.executeQuery();
            while (executeQuery.next()) {
                ret = executeQuery.getInt(1);
            }
            executeQuery.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return ret;
    }

    public static void removeOrphnaedTags() {
        try {
            PreparedStatement stmt = con.prepareStatement("Delete from tag where name not in (Select tagName from imageTag)");
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ImageSQLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param filter 1= suffix 2= 3=counts whiteList 4= blackList
     */
    public static void getFilteredImages(Object[] filter) {
        if (filter == null) {
            return;
        }
        filterArray = filter;
        StringBuilder base = new StringBuilder("Select Distinct name,path,ThumbPath from image ");
        String suffix = (String) filter[0];
        if (!suffix.equals("")) {
            base.append(" Where filetype = '").append(suffix).append("'");
        } else {
            base.append(" Where true ");
        }
        int[] counts = {(int) filter[1], (int) filter[2]};
        if (counts[1] == -1 || counts[0] == -1) {
            base.append("AND image.name in (Select image.name from Image left join imageTag on imageName = name group by name HAVING count(Imagename) < 1 )");
        } else {
            base.append(" AND image.name in (\n" + "Select ImageName from imageTag left join image on imageName = name " + "group by ImageName \n" + "HAVING count(TagName) BETWEEN ").append(counts[0]).append(" AND ").append(counts[1]).append(") ");
        }
        //System.out.println(base.toString());

        StringBuilder whereAnd = new StringBuilder("");
        StringBuilder whereAndNotIn = new StringBuilder("");

        ArrayList<String> tagList = (ArrayList<String>) filterArray[3];
        ArrayList<String> BLtagList = (ArrayList<String>) filterArray[4];
        tagList.stream().forEach((_item) -> {
            whereAnd.append(" AND image.Name IN (select image.name from image left join imageTag on image.Name = imageTag.imageName Where imageTag.tagName = ?) ");
        });
        BLtagList.stream().forEach((_item) -> {
            whereAndNotIn.append(" AND image.Name NOT IN (select image.name from image left join imageTag on image.Name = imageTag.imageName Where imageTag.tagName = ?) ");
        });
        try {
            PreparedStatement statement = con.prepareStatement(base.toString() + whereAnd.toString() + whereAndNotIn + " Order by Image.name");
            statement.closeOnCompletion();
            for (int i = 0; i < tagList.size(); i++) {
                statement.setString(i + 1, tagList.get(i));
            }
            for (int i = 0; i < BLtagList.size(); i++) {
                statement.setString(i + 1 + tagList.size(), BLtagList.get(i));
            }
            //System.out.println(statement.toString());
//            imageQuery.close();
            ResultSet executeQuery = statement.executeQuery();
            imageQuery = executeQuery;
            imageQuery.first();
            int i = 1;
            while (imageQuery.next()) {
                i++;
            }
            tableSize = i;
            imageQuery.first();
            //Main.getGui().getImagePanel().setCurrent(0);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }
}
