package publisher;

/**
 * Logan Lumetta 5/28
 * Window to modify room creation settings
 *
 */

import javax.swing.*;
import java.awt.*;

public class CreateRoomPanel extends JPanel {

    private CreateRoomNanny createRoomNanny;
    private JTextField nameField;
    private JComboBox<String> comboBox;
    private CreateSessionID createSessionID;

    public CreateRoomPanel(CreateRoomNanny createRoomNanny) {
        this.createRoomNanny = createRoomNanny;
        this.createSessionID = new CreateSessionID();

        setLayout(new GridLayout(4, 1));

        JLabel title = new JLabel("Create new Room");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JPanel box1 = new JPanel();
        box1.setLayout(new GridLayout(1, 2));
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField("CSC307");
        box1.add(nameLabel);
        box1.add(nameField);
        add(box1);

        JPanel box2 = new JPanel();
        box2.setLayout(new GridLayout(1, 2));
        JLabel modeLabel = new JLabel("Mode:");
        String[] options = { "Scrum", "Fibonacci", "Sequential", "Hours", "T-shirt", "Custom deck" };
        comboBox = new JComboBox<>(options);
        box2.add(modeLabel);
        box2.add(comboBox);
        add(box2);

        JPanel box3 = new JPanel();
        JButton createButton = new JButton("Create");
        box3.add(createButton);
        add(box3);

        String sessionID = createSessionID.createSessionID();
        createButton.addActionListener(e ->
                createRoomNanny.createRoom(nameField.getText(), (String) comboBox.getSelectedItem(), sessionID)
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Create Room Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        CreateRoomNanny nanny = new CreateRoomNanny();
        CreateRoomPanel panel = new CreateRoomPanel(nanny);

        frame.add(panel);
        frame.setVisible(true);
    }
}
