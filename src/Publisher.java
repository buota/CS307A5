
import org.eclipse.paho.client.mqttv3.*;

import java.util.Observable;
import java.util.Observer;

public class Publisher implements Runnable, Observer {
    private MqttClient client;
    private final String broker = "tcp://broker.hivemq.com:1883";
    private final String topic = "software/360";
    private final String clientId = "ASU-publisher";

    @Override
    public void run() {
        try {
            client = new MqttClient(broker, clientId);
            client.connect();
            System.out.println("Connected to broker: " + broker);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (client != null && client.isConnected()) {
            Repository repo = (Repository) o;
            int x = repo.getX();
            int y = repo.getY();
            String message = x + " " + y;

            try {
                client.publish(topic, new MqttMessage(message.getBytes()));
                System.out.println("Published: " + message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Client not connected. Cannot publish.");
        }
    }
}
