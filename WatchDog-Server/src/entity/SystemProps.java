package entity;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tbl_systemprops")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemProps {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String clientIp;
    private String clientHostName;
    
    private long time;    
    private long interval;

    
    @XmlElement(name="total-memory")
    private long totalMem;

    @XmlElement(name="free-memory")
    private long freeMem;

    @XmlElement(name="system-cpu-load")
    private double CpuLoad;

    @XmlElement(name="process-name")
    private String procName;

    @XmlElement(name="process-is-alive")
    private boolean procLive;
    
    @Transient
    private int cpuKpi = 50;
    @Transient
    private int memKpi = 50;
    
    public static SystemProps unmarshalXml(InputStream is) throws IOException {
        
        SystemProps sys = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SystemProps.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
            sys = (SystemProps) jaxbUnmarshaller.unmarshal(is);
        } catch (JAXBException ex) {
            System.err.println("Could not unmarshll XML.");
        }
        
        return sys;
    }
    
    public void copyTo(SystemProps in){
        in.time = this.time;
        in.interval = this.interval;
        in.CpuLoad = this.CpuLoad;
        in.clientIp = this.clientIp;
        in.freeMem = this.freeMem;
        in.totalMem = this.totalMem;
        in.procName = this.procName;
        in.procLive = this.procLive;
    }

    public long getId() {
        return id;
    }

    
    public long getInterval() {
        return interval;
    }
    
    public long getTime() {
        return time;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getClientHostName() {
        return clientHostName;
    }

    public long getTotalMem() {
        return totalMem;
    }

    public long getFreeMem() {
        return freeMem;
    }

    public double getCpuLoad() {
        return CpuLoad;
    }

    public String getProcName() {
        return procName;
    }

    public boolean isProcLive() {
        return procLive;
    }

    public int getCpuKpi() {
        return cpuKpi;
    }

    public void setCpuKpi(int cpuKpi) {
        this.cpuKpi = cpuKpi;
    }

    public int getMemKpi() {
        return memKpi;
    }

    public void setMemKpi(int memKpi) {
        this.memKpi = memKpi;
    }   
}
