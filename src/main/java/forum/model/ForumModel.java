package forum.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForumModel {

    private HashMap<String, List<ThreadModel>> forum;

    public ForumModel() {
        forum = new HashMap<>();
    }

    public void addThread(String topic, ThreadModel thread) {
        List<ThreadModel> threads;

        if (forum.get(topic) == null) {
            threads = new ArrayList<>();
        } else {
            threads = forum.get(topic);
        }

        threads.add(thread);
        forum.put(topic, threads);
    }

    public boolean threadExistsInForum(String topic, String threadName) {
        if (forum.get(topic) != null) {
            List<ThreadModel> threads = forum.get(topic);

            for (ThreadModel thread : threads) {
                if (thread.checkThreadName(threadName)) return true;
            }
        }

        return false;
    }

    public ThreadModel getThreadInForum(String topic, String threadName) {

        if (forum.get(topic) != null) {
            List<ThreadModel> threads = forum.get(topic);

            for (ThreadModel thread : threads) {
                if (thread.checkThreadName(threadName)) return thread;
            }
        }

        return null;
    }

    public HashMap<String, List<ThreadModel>> getForum() {
        return forum;
    }

}
