package cathywu.mailmonitor.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzwu
 * @since 7/30/15
 */
public class TaskPool {

    private static TaskPool instance = null;

    private Map<String, List<MailTask>> tasks;

    public static TaskPool getInstance() {
        if (instance == null) {
            instance = new TaskPool();
        }
        return instance;
    }

    private TaskPool() {
        this.tasks = new ConcurrentHashMap<String, List<MailTask>>();
    }

    public void addUserInfo(String userName) {
        this.tasks.put(userName, new ArrayList<MailTask>());
    }

    public void addTask(String userName, MailTask task) {
        if (!this.tasks.containsKey(userName)) {
            addUserInfo(userName);
        }
        this.tasks.get(userName).add(task);
    }
}
