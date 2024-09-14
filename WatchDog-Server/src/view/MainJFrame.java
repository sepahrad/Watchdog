package view;

import biz.Core;
import entity.SystemProps;
import entity.WatchDogLogger;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class MainJFrame extends javax.swing.JFrame {

    private String selectedClient;
    private Core controller;
    
    public MainJFrame(Core controller) {
        this.controller = controller;
        
        initComponents();
        Timer timer_serverLog = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (BufferedReader reader = new BufferedReader(new FileReader(WatchDogLogger.getFileName()))) {
                    txt_serverLog.read(reader, null);
                } catch (Exception ex) {
                    System.err.println("Could not read server log at GUI.");
                }
            }
        });
        timer_serverLog.start();
        
        Timer timer_clientsTable = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWatchingSystems();
            }
        });
        timer_clientsTable.start();
        
        Timer timer_clientDetails = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (String key : Core.getServerObjectsHashMap().keySet()) {
                    if (key.equals(selectedClient))
                        printClientDetails(Core.getServerObjectsHashMap().get(key));
                }
            }
        });
        timer_clientDetails.start();
        
        Timer timer_alarms = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, SystemProps> hmObj = Core.getServerObjectsHashMap();
                SystemProps obj;
                
                txt_alarms.setText(null);
                for (String key : hmObj.keySet()){
                    obj = hmObj.get(key);
                    
                    if (System.currentTimeMillis() - obj.getTime() > obj.getInterval() * 1000 * 2) {
                        txt_alarms.setForeground(Color.red);
                        txt_alarms.append(obj.getClientIp() + "/" + obj.getClientHostName() + ": " + "No heartbeat.\n");
                    }
                    
                    if (obj.getCpuLoad() * 100 > obj.getCpuKpi()){
                        txt_alarms.setForeground(Color.red);
                        txt_alarms.append(obj.getClientIp() + "/" + obj.getClientHostName() + ": " + "CPU load higher than CPU KPI.\n");
                    }
                    
                    if (obj.getTotalMem() * obj.getMemKpi() / 100 < obj.getTotalMem() - obj.getFreeMem()){
                        txt_alarms.setForeground(Color.red);
                        txt_alarms.append(obj.getClientIp() + "/" + obj.getClientHostName() + ": " + "Memory usage higher than MEM KPI.\n"); 
                    }
                        
                }
            }
        });
        timer_alarms.start();
    }

    private void printClientDetails(SystemProps obj) {
        if (System.currentTimeMillis() - obj.getTime() > obj.getInterval() * 1000 * 2) {
            if (txt_clientTime.getText().toLowerCase().contains("no heart beat"))
                return;
            String date = txt_clientTime.getText() + " (NO HEART BEAT)";
            txt_clientTime.setText(date);
            txt_clientTime.setBackground(Color.red);
            return;
        }
        
        String date = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").format(new Date (obj.getTime()));
        txt_clientTime.setText("Client(" + obj.getClientIp() + "/" + obj.getClientHostName() +")"
                + " Time: " + date);
        txt_clientTime.setBackground(Color.LIGHT_GRAY);
        
        txt_totalMem.setText(Long.toString(obj.getTotalMem() / 1024 / 1024) + " MB");
        txt_freeMem.setText(Long.toString(obj.getFreeMem() / 1024 / 1024) + " MB");
        txt_usedMem.setText(Long.toString((obj.getTotalMem() - obj.getFreeMem()) / 1024 / 1024) + " MB");
        txt_cpuLoad.setText(Double.toString(obj.getCpuLoad()));
        
        if ("noProc".equals(obj.getProcName()))
            txt_procName.setText("No program");
        else
            txt_procName.setText(obj.getProcName());
        
        if (obj.isProcLive())
            txt_procLive.setText("Yes");
        else
            txt_procLive.setText("No");
        
        txt_cpuKpiDetails.setText(Integer.toString(obj.getCpuKpi()));
        txt_memKpiDetails.setText(Integer.toString(obj.getMemKpi()));
    }
    
    private void addWatchingSystems() {
        DefaultTableModel tblModel = (DefaultTableModel) tbl_clientsTable.getModel();
        tblModel.setRowCount(0);
        for (String key : Core.getServerObjectsHashMap().keySet()) {
            Object[] rowData = new Object[2];
            rowData[0] = Core.getServerObjectsHashMap().get(key).getClientIp();
            rowData[1] = Core.getServerObjectsHashMap().get(key).getClientHostName();
            tblModel.addRow(rowData);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        txt_clientTime = new javax.swing.JTextField();
        jp_clientDetails1 = new javax.swing.JPanel();
        lbl_totalMem = new javax.swing.JLabel();
        txt_totalMem = new javax.swing.JTextField();
        lbl_freeMem = new javax.swing.JLabel();
        txt_freeMem = new javax.swing.JTextField();
        lbl_usedMem = new javax.swing.JLabel();
        txt_usedMem = new javax.swing.JTextField();
        lbl_cpuLoad = new javax.swing.JLabel();
        txt_cpuLoad = new javax.swing.JTextField();
        jp_clientDetails2 = new javax.swing.JPanel();
        lbl_procName = new javax.swing.JLabel();
        txt_procName = new javax.swing.JTextField();
        lbl_procLive = new javax.swing.JLabel();
        txt_procLive = new javax.swing.JTextField();
        lbl_cpuKpiDetails = new javax.swing.JLabel();
        txt_cpuKpiDetails = new javax.swing.JTextField();
        lbl_memKpiDetails = new javax.swing.JLabel();
        txt_memKpiDetails = new javax.swing.JTextField();
        scr_clientsTable = new javax.swing.JScrollPane();
        tbl_clientsTable = new javax.swing.JTable();
        sld_cpuKpi = new javax.swing.JSlider();
        sld_memKpi = new javax.swing.JSlider();
        btn_kpi = new javax.swing.JButton();
        lbl_cpuKpi = new javax.swing.JLabel();
        scr_serverLog = new javax.swing.JScrollPane();
        txt_serverLog = new javax.swing.JTextArea();
        lbl_memKpi = new javax.swing.JLabel();
        lvl_serverLog = new javax.swing.JLabel();
        scr_alarms = new javax.swing.JScrollPane();
        txt_alarms = new javax.swing.JTextArea();
        lbl_clientDetails = new javax.swing.JLabel();
        lbl_alarms = new javax.swing.JLabel();
        lbl_from = new javax.swing.JLabel();
        dateChooserCombo_From = new datechooser.beans.DateChooserCombo();
        spin_fromHour = new javax.swing.JSpinner();
        lbl_to = new javax.swing.JLabel();
        dateChooserCombo_To = new datechooser.beans.DateChooserCombo();
        spin_toHour = new javax.swing.JSpinner();
        lbl_date = new javax.swing.JLabel();
        lbl_hour = new javax.swing.JLabel();
        lbl_minute = new javax.swing.JLabel();
        lbl_second = new javax.swing.JLabel();
        spin_fromMinute = new javax.swing.JSpinner();
        spin_toMinute = new javax.swing.JSpinner();
        spin_fromSecond = new javax.swing.JSpinner();
        spin_toSecond = new javax.swing.JSpinner();
        btn_search = new javax.swing.JButton();
        scr_historyTable = new javax.swing.JScrollPane();
        tbl_history = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        txt_ip = new javax.swing.JTextField();
        lbl_ip = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        File_Exit = new javax.swing.JMenuItem();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WatchDog Server GUI");

        txt_clientTime.setEditable(false);
        txt_clientTime.setText("Client Time: ");

        lbl_totalMem.setText("Total Memory:");

        txt_totalMem.setEditable(false);
        txt_totalMem.setText("0");

        lbl_freeMem.setText("Free Memory:");

        txt_freeMem.setEditable(false);
        txt_freeMem.setText("0");

        lbl_usedMem.setText("Used Memory:");

        txt_usedMem.setEditable(false);
        txt_usedMem.setText("0");

        lbl_cpuLoad.setText("Cpu Load:");

        txt_cpuLoad.setEditable(false);
        txt_cpuLoad.setText("0");

        javax.swing.GroupLayout jp_clientDetails1Layout = new javax.swing.GroupLayout(jp_clientDetails1);
        jp_clientDetails1.setLayout(jp_clientDetails1Layout);
        jp_clientDetails1Layout.setHorizontalGroup(
            jp_clientDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_clientDetails1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jp_clientDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_totalMem, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_freeMem, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cpuLoad)
                    .addComponent(lbl_usedMem, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jp_clientDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_usedMem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_totalMem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(txt_freeMem, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_cpuLoad, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jp_clientDetails1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txt_cpuLoad, txt_freeMem, txt_totalMem, txt_usedMem});

        jp_clientDetails1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lbl_cpuLoad, lbl_freeMem, lbl_totalMem, lbl_usedMem});

        jp_clientDetails1Layout.setVerticalGroup(
            jp_clientDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_clientDetails1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jp_clientDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_totalMem)
                    .addComponent(txt_totalMem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_clientDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_freeMem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_freeMem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_clientDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_usedMem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_usedMem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_clientDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_cpuLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_cpuLoad))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jp_clientDetails1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txt_cpuLoad, txt_freeMem, txt_totalMem, txt_usedMem});

        jp_clientDetails1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lbl_cpuLoad, lbl_freeMem, lbl_totalMem, lbl_usedMem});

        lbl_procName.setText("Process Name:");

        txt_procName.setEditable(false);
        txt_procName.setText("0");

        lbl_procLive.setText("Proc Live:");

        txt_procLive.setEditable(false);
        txt_procLive.setText("No");

        lbl_cpuKpiDetails.setText("CPU Load KPI:");

        txt_cpuKpiDetails.setEditable(false);
        txt_cpuKpiDetails.setText("0");

        lbl_memKpiDetails.setText("Memory KPI:");

        txt_memKpiDetails.setEditable(false);
        txt_memKpiDetails.setText("0");

        javax.swing.GroupLayout jp_clientDetails2Layout = new javax.swing.GroupLayout(jp_clientDetails2);
        jp_clientDetails2.setLayout(jp_clientDetails2Layout);
        jp_clientDetails2Layout.setHorizontalGroup(
            jp_clientDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_clientDetails2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jp_clientDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_procName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_cpuKpiDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_memKpiDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_procLive))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_clientDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_cpuKpiDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(txt_procName, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(txt_memKpiDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(txt_procLive))
                .addContainerGap())
        );

        jp_clientDetails2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txt_cpuKpiDetails, txt_memKpiDetails, txt_procLive, txt_procName});

        jp_clientDetails2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lbl_cpuKpiDetails, lbl_memKpiDetails, lbl_procLive, lbl_procName});

        jp_clientDetails2Layout.setVerticalGroup(
            jp_clientDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_clientDetails2Layout.createSequentialGroup()
                .addGroup(jp_clientDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_procName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_procName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_clientDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_procLive)
                    .addComponent(txt_procLive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_clientDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_memKpiDetails)
                    .addComponent(lbl_memKpiDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_clientDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cpuKpiDetails)
                    .addComponent(lbl_cpuKpiDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jp_clientDetails2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txt_cpuKpiDetails, txt_memKpiDetails, txt_procLive, txt_procName});

        jp_clientDetails2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lbl_cpuKpiDetails, lbl_memKpiDetails, lbl_procLive, lbl_procName});

        tbl_clientsTable.setFont(new java.awt.Font("Cantarell", 0, 12)); // NOI18N
        tbl_clientsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IP Address: ", "Host Name:"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_clientsTable.setColumnSelectionAllowed(true);
        scr_clientsTable.setViewportView(tbl_clientsTable);
        tbl_clientsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tbl_clientsTable.getColumnModel().getColumnCount() > 0) {
            tbl_clientsTable.getColumnModel().getColumn(0).setResizable(false);
            tbl_clientsTable.getColumnModel().getColumn(1).setResizable(false);
        }
        tbl_clientsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (tbl_clientsTable.getSelectedRow() > -1) {
                    selectedClient = (String) tbl_clientsTable.getValueAt(tbl_clientsTable.getSelectedRow(), 0);
                    txt_ip.setText(selectedClient);
                }
            }
        });

        sld_cpuKpi.setMajorTickSpacing(20);
        sld_cpuKpi.setMinorTickSpacing(5);
        sld_cpuKpi.setPaintLabels(true);
        sld_cpuKpi.setPaintTicks(true);
        sld_cpuKpi.setSnapToTicks(true);

        sld_memKpi.setMajorTickSpacing(20);
        sld_memKpi.setMinorTickSpacing(5);
        sld_memKpi.setPaintLabels(true);
        sld_memKpi.setPaintTicks(true);
        sld_memKpi.setSnapToTicks(true);

        btn_kpi.setText("Apply");
        btn_kpi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kpiActionPerformed(evt);
            }
        });

        lbl_cpuKpi.setText("CPU Load KPI");

        txt_serverLog.setEditable(false);
        txt_serverLog.setColumns(20);
        txt_serverLog.setRows(5);
        scr_serverLog.setViewportView(txt_serverLog);

        lbl_memKpi.setText("Mem KPI");

        lvl_serverLog.setText("Server Log:");

        txt_alarms.setEditable(false);
        txt_alarms.setColumns(20);
        txt_alarms.setRows(5);
        scr_alarms.setViewportView(txt_alarms);

        lbl_clientDetails.setText("Client Details:");

        lbl_alarms.setText("Alarms:");

        lbl_from.setText("From:");

        spin_fromHour.setMaximumSize(new java.awt.Dimension(24, 24));
        spin_fromHour.setMinimumSize(new java.awt.Dimension(0, 0));
        spin_fromHour.setRequestFocusEnabled(false);
        spin_fromHour.setModel(new SpinnerNumberModel(0, 0, 24, 1));

        lbl_to.setText("To:");

        spin_toHour.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        spin_toHour.setModel(new SpinnerNumberModel(0, 0, 24, 1));

        lbl_date.setText("Date:");

        lbl_hour.setText("Hour(s)");

        lbl_minute.setText("Minute(s)");

        lbl_second.setText("Second(s)");

        spin_fromMinute.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        spin_toMinute.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        spin_fromSecond.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        spin_toSecond.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        btn_search.setText("Search");
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });

        tbl_history.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Time", "Interval", "CPU Load", "Total Memory", "Free Mem", "Process Name", "Process Live"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Long.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scr_historyTable.setViewportView(tbl_history);
        if (tbl_history.getColumnModel().getColumnCount() > 0) {
            tbl_history.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        lbl_ip.setText("IP Address:");

        File.setText("File");

        File_Exit.setText("Exit");
        File_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                File_ExitActionPerformed(evt);
            }
        });
        File.add(File_Exit);

        MenuBar.add(File);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scr_historyTable, javax.swing.GroupLayout.DEFAULT_SIZE, 1115, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_from, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_to, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserCombo_From, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooserCombo_To, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(lbl_date)))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spin_toHour)
                    .addComponent(lbl_hour, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spin_fromHour, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_minute)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(spin_fromMinute, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(spin_toMinute, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_second)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(spin_fromSecond, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spin_toSecond, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                        .addGap(31, 31, 31)
                        .addComponent(lbl_ip)
                        .addGap(18, 18, 18)
                        .addComponent(txt_ip, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btn_search, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(251, Short.MAX_VALUE))
            .addComponent(jSeparator3)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sld_cpuKpi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scr_clientsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_kpi, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(sld_memKpi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(lbl_cpuKpi))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(lbl_memKpi)))
                        .addGap(19, 19, 19)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scr_serverLog)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lvl_serverLog)
                                    .addComponent(lbl_clientDetails)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jp_clientDetails1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jp_clientDetails2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(txt_clientTime, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbl_alarms)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(scr_alarms))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_clientDetails)
                            .addComponent(lbl_alarms))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txt_clientTime, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jp_clientDetails1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jp_clientDetails2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(scr_alarms))
                        .addGap(0, 5, Short.MAX_VALUE))
                    .addComponent(scr_clientsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lvl_serverLog)
                            .addComponent(lbl_cpuKpi, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sld_cpuKpi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_memKpi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sld_memKpi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_kpi))
                    .addComponent(scr_serverLog))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbl_hour)
                                    .addComponent(lbl_minute)
                                    .addComponent(lbl_second)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lbl_date)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_from, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateChooserCombo_From, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(spin_fromHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spin_fromMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spin_fromSecond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lbl_to, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dateChooserCombo_To, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(spin_toHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spin_toMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spin_toSecond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_ip)
                            .addComponent(txt_ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_search))))
                .addGap(11, 11, 11)
                .addComponent(scr_historyTable, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void File_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_File_ExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_File_ExitActionPerformed

    private void btn_kpiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kpiActionPerformed
        if (selectedClient == null) {
            JOptionPane.showMessageDialog(null, "Please choose a client.");
            return;
        }
        for (String key : Core.getServerObjectsHashMap().keySet()) {
            if (key.equals(selectedClient)) {
                SystemProps obj = Core.getServerObjectsHashMap().get(key);
                obj.setCpuKpi(sld_cpuKpi.getValue());
                obj.setMemKpi(sld_memKpi.getValue());
                return;
            }
        }
    }//GEN-LAST:event_btn_kpiActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
  
        if (selectedClient == null) {
            JOptionPane.showMessageDialog(null, "Please choose a client.");
            return;
        }
        
        /* Calculate from date to milliseconds */
        Calendar fromDateMillis = dateChooserCombo_From.getCurrent();
        int from_numOfYear = fromDateMillis.get(Calendar.YEAR) - 1970;
        int from_numOfDay = fromDateMillis.get(Calendar.DAY_OF_YEAR);
        int from_numOfHours = Integer.parseInt(spin_fromHour.getValue().toString());
        int from_numOfMinutes = Integer.parseInt(spin_fromMinute.getValue().toString());
        int from_numOfSeconds = Integer.parseInt(spin_fromSecond.getValue().toString());
        
        long fromMillis = (from_numOfYear * 31556952000L) + TimeUnit.DAYS.toMillis(from_numOfDay) + TimeUnit.HOURS.toMillis(from_numOfHours)
                + TimeUnit.MINUTES.toMillis(from_numOfMinutes) + TimeUnit.SECONDS.toMillis(from_numOfSeconds) - 50554116;
        
        /* Calculate to date to milliseconds */
        Calendar toDateMillis = dateChooserCombo_To.getCurrent();
        int to_numOfYear = toDateMillis.get(Calendar.YEAR) - 1970;
        int to_numOfDay = toDateMillis.get(Calendar.DAY_OF_YEAR);
        int to_numOfHours = Integer.parseInt(spin_toHour.getValue().toString());
        int to_numOfMinutes = Integer.parseInt(spin_toMinute.getValue().toString());
        int to_numOfSeconds = Integer.parseInt(spin_toSecond.getValue().toString());
        
        long toMillis = (to_numOfYear * 31556952000L) + TimeUnit.DAYS.toMillis(to_numOfDay) + TimeUnit.HOURS.toMillis(to_numOfHours)
                + TimeUnit.MINUTES.toMillis(to_numOfMinutes) + TimeUnit.SECONDS.toMillis(to_numOfSeconds) - 50554116;
        
        List<SystemProps> list = null;
        list = controller.tableRecords(fromMillis, toMillis, txt_ip.getText());
        
        DefaultTableModel tblModel = (DefaultTableModel) tbl_history.getModel();
        tblModel.setRowCount(0);
        for (SystemProps obj : list) {
            Object[] rowData = new Object[8];
            rowData[0] = obj.getId();
            
            String date = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").format(new Date (obj.getTime()));
            rowData[1] = date;
            
            rowData[2] = obj.getInterval();
            rowData[3] = obj.getCpuLoad();
            rowData[4] = obj.getTotalMem() / 1024 / 1024 + " MB" ;
            rowData[5] = obj.getFreeMem() / 1024 / 1024 + " MB";
            
            if ("noProc".equals(obj.getProcName()))
                rowData[6] = "No Program";
            
            if (obj.isProcLive())
                rowData[7] = "Live";
            else
                rowData[7] = "Dead";
            
            tblModel.addRow(rowData);
        }        
        for (Object a : list) {

        }
    }//GEN-LAST:event_btn_searchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu File;
    private javax.swing.JMenuItem File_Exit;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JButton btn_kpi;
    private javax.swing.JButton btn_search;
    private datechooser.beans.DateChooserCombo dateChooserCombo_From;
    private datechooser.beans.DateChooserCombo dateChooserCombo_To;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPanel jp_clientDetails1;
    private javax.swing.JPanel jp_clientDetails2;
    private javax.swing.JLabel lbl_alarms;
    private javax.swing.JLabel lbl_clientDetails;
    private javax.swing.JLabel lbl_cpuKpi;
    private javax.swing.JLabel lbl_cpuKpiDetails;
    private javax.swing.JLabel lbl_cpuLoad;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_freeMem;
    private javax.swing.JLabel lbl_from;
    private javax.swing.JLabel lbl_hour;
    private javax.swing.JLabel lbl_ip;
    private javax.swing.JLabel lbl_memKpi;
    private javax.swing.JLabel lbl_memKpiDetails;
    private javax.swing.JLabel lbl_minute;
    private javax.swing.JLabel lbl_procLive;
    private javax.swing.JLabel lbl_procName;
    private javax.swing.JLabel lbl_second;
    private javax.swing.JLabel lbl_to;
    private javax.swing.JLabel lbl_totalMem;
    private javax.swing.JLabel lbl_usedMem;
    private javax.swing.JLabel lvl_serverLog;
    private javax.swing.JScrollPane scr_alarms;
    private javax.swing.JScrollPane scr_clientsTable;
    private javax.swing.JScrollPane scr_historyTable;
    private javax.swing.JScrollPane scr_serverLog;
    private javax.swing.JSlider sld_cpuKpi;
    private javax.swing.JSlider sld_memKpi;
    private javax.swing.JSpinner spin_fromHour;
    private javax.swing.JSpinner spin_fromMinute;
    private javax.swing.JSpinner spin_fromSecond;
    private javax.swing.JSpinner spin_toHour;
    private javax.swing.JSpinner spin_toMinute;
    private javax.swing.JSpinner spin_toSecond;
    private javax.swing.JTable tbl_clientsTable;
    private javax.swing.JTable tbl_history;
    private javax.swing.JTextArea txt_alarms;
    private javax.swing.JTextField txt_clientTime;
    private javax.swing.JTextField txt_cpuKpiDetails;
    private javax.swing.JTextField txt_cpuLoad;
    private javax.swing.JTextField txt_freeMem;
    private javax.swing.JTextField txt_ip;
    private javax.swing.JTextField txt_memKpiDetails;
    private javax.swing.JTextField txt_procLive;
    private javax.swing.JTextField txt_procName;
    private javax.swing.JTextArea txt_serverLog;
    private javax.swing.JTextField txt_totalMem;
    private javax.swing.JTextField txt_usedMem;
    // End of variables declaration//GEN-END:variables
}
