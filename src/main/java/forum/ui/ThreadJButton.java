package forum.ui;

import forum.model.PostModel;

import javax.swing.*;
import java.util.List;

public class ThreadJButton extends JButton {

    private List<PostModel> posts;

    public ThreadJButton(String topic, List<PostModel> posts) {
        super(topic);

        this.posts = posts;
    }

    public List<PostModel> getPosts() {
        return posts;
    }
}
