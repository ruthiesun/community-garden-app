package plant.ui;

import delegates.PlantDelegate;
import plant.ui.details.DetailsFrame;
import plant.ui.lookup.LookupPanel;
import ui.MainWindow;

import javax.swing.*;

/**
 * Main panel for the plant UI
 */
public class PlantUI extends JPanel {
    private LookupPanel lookupPanel;
    public PlantUI(PlantDelegate delegate) {
        super();
        lookupPanel = new LookupPanel(delegate);
        this.add(lookupPanel);
    }
}
