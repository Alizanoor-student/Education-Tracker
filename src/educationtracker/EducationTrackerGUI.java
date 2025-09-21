package educationtracker;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EducationTrackerGUI extends JFrame {

    private DefaultListModel<Subject> subjectListModel = new DefaultListModel<>();
    private JList<Subject> subjectJList = new JList<>(subjectListModel);
    private List<Subject> subjects = new ArrayList<>();
    private JPanel progressPanel = new JPanel();

    public EducationTrackerGUI() {
        setTitle("Education Tracker");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        initUI();
        setVisible(true);
    }

    private void initUI() {
        // Gradient Title
        JLabel title = new JLabel("📚 Education Tracker 📊", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(0, 102, 204), getWidth(), 0, new Color(0, 204, 153)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        title.setFont(new Font("Verdana", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setOpaque(false);
        title.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Left Panel - Subjects
        JPanel leftPanel = createRoundedPanel(new Color(220, 240, 255), "Subjects");
        subjectJList.setFont(new Font("Arial", Font.PLAIN, 16));
        subjectJList.setBackground(new Color(245, 255, 255));
        subjectJList.setForeground(new Color(0, 0, 70));
        leftPanel.add(new JScrollPane(subjectJList), BorderLayout.CENTER);

        JPanel leftButtons = createButtonPanel();
        JButton addSub = createHoverButton("Add Subject");
        JButton delSub = createHoverButton("Delete Subject");
        JButton report = createHoverButton("Show Report");
        leftButtons.add(addSub);
        leftButtons.add(delSub);
        leftButtons.add(report);
        leftPanel.add(leftButtons, BorderLayout.SOUTH);

        // Right Panel - Topics
        JPanel rightPanel = createRoundedPanel(new Color(255, 245, 220), "Topics");
        DefaultListModel<Topic> topicModel = new DefaultListModel<>();
        JList<Topic> topicJList = new JList<>(topicModel);
        topicJList.setFont(new Font("Tahoma", Font.PLAIN, 14));
        topicJList.setBackground(new Color(255, 255, 235));
        topicJList.setForeground(new Color(50, 50, 0));
        rightPanel.add(new JScrollPane(topicJList), BorderLayout.CENTER);

        JPanel topicButtons = createButtonPanel();
        JButton addTopic = createHoverButton("Add Topic");
        JButton editTopic = createHoverButton("Edit %");
        JButton delTopic = createHoverButton("Delete Topic");
        topicButtons.add(addTopic);
        topicButtons.add(editTopic);
        topicButtons.add(delTopic);
        rightPanel.add(topicButtons, BorderLayout.SOUTH);

        // Center Split
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        centerSplit.setResizeWeight(0.4);
        centerSplit.setOneTouchExpandable(true);
        add(centerSplit, BorderLayout.CENTER);

        // Bottom Progress Panel
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 150, 0), 2),
                "Progress", 0, 0, new Font("Arial", Font.BOLD, 16), new Color(0, 150, 0)));
        JScrollPane progressScroll = new JScrollPane(progressPanel);
        progressScroll.setPreferredSize(new Dimension(900, 150));
        progressScroll.getVerticalScrollBar().setUnitIncrement(10);
        add(progressScroll, BorderLayout.SOUTH);

        // ----- Listeners -----
        addSub.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter subject name:");
            if (name != null && !name.trim().isEmpty()) {
                Subject s = new Subject(name.trim());
                subjects.add(s);
                subjectListModel.addElement(s);
                updateProgressBars();
            }
        });

        delSub.addActionListener(e -> {
            int idx = subjectJList.getSelectedIndex();
            if (idx != -1) {
                subjects.remove(idx);
                subjectListModel.remove(idx);
                topicModel.clear();
                updateProgressBars();
            }
        });

        report.addActionListener(e -> {
            String r = ProgressReport.generateReport(subjects);
            JOptionPane.showMessageDialog(this, r, "Progress Report", JOptionPane.INFORMATION_MESSAGE);
        });

        subjectJList.addListSelectionListener(e -> {
            topicModel.clear();
            Subject s = subjectJList.getSelectedValue();
            if (s != null) {
                for (Topic t : s.getTopics()) topicModel.addElement(t);
            }
        });

        addTopic.addActionListener(e -> {
            Subject s = subjectJList.getSelectedValue();
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Select a subject first.");
                return;
            }
            String tname = JOptionPane.showInputDialog(this, "Enter topic name:");
            String perc = JOptionPane.showInputDialog(this, "Enter completion % (0-100):");
            try {
                int p = Integer.parseInt(perc);
                Topic t = new Topic(tname, p);
                s.addTopic(t);
                topicModel.addElement(t);
                subjectJList.repaint();
                updateProgressBars();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid %");
            }
        });

        editTopic.addActionListener(e -> {
            Subject s = subjectJList.getSelectedValue();
            Topic t = topicJList.getSelectedValue();
            if (s == null || t == null) {
                JOptionPane.showMessageDialog(this, "Select subject & topic first.");
                return;
            }
            String perc = JOptionPane.showInputDialog(this, "Enter new %:", t.getCompletion());
            try {
                int p = Integer.parseInt(perc);
                t.setCompletion(p);
                topicJList.repaint();
                subjectJList.repaint();
                updateProgressBars();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid %");
            }
        });

        delTopic.addActionListener(e -> {
            Subject s = subjectJList.getSelectedValue();
            int idx = topicJList.getSelectedIndex();
            if (s != null && idx != -1) {
                s.removeTopicAt(idx);
                topicModel.remove(idx);
                subjectJList.repaint();
                updateProgressBars();
            }
        });
    }

    // Rounded Panel
    private JPanel createRoundedPanel(Color bgColor, String title) {
        JPanel panel = new JPanel(new BorderLayout(5, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    // Button panel container
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        return panel;
    }

    // Button with hover and black text
    private JButton createHoverButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(220, 220, 220));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 35));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(200, 200, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(220, 220, 220));
            }
        });
        return btn;
    }

    // Update progress bars dynamically
    private void updateProgressBars() {
        progressPanel.removeAll();
        for (Subject s : subjects) {
            JProgressBar bar = new JProgressBar(0, 100) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(getForeground());
                    int width = (int) ((getWidth() - 4) * getPercentComplete());
                    g2.fillRoundRect(2, 2, width, getHeight() - 4, 20, 20);
                }
            };
            int val = s.getProgressPercent();
            bar.setValue(val);
            bar.setStringPainted(true);
            bar.setString(s.getName() + " - " + val + "%");
            bar.setPreferredSize(new Dimension(880, 25));

            // Fixed color assignment
            if (val <= 40) bar.setForeground(new Color(220, 0, 0));
            else if (val <= 70) bar.setForeground(new Color(255, 165, 0));
            else bar.setForeground(new Color(0, 150, 0));

            progressPanel.add(bar);
            progressPanel.add(Box.createVerticalStrut(5));
        }
        progressPanel.revalidate();
        progressPanel.repaint();
    }
}
