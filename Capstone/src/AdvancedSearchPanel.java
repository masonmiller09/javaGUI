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

import java.sql.Date;
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
import javax.swing.ScrollPaneConstants;
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
    public static boolean online,dayboxopen;
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
    static final String INITIAL_QUERY = "SELECT Customer_ID, First_Name, Last_Name, Street_Address, City, Zip_Code, Phone_Number FROM jos_fb_customer ORDER BY Customer_ID ASC";
    static final String INITIAL_QUERY_COUNT = "SELECT COUNT(*), Customer_ID, First_Name, Last_Name, Street_Address, City, Zip_Code, Phone_Number FROM jos_fb_customer;";
    static final String SIZE_DATABASE_QUERY = "SELECT COUNT(*), Customer_ID FROM jos_fb_customer;";
    private String query = "",prevMonth;
    public String Aname = "";
    public String AGENCY_SEARCH_QUERY = "SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = ";
    public String agencyonlyquery = "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, jos_fb_monthlydist.theDate, jos_fb_monthlydist.Acct_Num FROM jos_fb_monthlydist " +
        	"INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Acct_Num = '" + Aname + "' AND jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID ORDER BY jos_fb_monthlydist.theDate DESC, jos_fb_customer.Last_Name ASC";
    
    boolean ag = false;
    boolean cid = false;
    boolean mo = false;
    boolean ye = false;
    boolean da = false;
    
    public AdvancedSearchPanel(Connection connL2, queryQue que, boolean online2, boolean visible, FeastMainFrame f) {
        super("Advanced Search");
        connL = connL2;
        online = online2;
        dayboxopen =false;
        try
        {
            stmtL = connL.createStatement();
           
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
        agencyBox = new JComboBox();
        agencyBox.addItem( "" );
        try
        {
            stmtL = connL.createStatement();
            result = stmtL.executeQuery( "SELECT Agency_Name FROM jos_fb_agency ORDER BY Agency_Name ASC;" );
            while(result.next()){
                agencyBox.addItem( result.getObject( "Agency_Name" ));
            }
            
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        model = new AdvancedTableModel( connL, online, agencyonlyquery, "SELECT COUNT(*), * FROM jos_fb_monthlydist WHERE Customer_ID = -1;" );
        JTable table = new JTable(model);
        
        //table.setPreferredSize(new Dimension(getWidth()-50,getHeight()/3));
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(table);
        //scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
        listModel = table.getSelectionModel();
        listModel.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listModel.addListSelectionListener( new AdvancedSelectionHandler() );
        table.setSelectionModel( listModel );
        table.setPreferredScrollableViewportSize( (new Dimension(getWidth()-80,getHeight()/4))) ;
        table.setFillsViewportHeight( true );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        
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
                ag = false;
                cid = false;
                mo = false;
                ye = false;
                da = false;
                String customerid = customerBox.getText();
                String agency = (String)agencyBox.getSelectedItem();
                
                String month = (String)monthBox.getSelectedItem();
                String day = "";
                             
                String year = yearBox.getText();
                if ( month != "" )
                {
                    if ( dayBox.isVisible() )
                    {
                        day = (String)dayBox.getSelectedItem();
                        if (day != ""){
                            da = true;
                        }
                    }
                }
                System.out.println("AGENCY IS:"+agency+".");
                System.out.println("CUSTOMERID IS:"+customerid+".");
                
                if ( !customerid.isEmpty())
                {
                    cid = true;
                }
                if ( agency != "" )
                {
                    ag = true;
                }
                if ( month != "" )
                {
                    mo = true;
                }
                if ( !year.isEmpty() )
                {
                    ye = true;
                }
                
                try
                {
                    //model.setQuery(agencyonlyquery, "SELECT COUNT(*), * FROM jos_fb_monthlydist;");
                    //Just the Customer_ID
                    System.out.println("cid: "+ cid + " ag: " + ag + " mo: " + mo + " ye: " + ye);
                    if(cid && !ag && !mo && !ye)
                    {
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.Customer_ID = " + customerid + " ORDER BY jos_fb_customer.Customer_ID ASC",
                            "SELECT COUNT(*), Customer_ID FROM jos_fb_monthlydist WHERE Customer_ID = "+ customerid + ";");
                    }
                    //THe Customer_ID and the agency
                    else if(cid && ag && !mo && !ye)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                    //The Customer_ID, the agency and the Month
                    else if(cid && ag && mo && !ye && !da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '%-" + month + "-%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '%-" + month + "-%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                  //The Customer_ID, the agency and the year
                    else if(cid && ag && !mo && ye && !da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '" + year + "-%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '" + year + "-%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                  //The Customer_ID, the agency and the Month and the day
                    else if(cid && ag && mo && !ye && da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '%-" + month + "-" + day + "' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '%-" + month + "-" + day + "' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                    //The CUstomer_ID, the agency, the month and year
                    else if(cid && ag && mo && ye && !da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "-" + month + "-%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "-" + month + "-%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");   
                    }
                  
                    //All of them
                    else if(cid && ag && mo && ye && da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "-" + month + "-" + day + "' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "-" + month + "-" + day + "' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                    //Customer ID and Month
                    else if(cid && !ag && mo && !ye && !da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '%-" + month + "-%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '%-" + month + "-%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + ";");
                    }
                    //Customer_ID and MOnth and Day
                    else if(cid && !ag && mo && !ye && da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '%-" + month + "-" + day + "' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '%-" + month + "-" + day + "' AND jos_fb_monthlydist.Customer_ID = "+ customerid + ";");
                    }
                    //Customer ID, Month and Year
                    else if(cid && !ag && mo && ye && !da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "-" + month + "%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "-" + month + "%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + ";");
                    }
                  //Customer ID, Month day and Year
                    else if(cid && !ag && mo && ye && da)
                    {
                        //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "-" + month + "-" + day + "' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "-" + month + "-" + day + "' AND Customer_ID = "+ customerid + ";");   
                    }
                    //Customer ID and Year
                    else if(cid && !ag && !mo && ye)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + " ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "%' AND jos_fb_monthlydist.Customer_ID = "+ customerid + ";");  
                    }
                    //Just Agency
                    else if(!cid && ag && !mo && !ye)
                    {
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') AND jos_fb_customer.Customer_ID = jos_fb_monthlydist.Customer_ID ORDER BY jos_fb_monthlydist.Customer_ID ASC",
                            "SELECT COUNT(*), jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') AND jos_fb_customer.Customer_ID = jos_fb_monthlydist.Customer_ID;");
                    }
                    //Agency and Month
                    else if(!cid && ag && mo && !ye && !da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '%-" + month + "-%' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '%-" + month + "-%' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                  //Agency and Month and day
                    else if(!cid && ag && mo && !ye && da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '%" + month + "-" + day + "' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_monthlydist.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '%-" + month + "-" + day + "' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                    //Agency, Month and Year
                    else if(!cid && ag && mo && ye && !da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "-" + month + "%' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_customer.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "-" + month + "%' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                    //Agency, Month and Year and day
                    else if(!cid && ag && mo && ye && da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "-" + month + "-" + day + "' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_customer.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "-" + month + "-" + day + "' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                    //Agency and Year
                    else if(!cid && ag && !mo && ye)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "%' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "') ORDER BY jos_fb_customer.Customer_ID ASC"
                            ,"SELECT COUNT(*), jos_fb_monthlydist.Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "%' AND Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agency + "');");
                    }
                    //Just Month
                    else if(!cid && !ag && mo && !ye && !da)
                    {
                        //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '%-" + month + "-%' ORDER BY jos_fb_customer.Customer_ID ASC",
                            "SELECT COUNT(*), Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '%-" + month + "-%';");
                    }
                    //Just Month and Year and day
                    else if(!cid && !ag && mo && ye && da)
                    {
                        //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate = '" + year + "-" + month + "-" + day + "' ORDER BY jos_fb_customer.Customer_ID ASC",
                            "SELECT COUNT(*), Customer_ID FROM jos_fb_monthlydist WHERE theDate = '"+ year + "-" + month + "-" + day + "';");
                   
                    }
                    //JUST MONTH AND YEAR
                    else if(!cid && !ag && mo && ye && !da)
                    {
                        //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '" + year + "-" + month + "%' ORDER BY jos_fb_customer.Customer_ID ASC",
                            "SELECT COUNT(*), Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "-" + month + "%';");
                   
                    }
                  //Just Year
                    else if(!cid && !ag && !mo && ye)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '"+ year + "%' ORDER BY jos_fb_customer.Customer_ID ASC",
                            "SELECT COUNT(*), Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '"+ year + "%';");
                    }
                    //just month day
                    else if(!cid && !ag && mo && !ye && da)
                    {
                      //System.out.println("Date: " + date);
                        model.setQuery( "SELECT jos_fb_customer.Customer_ID, jos_fb_customer.First_Name, jos_fb_customer.Last_Name, theDate, Acct_Num FROM jos_fb_monthlydist INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID AND jos_fb_monthlydist.theDate LIKE '%-" + month + "-" + day + "' ORDER BY jos_fb_customer.Customer_ID ASC",
                            "SELECT COUNT(*), Customer_ID FROM jos_fb_monthlydist WHERE theDate LIKE '%-" + month + "-" + day + "';");
                    }
                    else{
                        displayFailedDialog();
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
                if (prevMonth == null) {
                    panel2.remove(yearLabel);
                    panel2.remove(yearBox);
                    dayBox = new JComboBox(days);
                    panel2.add(dayLabel,BorderLayout.EAST);
                    panel2.add(dayBox,BorderLayout.EAST);
                    panel2.add(yearLabel,BorderLayout.EAST);
                    panel2.add(yearBox,BorderLayout.EAST);
                    revalidate();
                    dayboxopen = true;
                }
                else if (m == "") {
                    System.out.println("Month is blank");
                    panel2.remove(dayLabel);
                    panel2.remove(dayBox);
                    revalidate();
                    dayboxopen = false;
                }
                else {
                    panel2.removeAll();
                    dayBox = new JComboBox(days);
                    panel2.add(dateLabel,BorderLayout.EAST);
                    panel2.add(monthLabel,BorderLayout.EAST);
                    panel2.add(monthBox,BorderLayout.EAST);
                    panel2.add(dayLabel,BorderLayout.EAST);
                    panel2.add(dayBox,BorderLayout.EAST);
                    panel2.add(yearLabel,BorderLayout.EAST);
                    panel2.add(yearBox,BorderLayout.EAST);
                    revalidate();
                    dayboxopen = true;
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
        clearButton.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                agencyBox.setSelectedIndex(0);
                yearBox.setText("");
                agencyBox.setSelectedIndex(0);
                monthBox.setSelectedIndex(0);
                customerBox.setText("");
                prevMonth = null;
                if(dayboxopen) {
                    System.out.println("Here");
                    panel2.remove( dayLabel );
                    panel2.remove( dayBox );
                    dayboxopen = false;
                }
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
                query = query2;
                ids.clear();
                    result = stmtL.executeQuery( reqCount );
                    numberOfRows = result.getInt( 1 );
                    result = stmtL.executeQuery( query + ";" );
                  
                    while(result.next())
                    {
                            ids.add(result.getString("Customer_ID"));
                    }
                    result = stmtL.executeQuery( query + ";" );                  
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
                result = stmtL.executeQuery( query + " LIMIT 1 OFFSET " + r + ";" );
                    if(col == 0)
                    {
                        return result.getInt("Customer_ID");
                    }
                    else if(col == 1){
                        return result.getString("Last_Name");
                    }
                    else if(col == 2)
                    {
                        return result.getString("First_Name");
                    }
                    else if(col == 3)
                    {
                        return result.getString("theDate");
                    }
                    else if(col == 4)
                    {
                        return result.getString("Acct_Num");
                    }
                    else
                    {
                       return "";
                    }
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
            //THERE IS NO COLUMN FOR IT TO GET A CLASS NAME FROM SO THE PROBLEM IS IN GET VALUE AT WITH THE RESULT SET ENDING.
            }
            
            catch ( Exception exception )
            {
                exception.printStackTrace();
            }
            return Object.class;
        }


        public boolean isCellEditable( int row, int col )
        {
           
                return false;
       
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
    private void displayFailedDialog() {
        JOptionPane.showMessageDialog( this, "Search failed \nPlease make sure to Search for something." );
    }
}
