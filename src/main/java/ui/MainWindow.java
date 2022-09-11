package ui;

import communitygarden.ui.CommunityGardenUI;
import controller.Controller;
import delegates.CommunityGardenDelegate;
import delegates.ForumDelegate;
import delegates.PlantDelegate;
import forum.ui.ForumUI;
import plant.ui.PlantUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main window that contains all the GUI elements
 */
public class MainWindow extends JFrame implements ActionListener {
    private Controller controller;
    private JToolBar mainMenu;
    private JButton activityLog;
    private JButton forums;
    private JButton myGardens;
    private JButton plantLookup;
    public static int FRAME_WIDTH = 800;
    public static int FRAME_HEIGHT = 600;
    public static int BORDER_WIDTH = 10;
    private PlantUI plantPanel;
    private CommunityGardenUI gardenPanel;
    private ForumUI forumPanel;

    public MainWindow(PlantDelegate delegate, Controller controller) {
        super("Gardening App");
        this.controller = controller;

        // SOURCE: https://www.clear.rice.edu/comp310/JavaResources/frame_close.html
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.finished();
            }
        });

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(FRAME_WIDTH,FRAME_HEIGHT);

        setup(delegate);

        this.setVisible(true);
    }

    private void setup(PlantDelegate delegate) {
        setupMainMenu();

        plantPanel = new PlantUI(delegate);
        gardenPanel = new CommunityGardenUI((CommunityGardenDelegate) delegate, this);
        forumPanel = new ForumUI((ForumDelegate) delegate, this);

        this.add(plantPanel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        refresh();
    }

    /**
     * Sets up the main menu bar with its buttons
     */
    private void setupMainMenu() {
        mainMenu = new JToolBar();
        activityLog = new JButton("Activity Log");
        activityLog.addActionListener(this);
        forums = new JButton("Forums");
        forums.addActionListener(this);
        myGardens = new JButton("Community Garden Map");
        myGardens.addActionListener(this);
        plantLookup = new JButton("Plant Lookup");
        plantLookup.addActionListener(this);

        mainMenu.add(activityLog);
        mainMenu.add(forums);
        mainMenu.add(myGardens);
        mainMenu.add(plantLookup);

        showMainMenu();
    }

    private void showMainMenu() {
        this.add(mainMenu, BorderLayout.PAGE_START);
    }

    /**
     * Updates window with visual changes
     * Call this whenever a change is made
     */
    public void refresh() {
        this.validate();
        this.repaint();
        this.pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * Handles main menu button clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        resetFrame();

        if (e.getSource() == activityLog) {
            System.out.println("Activity log!");
            refresh();
        } else if (e.getSource() == forums) {
            this.add(forumPanel, BorderLayout.CENTER);
            forumPanel.showForum();
            this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
            refresh();
        } else if (e.getSource() == myGardens) {
            this.add(gardenPanel, BorderLayout.CENTER);
            gardenPanel.showMap();
            this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
            refresh();
        } else if (e.getSource() == plantLookup) {
            this.add(plantPanel, BorderLayout.CENTER);
            this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
            refresh();
        }
    }

    /**
     * Remove all, remount menu
     */
    public void resetFrame() {
        this.getContentPane().removeAll();
        showMainMenu();
    }

}
