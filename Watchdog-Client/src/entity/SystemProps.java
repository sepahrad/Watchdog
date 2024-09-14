package entity;

import com.sun.management.OperatingSystemMXBean;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemProps {

    private String clientIp;
    private String clientHostName;
    private long time;
    
    @XmlElement
    private long interval;
    
    @XmlElement(name="total-memory")
    private long totalMem;

    @XmlElement(name="free-memory")
    private long freeMem;

    @XmlElement(name="system-cpu-load")
    private double CpuLoad;
    
    @XmlTransient
    private Process p;

    @XmlElement(name="process-name")
    private String procName;

    @XmlElement(name="process-is-alive")
    private boolean procLive;

    public void setProps() {
        OperatingSystemMXBean osBean
                = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        try {
            setIp(InetAddress.getLocalHost().getHostAddress());
            setHostname(InetAddress.getLocalHost().getHostName());
            
        } catch (UnknownHostException ex) {
            System.err.println("Could not set InetAddress.");
        }
        setTime(System.currentTimeMillis());
        setTotalMem(osBean.getTotalPhysicalMemorySize());
        setFreeMem(osBean.getFreePhysicalMemorySize());
        setCpuLoad(osBean.getSystemLoadAverage());
    }
    
    public void runProg(){

        if (procName.equals("noProc") || procLive == true)
            return;

        try {
            p = Runtime.getRuntime().exec(procName);
            procLive = true;
        } catch (IOException ex) {
            System.err.println("Could not start the process " + procName + ".");
        }
    }

    public void checkProcLive(){
        if (procLive == true){
            if (! p.isAlive())
                procLive = false;
        }
    }

    public void convToXml(OutputStream out) throws IOException {
        
        try{
            JAXBContext context = JAXBContext.newInstance(SystemProps.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, out);
        } catch (JAXBException ex){
            System.err.println("Could not marshal.");
        } finally {
            out.flush();
        }
    }
    
    public String getIp() {
        return clientIp;
    }

    public void setIp(String ip) {
        this.clientIp = ip;
    }

    public String getHostname() {
        return clientHostName;
    }

    public void setHostname(String hostname) {
        this.clientHostName = hostname;
    }
    
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(long totalMem) {
        this.totalMem = totalMem;
    }

    public long getFreeMem() {
        return freeMem;
    }

    public void setFreeMem(long freeMem) {
        this.freeMem = freeMem;
    }

    public double getCpuLoad() {
        return CpuLoad;
    }

    public void setCpuLoad(double CpuLoad) {
        this.CpuLoad = CpuLoad;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public boolean isProcLive() {
        return procLive;
    }

    public void setProcLive(boolean procLive) {
        this.procLive = procLive;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
