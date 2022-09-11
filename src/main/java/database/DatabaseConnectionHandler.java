package database;

import communitygarden.model.Garden;
import forum.model.ForumModel;
import forum.model.PostModel;
import forum.model.ThreadModel;
import plant.model.*;
import util.PrintablePreparedStatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all database related transactions
 * SOURCE: https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project/blob/master/src/ca/ubc/cs304/database/DatabaseConnectionHandler.java
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            // SEE HERE: https://blogs.oracle.com/developers/post/adding-the-oracle-jdbc-drivers-and-oracle-ucp-to-a-java-project
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    /**
     * PLANT LOOKUP QUERIES (BEGIN)
     */
    public SoilModel[] getSoilInfo(String q) {
        ArrayList<SoilModel> result = new ArrayList<>();

        try {
            String query = q;

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                SoilModel model = new SoilModel(rs.getString("type"),
                        rs.getString("alkalinity"),
                        rs.getString("aeration"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new SoilModel[result.size()]);
    }

    public ThrivesInModel[] getThrivesInModel(String q) {
        ArrayList<ThrivesInModel> result = new ArrayList<>();

        try {
            String query = q;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                ThrivesInModel model = new ThrivesInModel(
                        rs.getString("name"),
                        null,
                        null,
                        null);
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ThrivesInModel[result.size()]);
    }
    public ThrivesInModel[] getThrivesInModelGroupByName(String q, boolean count, boolean min, boolean max) {
        ArrayList<ThrivesInModel> result = new ArrayList<>();

        try {
            String query = "select name";
            if (count) {
                query += ", count(zonenum)";
            }
            if (min) {
                query += ", min(zonenum)";
            }
            if (max) {
                query += ", max(zonenum)";
            }
            query += q;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name");
                Integer countZone = null;
                Integer minZone = null;
                Integer maxZone = null;
                if (count) {
                    countZone = rs.getInt("count(zonenum)");
                }
                if (min) {
                    minZone = rs.getInt("min(zonenum)");
                }
                if (max) {
                    maxZone = rs.getInt("max(zonenum)");
                }

                ThrivesInModel model = new ThrivesInModel(name, countZone, minZone, maxZone);
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ThrivesInModel[result.size()]);
    }

    public SuppliersModel[] getSuppliersInfo(String q) {
        ArrayList<SuppliersModel> result = new ArrayList<>();

        try {
            String query = q;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                SuppliersModel model = new SuppliersModel(
                        rs.getString("BUSINESSNAME"),
                        rs.getInt("HOUSENUM"),
                        rs.getString("STREET"),
                        rs.getString("POSTALCODE"),
                        rs.getString("CITY"),
                        rs.getString("PROVINCE"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new SuppliersModel[result.size()]);
    }

    public FertilizerModel[] getFertilizerInfo(String q, boolean npk, boolean type, boolean details) {
        ArrayList<FertilizerModel> result = new ArrayList<>();

        try {
            String query = "select FERTILIZER.NAME";
            if (npk) {
                query += ", FERTILIZER.NPKRATIO";
            }
            if (type) {
                query += ", FERTILIZER.TYPE";
            }
            if (details) {
                query += ", FERTILIZER.DESCRIPTION";
            }
            query += q;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String nameString = rs.getString("name");
                String npkString = null;
                String typeString = null;
                String detailsString = null;
                if (npk) {
                    npkString = rs.getString("npkratio");
                }
                if (type) {
                    typeString = rs.getString("type");
                }
                if (details) {
                    detailsString = rs.getString("description");
                }
                FertilizerModel model = new FertilizerModel(nameString,npkString,typeString,detailsString);
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new FertilizerModel[result.size()]);
    }

    public AnnualModel[] getAnnualInfo(String q) {
        ArrayList<AnnualModel> result = new ArrayList<>();

        try {
            String query = "select *" +
                    " from plant, annual" +
                    " where annual.name=plant.name" +
                    " and plant.name in" +
                    " (";
            query += q + ")";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                AnnualModel model = new AnnualModel(
                        rs.getString("name"),
                        rs.getByte("sowingstartdate"),
                        rs.getByte("sowingenddate"),
                        rs.getByte("plantstartdate"),
                        rs.getByte("plantenddate"),
                        rs.getByte("harveststartdate"),
                        rs.getByte("harvestenddate"),
                        rs.getByte("dormancystartdate"),
                        rs.getByte("dormancyenddate"),
                        rs.getString("water"),
                        rs.getString("fertilization"),
                        rs.getString("shade"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new AnnualModel[result.size()]);
    }

    public PerennialModel[] getPerennialInfo(String q) {
        ArrayList<PerennialModel> result = new ArrayList<>();

        try {
            String query = "select *" +
                    " from plant, perennial" +
                    " where perennial.name=plant.name" +
                    " and plant.name in" +
                    " (";
            query += q + ")";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PerennialModel model = new PerennialModel(
                        rs.getString("name"),
                        rs.getByte("sowingstartdate"),
                        rs.getByte("sowingenddate"),
                        rs.getByte("plantstartdate"),
                        rs.getByte("plantenddate"),
                        rs.getByte("harveststartdate"),
                        rs.getByte("harvestenddate"),
                        rs.getByte("dormancystartdate"),
                        rs.getByte("dormancyenddate"),
                        rs.getString("water"),
                        rs.getString("fertilization"),
                        rs.getString("shade"),
                        rs.getInt("lifespanyears"),
                        rs.getByte("pruningdate"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new PerennialModel[result.size()]);
    }

    public BiennialModel[] getBiennialInfo(String q) {
        ArrayList<BiennialModel> result = new ArrayList<>();

        try {
            String query = "select *" +
                    " from plant, biennial" +
                    " where biennial.name=plant.name" +
                    " and plant.name in" +
                    " (";
            query += q + ")";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                BiennialModel model = new BiennialModel(
                        rs.getString("name"),
                        rs.getByte("sowingstartdate"),
                        rs.getByte("sowingenddate"),
                        rs.getByte("plantstartdate"),
                        rs.getByte("plantenddate"),
                        rs.getByte("harveststartdate"),
                        rs.getByte("harvestenddate"),
                        rs.getByte("dormancystartdate"),
                        rs.getByte("dormancyenddate"),
                        rs.getString("water"),
                        rs.getString("fertilization"),
                        rs.getString("shade"),
                        rs.getInt("vernalizationtemp"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new BiennialModel[result.size()]);
    }
    /**
     * PLANT LOOKUP QUERIES (END)
     */

    /**
     * COMMUNITY GARDEN QUERIES (BEGIN)
     */
    public List<Garden> getGardenInfo() {
        List<Garden> gardens = new ArrayList<>();
        try {
            String query = "SELECT G.GARDENID, G.NAME, G.HOUSENUM, G.STREET, CITY, PROVINCE, G.POSTALCODE\n" +
                    "FROM COMMUNITYGARDEN_LOCATEDIN G\n" +
                    "INNER JOIN LOCATION_POSTALCODECITY LP on G.POSTALCODE = LP.POSTALCODE\n" +
                    "INNER JOIN LOCATION_POSTALCODEPROVINCE P on LP.POSTALCODE = P.POSTALCODE";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int gardenID = rs.getInt("GARDENID");
                String gardenName = rs.getString("NAME");
                int gardenHouseNum = rs.getInt("HOUSENUM");
                String gardenStreet = rs.getString("STREET");
                String gardenCity = rs.getString("CITY");
                String gardenProvince = rs.getString("PROVINCE");
                String gardenPostalCode = rs.getString("POSTALCODE");
                Garden garden = new Garden(gardenID, gardenName, gardenHouseNum, gardenStreet, gardenCity, gardenProvince, gardenPostalCode);
                gardens.add(garden);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return gardens;
    }
    public void insertGarden(String gardenName, String username) {
        try {
            String gardenIDQuery = "SELECT GARDENID FROM COMMUNITYGARDEN_LOCATEDIN WHERE NAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(gardenIDQuery), gardenIDQuery, false);
            ps.setString(1, gardenName);
            ResultSet rs = ps.executeQuery();
            int gardenID = -1;
            while(rs.next()) {
                gardenID = rs.getInt("GARDENID");
            }
            // Could not find the GardenID to add to this user
            if (gardenID == -1) return;
            // Actually update the Uses table
            String query = "INSERT INTO Uses VALUES(?, ?)";
            ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, username);
            ps.setInt(2, gardenID);
            ps.executeUpdate();
            connection.commit();
            System.out.println("Added " + gardenID + " to user!");
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
    /**
     * COMMUNITY GARDEN QUERIES (END)
     */

    /**
     * FORUM QUERIES (BEGIN)
     */
    public ForumModel getForumInfo(String username) {
        ForumModel forum = new ForumModel();
        try {
            String query = "SELECT POSTID, NOTES, POSTDATE, USERNAME, DECODE(USERNAME, ?, 'Y', 'N') MY_POST, F.SUBTOPIC, F.FORUMID, TOPIC, DECODE(USERNAME, ?, DECODE(POSTID, 1, 'Y', 'N'), 'N') MY_THREAD\n" +
                    "FROM POSTS_POPULATE_POSTSON\n" +
                    "         INNER JOIN FORUM F on POSTS_POPULATE_POSTSON.FORUMID = F.FORUMID\n" +
                    "         INNER JOIN FORUM_SUBTOPICTOPIC FS on FS.SUBTOPIC = F.SUBTOPIC\n" +
                    "ORDER BY F.FORUMID, POSTID";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, username);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int postID = rs.getInt("POSTID");
                String postNotes = rs.getString("NOTES");
                Timestamp postDate = rs.getTimestamp("POSTDATE");
                String postUser = rs.getString("USERNAME");
                String postOwnership = rs.getString("MY_POST");
                String threadName = rs.getString("SUBTOPIC");
                String threadOwnership = rs.getString("MY_THREAD");
                int forumID = rs.getInt("FORUMID");
                String forumTopic = rs.getString("TOPIC");
                PostModel post = new PostModel(postID, postNotes, postDate, postUser, postOwnership);
                ThreadModel thread;
                if (forum.threadExistsInForum(forumTopic, threadName)) {
                    // Add post to existing thread
                    thread = forum.getThreadInForum(forumTopic,threadName);
                    thread.addPost(post);
                } else {
                    // New thread and post
                    thread = new ThreadModel(threadName, threadOwnership);
                    thread.addPost(post);
                    forum.addThread(forumTopic, thread);
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return forum;
    }

    public void insertPost(String threadName, String reply, String username) {
        try {
            String forumIDquery = "SELECT FORUMID FROM FORUM WHERE SUBTOPIC = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(forumIDquery), forumIDquery, false);
            ps.setString(1, threadName);
            ResultSet rs = ps.executeQuery();
            int forumID = -1;
            while(rs.next()) {
                forumID = rs.getInt("FORUMID");
            }
            // Could not find the forumID to add to this user
            if (forumID == -1) return;
            // Actually update the Uses table
            // TODO: Replace this with the actual user
            String query = "INSERT INTO POSTS_POPULATE_POSTSON(NOTES, FORUMID, USERNAME) VALUES(?, ? , ?)";
            ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, reply);
            ps.setInt(2, forumID);
            ps.setString(3, username);
            ps.executeUpdate();
            connection.commit();
            System.out.println("Added post!");
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
    public void insertThread(String topic, String threadName) {
        try {
            String insertIntoSubtopicTopic = "INSERT INTO FORUM_SUBTOPICTOPIC VALUES(?, ?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(insertIntoSubtopicTopic), insertIntoSubtopicTopic, false);
            ps.setString(1, threadName);
            ps.setString(2, topic);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) return;
            String insertIntoForum = "INSERT INTO Forum VALUES(DEFAULT, ?)";
            ps = new PrintablePreparedStatement(connection.prepareStatement(insertIntoForum), insertIntoForum, false);
            ps.setString(1, threadName);
            rowCount = ps.executeUpdate();
            if (rowCount == 0) return;
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
    public void deletePostFromUser(String threadName, int postID) {
        try {
            // First, get the ForumID
            String forumIDquery = "SELECT FORUMID FROM FORUM WHERE SUBTOPIC = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(forumIDquery), forumIDquery, false);
            ps.setString(1, threadName);
            ResultSet rs = ps.executeQuery();
            int forumID = -1;
            while(rs.next()) {
                forumID = rs.getInt("FORUMID");
            }
            // Could not find the forumID to add to this user
            if (forumID == -1) return;
            // Next, set user to NULL
            String updateUserToNullQuery = "UPDATE POSTS_POPULATE_POSTSON SET USERNAME = NULL WHERE POSTID = ? AND FORUMID = ?";
            ps = new PrintablePreparedStatement(connection.prepareStatement(updateUserToNullQuery), updateUserToNullQuery, false);
            ps.setInt(1, postID);
            ps.setInt(2, forumID);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Post does not exist!");
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
    public void deleteThread(String threadName) {
        try {
            // Delete from FORUM_SUBTOPICTOPIC (which cascades through FORUM and POSTS_POPULATE_POSTSON)
            String deleteFromForumSubtopicQuery = "DELETE FROM FORUM_SUBTOPICTOPIC WHERE SUBTOPIC = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(deleteFromForumSubtopicQuery), deleteFromForumSubtopicQuery, false);
            ps.setString(1, threadName);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Could not delete thread!");
            }
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
    /**
     * FORUM QUERIES (END)
     */

    /**
     * LOGIN QUERIES (BEGIN)
     */
    public boolean loginToUser(String username, String password) {
        try {
            String sql = "SELECT * from USER_LIVESIN WHERE username= ? and password= ?";
            PreparedStatement login = connection.prepareStatement(sql);
            login.setString(1, username);
            login.setString(2, password);
            ResultSet rs = login.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return false;
    }
    /**
     * LOGIN QUERIES (END)
     */

}
