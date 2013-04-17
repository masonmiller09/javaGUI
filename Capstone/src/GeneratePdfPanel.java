import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.NoSuchObjectException;
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
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;


public class GeneratePdfPanel extends JFrame {
    
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
    private PDFTableModel model;
    private ListSelectionModel listModel;
    private ArrayList<Integer> selected;
    JPanel panel1,panel2,panel3,panel;
    JLabel agencyLabel,descriptionLabel;
    JButton searchButton,pdfButton,mainPage;
    JComboBox agencyBox;
    int agencySize, population;
    private Object[] currentInfo;
    private String[] agencyOptions/* = {"","Agape Cathedral Center","An Acheivable Dream","Andrews Elementary","Beauty for Ashes","Behind the Veil Ministry","Berachah Church","Berkley Village Apartments","Bethel Temple","Booker Elementary","Bread for Life","Buckroe Baptist Church",
            "C.H.P. Lafayette Village","C.H.P. Rivermede","C.H.P. Woods @ Yorktown", "Cary Elementary","Checed Warwick","Christ Temple Holiness Church","Coastal Community Church","Deeper Life Assembly","Doris Miller Center","Dunamis Christian Center",
            "Emmanuel House Inc.","Empowered Believers Christian","First Baptist - Jefferson Park","First Baptist Denbigh","Five Loaves","Forrest Elementary","Garden of Prayer","Gleaning Baptist Church","Greater Bethlehem Christ. As.","Greater Emmanuel Ministries",
            "Greater Works Ministries","Grove Christian Outreach","Holy Tabernacle Church","Hope House Ministries","Ivy Baptist Church","Ivy Farms Church","JTC Lifechanging Center Inc.","Just-Us-Kidz Inc.","Langley Elementary","Langley Village","Lexington Commons",
            "Lifeline Full Gospel","Living Faith Christian Center","Living Waters Family Outreach","Living Waters Way of the Cross","Magruder Elementary","Mercy Seat Bapt.","Mt. Calvary SDA Church","Mt. Calvary Baptist","New Beginnings","New Bethel International",
            "New Hope Baptist Church","New Life Ministry Center","Newport News Housing","Oasis of Life","Open Door Full Gospel","Operation Breaking Through","Peninsula Hispanic SDA","Perfecting Saints Ministries","Pinecroft","Pocahontas Temple","Poquoson",
            "Salem UMC","Salvation Army (Hampton)","Salvation Army (Williamsburg)","Seton Manor","Smith Elementary","Spirit of Truth Ministries","Sr. Christian Village of E. VA.","St. James Deliverance","St. John Baptist Church","St. Marks UMC","St. Timothy Church",
            "Surry Community Center 1","Tarrant Elementary","Temple of Life / New Life","Temple of Peace","Temple of Refuge","Triumph Christian Center","Tyler Elementary","W.M. Ratley Road Ahead","Warwick Assembly of God","White Marsh Baptist Church",
            "Williamsburg/Blayton Building","Williamsburg/Burnt Ordinary","Williamsburg/Katherine Circle","Williamsburg/Mimosa Woods","Williamsburg/Sylvia Brown","World Outreach Worship Center","YWCA","Zion Prospect Baptist Church"}*/;
    private String query = "";
    private String Aname ="";
    static final String INITIAL_QUERY = "SELECT Customer_ID, First_Name, Last_Name, City, Zip_Code, Phone_Number FROM jos_fb_customer WHERE Customer_ID = 'a';";
    
