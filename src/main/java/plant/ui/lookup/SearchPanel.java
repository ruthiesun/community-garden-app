package plant.ui.lookup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/**
 * Search bar and button
 */
public class SearchPanel extends Observable implements ActionListener{
    private GridBagConstraints gbc;
    private JButton searchButton;
    private JTextField searchBar;
    private JPanel panel;

    public String getSearchString() {
        return searchBar.getText();
    }

    public JPanel getPanel() {
        return panel;
    }

    public SearchPanel() {
        panel = new JPanel();

        searchButton = new JButton("Search!");
        searchButton.addActionListener(this);
        searchBar = new JTextField(30);

        panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridwidth = 8;
        gbc.gridheight = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;

        panel.add(searchBar);

        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.gridy = 0;
        gbc.gridx = 8;

        panel.add(searchButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setChanged();
        notifyObservers();
    }
}
