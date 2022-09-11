package plant.ui.lookup;

import plant.util.MonthConverter;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Displays search filters
 */
public class FiltersPanel extends FilterResultsPanel {
    private JScrollPane scrollPane;
    private JPanel filtersPanel;
    private HashMap<JLabel, ArrayList<JComponent>> filters;
    private HashMap<JPanel, JSlider> sliderMap;
    private ArrayList<JLabel> filterLabels;

    protected HashSet<String> selectedSoil;
    protected HashSet<String> selectedType;
    protected int[] selectedSowingDates;
    protected int[] selectedPlantingDates;
    protected int[] selectedHarvestDates;
    protected int[] selectedHardinessZones;
    protected HashSet<String> selectedCommunityGardenZones;
    protected boolean selectedEdible;
    protected boolean selectedOrnamental;



    public FiltersPanel() {
        super("Filters");
        setupFilters();
        setupFiltersPanel();
        scrollPane = new JScrollPane(filtersPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(scrollPane);
    }

    private void setupFilters() {
        filters = new HashMap<>();
        filterLabels = new ArrayList<>();
        sliderMap = new HashMap<>();
        ArrayList<JComponent> temp;
        JLabel tempLabel;

        temp = new ArrayList<>();
        temp.add(new JCheckBox("Annual", true));
        temp.add(new JCheckBox("Perennial", true));
        temp.add(new JCheckBox("Biennial", true));
        tempLabel = new JLabel("Type");
        filters.put(tempLabel, temp);
        filterLabels.add(tempLabel);

        temp = new ArrayList<>();
        temp.add(new JCheckBox("Loamy", true));
        temp.add(new JCheckBox("Chalky", true));
        temp.add(new JCheckBox("Peaty", true));
        temp.add(new JCheckBox("Silty", true));
        temp.add(new JCheckBox("Sandy", true));
        temp.add(new JCheckBox("Clay", true));
        tempLabel = new JLabel("Soil");
        filters.put(tempLabel, temp);
        filterLabels.add(tempLabel);

        temp = new ArrayList<>();
        Hashtable table = new Hashtable();
        for (int i=1; i<=13; i++) {
            JLabel label = new JLabel(Integer.toString(i));
            label.setForeground(Color.gray);
            label.setSize(MonthConverter.labelWidth,MonthConverter.labelHeight);
            table.put(i, label);
        }
        temp.add(makeSliderPanel(true, 1, 13, table));
        temp.add(makeSliderPanel(false, 1, 13, table));
        //temp.add(new JCheckBox("use <community garden name> zone"));
        tempLabel = new JLabel("USDA Hardiness Zone");
        filters.put(tempLabel, temp);
        filterLabels.add(tempLabel);

        temp = new ArrayList<>();
        temp.add(makeSliderPanel(true, 1, 12, MonthConverter.numToLabel()));
        temp.add(makeSliderPanel(false, 1 , 12, MonthConverter.numToLabel()));
        tempLabel = new JLabel("Sowing period");
        filters.put(tempLabel, temp);
        filterLabels.add(tempLabel);

        temp = new ArrayList<>();
        temp.add(makeSliderPanel(true, 1, 12, MonthConverter.numToLabel()));
        temp.add(makeSliderPanel(false, 1, 12, MonthConverter.numToLabel()));
        tempLabel = new JLabel("Planting period");
        filters.put(tempLabel, temp);
        filterLabels.add(tempLabel);

        temp = new ArrayList<>();
        temp.add(makeSliderPanel(true, 1, 12, MonthConverter.numToLabel()));
        temp.add(makeSliderPanel(false, 1, 12, MonthConverter.numToLabel()));
        tempLabel = new JLabel("Harvesting period");
        filters.put(tempLabel, temp);
        filterLabels.add(tempLabel);

        temp = new ArrayList<>();
        temp.add(new JCheckBox("Fruit/Vegetables", true));
        temp.add(new JCheckBox("Ornamental", true));
        tempLabel = new JLabel("Misc.");
        filters.put(tempLabel, temp);
        filterLabels.add(tempLabel);
    }

    private JPanel makeSliderPanel(boolean initMin, int min, int max, Hashtable labels) {
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        JSlider slider = new JSlider();
        JLabel label = new JLabel();
        Font f = new Font(label.getFont().getFontName(), Font.ITALIC, label.getFont().getSize());
        label.setFont(f);
        label.setForeground(Color.gray);
        if (initMin) {
            label.setText("Min: ");
            slider = new JSlider(min,max,min);
        } else {
            label.setText("Max: ");
            slider = new JSlider(min,max,max);
        }
        slider.setSnapToTicks(true);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        slider.updateUI();
        sliderPanel.add(label);
        sliderPanel.add(slider);
        sliderMap.put(sliderPanel,slider);
        return sliderPanel;
    }
    private void setupFiltersPanel() {
        filtersPanel = new JPanel();
        filtersPanel.setLayout(new BoxLayout(filtersPanel, BoxLayout.Y_AXIS));
        for (JLabel l : filterLabels) {
            Font f = new Font(l.getFont().getFontName(), Font.BOLD, l.getFont().getSize()+3);
            l.setFont(f);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            filtersPanel.add(l);
            ArrayList<JComponent> componentsToAdd = filters.get(l);
            for (JComponent c : componentsToAdd) {
                c.setAlignmentX(Component.LEFT_ALIGNMENT);
                filtersPanel.add(c);
            }
        }
    }

    protected void getSelections() {
        selectedSoil = new HashSet<>();
        selectedType = new HashSet<>();
        selectedSowingDates = new int[2];
        selectedPlantingDates = new int[2];
        selectedHarvestDates = new int[2];
        selectedHardinessZones = new int[2];
        selectedCommunityGardenZones = new HashSet<>();
        selectedEdible = true;
        selectedOrnamental = true;

        for (JLabel label : filterLabels) {
            String text = label.getText();
            ArrayList<JComponent> list = filters.get(label);

            if (text.equals("Type")) {
                addBoxText(selectedType,list);
            } else if (text.equals("Soil")) {
                addBoxText(selectedSoil,list);
            } else if (text.equals("USDA Hardiness Zone")) {
                selectedHardinessZones[0] = (sliderMap.get(list.get(0))).getValue();
                selectedHardinessZones[1] = (sliderMap.get(list.get(1))).getValue();
                addBoxText(selectedCommunityGardenZones,list.subList(2,list.size()));
            } else if (text.equals("Sowing period")) {
                selectedSowingDates[0] = (sliderMap.get(list.get(0))).getValue();
                selectedSowingDates[1] = (sliderMap.get(list.get(1))).getValue();
            } else if (text.equals("Planting period")) {
                selectedPlantingDates[0] = (sliderMap.get(list.get(0))).getValue();
                selectedPlantingDates[1] = (sliderMap.get(list.get(1))).getValue();
            } else if (text.equals("Harvesting period")) {
                selectedHarvestDates[0] = (sliderMap.get(list.get(0))).getValue();
                selectedHarvestDates[1] = (sliderMap.get(list.get(1))).getValue();
            } else if (text.equals("Misc.")) {
                selectedEdible = ((JCheckBox) list.get(0)).isSelected();
                selectedOrnamental = ((JCheckBox) list.get(1)).isSelected();
            }
        }
        setChanged();
        notifyObservers();
    }

    private void addBoxText(HashSet<String> set, List<JComponent> list) {
        for (JComponent box : list) {
            if (((JCheckBox) box).isSelected()) {
                set.add(((JCheckBox) box).getText());
            }
        }
    }
}
