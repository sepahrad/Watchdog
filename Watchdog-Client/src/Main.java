
import entity.SystemProps;
import entity.WatchDogProps;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static WatchDogProps wd = new WatchDogProps();
    private static SystemProps sys = new SystemProps();
    private static OutputStream out;
    private static Socket socket;
    private static BufferedOutputStream bos;

    public static void main(String[] args) {

        if ( ! argValidate(args) )
            System.exit(1);
        
        try {
            socket = new Socket(wd.getServerIp(), wd.getServerPort());
            bos = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Could not connect to the server.");
            System.exit(0);
        }
        
        /* Starting the program and watching it */
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        sys.runProg();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {                
                sys.setProps();
                sys.checkProcLive();
                sys.setInterval(wd.getInterval());
                try {
                    sys.convToXml(bos);
                } catch (IOException ex) {
                    System.err.println("IOException occurs while converting to XML.");
                    try {    
                        bos.close();
                        out.close();
                        socket.close();
                    } catch (IOException ex1) {
                        System.err.println("Could not close resources.");
                    }
                    
                    try {
                        socket = new Socket(wd.getServerIp(), wd.getServerPort());
                        bos = new BufferedOutputStream(socket.getOutputStream());
                    } catch (IOException ex1) {
                        System.err.println("Could not create socket to the: " + wd.getServerIp() + ":" + wd.getServerPort());
                    }
                }
            }
        }, 3, wd.getInterval(), TimeUnit.SECONDS);

        Thread shutdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bos.close();
                    socket.close();
                } catch (IOException ex) {
                    System.err.println("Could not close resources at Shutdown Hook.");
                } finally {
                    System.out.println("Bye!");
                }
            }
        });
        shutdownThread.setName("Thread: Shutdown");
        
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    private static boolean argValidate(String[] args){

        if (args.length != 5 || ! args[0].equals("watch")){
            System.out.println("Incorrect Option(s).");
            return false;
        }

        if (args[1].equals("machine")){
            sys.setProcName("noProc");
            sys.setProcLive(false);
        } else {
            sys.setProcName(args[1]);
            sys.setProcLive(false);
        }

        wd.setServerIp(args[2]);
        wd.setServerPort(Integer.parseInt(args[3]));
        wd.setInterval(Integer.parseInt(args[4]));

        return true;
    }
}
