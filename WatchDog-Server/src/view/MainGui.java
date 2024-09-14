package view;

import biz.Core;
import javax.swing.*;

public class MainGui extends JFrame{
        
    public MainGui(Core controller){
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainJFrame(controller).setVisible(true);
            }
        });
        
    } 
}
