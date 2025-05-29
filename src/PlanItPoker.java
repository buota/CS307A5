import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PlanItPoker {
    private static MainMenu mainMenu;

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main menu
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainMenu = new MainMenu();
            }
        });
    }

    // Callback method for when session is successfully joined
    public static void onSessionJoined(String sessionId, String userName, JFrame joinFrame) {
        // Close the join session window
        joinFrame.dispose();

        // Hide main menu
        if (mainMenu != null) {
            mainMenu.setVisible(false);
        }

        // Create and show your Voting class instance
        SwingUtilities.invokeLater(() -> {
            Voting voting = new Voting();

            // Register the user who just joined
            voting.registerParticipant("current_user", userName);

            // Show voting popup for this user
            voting.showVotingPopup("current_user");
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

        // Title
        JLabel titleLabel = new JLabel("PlanIt Poker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        JButton joinSessionBtn = new JButton("Join/Create Session");
        JButton exitBtn = new JButton("Exit");

        // Set button properties
        Dimension buttonSize = new Dimension(200, 40);
        joinSessionBtn.setMaximumSize(buttonSize);
        exitBtn.setMaximumSize(buttonSize);

        joinSessionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button actions
        joinSessionBtn.addActionListener(e -> {
            // Run modified JoinSession class
            SwingUtilities.invokeLater(() -> new ModifiedJoinSession());
        });

        exitBtn.addActionListener(e -> System.exit(0));

        // Add components
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

// Modified version of your JoinSession that integrates with the flow
class ModifiedJoinSession extends JFrame {
    private JTextField sessionIdField;
    private JTextField nameField;
    private JButton joinButton;
    private JButton createButton;

    // Mock database to store sessions and participants
    private static HashMap<String, ArrayList<String>> sessions = new HashMap<>();

    static {
        // Create test session
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

        // Name panel
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

        // Session ID panel
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

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        joinButton = new JButton("Join Session");
        createButton = new JButton("Create New Session");

        // Join button action
        joinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sessionId = sessionIdField.getText().trim();
                String userName = nameField.getText().trim();

                // Validate inputs
                if (sessionId.isEmpty() || userName.isEmpty()) {
                    JOptionPane.showMessageDialog(ModifiedJoinSession.this,
                            "Please enter both your name and Session ID",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if session exists
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

                // Call the callback to transition to voting
                PlanItPoker.onSessionJoined(sessionId, userName, ModifiedJoinSession.this);
            }
        });

        // Create button action
        createButton.addActionListener(e -> {
            String userName = nameField.getText().trim();
            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate random session ID
            String newSessionId = String.valueOf((int)(Math.random() * 900000) + 100000);
            ArrayList<String> newSession = new ArrayList<>();
            newSession.add(userName);
            sessions.put(newSessionId, newSession);

            JOptionPane.showMessageDialog(this,
                    "Session created! Session ID: " + newSessionId,
                    "Session Created", JOptionPane.INFORMATION_MESSAGE);

            // Transition to voting
            PlanItPoker.onSessionJoined(newSessionId, userName, this);
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