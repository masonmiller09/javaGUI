import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
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
    private String[] agencyOptions;
    private String query = "";
    private String Aname ="";
    static final String INITIAL_QUERY = "SELECT Customer_ID, First_Name, Last_Name, City, Zip_Code, Phone_Number FROM jos_fb_customer WHERE Customer_ID = 'a'";   
    static final String SIZE_DATABASE_QUERY = "SELECT COUNT(*),Customer_ID FROM jos_fb_customer;";
    
    
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
        setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3+25, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3);
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
        table.setPreferredSize(new Dimension(getWidth()-50,getHeight()/2));
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
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
                "INNER JOIN jos_fb_customer ON jos_fb_monthlydist.Acct_Num = (SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + Aname + "') AND jos_fb_monthlydist.Customer_ID = jos_fb_customer.Customer_ID ORDER BY jos_fb_customer.Customer_ID ASC, jos_fb_monthlydist.theDate ASC";
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
        
        public PDFTableModel(
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
                    //    System.out.println("Selected " + selected.get( i+1 ));
                    }
                }
            }
        }
    }
}