    static final String SIZE_DATABASE_QUERY = "SELECT COUNT(Customer_ID) FROM jos_fb_customer;";
    
    
    public GeneratePdfPanel(Connection connL2, queryQue que, boolean online2, boolean visible, FeastMainFrame f) {
        super("PDF Generator");
        connL = connL2;
        online = online2;
        try
        {
            stmtL = connL.createStatement();
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
                result.next();
            }
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fmf = f;
        setSize(650,150);
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
        panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel3 = new JPanel(new FlowLayout());
        panel = new JPanel(new GridLayout(0,1));
        panel1.setPreferredSize(new Dimension(getWidth(),40));
        panel2.setPreferredSize(new Dimension(getWidth(),40));
        panel3.setPreferredSize(new Dimension(getWidth(),40));
        
        //JLabels
        agencyLabel = new JLabel("Select Agency:");
        descriptionLabel = new JLabel("Select an agency to generate a list of all customers who have visited that agency");
        
        //ComboBoxes
        agencyBox = new JComboBox(agencyOptions);
        
        //JButtons
        searchButton = new JButton("Search");
        pdfButton = new JButton("Open Selected PDFs");
        pdfButton.setVisible(false);
        mainPage = new JButton("Return to Main Menu");
        
        //table
        model = new PDFTableModel( connL, online, INITIAL_QUERY, "SELECT COUNT(*), Customer_ID FROM jos_fb_customer WHERE Customer_ID = 'a'" );
        JTable table = new JTable(model);
        table.setPreferredSize(new Dimension(getWidth(),200));
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        final JScrollPane scroll = new JScrollPane(table);
        listModel = table.getSelectionModel();
        listModel.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        listModel.addListSelectionListener( new PDFSelectionHandler() );
        table.setSelectionModel( listModel );
        table.setPreferredScrollableViewportSize( table.getPreferredSize() );
        table.setFillsViewportHeight( true );
        scroll.setVisible(false);
        
        //Listeners
        mainPage.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                fmf.setVisible( true );
                fmf.customerSearchField.setText( "" );
                dispose();
            }
        });
        searchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String agency = (String)agencyBox.getSelectedItem();
                Aname = agency;
                System.out.println("Aname: "+Aname);
                String agencyonlyquery = "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, jos_fb_monthlydist.theDate, jos_fb_monthlydist.Acct_Num FROM jos_fb_monthlydist " +
                "INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + Aname + "') AND jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID ORDER BY jos_fb_monthlydist.theDate DESC, jos_fb_customer.Last_Name ASC;";
            String requery = "SELECT COUNT(*), Customer_ID FROM jos_fb_monthlydist WHERE Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '"+Aname+"');";
                setSize(650,300);
                scroll.setVisible(true);
                pdfButton.setVisible(true);
                model.setQuery( agencyonlyquery, requery);
            }
        });
        pdfButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int move;
                try
                {
                    move = selected.get(0);
                    String SELECT_ONE_ROW = "SELECT * FROM jos_fb_customer LIMIT 1 OFFSET "
                        + move + ";";
                    result = stmtL.executeQuery(SELECT_ONE_ROW);
                    for(int i = 0; i < selected.size(); i++)
                    {
                        System.out.println("indexes: " + selected.get(i));
                    }
                    System.out.println("move: " + move);
                }
                catch(SQLException e2)
                {
                    e2.printStackTrace();
                }
                for(int i = 0; i < numberOfRows; i++)
                {
                    try
                    {
                        if(selected != null)
                        {
                            for(int j = 0; j < selected.size(); j++)
                            {
                                if(result.getRow() == selected.get(j))
                                {
                                    Integer id = (Integer)result.getInt(1);
                                    System.out.println("Customer ID " + id);
                                    String acct_num = "2843";
                                    int one = 1;
                                    try
                                    {
                                        currentInfo = functions.retrieveUser( id,
                                            online , connL);
                                        System.out.println( "Retrieving Agency" );
                                        Object[] agencyInfo = functions.retrieveAgency( acct_num,
                                            online );
                                        System.out.println( "Retrieving Agent" );
                                        Object[] agentInfo = functions.retrieveAgent( one,
                                            online );
                                        PdfGenerator.populateSDIForm( currentInfo,
                                            agencyInfo,
                                            agentInfo,
                                            population );
                                        population++;
                                    }
                                    catch ( NoSuchObjectException o )
                                    {
                                        // TODO Auto-generated catch block
                                        JOptionPane.showMessageDialog( null,
                                            "There is no user: \"" + id
                                            + "\" in the database",
                                            "Customer Search Error",
                                            JOptionPane.INFORMATION_MESSAGE );
                                    }
                                    catch ( NumberFormatException o )
                                    {
                                        // TODO Auto-generated catch block
                                        o.printStackTrace();
                                    }
                                }
                            }
                        }
                        result.next();
                    }
                    catch(Exception b)
                    {
                        b.printStackTrace();
                    }
                }
                PdfGenerator.mergePdfFiles(population);
                population = 0;
            }
        });
        panel1.add(descriptionLabel,BorderLayout.EAST);
        panel1.add(agencyLabel,BorderLayout.EAST);
        panel1.add(agencyBox,BorderLayout.EAST);
        panel1.add(searchButton,BorderLayout.EAST);
        panel3.add(pdfButton);
        panel3.add(mainPage);
        
        panel.add(panel1);
        //panel.add(panel2);
        panel.add(scroll);
        //panel.add(panel2);
        panel.add(panel3,BorderLayout.SOUTH);
        this.add(panel);
    }
    class PDFTableModel extends AbstractTableModel
    {
        /**
         * 
         */
        private ResultSetMetaData metaData;

        private boolean connectedToDatabase = false;

        private static final long serialVersionUID = 1L;
        ArrayList<String> ids = new ArrayList<String>();
        
        //boolean first = false;
        
        public PDFTableModel(
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
                query = query1;
                setQuery( query1, rcQuery );
            }
            catch ( SQLException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        private void setQuery( String query2, String reqCount )
        {
            try
            {
                System.out.println("IM SETTING A QUERY B4: "+ query);
                System.out.println("Set Query");
                query = query2;
                System.out.println("IM SETTING A QUERY After: "+ query);
                ids.clear();
                System.out.println("reqCount: "+reqCount);
                    result = stmtL.executeQuery( reqCount );
                    numberOfRows = result.getInt( 1 );
                    System.out.println("numRows: " + numberOfRows);
                    result = stmtL.executeQuery( query );
                  
                    while(result.next())
                    {
                        //this is for agencies not customers
                            //System.out.println("Set to a distribution query");
                            String temp = result.getString("Customer_ID");
                            System.out.println(result.getString("Customer_ID") + "," + result.getString("Last_Name") + "," + result.getString("First_Name") + "," + result.getString("theDate") + "," + result.getString("Acct_Num"));
                            ids.add(temp);
                    }
                    //first = true;
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
               // System.out.println("getting column count: "+metaData.getColumnCount());
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
            //System.out.println("getting row count: "+numberOfRows);
            return numberOfRows;
        }


        @Override
        public String getColumnName( int column )
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
               System.out.println("r: " + r + "; col: " + col);
                String temp = "";
                    if((r < ids.size() && result.getRow() != ids.size()+1) || (result.getRow() == 1 && ids.size() == 1)){
                    //String SELECT_ONE_ROW = "SELECT Customer_ID, Last_Name, First_Name, Street_Address, City, Zip_Code, Phone_Number FROM jos_fb_customer WHERE Customer_ID = ";
                    System.out.println("Im here");
                   
                    if(col == 1){
                        System.out.println("Im here at col =1");
                        return result.getString("Last_Name");
                    }
                    else if(col == 2)
                    {
                        System.out.println("Im here at col = 2");
                        return result.getString("First_Name");
                    }
                    else if(col == 3)
                    {
                        System.out.println("Im here at col = 3");
                        return result.getString("theDate");
                    }
                    else if(col == 4)
                    {
                        System.out.println("Im here at col = 4");
                        return result.getString("Acct_Num");
                    }
                    else if(col > 4)
                    {
                       return "";
                    }
                    
                    result.next();
                    return  result.getObject( col + 1  );
                      //  temp = ids.get(r);
                    //result = stmtL.executeQuery( SELECT_ONE_ROW + "'" + temp + "';" );
                    //return result.getObject( col + 1  );
                    }
                    return "";

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            return "";
        }


        public Class getColumnClass( int c )
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

    class PDFSelectionHandler implements ListSelectionListener
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
                        selected.add( i + 1 );
                        System.out.println("Selected " + selected.get( i+1 ));
                    }
                }
            }
        }
    }
}
