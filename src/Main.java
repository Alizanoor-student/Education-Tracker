package educationtracker;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ignore, use default
        }

        SwingUtilities.invokeLater(() -> new EducationTrackerGUI());
    }
}
