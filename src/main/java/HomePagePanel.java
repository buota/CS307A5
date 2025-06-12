/**
 * Logan Lumetta 5/29
 * Class to display the home page
 */
import javax.swing.*;
import java.awt.*;

public class HomePagePanel extends JPanel {

    private HomePageNanny homePageNanny;

    public HomePagePanel(HomePageNanny homePageNanny) {
        this.homePageNanny = homePageNanny;

        setLayout(new GridLayout(3, 1));

        JLabel title = new JLabel("Home Page");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JPanel buttonPanel1 = new JPanel();
        JButton createRoomButton = new JButton("Create Room");
        buttonPanel1.add(createRoomButton);
        add(buttonPanel1);

        JPanel buttonPanel2 = new JPanel();
        JButton joinRoomButton = new JButton("Join Room");
        buttonPanel2.add(joinRoomButton);
        add(buttonPanel2);

        createRoomButton.addActionListener(e -> homePageNanny.createRoom(Repository.getInstance().getName(), Repository.getInstance().getMode()));
        joinRoomButton.addActionListener(e -> homePageNanny.joinRoom(Repository.getInstance().getSessionID()));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Home Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        HomePageNanny nanny = new HomePageNanny();
        HomePagePanel panel = new HomePagePanel(nanny);

        frame.add(panel);
        frame.setVisible(true);
    }
}
