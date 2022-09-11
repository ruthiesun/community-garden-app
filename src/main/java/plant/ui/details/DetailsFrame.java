package plant.ui.details;

import delegates.FertilizerDelegate;
import delegates.SoilDelegate;
import delegates.SuppliersDelegate;
import delegates.ThrivesInDelegate;
import plant.model.FertilizerModel;
import plant.model.PlantModel;
import plant.model.SoilModel;
import plant.model.SuppliersModel;
import plant.ui.fertilizer.AttributeSelectionDialog;
import plant.ui.hardinesszone.FunFactsFrame;
import plant.util.*;
import plant.util.querybuilder.FertilizersQueryBuilder;
import plant.util.querybuilder.SoilQueryBuilder;
import plant.util.querybuilder.SuppliersQueryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Panel that displays detailed information about a single plant
 */
public class DetailsFrame extends JFrame implements ActionListener, Observer {
    private PlantModel plantModel;
    private SoilDelegate soilDelegate;
    private SuppliersDelegate suppliersDelegate;
    private FertilizerDelegate fertilizerDelegate;
    private GridBagConstraints gbc;

    private ArrayList<JButton> buttons;
    private JButton suppliersButton;
    private JButton logButton;
    private JButton coolFactsButton;
    private JButton fertilizersButton;
    private QueryResultTable tableFrame;
    private AttributeSelectionDialog attributeSelectionDialog;
    private FunFactsFrame funFactsFrame;
    private LinkedHashMap<JLabel,JComponent> basicCareReqs;
    private LinkedHashMap<JLabel,JComponent> advancedCareReqs;
    public DetailsFrame(PlantModel plantModel, SoilDelegate soilDelegate) {
        super(plantModel.getName());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.plantModel = plantModel;
        this.soilDelegate = soilDelegate;
        this.suppliersDelegate = (SuppliersDelegate) soilDelegate;
        this.fertilizerDelegate = (FertilizerDelegate) soilDelegate;
        setupComponents();

        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        layOutComponents();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void layOutComponents() {
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        JLabel plantNameLabel = new JLabel(plantModel.getName());
        Font f = new Font(plantNameLabel.getFont().getFontName(), Font.BOLD, plantNameLabel.getFont().getSize()+7);
        plantNameLabel.setFont(f);
        plantNameLabel.setForeground(Color.gray);
        this.add(plantNameLabel, gbc);

        addVertically(basicCareReqs, 2, true);
        addVertically(advancedCareReqs, 2+2*basicCareReqs.size(), basicCareReqs.size()%2==0);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,2));

