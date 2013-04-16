import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
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
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


public class AdvancedSearchPanel extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //static FBDatabase fbd;
    public static boolean online;
    public static Connection connL = null;
    public ResultSet result = null;
    public Statement stmtL;
    FeastMainFrame fmf;
    private int numberOfRows;
    private AdvancedTableModel model;
    private ListSelectionModel listModel;
    private ArrayList<Integer> selected;
    JPanel panel1,panel2,panel3,panel4,panel5,panel6,panel;
    JLabel agencyLabel,dateLabel,customerLabel,monthLabel,dayLabel,yearLabel;
    JComboBox agencyBox,monthBox,dayBox;
    JTextField customerBox,yearBox;
    JButton searchButton,clearButton,mainPage;
    private String[] monthList = {"","01","02","03","04","05","06","07","08","09","10","11","12"};
    private String[] agencyOptions;
    public int agencySize = 0;
    static final String INITIAL_QUERY = "SELECT Customer_ID, First_Name, Last_Name, Street_Address, City, Zip_Code, Phone_Number FROM jos_fb_customer ORDER BY Customer_ID ASC;";
    static final String INITIAL_QUERY_COUNT = "SELECT COUNT(*), Customer_ID, First_Name, Last_Name, Street_Address, City, Zip_Code, Phone_Number FROM jos_fb_customer ORDER BY Customer_ID ASC;";
    static final String SIZE_DATABASE_QUERY = "SELECT COUNT(Customer_ID) FROM jos_fb_customer;";
    private String query = "",prevMonth;
    public String Aname = "";
    public String AGENCY_SEARCH_QUERY = "SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = ";
   // public String ADV_SEARCH_QUERY = "SELECT Customer_ID, First_Name, Last_Name FROM jos_fb_customer WHERE Customer_ID = (SELECT Customer_ID, theDate " +
   //   	" FROM jos_fb_monthlydist WHERE Acct_Num = (SELECT Acct_Num From jos_fb_agency WHERE Agency_Name = '" + name + "'););";
    public String agencyonlyquery = "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, jos_fb_monthlydist.theDate, jos_fb_monthlydist.Acct_Num FROM jos_fb_monthlydist " +
    		"INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Acct_Num = '" + Aname + "' AND jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID ORDER BY jos_fb_monthlydist.theDate DESC, jos_fb_customer.Last_Name ASC";
    
    public AdvancedSearchPanel(Connection connL2, queryQue que, boolean online2, boolean visible, FeastMainFrame f) {
        super("Advanced Search");
        connL = connL2;
        online = online2;
        try
        {
            //name = "YWCA";
            stmtL = connL.createStatement();
            result = stmtL.executeQuery( agencyonlyquery );
            int test = 0;
            while(result.next())
            {
                System.out.println("Here");
                System.out.println(result.getString( "Customer_ID" ));
                System.out.println(result.getString("First_Name"));
                System.out.println(result.getString( "Last_Name" ));
                System.out.println(result.getString("theDate"));
                System.out.println(result.getString( "Acct_Num" ));
                test++;
            }
            System.out.println(test);
            result = stmtL.executeQuery( "SELECT COUNT(*), Agency_Name FROM jos_fb_agency;" );
            agencySize = result.getInt(1);
            System.out.println("agency Size: " + agencySize);
            agencyOptions = new String[agencySize+1];
            agencyOptions[0] = "";
            result = stmtL.executeQuery( "SELECT Agency_Name FROM jos_fb_agency ORDER BY Agency_Name ASC;" );
            result.next();
            for(int i = 1; i <= agencySize; i++)
            {
                agencyOptions[i] = result.getString( "Agency_Name" );
                System.out.println("aName: " + agencyOptions[i]);
                result.next();
            }
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fmf = f;
        setSize(700,500);
        initComponents();
        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        addWindowListener( new WindowListener()
        {
            public void windowActivated( WindowEvent arg0 )
            {
            }
            public void windowClosed( WindowEvent arg0 )
            {
            }
            public void windowClosing( WindowEvent arg0 )
            {
                fmf.setVisible( true );
                fmf.customerSearchField.setText( "" );
                dispose();
            }
            @Override
            public void windowDeactivated( WindowEvent arg0 )
            {
                // TODO Auto-generated method stub

            }
            @Override
            public void windowDeiconified( WindowEvent arg0 )
            {
                // TODO Auto-generated method stub

            }
            public void windowIconified( WindowEvent arg0 )
            {
            }
            public void windowOpened( WindowEvent arg0 )
            {
            }
        });
    }
    
    private void initComponents() {
        panel = new JPanel();
        panel1 = new JPanel(new FlowLayout());
        panel2 = new JPanel(new FlowLayout());
        panel3 = new JPanel(new FlowLayout());
        panel4 = new JPanel(new FlowLayout());
        panel5 = new JPanel();
        panel6 = new JPanel();
        panel1.setPreferredSize(new Dimension(getWidth(),40));
        panel2.setPreferredSize(new Dimension(getWidth(),40));
        panel3.setPreferredSize(new Dimension(getWidth(),40));
        panel4.setPreferredSize(new Dimension(getWidth(),40));
        panel5.setPreferredSize(new Dimension(getWidth(),40));
        panel6.setPreferredSize(new Dimension(getWidth(),40));
        agencyLabel = new JLabel("Select Agency:");
        agencyBox = new JComboBox(agencyOptions);
        dateLabel = new JLabel("Enter Date: ");
        monthLabel = new JLabel("M");
        monthBox = new JComboBox(monthList);
        dayLabel = new JLabel("D");
        yearLabel = new JLabel("Year(YYYY)");
        yearBox = new JTextField(4);
        customerLabel = new JLabel("Enter Customer ID");
        customerBox = new JTextField(20);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear Search");
        mainPage = new JButton("Return to Main Menu");
        model = new AdvancedTableModel( connL, online, agencyonlyquery, "SELECT COUNT(*), * FROM jos_fb_monthlydist;" );
        JTable table = new JTable(model);
        table.setPreferredSize(new Dimension(getWidth(),250));
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(table);
        listModel = table.getSelectionModel();
        listModel.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        listModel.addListSelectionListener( new AdvancedSelectionHandler() );
        table.setSelectionModel( listModel );
        table.setPreferredScrollableViewportSize( table.getPreferredSize() );
        table.setFillsViewportHeight( true );
        
        panel1.add(agencyLabel,BorderLayout.EAST);
        panel1.add(agencyBox,BorderLayout.EAST);
        panel2.add(dateLabel,BorderLayout.EAST);
        panel2.add(monthLabel,BorderLayout.EAST);
        panel2.add(monthBox,BorderLayout.EAST);
        panel2.add(yearLabel,BorderLayout.EAST);
        panel2.add(yearBox,BorderLayout.EAST);
        panel3.add(customerLabel,BorderLayout.EAST);
        panel3.add(customerBox,BorderLayout.EAST);
        panel4.add(searchButton,BorderLayout.EAST);
        panel4.add(clearButton,BorderLayout.EAST);
        panel6.add(mainPage,BorderLayout.EAST);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);
        panel.add(scroll);
        panel.add(panel6,BorderLayout.SOUTH);
        this.add(panel);
        searchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed( ActionEvent e ) 
            {
                String customerid = customerBox.getText();
                String agency = (String)agencyBox.getSelectedItem();
                String month = (String)monthBox.getSelectedItem();
                //String day = (String)dayBox.getSelectedItem();
                String year = yearBox.getText();
                System.out.println("AGENCY IS:"+agency+".");
                System.out.println("CUSTOMERID IS:"+customerid+".");
                boolean ag = false, cid = false, mo = false, ye = false;
                if(customerid != "")
                    cid = true;
                if(agency != "")
                    ag = true;
                if(month != "")
                    mo = true;
                if(year != "")
                    ye = true;              
                try
                {
                    //Just the Customer_ID
                    if(cid && !ag && !mo && !ye)
                    {
                       
                    }
                    //THe Customer_ID and the agency
                    else if(cid && ag && !mo && !ye)
                    {
                        
                    }
                    //The Customer_ID, the agency and the Month
                    else if(cid && ag && mo && !ye)
                    {
                        
                    }
                    //ALL of them
                    else if(cid && ag && mo && ye)
                    {
                        
                    }
                    //None of them
                    else if(!cid && !ag && !mo && !ye)
                    {
                        
                    }
                    else if(cid && !ag && !mo && !ye)
                    {
                        
                    }
                    else if(cid && !ag && !mo && !ye)
                    {
                        
                    }
                    else if(cid && !ag && !mo && !ye)
                    {
                        
                    }
                    else if(cid && !ag && !mo && !ye)
                    {
                        
                    }
                    else if(cid && !ag && !mo && !ye)
                    {
                        
                    }
                    model.setQuery(agencyonlyquery, "SELECT COUNT(*), * FROM jos_fb_monthlydist;");
                    if (customerid != "") {
                        System.out.println("WHOOPS");
                        model.setQuery( "SELECT Customer_ID, First_Name, Last_Name, Street_Address, City, Zip_Code, Phone_Number FROM jos_fb_customer WHERE Customer_ID = '"+customerid+"' ORDER BY Customer_ID ASC;",
                            "SELECT COUNT(*), Customer_ID, First_Name, Last_Name, Street_Address, City, Zip_Code, Phone_Number FROM jos_fb_customer WHERE Customer_ID = '"+customerid+"' ORDER BY Customer_ID ASC;");
                    }
                    else if (agency != "") {
                        System.out.println("Got agency: "+agency);
                        model.setQuery( "SELECT * FROM jos_fb_agency WHERE Agency_Name = \'"+agency+"\'",  "SELECT COUNT(*),* FROM jos_fb_agency WHERE Agency_Name = \'"+agency+"\'");
                    }
                    else if (month != "") {
                        
                    }
                    else {
                        System.out.println("GUESS I DIDN'T SEE IT");
                    }
                }
                catch ( Exception e1 )
                {
                    JOptionPane.showMessageDialog( null,
                        e1.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE );
                    try
                    {
                        model.setQuery( INITIAL_QUERY, INITIAL_QUERY_COUNT );
                    }
                    catch ( Exception e2 )
                    {
                        JOptionPane.showMessageDialog( null,
                            e2.getMessage(),
                            "Database error",
                            JOptionPane.ERROR_MESSAGE );
                        System.exit( 0 );
                    }
                }
            }
        });
        monthBox.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent l) {
                String m = (String)monthBox.getSelectedItem();
                String[] days;
                if (m == "02") {
                    String[] dayList = {"","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29"};
                    days = dayList;
                }
                else if (m == "04" || m == "06" || m == "09" || m == "11") {
                    String[] dayList = {"","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
                    days = dayList;
                }
                else {
                    String[] dayList = {"","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
                    days = dayList;
                }
                dayBox = new JComboBox(days);
                if (prevMonth == null) {
                    panel2.remove(yearLabel);
                    panel2.remove(yearBox);
                    panel2.add(dayLabel,BorderLayout.EAST);
                    panel2.add(dayBox,BorderLayout.EAST);
                    panel2.add(yearLabel,BorderLayout.EAST);
                    panel2.add(yearBox,BorderLayout.EAST);
                    revalidate();
                }
                else if (m == "") {
                    panel2.remove(dayLabel);
                    panel2.remove(dayBox);
                    revalidate();
                }
                else {
                    panel.removeAll();
                    panel2.add(dateLabel,BorderLayout.EAST);
                    panel2.add(monthLabel,BorderLayout.EAST);
                    panel2.add(monthBox,BorderLayout.EAST);
                    panel2.add(dayLabel,BorderLayout.EAST);
                    panel2.add(dayBox,BorderLayout.EAST);
                    panel2.add(yearLabel,BorderLayout.EAST);
                    panel2.add(yearBox,BorderLayout.EAST);
                    revalidate();
                }
                prevMonth = m;
            }
        });
        yearBox.setDocument(new PlainDocument(){
            @Override
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if(getLength() + str.length() <= 4)
                    super.insertString(offs, str, a);
            }
        });
        mainPage.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                fmf.setVisible( true );
                fmf.customerSearchField.setText( "" );
                dispose();
            }
        });
    }
    
    class AdvancedTableModel extends AbstractTableModel
    {
        /**
         * 
         */
        private ResultSetMetaData metaData;

        private boolean connectedToDatabase = false;

        private static final long serialVersionUID = 1L;
        ArrayList<String> ids = new ArrayList<String>();


        public AdvancedTableModel(
            Connection connL,
            boolean online,
            String query1, String rcQuery )
        {
            connectedToDatabase = online;
            try
            {
                System.out.println( "Test connection local: "
                    + connL.toString() );
                stmtL = connL.createStatement();
             //   query = query1;
                setQuery( query1, rcQuery );
            }
            catch ( SQLException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        private void setQuery( String query1, String reqCount )
        {
            try
            {
                System.out.println("IM SETTING A QUERY: "+query);
                System.out.println("Set Query");
                query = query1;
                ids.clear();
                    result = stmtL.executeQuery( reqCount );
                    numberOfRows = result.getInt( 1 );
                    result = stmtL.executeQuery( query );
                    while(result.next())
                    {
                        //this is for agencies not customers
                        if(query1.contains( "jos_fb_agency" ))
                        {
                            ids.add(result.getString("Acct_Num"));
                        }
                        else if (query1.contains("jos_fb_monthlydist")) {
                            ids.add(result.getString("Customer_ID"));
                        }
                        else{
                            ids.add( result.getString("Street_Address") );
                        }
                    }
                    result = stmtL.executeQuery( query );
                    metaData = result.getMetaData();

                fireTableStructureChanged();
                
            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }
        }
        
        @Override
        public int getColumnCount()
        {
            try
            {
                System.out.println("getting column count: "+metaData.getColumnCount());
                return metaData.getColumnCount();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            return 0;
        }


        @Override
        public int getRowCount()
        {
            //Number of rows is the whole database
            System.out.println("getting row count: "+numberOfRows);
            return numberOfRows;
        }


        @Override
        public String getColumnName( int column ) throws IllegalStateException
        {
            try
            {
                return metaData.getColumnName( column + 1 );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            return "";
        }


        @Override
        public Object getValueAt( int r, int col )
        {
            try
            {
                //Problem is here because it selects everything!! It selects everything because the r value
                //goes through all r possibilitites
                    if(r < ids.size()){
                    String SELECT_ONE_ROW = "SELECT * FROM jos_fb_customer WHERE Customer_ID = ";
                    
                    result = stmtL.executeQuery( SELECT_ONE_ROW + "'" + ids.get(r) + "';" );
                    return result.getObject( col + 1  );
                    }
                    return (Object)" ";

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            return "";
        }


        public Class getColumnClass( int c ) throws IllegalStateException
        {
            try
            {
                String className = metaData.getColumnClassName( c + 1 );
                return Class.forName( className );
            }
            catch ( Exception exception )
            {
                exception.printStackTrace();
            }
            return Object.class;
        }


        public boolean isCellEditable( int row, int col )
        {
            if ( col == 1 )
            {
                return false;
            }
            else
            {
                return false;
            }
        }
    }

    class AdvancedSelectionHandler implements ListSelectionListener
    {
        public void valueChanged( ListSelectionEvent e )
        {
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            selected = new ArrayList<Integer>();
            String output = "";
            if ( lsm.isSelectionEmpty() )
            {
                System.out.println( " <none>" );
            }
            else
            {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for ( int i = minIndex; i <= maxIndex; i++ )
                {
                    if ( lsm.isSelectedIndex( i ) )
                    {
                        output += ( " " + i );
                        selected.add( i + 1 );
                    }
                }
            }
            output += "\n";
            System.out.println( output );
        }
    }
}
