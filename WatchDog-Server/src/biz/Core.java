package biz;

import dao.WatchDogDao;
import entity.Listener;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import entity.SystemProps;
import static entity.SystemProps.unmarshalXml;
import static entity.WatchDogLogger.log;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class Core {
    
    private final Properties props = new Properties();
    private final String propertyFile = "server.properties";
    
    private Thread listener;
    private final static HashMap<String, SystemProps> serverObjectsHashMap = new HashMap<>();
    private final WatchDogDao wdDao = new WatchDogDao();

    public Core() {
        
        try {
            props.load(new FileInputStream(propertyFile));
        } catch (IOException ex) {
            System.err.println("Could not load server.properties at Core.");
            System.exit(1);
        }
    }
    
    
    public void startListener() {
        log(Level.INFO, "Starting Listener...");
        listener = new Thread(new Listener(Integer.parseInt(props.getProperty("port")), this));
        listener.setName("Thread: Listener");
        listener.start();
    }
    
    public synchronized void processData(InputStream in) {
        SystemProps clientObject = null;
        try {
            clientObject = unmarshalXml(in);
        } catch (IOException ex) {
            System.err.println("IOException occurs while unmarshaling at controller.");
        }
        
        if (clientObject == null)
            return;

        for (String key : serverObjectsHashMap.keySet()) {
            if (key.equals(clientObject.getClientIp())) {
                SystemProps hmObject = serverObjectsHashMap.get(key);
                clientObject.copyTo(hmObject);
                wdDao.persistObject(clientObject);
                return;
            }
        }
        serverObjectsHashMap.put(clientObject.getClientIp(), clientObject);
        log(Level.INFO, "Starting to watch: " + clientObject.getClientIp());
        
        wdDao.persistObject(clientObject);
    }
    
    public List<SystemProps> tableRecords(long fromTime, long toTime, String ip) {
        return wdDao.selectByTime(fromTime, toTime, ip);
    }
    
    public void close() {
        wdDao.close();
    }
    
    public static HashMap<String, SystemProps> getServerObjectsHashMap() {
        return serverObjectsHashMap;
    }
}
