package plant.ui.lookup;

import delegates.PlantDelegate;
import delegates.SoilDelegate;
import plant.model.PlantModel;
import plant.util.querybuilder.PlantQueryBuilder;
import ui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

/**
 * Main panel for plant lookup feature
 */
public class LookupPanel extends JPanel implements Observer {
    private SearchPanel searchPanel;
    private ResultsPanel resultsPanel;
    private FiltersPanel filtersPanel;
    private PlantQueryBuilder qb;
    private GridBagConstraints gbc;
    private PlantDelegate delegate;

    public LookupPanel(PlantDelegate delegate) {
        super();
        this.delegate = delegate;
        searchPanel = new SearchPanel();
        resultsPanel = new ResultsPanel((SoilDelegate) delegate);
        resultsPanel.getPanel().setPreferredSize(new Dimension(
                MainWindow.FRAME_WIDTH*1/3 - 2*MainWindow.BORDER_WIDTH,
                MainWindow.FRAME_HEIGHT - 2*MainWindow.BORDER_WIDTH - searchPanel.getPanel().getPreferredSize().height*5));
        filtersPanel = new FiltersPanel();
        filtersPanel.getPanel().setPreferredSize(new Dimension(
                MainWindow.FRAME_WIDTH*2/3 - 2*MainWindow.BORDER_WIDTH,
                MainWindow.FRAME_HEIGHT - 2*MainWindow.BORDER_WIDTH - searchPanel.getPanel().getPreferredSize().height*5));
        qb = new PlantQueryBuilder();
        searchPanel.addObserver(this);
        filtersPanel.addObserver(this);


        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;

        this.add(searchPanel.getPanel(), gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 10;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 1;

        this.add(filtersPanel.getPanel(), gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 10;
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.weightx = 1;

        this.add(resultsPanel.getPanel(), gbc);

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass() == SearchPanel.class) {
            filtersPanel.getSelections();
        } else if (o.getClass() == FiltersPanel.class) {
            qb = new PlantQueryBuilder();
            qb.makeQuery(searchPanel.getSearchString(), filtersPanel.selectedSoil, filtersPanel.selectedHardinessZones,
                    filtersPanel.selectedSowingDates, filtersPanel.selectedPlantingDates, filtersPanel.selectedHarvestDates,
                    filtersPanel.selectedCommunityGardenZones, filtersPanel.selectedEdible, filtersPanel.selectedOrnamental);
            HashSet<String> types = filtersPanel.selectedType;
            boolean annual = false;
            boolean perennial = false;
            boolean biennial = false;
            if (types.contains("Annual")) {
                annual = true;
            }
            if (types.contains("Perennial")) {
                perennial = true;
            }
            if (types.contains("Biennial")) {
                biennial = true;
            }
            PlantModel[] results = delegate.getPlantModel(qb.getFinalQuery(), annual, perennial, biennial);
            resultsPanel.displayResults(results);
        }
    }
}
