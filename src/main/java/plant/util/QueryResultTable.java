package plant.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Window that displays given data in a table
 */
public class QueryResultTable extends JFrame {
    private JPanel mainPanel;

    public QueryResultTable(String tableName, ArrayList<String> headers, ArrayList<ArrayList<String>> tuples) {
        super(tableName);
        setupTable(headers, tuples);
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setupTable(ArrayList<String> headers, ArrayList<ArrayList<String>> tuples) {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setLayout(new GridLayout(tuples.size() + 1,headers.size(),1, 1));
        mainPanel.setBorder(new EmptyBorder(3,3,3,3));

        addRow(headers);

        for (ArrayList<String> tuple : tuples) {
            addRow(tuple);
        }
    }

    private void addRow(ArrayList<String> tuples) {
        for (String tuple : tuples) {
            JPanel panel = new JPanel();
            JLabel label = new JLabel(tuple);
            panel.add(label);
            mainPanel.add(panel);
        }
    }


}
