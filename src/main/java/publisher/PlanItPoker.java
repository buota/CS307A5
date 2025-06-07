package publisher;

import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PlanItPoker {
    private static MainMenu mainMenu;
    private static Publisher publisher;
    private static Subscriber subscriber;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> mainMenu = new MainMenu());
    }

    public static void onSessionJoined(String sessionId, String userName, JFrame joinFrame) {
        joinFrame.dispose();

        if (mainMenu != null) {
            mainMenu.setVisible(false);
        }

        Repository repo = Repository.getInstance();
        repo.setSessionID(sessionId);
        repo.setName(userName);

        if (publisher == null) {
            publisher = new Publisher();
            new Thread(publisher).start();
        }
        if (subscriber == null) {
            subscriber = new Subscriber();
            new Thread(subscriber).start();
        }

        SwingUtilities.invokeLater(() -> {
            new StoryEntryScreen(sessionId, userName);
        });
    }
}
class MainMenu extends JFrame {
    public MainMenu() {
        super("PlanIt Poker - Main Menu");
        initializeGUI();
    }

    private void initializeGUI() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("PlanIt Poker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton joinSessionBtn = new JButton("Join/Create Session");
        JButton exitBtn = new JButton("Exit");

        Dimension buttonSize = new Dimension(200, 40);
        joinSessionBtn.setMaximumSize(buttonSize);
        exitBtn.setMaximumSize(buttonSize);

        joinSessionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        joinSessionBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new ModifiedJoinSession());
        });

        exitBtn.addActionListener(e -> System.exit(0));

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(joinSessionBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(exitBtn);

        add(mainPanel, BorderLayout.CENTER);

        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class ModifiedJoinSession extends JFrame {
    private JTextField sessionIdField;
    private JTextField nameField;
    private JButton joinButton;
    private JButton createButton;

    private static HashMap<String, ArrayList<String>> sessions = new HashMap<>();

    static {
        ArrayList<String> testSession = new ArrayList<>();
        testSession.add("Steven");
        testSession.add("Anthony");
        sessions.put("123456", testSession);
    }

    public ModifiedJoinSession() {
        super("Join Session - PlanItPoker");
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Join a Planning Poker Session");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        namePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel nameLabel = new JLabel("Your Name:");
        nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(300, 30));

        namePanel.add(nameLabel);
        namePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        namePanel.add(nameField);

        JPanel sessionPanel = new JPanel();
        sessionPanel.setLayout(new BoxLayout(sessionPanel, BoxLayout.Y_AXIS));
        sessionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sessionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel sessionLabel = new JLabel("Session ID:");
        sessionIdField = new JTextField(20);
        sessionIdField.setMaximumSize(new Dimension(300, 30));

        sessionPanel.add(sessionLabel);
        sessionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sessionPanel.add(sessionIdField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        joinButton = new JButton("Join Session");
        createButton = new JButton("Create New Session");

        joinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sessionId = sessionIdField.getText().trim();
                String userName = nameField.getText().trim();

                if (sessionId.isEmpty() || userName.isEmpty()) {
                    JOptionPane.showMessageDialog(ModifiedJoinSession.this,
                            "Please enter both your name and Session ID",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!sessions.containsKey(sessionId)) {
                    JOptionPane.showMessageDialog(ModifiedJoinSession.this,
                            "Session ID does not exist. Please check the ID or create a new session.",
                            "Session Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<String> participants = sessions.get(sessionId);
                participants.add(userName);
                sessions.put(sessionId, participants);

                JOptionPane.showMessageDialog(ModifiedJoinSession.this,
                        "Successfully joined session: " + sessionId + " as " + userName,
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                PlanItPoker.onSessionJoined(sessionId, userName, ModifiedJoinSession.this);
            }
        });

        createButton.addActionListener(e -> {
            String userName = nameField.getText().trim();
            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Launch Taiga login window, passing callback logic
            new TaigaLoginFrame((JSONArray stories) -> {
                // After successful login and fetch
                Repository.getInstance().setFetchedStories(stories);

                // Generate session
                String newSessionId = String.valueOf((int)(Math.random() * 900000) + 100000);
                ArrayList<String> newSession = new ArrayList<>();
                newSession.add(userName);
                sessions.put(newSessionId, newSession);

                // Store stories in Repository
                Repository.getInstance().setFetchedStories(stories);

                // Inform user
                JOptionPane.showMessageDialog(this,
                        "Session created! Session ID: " + newSessionId,
                        "Session Created", JOptionPane.INFORMATION_MESSAGE);

                // Transition to story entry / voting
                PlanItPoker.onSessionJoined(newSessionId, userName, this);
            }).setVisible(true);
        });


        buttonPanel.add(joinButton);
        buttonPanel.add(createButton);

        mainPanel.add(titleLabel);
        mainPanel.add(namePanel);
        mainPanel.add(sessionPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}