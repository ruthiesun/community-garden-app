package forum.ActionListeners;

import forum.ui.ForumUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForumActionListener implements ActionListener {

    private String topic;
    private ForumUI forumUI;

    public ForumActionListener(String topic, ForumUI forumUI) {
        super();

        this.forumUI = forumUI;
        this.topic = topic;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        forumUI.showThreads(topic);
    }
}
