/**
 * @author: Bryce Uota
 */

package publisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JoinSession extends JFrame {
    private JTextField sessionIdField;
    private JTextField nameField;
    private JButton joinButton;
    private JButton createButton;

    private static HashMap<String, ArrayList<String>> sessions = new HashMap<>();

    public JoinSession() {
        // Set the title for the frame
        super("Join Session - PlanItPoker");

        // Set layout manager
        setLayout(new BorderLayout());

        // Create main panel with some padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create title label
        JLabel titleLabel = new JLabel("Join a Planning Poker Session");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create name panel
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

        // Create session ID panel
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

        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        joinButton = new JButton("Join Session");
        createButton = new JButton("Create New Session");

        // Add action listener for join button
        joinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sessionId = sessionIdField.getText().trim();
                String userName = nameField.getText().trim();

                // Debug info
                System.out.println("Attempting to join session: " + sessionId);
                System.out.println("Current sessions: " + sessions.keySet());
                System.out.println("Session exists check: " + sessions.containsKey(sessionId));

                // Validate inputs
                if (sessionId.isEmpty() || userName.isEmpty()) {
                    JOptionPane.showMessageDialog(JoinSession.this,
                            "Please enter both your name and Session ID",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if session exists
                if (!sessions.containsKey(sessionId)) {
                    JOptionPane.showMessageDialog(JoinSession.this,
                            "Session ID does not exist. Please check the ID or create a new session.",
                            "Session Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<String> participants = sessions.get(sessionId);

                // Add participant to session
                participants.add(userName);
                sessions.put(sessionId, participants);

                // Show success message
                JOptionPane.showMessageDialog(JoinSession.this,
                        "Successfully joined session: " + sessionId + " as " + userName,
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear fields
                sessionIdField.setText("");
                nameField.setText("");

                // Here would open the poker session window
                // Temp display the current participants
                StringBuilder participantsString = new StringBuilder("Current participants in session:\n");
                for (String participant : participants) {
                    participantsString.append("- ").append(participant).append("\n");
                }

                JOptionPane.showMessageDialog(JoinSession.this,
                        participantsString.toString(),
                        "Session Participants", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonPanel.add(joinButton);
        buttonPanel.add(createButton);

        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(namePanel);
        mainPanel.add(sessionPanel);
        mainPanel.add(buttonPanel);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Set the size of the window
        setSize(500, 400);

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Make the window visible
        setVisible(true);
    }
}
