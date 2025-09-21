package educationtracker;

public class Topic {
    private String name;
    private int completion; // 0 to 100

    public Topic(String name, int completion) {
        this.name = name;
        setCompletion(completion);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCompletion() { return completion; }
    public void setCompletion(int completion) {
        if (completion < 0) completion = 0;
        if (completion > 100) completion = 100;
        this.completion = completion;
    }

    @Override
    public String toString() {
        return name + " (" + completion + "%)";
    }
}
