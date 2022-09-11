package communitygarden.ui;

import communitygarden.model.Garden;
import communitygarden.model.InfoWindow;
import delegates.CommunityGardenDelegate;
import javafx.util.Pair;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.util.*;
import java.util.List;


// SOURCE: https://github.com/msteiger/jxmapviewer2
// The jxmapviewer documentation

// Inspired by CPSC 210 lab #8

public class CommunityGardenUI extends JPanel{

    private final JFrame mainWindow;
    private List<Garden> gardens;
    private JXMapViewer mapViewer;

    private List<Pair<Garden, GeoPosition>> gardenGeoPosition;
    Set<Waypoint> waypoints;

    public static CommunityGardenDelegate delegate;

    public CommunityGardenUI(CommunityGardenDelegate delegate, JFrame mainWindow) {
        this.delegate = delegate;
        this.mainWindow = mainWindow;
    }


    public void showMap() {
        getGardens();
        getGardenGeoPositions(gardens);
        createWaypoints();
        createMapViewer();

        // Display the viewer in a JFrame
        mainWindow.add(mapViewer);
    }

    public void getGardens() {
        gardens = delegate.getGardens();
    }

    public void getGardenGeoPositions(List<Garden> gardens) {
        gardenGeoPosition = delegate.getGardenGeoPositions(gardens);
    }

    private void createWaypoints() {
        waypoints = new HashSet<>();

        for (Pair<Garden, GeoPosition> gardenGeoPositionPair: gardenGeoPosition) {
            GeoPosition geoPosition = gardenGeoPositionPair.getValue();
            waypoints.add(new DefaultWaypoint(geoPosition));
        }
    }

    private void createMapViewer() {
        mapViewer = new JXMapViewer();


        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Set the focus
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(new GeoPosition(49.2827, -123.1207)); // Vancouver


        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Pop-ups
        InfoWindow infoWindow = new InfoWindow(mapViewer, gardenGeoPosition);
        mapViewer.addMouseListener(infoWindow);
        mapViewer.add(infoWindow);

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(waypoints);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

}
