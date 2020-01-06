package IO;

import Gui.ImageViewer;
import DataStructures.ImageList;
import DataStructures.ImageWrapper;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Peter
 */
public class IO {

    static Path pathPath = Paths.get("G:/Library/E/Single Images/Sorting Batches");
    static Path thumbFolder = Paths.get("G:/Library/E/Single Images/Sorting Batches/thumbs");
    static File rootFolder = new File(pathPath.toUri());

    public static  File[] listThumbs(){
        File[] listFiles = thumbFolder.toFile().listFiles();
        return listFiles;
    }
    public static File findThumbnailFor(String image){
        String thumbnailName = Utils.Utils.thumbRename(image);
        return (new File(thumbFolder.toString() +"/"+ thumbnailName));
    }
    public static void save() {
//        saveTags();
//        saveImageXmls();
        System.out.println("SAVED!");
    }

    public static void load() {
//        loadTags();
        loadImageXml();
    }
    
    public static Path getThumbFolder(){
        return thumbFolder;
    }

//    public static void saveTags() {
//        try {
//            JAXBContext context = JAXBContext.newInstance(Tags.class);
//            Marshaller marshaller = context.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            //marshaller.marshal(Data.getTagList(), System.out);
//
//            Writer writer = null;
//            File file = new File("Data/Xml/Tags/Tags.xml");
//            if (!file.exists()) {
//                try {
//                    file.createNewFile();
//                } catch (IOException ex) {
//                    Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            try {
//                writer = new FileWriter("Data/Xml/Tags/Tags.xml");
//                marshaller.marshal(Data.getTagList(), writer);
//            } finally {
//                if (writer != null) {
//                    writer.close();
//                }
//            }
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void saveImageXmls() {
//        try {
//            JAXBContext context = JAXBContext.newInstance(ImageList.class);
//            Marshaller marshaller = context.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            //marshaller.marshal(Data.getImageList(), System.out);
//
//            Writer writer = null;
//            File file = new File("Data/Xml/Images/ImageTagWrapper.xml");
//            if (!file.exists()) {
//                try {
//                    file.createNewFile();
//                } catch (IOException ex) {
//                    Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            try {
//                writer = new FileWriter("Data/Xml/Images/ImageTagWrapper.xml");
//                marshaller.marshal(Data.getImageList(), writer);
//            } finally {
//                if (writer != null) {
//                    writer.close();
//                }
//            }
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void loadTags() {
//        File file = new File("Data/Xml/Tags/Tags.xml");
//        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance(Tags.class);
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            Data.setTagList((Tags) jaxbUnmarshaller.unmarshal(file));
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }
    public static void loadImageXml() {
        File file = new File("Data/Xml/Images/ImageTagWrapper.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ImageList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ImageList il = new ImageList();
            il = (ImageList) jaxbUnmarshaller.unmarshal(file);
            ImageSQLHandler.addTagsToImages(il.getAllImages());
        } catch (JAXBException e) {
            System.out.println("couldn't load xml" + e);
        }
    }

    public static void rescanImages() {
        ImageSQLHandler.getImages();
        ResultSet imageQuery = ImageSQLHandler.getImageQuery();
        ArrayList<String> images = new ArrayList<>();
        ArrayList<ImageWrapper> newimages = new ArrayList<>();
        int count = 0;
        try {
            imageQuery.beforeFirst();
            while (imageQuery.next()) {
                images.add(imageQuery.getString(1));
            }
        } catch (SQLException ex) {
        }
        try {
            for (File f : rootFolder.listFiles()) {
                if (!images.contains(f.toPath().getFileName().toString()) && !f.getName().equals("thumbs")) {
                    ImageWrapper wrapper = new ImageWrapper(f.toPath().toString(), null);
                    newimages.add(wrapper);
                    System.out.println("found: " + f.getPath());
                    ThumbIO.createThumbFile(f.getPath(), true);
                    count++;
                }
            }
        } catch (Exception ex) {
        }
        System.out.println("found total of: " +count+ " new images.");
        try {
            imageQuery.first();
            ImageSQLHandler.addAllImages(newimages);
        } catch (SQLException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//
//    public static void loadAllImagesAgain() {
//        for (File f : rootFolder.listFiles()) {
//            Data.getImageList().add(new ImageWrapper(f.getPath()));
//            System.out.println("found " + f.getPath());
//        }
//    }
}
