package forum.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NewThreadUI {

    private String topic;
    private JPanel panel;

    private ForumUI forumUI;

    public NewThreadUI(String topic, ForumUI forumUI) {
        this.topic = topic;
        this.forumUI = forumUI;

        initializePanel();
    }

    private void initializePanel() {
        // Create panel
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(30,30,30,30));

        int gbxIndex = 0;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;

        // Thread title
        JLabel threadNameLabel = new JLabel("Enter the name of your thread below");
        gbc.gridy = gbxIndex;
        panel.add(threadNameLabel, gbc);

        JTextArea threadTitle = new JTextArea(1, 1);
        threadTitle.setLineWrap(true);
        gbc.gridy = ++gbxIndex;
        panel.add(threadTitle, gbc);

        // Reply
        JLabel replyLabel = new JLabel("Enter the contents of your post");
        gbc.gridy = ++gbxIndex;
        panel.add(replyLabel, gbc);

        JTextArea postText = new JTextArea(5, 1);
        postText.setLineWrap(true);
        gbc.gridy = ++gbxIndex;
        panel.add(postText, gbc);

        TextAreaButton btn = new TextAreaButton("Create a new thread", threadTitle, postText);
        btn.addActionListener(e -> {
            String threadTitle1 = btn.getTextAreaString();
            String postText1 = btn.getSecondaryTextAreaString();

            if ((threadTitle1 == null || postText1 == null) ||
                    (threadTitle1.equals("") || postText1.equals(""))) {
                JOptionPane.showMessageDialog(new JFrame(), "Missing information!", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                forumUI.insertThread(topic, threadTitle1, postText1);
                forumUI.showThreads(topic);
            }
        });
        gbc.gridy = ++gbxIndex;
        panel.add(btn, gbc);

        // Add a spacer to pull everything to the top
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.add(new JLabel(" "), gbc);
    }

    public JPanel getPanel() {
        return panel;
    }


}
