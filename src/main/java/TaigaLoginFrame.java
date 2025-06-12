import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class TaigaLoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField slugField;
    private JButton loginButton;
    private Consumer<JSONArray> onLoginSuccess;

    public TaigaLoginFrame(Consumer<JSONArray> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;

        setTitle("Taiga Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Project Slug
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Project Slug:"), gbc);
        slugField = new JTextField(20);
        gbc.gridx = 1;
        add(slugField, gbc);

        // Login Button
        loginButton = new JButton("Login and Fetch Stories");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String slug = slugField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || slug.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loginButton.setEnabled(false);
        new Thread(() -> {
            try {
                String token = TaigaStoryFetcher.loginAndGetToken(username, password);
                int projectId = TaigaStoryFetcher.getProjectId(token, slug);
                JSONArray stories = TaigaStoryFetcher.fetchUserStories(token, projectId);

                // Pass stories to callback
                SwingUtilities.invokeLater(() -> {
                    onLoginSuccess.accept(stories);
                    dispose();
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    loginButton.setEnabled(true);
                });
            }
        }).start();
    }
}
