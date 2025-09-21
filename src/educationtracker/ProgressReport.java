package educationtracker;

import java.util.List;
import java.util.StringJoiner;

public class ProgressReport {

    public static String generateReport(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty()) return "No subjects available.";

        StringJoiner sj = new StringJoiner("\n");
        sj.add("=== Progress Report ===");

        int total = 0;
        for (Subject s : subjects) {
            int p = s.getProgressPercent();
            sj.add(s.getName() + ": " + p + "%");
            total += p;
        }

        int avg = Math.round((float) total / subjects.size());
        sj.add("----------------------");
        sj.add("Overall average: " + avg + "%");

        return sj.toString();
    }
}
