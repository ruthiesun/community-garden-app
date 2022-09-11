package plant.ui.hardinesszone;

import delegates.ThrivesInDelegate;
import plant.model.PlantModel;
import plant.model.ThrivesInModel;
import plant.util.querybuilder.HardinessQueryBuilder;
import plant.util.QueryResultTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Window that displays buttons that will query for fun facts about hardiness zones
 */
public class FunFactsFrame extends JFrame implements ActionListener {
    PlantModel plantModel;
    ThrivesInDelegate thrivesInDelegate;
    JButton factButton1;
    JButton factButton2;
    JButton factButton3;
    JButton factButton4;
    QueryResultTable tableFrame;

    public FunFactsFrame(PlantModel plantModel, ThrivesInDelegate thrivesInDelegate) {
        super("Hardiness zone facts about " + plantModel.getName());
        this.plantModel = plantModel;
        this.thrivesInDelegate = thrivesInDelegate;
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setupFacts();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setupFacts() {
        factButton1 = addButton("1. See all the plants in this database and how many zones each can grow in");
        factButton2 = addButton("2. " + plantModel.getName() + " can handle lower temperatures than these plants");
        factButton3 = addButton("3. See all the plants in this database that can grow in the widest range of temperatures");
        factButton4 = addButton("4. See plants that can grow in the same soil and hardiness regions as " + plantModel.getName());
    }

    private JButton addButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        this.add(button);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==factButton1) {
            countZones();
        } else if (e.getSource()==factButton2) {
            warmerWeatherPlants();
        } else if (e.getSource()==factButton3) {
            adaptablePlants();
        } else if (e.getSource()==factButton4) {
            sameSoilAndZonePlants();
        } else {
            System.out.println("no match");
        }
    }

    private void sameSoilAndZonePlants() {
        HardinessQueryBuilder qb = new HardinessQueryBuilder();
        qb.makeSameSoilAndZonesQuery(plantModel.getName());
        ThrivesInModel[] models = thrivesInDelegate.getThrivesInModel(qb.getFinalQuery());
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<ArrayList<String>> tuples = new ArrayList<>();
        headers.add("PLANT NAME");
        for (ThrivesInModel model : models) {
            ArrayList<String> tuple = new ArrayList<>();
            tuple.add(model.getName());
            tuples.add(tuple);
        }
        newQueryResultTable("Fun fact #4", headers, tuples);
    }

    private void adaptablePlants() {
        HardinessQueryBuilder qb = new HardinessQueryBuilder();
        qb.makeAdaptablePlantsQuery();
        ThrivesInModel[] models = thrivesInDelegate.getThrivesInModelGroupByName(qb.getFinalQuery(), false, true, true);
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<ArrayList<String>> tuples = new ArrayList<>();
        headers.add("PLANT NAME");
        headers.add("COLDEST SUITABLE HARDINESS ZONE");
        headers.add("WARMEST SUITABLE HARDINESS ZONE");
        for (ThrivesInModel model : models) {
            ArrayList<String> tuple = new ArrayList<>();
            tuple.add(model.getName());
            tuple.add(Integer.toString(model.getMinZoneNum()));
            tuple.add(Integer.toString(model.getMaxZoneNum()));
            tuples.add(tuple);
        }
        newQueryResultTable("Fun fact #3", headers, tuples);
    }

    private void warmerWeatherPlants() {
        HardinessQueryBuilder qb = new HardinessQueryBuilder();
        qb.makeWarmerWeatherPlantsQuery(plantModel.getName());
        ThrivesInModel[] models = thrivesInDelegate.getThrivesInModelGroupByName(qb.getFinalQuery(), false, true, false);
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<ArrayList<String>> tuples = new ArrayList<>();
        headers.add("PLANT NAME");
        headers.add("COLDEST SUITABLE HARDINESS ZONE");
        for (ThrivesInModel model : models) {
            ArrayList<String> tuple = new ArrayList<>();
            tuple.add(model.getName());
            tuple.add(Integer.toString(model.getMinZoneNum()));
            tuples.add(tuple);
        }
        newQueryResultTable("Fun fact #2", headers, tuples);
    }

    private void countZones() {
        HardinessQueryBuilder qb = new HardinessQueryBuilder();
        qb.makeCountZonesQuery();
        ThrivesInModel[] models = thrivesInDelegate.getThrivesInModelGroupByName(qb.getFinalQuery(), true, false, false);
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<ArrayList<String>> tuples = new ArrayList<>();
        headers.add("PLANT NAME");
        headers.add("NUMBER OF SUITABLE HARDINESS ZONES");
        for (ThrivesInModel model : models) {
            ArrayList<String> tuple = new ArrayList<>();
            tuple.add(model.getName());
            tuple.add(Integer.toString(model.getZoneNumCount()));
            tuples.add(tuple);
        }
        newQueryResultTable("Fun fact #1", headers, tuples);
    }

    private void newQueryResultTable(String name, ArrayList<String> headers, ArrayList<ArrayList<String>> tuples) {
        if (tableFrame!=null) {
            tableFrame.dispose();
        }
        tableFrame = new QueryResultTable(name, headers, tuples);
    }
}
