package forum.ui;

import delegates.ForumDelegate;
import forum.ActionListeners.ForumActionListener;
import forum.ActionListeners.ThreadActionListener;
import forum.model.ForumModel;
import forum.model.PostModel;
import forum.model.ThreadModel;
import ui.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class ForumUI extends JPanel{

    private final ForumDelegate delegate;
    private ForumModel forumModel;
    private HashMap<String, List<ThreadModel>> forum;
    private final MainWindow mainWindow;

    private JPanel topicPanel;
    private int topicCounter;
    private GridBagConstraints gbcTopics;


    private JPanel threadPanel;
    private JButton backToTopics;
    private int threadCounter;
    private GridBagConstraints gbcThreads;

    private JScrollPane scrollPosts;
    private JPanel postsPanel;
    private JButton backToPosts;
    private int postCounter;
    private GridBagConstraints gbcPosts;



    public ForumUI(ForumDelegate delegate, MainWindow mainWindow) {
        this.delegate = delegate;
        this.mainWindow = mainWindow;
    }

    public void queryForForumAndThreads() {
        forumModel = delegate.getForumModel();
        forum = forumModel.getForum();
    }

    public void showForum() {
        resetFrame();
        topicCounter = 0;
        queryForForumAndThreads();
        createAndShowTopics();
        setFrameBehaviour(topicPanel);
    }

    private void setFrameBehaviour(Component component) {
        mainWindow.add(component);
        mainWindow.refresh();
    }

    private void createAndShowTopics() {
        createTopicPanel();
        addTopics();
    }

    private void createTopicPanel() {
        topicPanel = new JPanel();
        topicPanel.setLayout(new GridBagLayout());
        topicPanel.setBorder(new EmptyBorder(MainWindow.BORDER_WIDTH,MainWindow.BORDER_WIDTH,MainWindow.BORDER_WIDTH,MainWindow.BORDER_WIDTH));
        gbcTopics = new GridBagConstraints();
        gbcTopics.gridx = 0;
        gbcTopics.fill = GridBagConstraints.HORIZONTAL;
        gbcTopics.anchor = GridBagConstraints.PAGE_START;
    }

    private void addTopics() {

        JLabel topicLabel = new JLabel("Forum Topics");
        topicLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbcTopics.gridy = topicCounter++;
        topicPanel.add(topicLabel, gbcTopics);

        gbcTopics.gridy = topicCounter++;
        JSeparator jSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        Dimension size = new Dimension(
                jSeparator.getMaximumSize().width,
                10
        );
        jSeparator.setMaximumSize(size);
        topicPanel.add(jSeparator, gbcTopics);

        gbcTopics.gridy = topicCounter++;
        topicPanel.add(Box.createRigidArea(new Dimension(10, 10)), gbcTopics);


        for (String topic : forum.keySet()) {
            List<ThreadModel> threads = forum.get(topic);

            TopicJButton btn = new TopicJButton(topic, threads);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setPreferredSize(new Dimension(btn.getMaximumSize().width, btn.getPreferredSize().height));
            btn.addActionListener(new ForumActionListener(topic, this));

            gbcTopics.gridy = topicCounter++;
            topicPanel.add(btn, gbcTopics);

            gbcTopics.gridy = topicCounter++;
            topicPanel.add(Box.createRigidArea(new Dimension(10, 10)), gbcTopics);

        }

        gbcTopics.weightx = 1;
        gbcTopics.weighty = 1;
        topicPanel.add(new JLabel(" "), gbcTopics);
    }

    public void showThreads(String topic) {
        queryForForumAndThreads(); // Get the latest data
        List<ThreadModel> threads = forum.get(topic);

        resetFrame();
        threadCounter = 0;
        createThreadPanel();
        createThreadHeader(topic);
        createThreads(topic, threads);

        setFrameBehaviour(threadPanel);
        mainWindow.repaint();
    }

    private void createThreads(String topic, List<ThreadModel> threads) {

        for (ThreadModel threadModel : threads) {

            // Press the thread to enter it
            ThreadJButton btn = new ThreadJButton(threadModel.getThreadName(), threadModel.getPosts());

            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setPreferredSize(new Dimension(btn.getMaximumSize().width, btn.getPreferredSize().height));
            btn.addActionListener(new ThreadActionListener(topic, threadModel, threadModel.getPosts(), this));
            gbcThreads.gridx = 0;
            gbcThreads.gridwidth = 2;
            gbcThreads.gridy = threadCounter;
            threadPanel.add(btn, gbcThreads);

            // Create JButtons to delete a thread... this should be greyed out if the user does not own the thread
            JButton editThreadBtn = new JButton(" Delete this thread ");
            editThreadBtn.setHorizontalAlignment(SwingConstants.CENTER);
            editThreadBtn.setPreferredSize(new Dimension(editThreadBtn.getMaximumSize().width, btn.getPreferredSize().height));
            editThreadBtn.addActionListener(e -> {
                if (askUserInput("Delete thread?", "Delete this thread? This can't be undone!")) {
                    delegate.deleteThread(threadModel.getThreadName());
                    showThreads(topic);
                }
            });

            if (!threadModel.isThreadOwnership()) {
                editThreadBtn.setVisible(false);
            }

            gbcThreads.gridx = 2;
            gbcThreads.gridwidth = 1;
            gbcThreads.gridy = threadCounter++;
            threadPanel.add(editThreadBtn, gbcThreads);

            // Border for spacing purposes
            gbcThreads.gridx = 0;
            gbcThreads.gridwidth = 3;
            gbcThreads.gridy = threadCounter++;
            threadPanel.add(Box.createRigidArea(new Dimension(10, 10)), gbcThreads);

        }

        gbcThreads.weightx = 1;
        gbcThreads.weighty = 1;
        threadPanel.add(new JLabel(" "), gbcThreads);
    }

    private void createThreadPanel() {
        threadPanel = new JPanel();
        threadPanel.setLayout(new GridBagLayout());
        threadPanel.setBorder(new EmptyBorder(10,10,10,10));
        gbcThreads = new GridBagConstraints();
        gbcThreads.gridx = 0;
        gbcThreads.fill = GridBagConstraints.HORIZONTAL;
        gbcThreads.anchor = GridBagConstraints.PAGE_START;
    }

    private void createThreadHeader(String topic) {
        createBackToTopicButton();
        createNewThreadButton(topic);
        createThreadHeaderInfo(topic);
    }

    private void createThreadHeaderInfo(String topic) {


        JLabel threadLabel = new JLabel(topic);
        threadLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbcThreads.gridx = 0;
        gbcThreads.gridwidth = 3;
        gbcThreads.gridy = ++threadCounter;
        threadPanel.add(threadLabel, gbcThreads);

        gbcThreads.gridy = ++threadCounter;
        JSeparator jSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        Dimension size = new Dimension(
                jSeparator.getMaximumSize().width,
                10
        );
        jSeparator.setMaximumSize(size);
        threadPanel.add(jSeparator, gbcThreads);

        gbcThreads.gridy = ++threadCounter;
        threadPanel.add(Box.createRigidArea(new Dimension(10, 10)), gbcThreads);

        gbcThreads.gridy = ++threadCounter;
        threadPanel.add(Box.createRigidArea(new Dimension(10, 10)), gbcThreads);
    }

    private void createBackToTopicButton() {
        backToTopics = new JButton(" << BACK");
        backToTopics.addActionListener(e -> showForum());
        gbcThreads.gridx = 0;
        gbcThreads.gridy = threadCounter;
        threadPanel.add(backToTopics, gbcThreads);
    }

    private void createNewThreadButton(String topic) {
        JLabel spacer = new JLabel(" ");
        gbcThreads.weightx = 1;
        gbcThreads.gridx = 1;
        gbcThreads.gridy = threadCounter;
        threadPanel.add(spacer, gbcThreads);

        ForumUI forumUI = this;

        JButton createNewThread = new JButton(" Create Thread ");
        createNewThread.addActionListener(e -> {
            // Clear pane
            resetFrame();

            // Dialog to add a new thread
            NewThreadUI newThreadUI = new NewThreadUI(topic, forumUI);
            setFrameBehaviour(newThreadUI.getPanel());
        });
        gbcThreads.weightx = 0;
        gbcThreads.gridx = 2;
        gbcThreads.gridy = threadCounter;
        threadPanel.add(createNewThread, gbcThreads);
    }

    public void showPosts(String topic, String desiredThread) {
        queryForForumAndThreads();
        List<ThreadModel> threads = forum.get(topic);
        List<PostModel> posts = null;

        // A hacky way to get the posts...
        for (ThreadModel threadModel: threads) {
            if (threadModel.getThreadName().equals(desiredThread)) {
                posts = threadModel.getPosts();
            }
        }

        if (posts != null) {
            resetFrame();
            postCounter = 0;
            createScrollPane();
            createPostsPanel();
            createPostsHeader(desiredThread, topic);
            createPosts(posts, topic, desiredThread);
            createNewPostButton(topic, desiredThread);

            scrollPosts.setViewportView(postsPanel);
            setFrameBehaviour(scrollPosts);
        }

        
    }

    private void createPosts(List<PostModel> posts, String topic, String threadName) {

        for (PostModel postModel : posts) {


            // Author information
            JLabel author = new JLabel(formatPostInfo(postModel));
            author.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
            gbcPosts.weightx = 0;
            gbcPosts.gridwidth = 2;
            gbcPosts.gridx = 0;
            gbcPosts.gridy = postCounter;
            postsPanel.add(author, gbcPosts);

            // The post content
            JLabel postNotes = new JLabel(postModel.getPostNotes());
            postNotes.setBorder(new EmptyBorder(0,10,5,0));
            postNotes.setPreferredSize(new Dimension(postNotes.getMaximumSize().width, author.getPreferredSize().height));

            gbcPosts.weightx = 1;
            gbcPosts.gridwidth = 0;
            gbcPosts.gridx = 2;
            gbcPosts.gridy = postCounter;
            postsPanel.add(postNotes, gbcPosts);

            // Create JButtons to delete a post... this should be greyed out if the user does not own the post
            JButton editPostBtn = new JButton(" Delete this post ");
            editPostBtn.setHorizontalAlignment(SwingConstants.CENTER);
            editPostBtn.setPreferredSize(new Dimension(editPostBtn.getMaximumSize().width, postNotes.getPreferredSize().height));
            // Do not allow the user to delete if
            //      They do not own the post
            //      It is the first post in the thread (they have to delete the thread)
            if (!postModel.isPostOwnership() || postModel.getPostID() == 1) {
                editPostBtn.setVisible(false);
            }
            editPostBtn.addActionListener(e -> {
                if (askUserInput("Delete post?", "Delete this post? This can't be undone!")) {
                    delegate.deletePostFromUser(topic, threadName, postModel.getPostID(), postModel.getPostNotes());
                    showPosts(topic, threadName);
                }
            });



            gbcPosts.gridx = 3;
            gbcPosts.gridwidth = 1;
            gbcPosts.gridy = postCounter++;
            postsPanel.add(editPostBtn, gbcPosts);

            // Border for spacing
            gbcPosts.gridx = 0;
            gbcPosts.gridwidth = 3;
            gbcPosts.gridy = postCounter++;
            postsPanel.add(Box.createRigidArea(new Dimension(10, 10)), gbcPosts);

        }

        gbcPosts.weightx = 1;
        gbcPosts.weighty = 1;
        postsPanel.add(new JLabel(" "), gbcPosts);
    }

    private String formatPostInfo(PostModel postModel) {

        String ret = "<html>";
        ret += "<b>" + postModel.getPostUser() + "</b><br>";
        ret += postModel.getPostDate() + "<br>";
        ret += "</html>";

        return ret;
    }

    private void createBackToThreadButton(String topic) {
        backToPosts = new JButton(" << BACK");
        backToPosts.addActionListener(e -> showThreads(topic));
        gbcPosts.gridx = 0;
        gbcPosts.gridy = postCounter;
        postsPanel.add(backToPosts, gbcPosts);

        // Allows the username field to be slightly bigger horizontally
        JLabel spacer = new JLabel("<html>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</html>");
        gbcPosts.weightx = 0;
        gbcPosts.gridx = 1;
        gbcPosts.gridy = postCounter;
        postsPanel.add(spacer, gbcPosts);

        spacer = new JLabel(" ");
        gbcPosts.weightx = 1;
        gbcPosts.gridx = 2;
        gbcPosts.gridy = postCounter;
        postsPanel.add(spacer, gbcPosts);
    }


    private void createPostsHeader(String threadName, String topic) {
        createBackToThreadButton(topic);
        createPostHeaderInfo(threadName);
    }

    private void createPostHeaderInfo(String threadName) {

        JLabel threadLabel = new JLabel(threadName);
        threadLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbcPosts.gridx = 0;
        gbcPosts.gridwidth = 5;
        gbcPosts.gridy = ++postCounter;
        postsPanel.add(threadLabel, gbcPosts);

        gbcPosts.gridy = ++postCounter;
        JSeparator jSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        Dimension size = new Dimension(
                jSeparator.getMaximumSize().width,
                10
        );
        jSeparator.setMaximumSize(size);
        postsPanel.add(jSeparator, gbcPosts);

        gbcPosts.gridy = ++postCounter;
        postsPanel.add(Box.createRigidArea(new Dimension(10, 10)), gbcPosts);

        gbcPosts.gridy = ++postCounter;
        postsPanel.add(Box.createRigidArea(new Dimension(10, 10)), gbcPosts);
    }

    private void createNewPostButton(String topic, String threadName) {

        JTextArea textPane = new JTextArea(3, 1);
        textPane.setLineWrap(true);
        gbcPosts.gridx = 0;
        gbcPosts.weightx = 1;
        gbcPosts.gridwidth = 3;
        gbcPosts.gridy = ++postCounter;
        gbcPosts.anchor = GridBagConstraints.PAGE_END;
        postsPanel.add(textPane, gbcPosts);


        TextAreaButton createNewReply = new TextAreaButton(" New Reply ", textPane);
        createNewReply.setPreferredSize(new Dimension(createNewReply.getPreferredSize().width, textPane.getPreferredSize().height));
        createNewReply.addActionListener(e -> {
            String reply = createNewReply.getTextAreaString();

            if (reply == null || reply.equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "Post can't be blank!", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

            delegate.insertPost(threadName, reply);
            showPosts(topic, threadName);
        });
        gbcPosts.weightx = 0;
        gbcPosts.gridx = 3;
        gbcPosts.gridy = postCounter;
        postsPanel.add(createNewReply, gbcPosts);
    }

    private void createScrollPane() {
        scrollPosts = new JScrollPane();
    }

    private void createPostsPanel() {
        postsPanel = new JPanel();
        postsPanel.setLayout(new GridBagLayout());
        postsPanel.setBorder(new EmptyBorder(10,10,10,10));
        gbcPosts = new GridBagConstraints();
        gbcPosts.gridx = 0;
        gbcPosts.fill = GridBagConstraints.HORIZONTAL;
        gbcPosts.anchor = GridBagConstraints.PAGE_START;
    }

    private void resetFrame() {
        mainWindow.resetFrame();
    }

    public void insertThread(String topic, String threadName, String post) {

        threadName = topic + ": " + threadName;

        delegate.insertThread(topic, threadName);
        delegate.insertPost(threadName, post);
    }

    private boolean askUserInput(String title, String message) {
        int n = JOptionPane.showOptionDialog(new JFrame(), message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
                new Object[] { "Yes", "No" }, JOptionPane.YES_OPTION);


        return n == JOptionPane.YES_OPTION;

    }
}
