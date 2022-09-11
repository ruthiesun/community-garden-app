package forum.model;

import java.sql.Timestamp;

public class PostModel {

    private int postID;
    private String postNotes;
    private Timestamp postDate;
    private String postUser;

    private boolean postOwnership;

    public PostModel(int pID, String pNotes, Timestamp pDate, String pUser, String postOwnership) {
        this.postID = pID;
        this.postNotes = pNotes;
        this.postDate = pDate;
        this.postUser = pUser;
        this.postOwnership = postOwnership.equals("Y") ? true : false;
    }

    public int getPostID() {
        return postID;
    }

    public String getPostNotes() {
        return postNotes;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public String getPostUser() {
        return postUser;
    }

    public boolean isPostOwnership() {
        return postOwnership;
    }
}
