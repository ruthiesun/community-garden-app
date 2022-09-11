package forum.ActionListeners;

import forum.model.PostModel;
import forum.model.ThreadModel;
import forum.ui.ForumUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ThreadActionListener implements ActionListener {

    private String topic;
    private ThreadModel thread;
    private List<PostModel> posts;
    private ForumUI forumUI;

    public ThreadActionListener(String topic, ThreadModel thread, List<PostModel> posts, ForumUI forumUI) {
        super();

        this.topic = topic;
        this.thread = thread;
        this.posts = posts;
        this.forumUI = forumUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        forumUI.showPosts(topic, thread.getThreadName());
    }
}
