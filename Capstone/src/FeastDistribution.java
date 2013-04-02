import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;


public class FeastDistribution extends JFrame {
    private JPanel topPanel, panel, bottomPanel;
    private String distribution;
    private String date;
    private JTable table;private JTextField searchBox;
    private JLabel searchLabel;
    private JComboBox<String> searchOptions;
    private JButton searchButton, add, delete;
    private String[] distributionOptions = {"", "Dist1", "Dist2", "Dist3", "Dist4" };
    private ArrayList<Integer> selected;
    String url = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
    String dbName = "feastdb";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "root";
    String password = "lampa";
    private int numberOfRows;
    private DistributionTableModel model; 
    private ListSelectionModel listModel;
    private ResultSet result;
    static final String DEFAULT_QUERY = "SELECT * FROM jos_fb_monthlyDist";
    
    public FeastDistribution() {
        super("Distribution");
        topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);
        setSize(1500,700);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener(){
            public void windowActivated(WindowEvent arg0){
                
            }
            public void windowClosed(WindowEvent arg0){}
            public void windowClosing(WindowEvent arg0){
                int result = displayExitDialog();
                if(result == JOptionPane.YES_OPTION){
                    dispose();
                }
            }
            @Override
            public void windowDeactivated(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void windowDeiconified(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            public void windowIconified(WindowEvent arg0){}
            public void windowOpened(WindowEvent arg0){
                distribution = selectDistributionDialog();
                if (distribution == null || distribution == "") {
                    distribution = pleaseSelectDistributionDialog();
                    if (distribution == null || distribution == "") {
                        while (distribution == null || distribution == "") {
                            distribution = pleaseSelectDistributionDialog();
                        }
                        System.out.println(distribution);
                        date = enterDateDialog();
                        if (date.length() != 10) {
                            System.out.println("Here");
                            date = pleaseEnterDateDialog();
                            if (date.length() != 10) {
                                while (date.length() != 10) {
                                    pleaseEnterDateDialog();
                                }
                                System.out.println(date);
                            }
                        }
                        else {
                            System.out.println(date);
                        }
                    }
                    else {
                        System.out.println(distribution);
                        date = enterDateDialog();
                        if (date.length() != 10) {
                            System.out.println("Here");
                            date = pleaseEnterDateDialog();
                            if (date.length() != 10) {
                                while (date.length() != 10) {
                                    pleaseEnterDateDialog();
                                }
                                System.out.println(date);
                            }
                        }
                        else {
                            System.out.println(date);
                        }
                    }
                }
                else {
                    System.out.println(distribution);
                    date = enterDateDialog();
                    if (date.length() != 10) {
                        System.out.println("Here1");
                        date = pleaseEnterDateDialog();
                        if (date.length() != 10) {
                            while (date.length() != 10) {
                                date = pleaseEnterDateDialog();
                            }
                            System.out.println(date);
                        }
                    }
                    else {
                        System.out.println(date);
                    }
                }
            }
        });
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchLabel = new JLabel("Select Distribution to View: ");
        searchOptions = new JComboBox(distributionOptions);
        searchBox = new JTextField(10);
        searchBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = searchBox.getText();
                String option = (String)searchOptions.getSelectedItem();
                String request;
                if (option == "Agency") {
                    request = new String ("SELECT * FROM jos_fb_agency WHERE Agency_Name LIKE \'"+ text +"%\'");
                }
                else if (text == "") {
                    request = DEFAULT_QUERY;
                }
                else {
                    request = new String("SELECT * FROM jos_fb_customer WHERE "+option+" LIKE  \'"+ text + "%\'");
                }
                try { 
                    model.setQuery(request); 
                }
                catch (Exception e1) {
                    JOptionPane.showMessageDialog( null, e1.getMessage(), "Database error",JOptionPane.ERROR_MESSAGE );
                    try {
                         model.setQuery(DEFAULT_QUERY);
                    }
                    catch (Exception e2) {
                        JOptionPane.showMessageDialog( null, e2.getMessage(),"Database error",JOptionPane.ERROR_MESSAGE );
                        model.disconnectFromDatabase();
                        System.exit(1);
                    }
               }
           }  
        });
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = searchBox.getText();
                String option = (String)searchOptions.getSelectedItem();
                String request;
                if (text == "" || option == "") {
                    request = DEFAULT_QUERY;
                }
                else {
                    request = new String("SELECT * FROM jos_fb_monthlyDist WHERE "+option+" LIKE  \'"+ text + "%\'");
                }
                try { 
                    model.setQuery(request); 
                }
                catch (Exception e1) {
                    JOptionPane.showMessageDialog( null, e1.getMessage(), "Database error",JOptionPane.ERROR_MESSAGE );
                    try {
                         model.setQuery(DEFAULT_QUERY);
                    }
                    catch (Exception e2) {
                        JOptionPane.showMessageDialog( null, e2.getMessage(),"Database error",JOptionPane.ERROR_MESSAGE );
                        model.disconnectFromDatabase();
                        System.exit(1);
                    }
               }
           }  
        });
        panel.add(searchLabel, BorderLayout.EAST);
        panel.add(searchOptions, BorderLayout.EAST);
        panel.add(searchBox, BorderLayout.EAST);
        panel.add(searchButton,BorderLayout.EAST);
        topPanel.add(panel, BorderLayout.EAST);
        searchBox.setFont(new Font("Verdana",Font.PLAIN,12));
        model = new DistributionTableModel(driver,url+dbName,userName,password, DEFAULT_QUERY);
        table = new JTable(model);
        listModel = table.getSelectionModel();
        listModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listModel.addListSelectionListener(new SharedListSelectionHandler());
        table.setSelectionModel(listModel);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        bottomPanel = new JPanel();
        add = new JButton("Add Customer");
        delete = new JButton("Remove Customer");
        bottomPanel.add(add);
        bottomPanel.add(delete);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public FeastDistribution(String load) {
        super("Distribution");
        topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);
        setSize(1500,700);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener(){
            public void windowActivated(WindowEvent arg0){
                
            }
            public void windowClosed(WindowEvent arg0){}
            public void windowClosing(WindowEvent arg0){
                int result = displayExitDialog();
                if(result == JOptionPane.YES_OPTION){
                    dispose();
                }
            }
            @Override
            public void windowDeactivated(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void windowDeiconified(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            public void windowIconified(WindowEvent arg0){}
            public void windowOpened(WindowEvent arg0){
                distribution = selectDistributionDialog();
                if (distribution == null || distribution == "") {
                    distribution = pleaseSelectDistributionDialog();
                    if (distribution == null || distribution == "") {
                        while (distribution == null || distribution == "") {
                            distribution = pleaseSelectDistributionDialog();
                        }
                        System.out.println(distribution);
                        date = enterValidDateDialog();
                        if (date.length() != 10) {
                            System.out.println("Here");
                            date = pleaseEnterValidDateDialog();
                            if (date.length() != 10) {
                                while (date.length() != 10) {
                                    pleaseEnterValidDateDialog();
                                }
                                System.out.println(date);
                            }
                        }
                        else {
                            System.out.println(date);
                        }
                    }
                    else {
                        System.out.println(distribution);
                        date = enterDateDialog();
                        if (date.length() != 10) {
                            System.out.println("Here");
                            date = pleaseEnterDateDialog();
                            if (date.length() != 10) {
                                while (date.length() != 10) {
                                    pleaseEnterDateDialog();
                                }
                                System.out.println(date);
                            }
                        }
                        else {
                            System.out.println(date);
                        }
                    }
                }
                else {
                    System.out.println(distribution);
                    date = enterValidDateDialog();
                    if (date.length() != 10) {
                        System.out.println("Here1");
                        date = pleaseEnterValidDateDialog();
                        if (date.length() != 10) {
                            while (date.length() != 10) {
                                date = pleaseEnterValidDateDialog();
                            }
                            System.out.println(date);
                        }
                    }
                    else {
                        System.out.println(date);
                    }
                }
            }
        });
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchLabel = new JLabel("Select Distribution to View: ");
        searchOptions = new JComboBox(distributionOptions);
        searchBox = new JTextField(10);
        searchBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = searchBox.getText();
                String option = (String)searchOptions.getSelectedItem();
                String request;
                if (option == "Agency") {
                    request = new String ("SELECT * FROM jos_fb_agency WHERE Agency_Name LIKE \'"+ text +"%\'");
                }
                else if (text == "") {
                    request = DEFAULT_QUERY;
                }
                else {
                    request = new String("SELECT * FROM jos_fb_customer WHERE "+option+" LIKE  \'"+ text + "%\'");
                }
                try { 
                    model.setQuery(request); 
                }
                catch (Exception e1) {
                    JOptionPane.showMessageDialog( null, e1.getMessage(), "Database error",JOptionPane.ERROR_MESSAGE );
                    try {
                         model.setQuery(DEFAULT_QUERY);
                    }
                    catch (Exception e2) {
                        JOptionPane.showMessageDialog( null, e2.getMessage(),"Database error",JOptionPane.ERROR_MESSAGE );
                        model.disconnectFromDatabase();
                        System.exit(1);
                    }
               }
           }  
        });
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = searchBox.getText();
                String option = (String)searchOptions.getSelectedItem();
                String request;
                if (option == "Agency") {
                    request = new String ("SELECT * FROM jos_fb_agency WHERE Agency_Name LIKE \'"+ text +"%\'");
                }
                else if (text == "" || option == "") {
                    request = DEFAULT_QUERY;
                }
                else {
                    request = new String("SELECT * FROM jos_fb_customer WHERE "+option+" LIKE  \'"+ text + "%\'");
                }
                try { 
                    model.setQuery(request); 
                }
                catch (Exception e1) {
                    JOptionPane.showMessageDialog( null, e1.getMessage(), "Database error",JOptionPane.ERROR_MESSAGE );
                    try {
                         model.setQuery(DEFAULT_QUERY);
                    }
                    catch (Exception e2) {
                        JOptionPane.showMessageDialog( null, e2.getMessage(),"Database error",JOptionPane.ERROR_MESSAGE );
                        model.disconnectFromDatabase();
                        System.exit(1);
                    }
               }
           }  
        });
        panel.add(searchLabel, BorderLayout.EAST);
        panel.add(searchOptions, BorderLayout.EAST);
        panel.add(searchBox, BorderLayout.EAST);
        panel.add(searchButton,BorderLayout.EAST);
        topPanel.add(panel, BorderLayout.EAST);
        model = new DistributionTableModel(driver,url+dbName,userName,password, DEFAULT_QUERY);
        table = new JTable(model);
        listModel = table.getSelectionModel();
        listModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listModel.addListSelectionListener(new SharedListSelectionHandler());
        table.setSelectionModel(listModel);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        bottomPanel = new JPanel();
        add = new JButton("Add Customer");
        delete = new JButton("Remove Customer");
        bottomPanel.add(add);
        bottomPanel.add(delete);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private int displayExitDialog() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure?", "Exit?", JOptionPane.YES_NO_OPTION , JOptionPane.WARNING_MESSAGE);
        return result;
    }
    
    private String selectDistributionDialog() {
        String dis = (String) JOptionPane.showInputDialog(this, "Which Distribution Agency?", "Distribution Site", JOptionPane.QUESTION_MESSAGE, null, distributionOptions, distributionOptions[0]);
        return dis;
    }
    
    private String pleaseSelectDistributionDialog() {
        String dis = (String) JOptionPane.showInputDialog(this, "Please actually select a Distribution site!", "Distribution Site", JOptionPane.QUESTION_MESSAGE, null, distributionOptions, distributionOptions[0]);
        return dis;
    }
    
    private String enterDateDialog() {
        String code = JOptionPane.showInputDialog(this, "Enter the date for the new Distribution with the following format \n YYYY-MM-DD", "Distribution Date", JOptionPane.QUESTION_MESSAGE);
        return code;
    }
    
    private String pleaseEnterDateDialog() {
        String code = JOptionPane.showInputDialog(this, "Please enter the date for the new Distribution! \n Example: 2013-03-25", "Distribution Date", JOptionPane.QUESTION_MESSAGE);
        return code;
    }
    
    private String enterValidDateDialog() {
        String code = JOptionPane.showInputDialog(this, "Please enter the date of a Distribution \n Example: YYYY-MM-DD", "Distribution Date", JOptionPane.QUESTION_MESSAGE);
        return code;
    }
    
    private String pleaseEnterValidDateDialog() {
        String code = JOptionPane.showInputDialog(this, "Please enter the date for an valid Distribution! \n Example: 2013-03-25", "Distribution Date", JOptionPane.QUESTION_MESSAGE);
        return code;
    }
    
    class DistributionTableModel extends AbstractTableModel {
        /**
         * 
         */
        private Connection conn;
        private Statement stmt;
        private ResultSetMetaData metaData;
        private boolean connectedToDatabase = false;
        private static final long serialVersionUID = 1L;
        
        public DistributionTableModel(String driver, String url, String username, String password, String query) {
            try {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url,username,password);
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                connectedToDatabase = true;
                setQuery(query);
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void setQuery(String query) {
            if (!connectedToDatabase)
                throw new IllegalStateException( "Not Connected to Database" );
            try {
                result = stmt.executeQuery(query);
                metaData = result.getMetaData();
                result.last();
                numberOfRows = result.getRow();
                System.out.println("Here");
                fireTableStructureChanged();
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public int getColumnCount() {
            if ( !connectedToDatabase )
                throw new IllegalStateException( "Not Connected to Database" );
            try {
                return metaData.getColumnCount();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public int getRowCount() {
            if ( !connectedToDatabase )
                throw new IllegalStateException( "Not Connected to Database" );
            return numberOfRows;
        }
        
        @Override
        public String getColumnName(int column) throws IllegalStateException {
            if (!connectedToDatabase)
                throw new IllegalStateException( "Not Connected to Database" );
            try {
                return metaData.getColumnName(column + 1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        public Object getValueAt(int r, int col) {
            if ( !connectedToDatabase )
                throw new IllegalStateException( "Not Connected to Database" );
            try {
                result.absolute(r+ 1);
                //System.out.println(r+1);
                return result.getObject(col + 1);
            }
            catch (Exception e ) { 
                e.printStackTrace();
            }
            return "";
        }
        
        public Class getColumnClass(int c) throws IllegalStateException {
            if (!connectedToDatabase)
                throw new IllegalStateException("Not Connected to Database");
            try {
                String className = metaData.getColumnClassName(c + 1);
                return Class.forName(className);
            }
            catch ( Exception exception ) {
                exception.printStackTrace(); 
            }
            return Object.class;
        }
        public void disconnectFromDatabase(){
            try {
                stmt.close();
                conn.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally { 
                connectedToDatabase = false;
            }
        }
        public boolean isCellEditable(int row, int col) {
            if (col == 1) {
                return false;
            }
            else {
                return true;
            }
        }
    }
    class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) { 
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting(); 
            selected = new ArrayList<Integer>();
            System.out.println("Event for indexes "
                          + firstIndex + " - " + lastIndex
                          + "; isAdjusting is " + isAdjusting
                          + "; selected indexes:");
            String output = "";
            if (lsm.isSelectionEmpty()) {
               System.out.println(" <none>");
            } 
            else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        output += (" " + i);
                        selected.add(i+1);
                    }
                }
            }
            output += "\n";
            System.out.println(output);
            for (int i = 0; i < selected.size(); i++) {
                System.out.println(selected.get(i));
            }
        }
    }
    
    public static void main(String [] args){
        JFrame GUI = new FeastDistribution();
        GUI.setLocationRelativeTo(null);
        GUI.setVisible(true);
    }
}
