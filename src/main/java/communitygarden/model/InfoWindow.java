package communitygarden.model;


// SOURCE: https://courses.students.ubc.ca/cs/courseschedule?pname=subjarea&tname=subj-course&dept=CPSC&course=210
// From a CPSC 210 project (lab #8)

import communitygarden.ui.CommunityGardenUI;
import javafx.util.Pair;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.List;

import static ui.MainWindow.FRAME_HEIGHT;
import static ui.MainWindow.FRAME_WIDTH;

public class InfoWindow extends JScrollPane implements MouseListener {
    private static final double X_SCALE = 0.5; // proportion of map window to be used for info window
    private static final double Y_SCALE = 0.25;
    private int WIDTH;
    private int HEIGHT;
    private JEditorPane textPane;
    private JXMapViewer mapViewer;

    private List<Pair<Garden, GeoPosition>> gardenGeoPosition;

    // EFFECTS: constructs info window, not visible to user
    public InfoWindow(JXMapViewer mapViewer, List<Pair<Garden, GeoPosition>> gardenGeoPosition) {
        super();
        this.mapViewer = mapViewer;
        this.gardenGeoPosition = gardenGeoPosition;

        this.WIDTH = (int) (FRAME_WIDTH * X_SCALE);
        this.HEIGHT = (int) (FRAME_HEIGHT * Y_SCALE);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        buildTextPane();

        getViewport().setView(textPane);
        setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: builds pane in which resource info will be displayed
    private void buildTextPane() {
        textPane = new JEditorPane("text/html", "");
        textPane.setEditable(false);
        textPane.setMargin(new Insets(4, 10, 4, 10));

        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
            }
        });

        textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    // A hacky way to get the community garden - relies on each garden being unique via it's name
                    // Better way would be to make a component that embeds the JEditorPane and JCheckbox...
                    System.out.println(e.getDescription() + " should be added to your community garden!");
                    CommunityGardenUI.delegate.insertGarden(e.getDescription());
                }
            }
        });
    }

    //---------------------------------------------------------------------
    // Implementation of MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {

        Point mouseXY = e.getPoint();

        for (Pair<Garden, GeoPosition> gardenGeoPositionPair: gardenGeoPosition) {

            Garden garden = gardenGeoPositionPair.getKey();
            GeoPosition gp = gardenGeoPositionPair.getValue();

            Point2D gardenXY = mapViewer.convertGeoPositionToPoint(gp);

            if (gardenSelected(mouseXY, gardenXY)) {
                displayGardenInfo(garden);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //---------------------------------------------------------------------
    // EFFECTS: returns true if point is sufficiently close to garden
    private boolean gardenSelected(Point mouse, Point2D garden) {

        int CLOSE_DIST_X = 15;
        int CLOSE_DIST_Y = 30;

        return Math.abs(mouse.y - garden.getY()) < CLOSE_DIST_Y && Math.abs(mouse.x - garden.getX()) < CLOSE_DIST_X;

    }

    // MODIFIES: this
    // EFFECTS: display information for given resource in text pane
    private void displayGardenInfo(Garden garden) {
        textPane.setText(formattedHTML(garden));
        setVisible(true);
        getParent().validate();
        getParent().repaint();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getVerticalScrollBar().setValue(0);
            }
        });
    }

    private String formattedHTML(Garden garden) {
        String formattedString = "<b>" + garden.getName() + "</b>";
        formattedString += "<p>";
        formattedString += garden.getAddress() + "<br>";
        formattedString += garden.getCityProvince() + "<br>";
        formattedString += garden.getPostalCode();
        formattedString += "</p>";

        String anchor = "";

        anchor += "<a href='";
        anchor += garden.getName();
        anchor += "'><br>";
        anchor += "Click here to this community garden to your profile.";
        anchor += "</a>";

        formattedString += anchor;

        return formattedString;
    }
}