        for (JButton button : buttons) {
            buttonPanel.add(button);
        }

        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.gridx = 0;
        gbc.gridy = 2+2*(basicCareReqs.size() + advancedCareReqs.size());
        this.add(buttonPanel, gbc);
    }

    private void addVertically(LinkedHashMap<JLabel, JComponent> careReqs, int yStart, boolean oddRowNum) {
        int yBoost = yStart;
        for (Map.Entry<JLabel,JComponent> entry : careReqs.entrySet()) {
            gbc.gridy = yBoost;

            JPanel temp = new JPanel();
            if (oddRowNum) {
                temp.setBackground(Color.LIGHT_GRAY);
            }
            temp.setLayout(new GridLayout(1,2));
            temp.add(entry.getKey());
            temp.add(entry.getValue());

            this.add(temp, gbc);
            yBoost += 2;
            oddRowNum = !oddRowNum;
        }
    }

    private void setupComponents() {
        buttons = new ArrayList<>();

        logButton = makeButton("Add to activity log");
        logButton.addActionListener(this);
        buttons.add(logButton);
        suppliersButton = makeButton("Find suppliers");
        suppliersButton.addActionListener(this);
        buttons.add(suppliersButton);
        fertilizersButton = makeButton("Recommended fertilizers");
        fertilizersButton.addActionListener(this);
        buttons.add(fertilizersButton);
        coolFactsButton = makeButton("Learn more about hardiness zones");
        coolFactsButton.addActionListener(this);
        buttons.add(coolFactsButton);

        basicCareReqs = new LinkedHashMap<>();
        basicCareReqs.put(makeHeaderLabel("Type: "), makeLabel(plantModel.getType()));
        basicCareReqs.put(makeHeaderLabel("Shade: "), makeLabel(plantModel.getShade()));
        basicCareReqs.put(makeHeaderLabel("Water: "), makeLabel(plantModel.getWater()));
        basicCareReqs.put(makeHeaderLabel("Soil: "), makeLabel(getSoilString()));
        basicCareReqs.put(makeHeaderLabel("Fertilization frequency: "), makeLabel(plantModel.getFertilization()));

        basicCareReqs.put(makeHeaderLabel("Sowing period: "),
                makeTimeline(MonthConverter.numToStringLong(plantModel.getSowingStartDate()), MonthConverter.numToStringLong(plantModel.getSowingEndDate())));
        basicCareReqs.put(makeHeaderLabel("Planting period: "),
                makeTimeline(MonthConverter.numToStringLong(plantModel.getPlantingStartDate()), MonthConverter.numToStringLong(plantModel.getPlantingEndDate())));
        basicCareReqs.put(makeHeaderLabel("Harvest period: "),
                makeTimeline(MonthConverter.numToStringLong(plantModel.getHarvestStartDate()), MonthConverter.numToStringLong(plantModel.getHarvestEndDate())));
        basicCareReqs.put(makeHeaderLabel("Dormancy period: "),
                makeTimeline(MonthConverter.numToStringLong(plantModel.getDormancyStartDate()), MonthConverter.numToStringLong(plantModel.getDormancyEndDate())));

        setupAdvancedCareReqs();
    }

    private String getSoilString() {
        SoilQueryBuilder qb = new SoilQueryBuilder();
        qb.makeQuery(plantModel.getName());
        SoilModel[] models = soilDelegate.getSoilModel(qb.getFinalQuery());
        String s = "";
        if (models.length > 0) {
            s += models[0].getType();
        }
        for (int i=1; i<models.length; i++) {
            s += ", ";
            s += models[i].getType();
        }
        return s;
    }

    private void setupAdvancedCareReqs() {
        advancedCareReqs = new LinkedHashMap<>();
        LinkedHashMap<String, String> reqs = plantModel.getAdvancedCareReqs();
        for (Map.Entry<String, String> entry : reqs.entrySet()) {
            advancedCareReqs.put(makeHeaderLabel(entry.getKey()), makeLabel(entry.getValue()));
        }
    }

    private JLabel makeTimeline(String first, String last) {
        String start = first;
        String end = last;
        if (first==null) {
            start = "N/A";
        }
        if (last==null) {
            end = "N/A";
        }
        JLabel label;
        if (first.equals(last)) {
            label = new JLabel(start);
        } else {
            label = new JLabel(start + " to " + end);
        }
        return label;
    }

    private JLabel makeLabel(String s) {
        if (s.length()==0) {
            return new JLabel("N/A");
        } else {
            return new JLabel(s);
        }
    }

    private JLabel makeHeaderLabel(String s) {
        JLabel label = new JLabel(s);
        Font f = new Font(label.getFont().getFontName(), Font.ITALIC, label.getFont().getSize());
        label.setFont(f);
        return label;
    }

    private JButton makeButton(String s) {
        JButton button = new JButton(s);
        button.setContentAreaFilled(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==logButton) {
            System.out.println("Activity log!");
        } else if (e.getSource()==suppliersButton) {
            handleSuppliersButton();
        } else if (e.getSource()==coolFactsButton) {
            handleCoolFactsButton();
        } else if (e.getSource()==fertilizersButton) {
            handleFertilizersButton();
        }
    }


    private void handleSuppliersButton() {
        SuppliersQueryBuilder qb = new SuppliersQueryBuilder();
        qb.makeQuery(plantModel.getName());
        SuppliersModel[] models = suppliersDelegate.getSuppliersModel(qb.getFinalQuery());
        ArrayList<String> headers = new ArrayList<>();
        headers.add("BUSINESS NAME");
        headers.add("ADDRESS");
        ArrayList<ArrayList<String>> tuples = new ArrayList<>();
        for (SuppliersModel model : models) {
            ArrayList<String> entry = new ArrayList<>();
            entry.add(model.getBusinessName());
            entry.add(model.getStreetNum() + " " + model.getStreetName() + ", " + model.getCity() + ", " +
                    model.getPostalCode() + " " + model.getProvince());
            tuples.add(entry);
        }
        newQueryResultTable("Suppliers for " + plantModel.getName(), headers, tuples);
    }

    private void handleFertilizersButton() {
        attributeSelectionDialog = new AttributeSelectionDialog(this,"Select fertilizer details",true);
        attributeSelectionDialog.addObserver(this);
        attributeSelectionDialog.popup();
    }

    private void handleCoolFactsButton() {
        if (funFactsFrame!=null) {
            funFactsFrame.dispose();
        }
        funFactsFrame = new FunFactsFrame(plantModel,(ThrivesInDelegate) soilDelegate);
    }

    private void parseFertilizerAttributeSelection(boolean[] arg) {
        attributeSelectionDialog.dispose();
        boolean[] selected = arg;
            boolean npk = selected[0];
            boolean type = selected[1];
            boolean details = selected[2];

            FertilizersQueryBuilder qb = new FertilizersQueryBuilder();
            qb.makeQuery(plantModel.getName());
            FertilizerModel[] models = fertilizerDelegate.getFertilizerModel(qb.getFinalQuery(), npk, type, details);
            ArrayList<String> headers = new ArrayList<>();
            ArrayList<ArrayList<String>> tuples = new ArrayList<>();
            headers.add("FERTILIZER NAME");
            if (npk) {
                headers.add("NPK RATIO");
            }
            if (type) {
                headers.add("TYPE");
            }
            if (details) {
                headers.add("DETAILS");
            }

            for (FertilizerModel model : models) {
                ArrayList<String> entry = new ArrayList<>();
                entry.add(model.getName());
                if (npk) {
                    entry.add(model.getNpkRatio());
                }
                if (type) {
                    entry.add(model.getType());
                }
                if (details) {
                    entry.add(model.getDescription());
                }
                tuples.add(entry);
            }
            newQueryResultTable("Fertilizers for " + plantModel.getName(), headers, tuples);

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass() == AttributeSelectionDialog.class) {
            parseFertilizerAttributeSelection((boolean[]) arg);
        }

    }

    private void newQueryResultTable(String name, ArrayList<String> headers, ArrayList<ArrayList<String>> tuples) {
        if (tableFrame!=null) {
            tableFrame.dispose();
        }
            tableFrame = new QueryResultTable(name, headers, tuples);
    }
}
