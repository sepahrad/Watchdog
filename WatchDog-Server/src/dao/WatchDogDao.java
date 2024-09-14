package dao;

import entity.SystemProps;
import entity.WatchDogLogger;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class WatchDogDao {
    
    private EntityManager em;
    
    public WatchDogDao() {
        WatchDogLogger.log(Level.INFO, "Starting Entity Manager for connecting to Database.");
        Properties props = new Properties();
        props.put("eclipselink.persistencexml", "META-INF/persistence.xml");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("wdJPA", props);
        this.em = emf.createEntityManager();        
    }
    
    public void persistObject(SystemProps obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e){
            System.err.println("Could not create record.");
        }
    }
    
    public List<SystemProps> selectByTime(long fromTime, long toTime, String ip) {
        List<SystemProps> list = null;
        Query query = em.createQuery("SELECT sys FROM SystemProps sys where sys.time BETWEEN :fromTime AND :toTime AND sys.clientIp LIKE :ip");
        query.setParameter("fromTime", fromTime)
             .setParameter("toTime", toTime)
             .setParameter("ip", ip);
                
        list = query.getResultList();
        
        return list;
    }
    
    public void close() {
        try {
            em.close();
        } catch (Exception e) {
            System.err.println("Could not close Entity Manager.");
        }
    }
    
}
