package delegates;

import forum.model.ForumModel;

public interface ForumDelegate {

    ForumModel getForumModel();

    void insertPost(String threadName, String reply);

    void insertThread(String topic, String threadName);

    void deletePostFromUser(String topic, String threadName, int postID, String postNotes);

    void deleteThread(String threadName);
}
