import biz.Core;
import view.MainGui;

public class Main {

    public static void main(String[] args) {
        
        Core core = new Core();
        core.startListener();
        new MainGui(core);
       
        Thread shutdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                core.close();
                System.out.println("Bye!");
            }
        });
        shutdownThread.setName("Thread: Shutdown");
        Runtime.getRuntime().addShutdownHook(shutdownThread);        
    }
    
}