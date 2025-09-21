package educationtracker;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private String name;
    private List<Topic> topics;

    public Subject(String name) {
        this.name = name;
        this.topics = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Topic> getTopics() { return topics; }

    public void addTopic(Topic topic) { topics.add(topic); }
    public void removeTopic(Topic topic) { topics.remove(topic); }
    public void removeTopicAt(int index) { topics.remove(index); }

    public int getProgressPercent() {
        if (topics.isEmpty()) return 0;
        int sum = 0;
        for (Topic t : topics) sum += t.getCompletion();
        return Math.round((float) sum / topics.size());
    }

    @Override
    public String toString() {
        return name + " — " + getProgressPercent() + "%";
    }
}
