/**
 * @author: Bryce Uota
 * Main hub for PlanIt Poker application
 */

package publisher;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanItPoker {
    private static MainMenu mainMenu;
    private static Publisher publisher;
    private static Subscriber subscriber;
    private static Logger logger;
    private static Repository repository;
    private static Blackboard blackboard;
    
    private static Map<String, ArrayList<String>> activeSessions = new HashMap<>();

    public static void main(String[] args) {
        logger = Logger.getLogger("planitpoker.log");
        logger.info("Starting PlanIt Poker application");
        
        repository = Repository.getInstance();
        blackboard = new Blackboard();
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            logger.info("Look and feel set successfully");
        } catch (Exception e) {
            logger.error("Failed to set look and feel", e);
        }

        SwingUtilities.invokeLater(() -> {
            mainMenu = new MainMenu();
            logger.info("Main menu launched");
        });
    }

    public static void onSessionJoined(String sessionId, String userName, JFrame currentFrame) {
        logger.info("User " + userName + " joining session " + sessionId);
        
        currentFrame.dispose();
        
        if (mainMenu != null) {
            mainMenu.setVisible(false);
        }

        repository.setSessionID(sessionId);
        repository.setName(userName);
        repository.addParticipant(userName, userName);
        
        blackboard.addName(userName);
        blackboard.addCurrentRoom(sessionId);

        initializeMQTT();
        publishJoinEvent(sessionId, userName);

        SwingUtilities.invokeLater(() -> {
            new StoryEntryScreen(sessionId, userName);
            logger.info("Story entry screen launched for user " + userName);
        });
    }
    
    private static void initializeMQTT() {
        if (publisher == null) {
            publisher = Publisher.getInstance();
            new Thread(publisher).start();
            logger.info("Publisher initialized and started");
        }
        if (subscriber == null) {
            subscriber = new Subscriber();
            new Thread(subscriber).start();
            logger.info("Subscriber initialized and started");
        }
    }
    
    private static void publishJoinEvent(String sessionId, String userName) {
        JSONObject joinMsg = new JSONObject();
        joinMsg.put("type", "join");
        joinMsg.put("sessionId", sessionId);
        joinMsg.put("participantId", userName);
        joinMsg.put("displayName", userName);
        
        publisher.publish("software/360", joinMsg.toString());
        logger.info("Published join event for " + userName);
    }
    
    public static Map<String, ArrayList<String>> getActiveSessions() {
        return activeSessions;
    }
    
    public static void addSession(String sessionId, String creatorName) {
        ArrayList<String> participants = new ArrayList<>();
        participants.add(creatorName);
        activeSessions.put(sessionId, participants);
        logger.info("New session created: " + sessionId + " by " + creatorName);
    }
    
    public static boolean addParticipantToSession(String sessionId, String participantName) {
        if (activeSessions.containsKey(sessionId)) {
            activeSessions.get(sessionId).add(participantName);
            logger.info("Participant " + participantName + " added to session " + sessionId);
            return true;
        }
        logger.warn("Attempted to add participant to non-existent session: " + sessionId);
        return false;
    }
