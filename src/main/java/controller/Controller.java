package controller;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import communitygarden.model.Garden;
import delegates.*;
import forum.model.ForumModel;
import forum.model.PostModel;
import forum.model.ThreadModel;
import javafx.util.Pair;
import org.jxmapviewer.viewer.GeoPosition;
import database.DatabaseConnectionHandler;
import plant.model.*;

import ui.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that serves as an interface between UI classes and database handler classes
 * SOURCE: https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project/blob/master/src/ca/ubc/cs304/controller/Bank.java
 */
public class Controller implements LoginWindowDelegate, SoilDelegate, SuppliersDelegate, FertilizerDelegate, PlantDelegate, ThrivesInDelegate, CommunityGardenDelegate, ForumDelegate {
    public static Controller controller;

    private String username;
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow;
    private static GeoApiContext context;

    public Controller() {
        dbHandler = new DatabaseConnectionHandler();
    }

    public String getUsername() {
        return this.username;
    }

    /**
     * Clean up connection
     */
    public void finished() {
        dbHandler.close();
        dbHandler = null;

        // Invoke .shutdown() after your application is done making requests
        context.shutdown();

        System.exit(0);
    }

    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        controller = new Controller();
        controller.login(OracleAuth.USERNAME, OracleAuth.PASSWORD);

        context = new GeoApiContext.Builder()
                .apiKey(OracleAuth.GMAPS_API_KEY)
                .build();
    }

    /**
     * LoginWindowDelegate Implementation
     *
     * connects to Oracle database with supplied username and password
     */
    @Override
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            loginWindow = new LoginWindow();
            loginWindow.showFrame(this);
        } else {
            throw new RuntimeException("Oracle login failed");
        }
    }

    /**
     * LoginWindowDelegate Implementation
     *
     * connects to plant database with supplied username and password
     */
    @Override
    public void loginToUser(String username, String password) {
        boolean didConnect = dbHandler.loginToUser(username, password);

        if (didConnect) {
            this.username = username;
            loginWindow.dispose();
            new MainWindow(this, this);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Login unsuccessful!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * SoilDelegate Implementation
     */
    @Override
    public SoilModel[] getSoilModel(String q) {
        return dbHandler.getSoilInfo(q);
    }

    /**
     * SuppliersDelegate Implementation
     */
    @Override
    public SuppliersModel[] getSuppliersModel(String q) {
        return dbHandler.getSuppliersInfo(q);
    }

    /**
     * FertilizerDelegate Implementation
     */
    @Override
    public FertilizerModel[] getFertilizerModel(String q, boolean npk, boolean type, boolean details ) {
        return dbHandler.getFertilizerInfo(q, npk, type, details);
    }

    /**
     * PlantDelegate Implementation
     */
    @Override
    public PlantModel[] getPlantModel(String q, boolean annual, boolean perennial, boolean biennial) {
        PlantModel[] annualModels = new PlantModel[0];
        PlantModel[] biennialModels = new PlantModel[0];
        PlantModel[] perennialModels = new PlantModel[0];

        if (annual) {
            annualModels = dbHandler.getAnnualInfo(q);
        }
        if (biennial) {
            biennialModels = dbHandler.getBiennialInfo(q);
        }
        if (perennial) {
            perennialModels = dbHandler.getPerennialInfo(q);
        }
        int totalSize = annualModels.length + biennialModels.length + perennialModels.length;

        PlantModel[] allPlants = new PlantModel[totalSize];
        int counter = 0;
        for (int i=0; i < annualModels.length; i++) {
            allPlants[i+counter] = annualModels[i];
        }
        counter += annualModels.length;
        for (int i=0; i < perennialModels.length; i++) {
            allPlants[i+counter] = perennialModels[i];
        }
        counter += perennialModels.length;
        for (int i=0; i < biennialModels.length; i++) {
            allPlants[i+counter] = biennialModels[i];
        }

        return allPlants;
    }

    /**
     * ThrivesInDelegate Implementation
     */
    @Override
    public ThrivesInModel[] getThrivesInModelGroupByName(String q, boolean count, boolean min, boolean max) {
        return dbHandler.getThrivesInModelGroupByName(q, count, min, max);
    }

    /**
     * ThrivesInDelegate Implementation
     */
    @Override
    public ThrivesInModel[] getThrivesInModel(String q) {
        return dbHandler.getThrivesInModel(q);
    }

    /**
     * CommunityGardenDelegate Implementation
     */
    @Override
    public List<Garden> getGardens() {

        List<Garden> gardens = queryGardens();
        getLatAndLong(gardens);

        return gardens;
    }

    /**
     * CommunityGardenDelegate Implementation
     */
    @Override
    public List<Pair<Garden, GeoPosition>> getGardenGeoPositions(List<Garden> gardens) {
        List<Pair<Garden, GeoPosition>> gardenGeoPosition = new ArrayList<>();

        for (Garden garden : gardens) {
            GeoPosition geoPosition = new GeoPosition(garden.getLatitude(), garden.getLongitude());
            gardenGeoPosition.add(new Pair<>(garden, geoPosition));
        }

        return gardenGeoPosition;
    }

    /**
     * CommunityGardenDelegate Implementation
     */
    @Override
    public void insertGarden(String gardenName) {
        dbHandler.insertGarden(gardenName, this.username);
    }

    /**
     * CommunityGardenDelegate helper
     */
    private void getLatAndLong(List<Garden> gardens) {
        for (Garden garden: gardens) {
            try {

                String gMapSearch  = garden.getName() + ", " + garden.getHouseNum() + " " + garden.getStreet() + ", " + garden.getPostalCode();
                GeocodingResult[] results =  GeocodingApi.geocode(context,gMapSearch).await();
                garden.setCoordinates(results[0].geometry.location.lat, results[0].geometry.location.lng);
            } catch (Exception e) {
                System.out.println("Could not get longitude and latitude for " + garden.getName() + "!");
            }
        }
    }

    /**
     * CommunityGardenDelegate helper
     */
    private List<Garden> queryGardens() {
        return dbHandler.getGardenInfo();
    }

    /**
     * ForumDelegate Implementation
     */
    @Override
    public ForumModel getForumModel() {
        ForumModel forumModel = dbHandler.getForumInfo(this.username);
        HashMap<String, List<ThreadModel>> forum = forumModel.getForum();

        System.out.format("%32s%64s%64s\n", "topic", "thread", "post");
        for (String topic: forum.keySet()) {
            List<ThreadModel> threads = forum.get(topic);

            for (ThreadModel thread: threads) {
                List<PostModel> posts = thread.getPosts();

                for (PostModel post: posts) {
                    System.out.format("%32s%64s%64s\n", topic, thread.getThreadName(), post.getPostNotes());
                }
            }
        }

        return forumModel;
    }

    /**
     * ForumDelegate Implementation
     */
    @Override
    public void insertPost(String threadName, String reply) {
        dbHandler.insertPost(threadName, reply, this.username);
    }

    /**
     * ForumDelegate Implementation
     */
    @Override
    public void insertThread(String topic, String threadName) {
        dbHandler.insertThread(topic, threadName);
    }

    /**
     * ForumDelegate Implementation
     */
    @Override
    public void deletePostFromUser(String topic, String threadName, int postID, String postNotes) {
        dbHandler.deletePostFromUser(threadName, postID);
    }

    /**
     * ForumDelegate Implementation
     */
    @Override
    public void deleteThread(String threadName) {
        dbHandler.deleteThread(threadName);
    }


}
