package IO;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotesSQLDataHandler {

    private static Connection con;

    private static ResultSet genRootNodes;

    public static void main(String[] args) {
        try {
            createConnection();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.out.println(ex);
        }
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

    public static ResultSet getNote(int id) {
        try {
            if (genRootNodes != null) {
                genRootNodes.close();
                genRootNodes = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM note where id = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            genRootNodes = stmt.executeQuery();

            return genRootNodes;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getRootNotes(String category) {
        try {
            if (genRootNodes != null) {
                genRootNodes.close();
                genRootNodes = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM note where category = '"+category+"' and TreeParent = -1");
            stmt.closeOnCompletion();

            genRootNodes = stmt.executeQuery();

            return genRootNodes;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getChildNotes(int id, String category) {
        try {
            PreparedStatement stmt = con.prepareStatement("Select * FROM note where category = '"+category+"' and TreeParent = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            ResultSet genChildNodes = stmt.executeQuery();

            return genChildNodes;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getRootNotesResultSet() {
        return genRootNodes;
    }

    public static void saveNote(int id, String type, String note, int treeParent, String category) {
        try {
            PreparedStatement existence = con.prepareStatement("Select id from note where id = ?");
            existence.setInt(1, id);
            ResultSet exQuery = existence.executeQuery();
            if (exQuery.absolute(1)) {
                PreparedStatement stmt = con.prepareStatement("UPDATE Note SET note = ?, type = ?, treeParent = ? where id = ?");
                stmt.setString(1, note);
                stmt.setString(2, type);
                stmt.setInt(3, treeParent);
                stmt.setInt(4, id);
                stmt.executeUpdate();
            } else {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO Note (category, Type, Note, TreeParent) VALUES ('"+category+"',?,?,?)");
                stmt.setString(1, type);
                stmt.setString(2, note);
                stmt.setInt(3, treeParent);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteNote(int id) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM note where id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
