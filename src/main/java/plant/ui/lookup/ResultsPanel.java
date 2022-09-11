package plant.ui.lookup;

import delegates.SoilDelegate;
import plant.model.PlantModel;
import plant.ui.details.DetailsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Panel that displays list of plant names that result from a search query
 */
public class ResultsPanel extends FilterResultsPanel implements ActionListener {
    private SoilDelegate soilDelegate;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private ArrayList<PlantModel> results;
    private HashMap<JButton, PlantModel> buttonModelMap;

    public ResultsPanel(SoilDelegate soilDelegate) {
        super("Results");
        this.soilDelegate = soilDelegate;
        results = new ArrayList<>();
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        setupResultsPanel();
        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(scrollPane);
    }

    private void setupResultsPanel() {
        resultsPanel.removeAll();
        buttonModelMap = new HashMap<>();

        for (PlantModel result : results) {
            JButton temp = new JButton(result.getName());
            temp.setBorderPainted(false);
            temp.setContentAreaFilled(false);
            temp.addActionListener(this);
            resultsPanel.add(temp);
            buttonModelMap.put(temp, result);
        }

        resultsPanel.validate();
        resultsPanel.repaint();
    }

    protected void displayResults(PlantModel[] results) {
        this.results = new ArrayList<>();
        for (PlantModel model : results) {
            this.results.add(model);
        }
        setupResultsPanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new DetailsFrame(buttonModelMap.get((JButton) e.getSource()), soilDelegate);
    }
}
