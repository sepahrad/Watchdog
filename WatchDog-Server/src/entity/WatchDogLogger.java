package entity;

import java.io.FileOutputStream;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class WatchDogLogger {
    
    private static final Logger logger = Logger.getLogger(WatchDogLogger.class.getName());
    private static Handler handler;
    private final static String FILENAME = "server.log";
    
    static {
        try {
            FileOutputStream out = new FileOutputStream(FILENAME);
            handler = new StreamHandler(out, new Formatter() {
                @Override
                public String format(LogRecord record) {
                    String str = record.getLevel() + ": " + record.getMessage() + "\n";
                    return str;
                }
            });
            logger.addHandler(handler);
        } catch (Exception e) {
            System.err.println("Could not create WatchDog logger file.");  
        }
    }

    public static void log(Level lvl, String str) {
        logger.log(lvl, str);
        try {
            handler.flush(); 
        } catch (Exception e) {
            System.err.println("Could not flush logger.");
        }
    }

    public static String getFileName() {
        return FILENAME;
    }  
}
