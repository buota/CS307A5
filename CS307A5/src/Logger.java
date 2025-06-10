import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    public enum Level {
        TRACE(0, "TRACE"),
        INFO(1, "INFO"),
        WARN(2, "WARN"),
        ERROR(3, "ERROR");
        
        private final int value;
        private final String name;
        
        Level(int value, String name) {
            this.value = value;
            this.name = name;
        }
        
        public int getValue() { return value; }
        public String getName() { return name; }
    }
    
    private Level currentLevel = Level.TRACE;
    private List<Appender> appenders = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    public Logger() {
        addAppender(new ConsoleAppender());
        addAppender(new FileAppender("application.log"));
    }
    
    public Logger(String logFile) {
        addAppender(new ConsoleAppender());
        addAppender(new FileAppender(logFile));
    }
    
    public void trace(String message) {
        log(Level.TRACE, message);
    }
    
    public void info(String message) {
        log(Level.INFO, message);
    }
    
    public void warn(String message) {
        log(Level.WARN, message);
    }
    
    public void error(String message) {
        log(Level.ERROR, message);
    }
    
    public void error(String message, Throwable throwable) {
        log(Level.ERROR, message + "\n" + getStackTrace(throwable));
    }
 
    public void setLevel(Level level) {
        this.currentLevel = level;
    }
    
    public void addAppender(Appender appender) {
        this.appenders.add(appender);
    }
    
    public void clearAppenders() {
        this.appenders.clear();
    }
    

    private void log(Level level, String message) {
        if (level.getValue() >= currentLevel.getValue()) {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] %s - %s", timestamp, level.getName(), message);
            
            for (Appender appender : appenders) {
                appender.append(logEntry);
            }
        }
    }
    
    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
    
    public interface Appender {
        void append(String message);
    }

    public static class ConsoleAppender implements Appender {
        @Override
        public void append(String message) {
            System.out.println(message);
        }
    }
    
    public static class FileAppender implements Appender {
        private String filename;
        
        public FileAppender(String filename) {
            this.filename = filename;
        }
        
        @Override
        public void append(String message) {
            try (FileWriter writer = new FileWriter(filename, true);
                 BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                System.err.println("Failed to write to log file: " + e.getMessage());
            }
        }
    }
    
    public static Logger getLogger() {
        return new Logger();
    }
    
    public static Logger getLogger(String logFile) {
        return new Logger(logFile);
    }
    
}
