package plant.ui.lookup;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

/**
 * Superclass for the scroll panels in the LookupPanel class
 */
public abstract class FilterResultsPanel extends Observable {
    protected JPanel panel;
    protected JLabel label;

    public FilterResultsPanel(String labelString) {
        panel = new JPanel();
        label = new JLabel(labelString);
        Font f = new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()+5);
        label.setFont(f);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public JPanel getPanel() {
        return panel;
    }
}
