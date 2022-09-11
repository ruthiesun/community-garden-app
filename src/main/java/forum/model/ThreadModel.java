package forum.model;


import java.util.ArrayList;
import java.util.List;

public class ThreadModel {

    private String threadName;
    private List<PostModel> posts;
    private boolean threadOwnership; // If a user owns a thread, they can delete it

    public ThreadModel(String threadName, String threadOwnership) {
        this.threadName = threadName;
        this.threadOwnership = threadOwnership.equals("Y") ? true : false;
        posts = new ArrayList<>();
    }

    public ThreadModel(String threadName, List<PostModel> posts) {
        this.threadName = threadName;
        this.threadOwnership = false;
        this.posts = posts;
    }

    public void addPost(PostModel post) {
        posts.add(post);
    }

    public String getThreadName() {
        return threadName;
    }

    public List<PostModel> getPosts() {
        return posts;
    }

    public boolean isThreadOwnership() { return threadOwnership; }

    public boolean checkThreadName(String threadName) {
        return this.threadName.equals(threadName);
    }
}
