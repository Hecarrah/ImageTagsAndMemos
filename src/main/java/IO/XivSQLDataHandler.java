package IO;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XivSQLDataHandler {

    private static Connection con;
    private static ResultSet xivCharacters;
    private static ResultSet xivCharacterNotes;
    private static ResultSet xivCCConnections;
    private static ResultSet xivCGConnections;
    private static ResultSet xivCCConnectionNote;
    private static ResultSet xivCGConnectionNote;

    private static ResultSet xivGroups;
    private static ResultSet xivGroupNotes;
    private static ResultSet xivGroupMembers;

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

    public static ResultSet getXivGroups() {
        try {
            if (xivGroups != null) {
                xivGroups.close();
                xivGroups = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM xiv_group");
            stmt.closeOnCompletion();

            xivGroups = stmt.executeQuery();

            return xivGroups;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getXivGroupsResultSet() {
        return xivGroups;
    }

    public static ResultSet getXivCharacters() {
        try {
            if (xivCharacters != null) {
                xivCharacters.close();
                xivCharacters = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM xiv_character");
            stmt.closeOnCompletion();

            xivCharacters = stmt.executeQuery();

            return xivCharacters;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getXivCharactersResultSet() {
        return xivCharacters;
    }

    public static ResultSet getXivCharacterNotes(int id) {
        try {
            if (xivCharacterNotes != null) {
                xivCharacterNotes.close();
                xivCharacterNotes = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM note where xiv_id = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            xivCharacterNotes = stmt.executeQuery();
            return xivCharacterNotes;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void setCharacterNote(int id, String type, String note) {
        try {
            PreparedStatement existence = con.prepareStatement("Select id from note where xiv_id = ? and type = ?");
            existence.setInt(1, id);
            existence.setString(2, type);
            ResultSet exQuery = existence.executeQuery();
            if (exQuery.absolute(1)) {
                PreparedStatement stmt = con.prepareStatement("UPDATE Note SET note = ? where xiv_id = ? and type = ?");
                stmt.setInt(2, id);
                stmt.setString(3, type);
                stmt.setString(1, note);
                stmt.executeUpdate();
            } else {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO Note (xiv_id, Type, Note) VALUES (?,?,?)");
                stmt.setInt(1, id);
                stmt.setString(2, type);
                stmt.setString(3, note);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveCharacter(int id, String lodestone, String server, String name, String race, String subrace, String sex, String job, String FC) {
        try {
            if (id > -1) {
                PreparedStatement existence = con.prepareStatement("Select id from xiv_character where id = ?");
                existence.setInt(1, id);
                ResultSet exQuery = existence.executeQuery();

                if (exQuery.absolute(1)) {
                    PreparedStatement stmt = con.prepareStatement("UPDATE xiv_character SET lodestone_url = ?, server = ?, name = ?, race = ?, subrace = ?, sex = ?, mainjob = ?, fc = ? where id = ?");
                    stmt.setString(1, lodestone);
                    stmt.setString(2, server);
                    stmt.setString(3, name);
                    stmt.setString(4, race);
                    stmt.setString(5, subrace);
                    stmt.setString(6, sex);
                    stmt.setString(7, job);
                    stmt.setString(8, FC);

                    stmt.setInt(9, id);
                    stmt.executeUpdate();
                } else {
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO xiv_character (lodestone_url, server, name, race, subrace, sex, mainjob, fc, id) VALUES (?,?,?,?,?,?,?,?,?)");
                    stmt.setString(1, lodestone);
                    stmt.setString(2, server);
                    stmt.setString(3, name);
                    stmt.setString(4, race);
                    stmt.setString(5, subrace);
                    stmt.setString(6, sex);
                    stmt.setString(7, job);
                    stmt.setString(8, FC);

                    stmt.setInt(9, id);
                    stmt.executeUpdate();
                }
            } else {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO xiv_character (lodestone_url, server, name, race, subrace, sex, mainjob, fc) VALUES (?,?,?,?,?,?,?,?)");
                stmt.setString(1, lodestone);
                stmt.setString(2, server);
                stmt.setString(3, name);
                stmt.setString(4, race);
                stmt.setString(5, subrace);
                stmt.setString(6, sex);
                stmt.setString(7, job);
                stmt.setString(8, FC);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteCharcater(int id) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM xiv_character where id = ?");
            stmt.setInt(1, id);

            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveGroup(int id, String name, String link) {
        try {
            if (id > -1) {
                PreparedStatement existence = con.prepareStatement("Select id from xiv_group where id = ?");
                existence.setInt(1, id);
                ResultSet exQuery = existence.executeQuery();

                if (exQuery.absolute(1)) {
                    PreparedStatement stmt = con.prepareStatement("UPDATE xiv_group SET name = ?, link = ? where id = ?");
                    stmt.setString(1, name);
                    stmt.setString(2, link);

                    stmt.setInt(3, id);
                    stmt.executeUpdate();
                } else {
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO xiv_group (name, link, id) VALUES (?,?, ?)");
                    stmt.setString(1, name);
                    stmt.setString(2, link);

                    stmt.setInt(3, id);
                    stmt.executeUpdate();
                }
            } else {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO xiv_group (name, link) VALUES (?,?)");
                stmt.setString(1, name);
                stmt.setString(2, link);

                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteGroup(int id) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM xiv_group where id = ?");
            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addCharacterToGroup(int group_id, int char_id) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO xiv_characterGroup (group_id, character_id) VALUES (?,?)");
            stmt.closeOnCompletion();
            stmt.setInt(1, group_id);
            stmt.setInt(2, char_id);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeCharacterFromGroup(int group_id, int char_id) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM xiv_characterGroup WHERE group_id = ? and character_id = ?  ");
            stmt.closeOnCompletion();
            stmt.setInt(1, group_id);
            stmt.setInt(2, char_id);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addCharacterToCharacterConnection(int char1, int char2, String label, Boolean directed) {
        int id = -1;
        try {
            PreparedStatement existence = con.prepareStatement("SELECT id FROM xiv_characterCharacter where char_id_1 = ? and char_id_2 = ? and label = ?");
            existence.closeOnCompletion();
            existence.setInt(1, char1);
            existence.setInt(2, char2);
            existence.setString(3, label);
            ResultSet existenceQuery = existence.executeQuery();
            if (existenceQuery.absolute(1)) {
                id = existenceQuery.getInt("ID");
            }
            if (id == -1) {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO xiv_characterCharacter (char_id_1, char_id_2, label) VALUES (?,?,?)");
                stmt.closeOnCompletion();
                stmt.setInt(1, char1);
                stmt.setInt(2, char2);
                stmt.setString(3, label);
                stmt.executeUpdate();
                if (!directed) {
                    PreparedStatement stmt2 = con.prepareStatement("INSERT INTO xiv_characterCharacter (char_id_1, char_id_2, label) VALUES (?,?,?)");
                    stmt2.closeOnCompletion();
                    stmt2.setInt(2, char1);
                    stmt2.setInt(1, char2);
                    stmt2.setString(3, label);
                    stmt2.executeUpdate();
                }
            } else {
                try {
                    PreparedStatement stmt = con.prepareStatement("UPDATE xiv_characterCharacter SET char_id_1 = ?, char_id_2 = ?, label = ? where id = ?");
                    stmt.closeOnCompletion();
                    stmt.setInt(1, char1);
                    stmt.setInt(2, char2);
                    stmt.setString(3, label);
                    stmt.setInt(4, id);
                    stmt.executeUpdate();
                    if (!directed) {
                        PreparedStatement stmt2 = con.prepareStatement("UPDATE xiv_characterCharacter SET char_id_1 = ?, char_id_2 = ?, label = ? where id = ?");
                        stmt2.closeOnCompletion();
                        stmt2.setInt(2, char1);
                        stmt2.setInt(1, char2);
                        stmt2.setString(3, label);
                        stmt2.setInt(4, id);
                        stmt2.executeUpdate();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeCharacterToCharacterConnection(int char1, int char2, String label) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM xiv_characterCharacter WHERE char_id_1 = ? and char_id_2 = ? and label = ?");
            stmt.closeOnCompletion();
            stmt.setInt(1, char1);
            stmt.setInt(2, char2);
            stmt.setString(3, label);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteCharacterNote(int id, String type) {
        try {
            PreparedStatement existence = con.prepareStatement("Select id from note where xiv_id = ? and type = ?");
            existence.setInt(1, id);
            existence.setString(2, type);
            ResultSet exQuery = existence.executeQuery();
            if (exQuery.absolute(1)) {
                PreparedStatement stmt = con.prepareStatement("DELETE FROM Note WHERE xiv_id = ? and type = ?");
                stmt.setInt(1, id);
                stmt.setString(2, type);
                stmt.executeUpdate();
            } else {
                System.out.println("Note not found.");

            }
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet getXivCharacterCharacterConnections(int id) {
        try {
            if (xivCCConnections != null) {
                xivCCConnections.close();
                xivCCConnections = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM xiv_charactercharacter left join xiv_character on char_id_2 = xiv_character.id where char_id_1 = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            xivCCConnections = stmt.executeQuery();

            return xivCCConnections;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getXivCharacterGroupConnections(int id) {
        try {
            if (xivCGConnections != null) {
                xivCGConnections.close();
                xivCGConnections = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM xiv_charactergroup left join xiv_group on group_id = xiv_group.id where character_id = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            xivCGConnections = stmt.executeQuery();

            return xivCGConnections;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getXivCharacterCharacterConnectionNotes(int id) {
        try {
            if (xivCCConnectionNote != null) {
                xivCCConnectionNote.close();
                xivCCConnectionNote = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM xiv_charactercharacter left join note on xiv_charactercharacter.id = xiv_cc_id where char_id_1 = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            xivCCConnectionNote = stmt.executeQuery();

            return xivCCConnectionNote;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getXivCharacterGroupConnectionNotes(int id) {
        try {
            if (xivCGConnectionNote != null) {
                xivCGConnectionNote.close();
                xivCGConnectionNote = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM xiv_charactergroup left join note on xiv_charactergroup.id = xiv_cg_id where character_id = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            xivCGConnectionNote = stmt.executeQuery();

            return xivCGConnectionNote;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getXivGroupNotes(int id) {
        try {
            if (xivGroupNotes != null) {
                xivGroupNotes.close();
                xivGroupNotes = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM note where xiv_grp_id = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            xivGroupNotes = stmt.executeQuery();

            return xivGroupNotes;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void setXivGroupNote(int charGrpID, String type, String text) {
        int id = -1;
        try {
            PreparedStatement existence = con.prepareStatement("SELECT id FROM note where xiv_grp_id = ? and type = ?");
            existence.closeOnCompletion();
            existence.setInt(1, charGrpID);
            existence.setString(2, type);
            ResultSet existenceQuery = existence.executeQuery();
            if (existenceQuery.absolute(1)) {
                id = existenceQuery.getInt("ID");
            }
            if (id == -1) {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO note (xiv_grp_id, type, Note) VALUES (?,?,?)");
                stmt.closeOnCompletion();
                stmt.setInt(1, charGrpID);
                stmt.setString(2, type);
                stmt.setString(3, text);
                stmt.executeUpdate();
            } else {
                try {
                    PreparedStatement stmt = con.prepareStatement("UPDATE note SET Note = ?, xiv_grp_id = ?, type = ? where id = ?");
                    stmt.closeOnCompletion();
                    stmt.setString(1, text);
                    stmt.setInt(2, charGrpID);
                    stmt.setString(3, type);
                    stmt.setInt(4, id);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        public static void deleteGroupNote(int id, String type) {
        try {
            PreparedStatement existence = con.prepareStatement("Select id from note where xiv_grp_id = ? and type = ?");
            existence.setInt(1, id);
            existence.setString(2, type);
            ResultSet exQuery = existence.executeQuery();
            if (exQuery.absolute(1)) {
                PreparedStatement stmt = con.prepareStatement("DELETE FROM Note WHERE xiv_grp_id = ? and type = ?");
                stmt.setInt(1, id);
                stmt.setString(2, type);
                stmt.executeUpdate();
            } else {
                System.out.println("Note not found.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet getXivGroupMembers(int id) {
        try {
            if (xivGroupMembers != null) {
                xivGroupMembers.close();
                xivGroupMembers = null;
            }
            PreparedStatement stmt = con.prepareStatement("Select * FROM xiv_charactergroup left join xiv_character on character_id = xiv_character.id where group_id = ?");
            stmt.setInt(1, id);
            stmt.closeOnCompletion();

            xivGroupMembers = stmt.executeQuery();

            return xivGroupMembers;

        } catch (SQLException ex) {
            Logger.getLogger(XivSQLDataHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet getXivCharacterNotesResultSet() {
        return xivCharacterNotes;
    }

    public static ResultSet getXivGroupNotesResultSet() {
        return xivGroupNotes;
    }

    public static ResultSet getXivGroupMembersResultSet() {
        return xivGroupMembers;
    }

    public static ResultSet getXivCharacterGroupConnectionGroupResultSet() {
        return xivCGConnections;
    }

}
