package forum.ui;

import javax.swing.*;

public class TextAreaButton extends JButton {

    private JTextArea jTextArea;
    private JTextArea secondaryJTextArea;

    public TextAreaButton(String name, JTextArea jTextArea) {
        super(name);
        this.jTextArea = jTextArea;
    }

    public TextAreaButton(String name, JTextArea jTextArea, JTextArea secondaryJTextArea) {
        super(name);
        this.jTextArea = jTextArea;
        this.secondaryJTextArea = secondaryJTextArea;
    }

    public String getTextAreaString() {
        return jTextArea.getText();
    }

    public String getSecondaryTextAreaString() { return secondaryJTextArea.getText(); }
}
