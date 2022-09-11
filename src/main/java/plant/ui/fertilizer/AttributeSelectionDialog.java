package plant.ui.fertilizer;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/**
 * Dialog that asks user to select fertilizer attributes to view
 */
public class AttributeSelectionDialog extends Observable implements ActionListener {
    JDialog mainFrame;
    JCheckBox npkBox;
    JCheckBox typeBox;
    JCheckBox descriptionBox;
    JLabel prompt;
    JButton enter;
    JFrame dialogParent;
    String header;
    boolean modal;

    public JDialog getMainFrame() {
        return mainFrame;
    }
    public AttributeSelectionDialog(JFrame dialogParent, String header, boolean modal) {
        this.dialogParent = dialogParent;
        this.header = header;
        this.modal = modal;

        prompt = new JLabel("What details do you want to know?");
        npkBox = new JCheckBox("NPK ratio");
        typeBox = new JCheckBox("Type");
        descriptionBox = new JCheckBox("Additional details");
        enter = new JButton("Enter");
        enter.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean[] checked = new boolean[3];
        checked[0] = npkBox.isSelected();
        checked[1] = typeBox.isSelected();
        checked[2] = descriptionBox.isSelected();
        setChanged();
        notifyObservers(checked);
    }

    public void popup() {
        mainFrame = new JDialog(dialogParent,header, modal);
        mainFrame.getContentPane().setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
        mainFrame.add(prompt);
        mainFrame.add(npkBox);
        mainFrame.add(typeBox);
        mainFrame.add(descriptionBox);
        mainFrame.add(enter);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public void dispose() {
        mainFrame.dispose();
    }
}
