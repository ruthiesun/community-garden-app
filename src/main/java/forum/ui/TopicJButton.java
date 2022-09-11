package forum.ui;

import forum.model.ThreadModel;

import javax.swing.*;
import java.util.List;

public class TopicJButton extends JButton {

    private List<ThreadModel> threads;

    public TopicJButton(String topic, List<ThreadModel> threads) {
        super(topic);

        this.threads = threads;
    }

    public List<ThreadModel> getThreads() {
        return threads;
    }
}
