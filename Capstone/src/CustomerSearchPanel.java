import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import sun.org.mozilla.javascript.internal.IdScriptableObject;



public class CustomerSearchPanel extends JFrame{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static boolean online;
    public static Connection connL = null;
    public ResultSet result = null;
    queryQue que;
    private int numberOfRows;
    private CustomerTableModel model;
    private ListSelectionModel listModel;
    private static String searching;
    ArrayList<String> ids = new ArrayList<String>();
    JTextField searchField;
    JLabel searchLabel;
    static final String DEFAULT_QUERY = "SELECT Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer;";
    static final String INITIAL_QUERY = "SELECT Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer WHERE First_Name LIKE  \'"+ searching + "%\' or Last_Name LIKE \'" + searching + "%\' ORDER BY Last_Name ASC;";
    static final String SIZE_DATABASE_QUERY = "SELECT COUNT(Customer_ID) FROM jos_fb_customer;";
    String query = "";
    boolean FMFvisible;
    FeastMainFrame fmf;
    String ONE_ROW = "SELECT Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer WHERE First_Name LIKE  \'"+ searching + "%\' or Last_Name LIKE \'" + searching + "%\' ORDER BY Customer_ID LIMIT 2 OFFSET 0;";
    JButton mainPage,searchButton;
    
    public CustomerSearchPanel( Connection conL, queryQue q, boolean isConnect, String text, boolean visible, FeastMainFrame f) {
        super("Customer Search");
        setSize(650,450);
        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        searching = text;
        FMFvisible = visible;
        fmf = f;
        JPanel topPanel = new JPanel();
        String request = "SELECT Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer WHERE First_Name LIKE  \'"+ searching + "%\' or Last_Name LIKE \'" + searching + "%\';";
        String reqCount = "SELECT COUNT(*), Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer WHERE First_Name LIKE  \'"+ searching + "%\' or Last_Name LIKE \'" + searching + "%\';";
        
        connL = conL;
        online = isConnect;
        que = q;
        mainPage = new JButton("Return to Main Menu");
        searchLabel = new JLabel("Search for a Customer by First or Last name");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        topPanel.add(searchLabel,BorderLayout.EAST);
        topPanel.add(searchField,BorderLayout.EAST);
        topPanel.add(searchButton,BorderLayout.EAST);
        add(topPanel,BorderLayout.NORTH);
        model = new CustomerTableModel(online, request, reqCount );
        JTable table = new JTable(model);
        table.setPreferredSize(new Dimension(650,300));
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(table);
        add(scroll);
        add(mainPage,BorderLayout.SOUTH);
        listModel = table.getSelectionModel();
        listModel.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listModel.addListSelectionListener( new CustomerSelectionHandler() );
        table.setSelectionModel( listModel );
        table.setPreferredScrollableViewportSize( table.getPreferredSize() );
        table.setFillsViewportHeight( true );
        searchButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                String text = searchField.getText();
                try
                {
                    String req = "";
                    String reqCount = "";
                    if (text == "") {
                        req = ( "SELECT Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer ORDER BY Last_Name ASC;");
                        reqCount = "SELECT COUNT(*), Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer ORDER BY Last_Name ASC;";
                    }
                    else{
                        req = "SELECT Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer WHERE First_Name LIKE  \'"+ text + "%\' or Last_Name LIKE \'" + text + "%\';";
                        reqCount = "SELECT COUNT(*), Last_Name, First_Name, Street_Address, City, Phone_Number FROM jos_fb_customer WHERE First_Name LIKE  \'"+ text + "%\' or Last_Name LIKE \'" + text + "%\';";
                    }
                    
                    model.setQuery(req, reqCount);
                }
                catch ( Exception e1 )
                {
                    JOptionPane.showMessageDialog( null,
                        e1.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE );
                    try
                    {
                        System.out.println("HAHA I GOT YOU");
                       // model.setQuery( DEFAULT_QUERY );
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
        mainPage.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                fmf.setVisible(true);
                fmf.customerSearchField.setText( "" );
                dispose();
            }
        });
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
               // FeastMainFrame frame = new FeastMainFrame();
                fmf.refreshVisible(true);
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
        } );
    }
    
    class CustomerTableModel extends AbstractTableModel
    {
        /**
         * 
         */
        private Statement stmtL;

        private ResultSetMetaData metaData;

        private boolean connectedToDatabase = false;

        private static final long serialVersionUID = 1L;
        
        private int count = 0;
        
       


        public CustomerTableModel(
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
                        ids.add( result.getString("Street_Address") );
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
                    String SELECT_ONE_ROW = "SELECT * FROM jos_fb_customer WHERE Street_Address = ";
                    
                    result = stmtL.executeQuery( SELECT_ONE_ROW + "'" + ids.get(r) + "';" );
                    return result.getObject( col + 1  );
                    }
                    return (Object)" ";
         
                    //if(r >= 1)
                    //    result.next();
               // System.out.println("Result from get value: "+ result.getString("First_Name")+ result.getString("Last_Name"));
              //      System.out.println("For: " + r + ", " + col + " there is: " + result.getObject( col + 1 ).toString());
                
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

    class CustomerSelectionHandler implements ListSelectionListener
    {
        public Statement stmtL;
        public void valueChanged( ListSelectionEvent e )
        {
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            //selected = new ArrayList<Integer>();
            
            if ( !lsm.isSelectionEmpty() && !e.getValueIsAdjusting())
            {
                int minIndex = lsm.getMinSelectionIndex();
                try
                {
                    System.out.println("CLICKED SOMETHING");
                    stmtL = connL.createStatement();
                    result = stmtL.executeQuery( "SELECT Customer_ID FROM jos_fb_customer WHERE Street_Address = '" + ids.get(minIndex) + "';" );
                    Integer id = result.getInt( 1 );
                    FBcustomerRS fbc = new FBcustomerRS(connL, true, id);
                    SDIForm sdi = new SDIForm(connL, false, que,online,fmf);
                    sdi.idField.setText( fbc.getCustomerID()+"" );
                    sdi.firstNameField.setText( fbc.getFirstName() );
                    sdi.lastNameField.setText( fbc.getLastName() );
                    sdi.addressField.setText( fbc.getAddress() );
                    sdi.apartmentNumberField.setText( fbc.getApt_Num()+"" );
                    sdi.cityField.setText( fbc.getCity() );
                    sdi.zipField.setText( fbc.getZip_Code()+"" );
                    sdi.phoneField.setText( fbc.getPhone_Num()+"" );
                    sdi.numChildrenField.setText( fbc.getNum_Children()+"" );
                    sdi.numAdultsField.setText( fbc.getNum_Adults()+"" );
                    sdi.numSeniorsField.setText( fbc.getNum_Seniors()+"" );
                    sdi.totInHouseholdField.setText( fbc.getTotal_Household()+"" );
                    boolean val = false;
                    if (fbc.getFoodstamps_Snap() == 0) {
                        val = false;
                    }
                    else{
                        val = true;
                    }
                    sdi.foodstamps.setSelected( val );
                    if (fbc.getTanf() == 0) {
                        val = false;
                    }
                    else {
                        val = true;
                    }
                    sdi.tanf.setSelected( val );
                    if (fbc.getSsi() == 0) {
                        val = false;
                    }
                    else {
                        val = true;
                    }
                    sdi.ssi.setSelected( val );
                    if (fbc.getMedicaid() == 0) {
                        val = false;
                    }
                    else {
                        val = true;
                    }
                    sdi.medicaid.setSelected( val );
                    sdi.hhIncomeField.setText( fbc.getHh_Income()+"" );
                    if (fbc.getInc_Weekly() == 0) {
                        val = false;
                    }
                    else {
                        val = true;
                    }
                    sdi.weekly.setSelected( val );
                    if (fbc.getInc_Monthly() == 0) {
                        val = false;
                    }
                    else {
                        val = true;
                    }
                    sdi.monthly.setSelected( val );
                    if (fbc.getInc_Yearly() == 0) {
                        val = false;
                    }
                    else {
                        val = true;
                    }
                    sdi.annually.setSelected( val );
                    sdi.setVisible( true );
                }
                catch ( SQLException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }
            //output += "\n";
            //System.out.println( output );
        }
    }
}
