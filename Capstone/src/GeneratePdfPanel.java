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
    public static Connection connR = null;
    public ResultSet result = null;
    FeastMainFrame fmf;
    private int numberOfRows;
    private PDFTableModel model;
    private ListSelectionModel listModel;
    private ArrayList<Integer> selected;
    JPanel panel1,panel2,panel3,panel;
    JLabel agencyLabel,descriptionLabel;
    JButton searchButton,pdfButton,mainPage;
    JComboBox agencyBox;
    private String[] agencyOptions = {"","Agape Cathedral Center","An Acheivable Dream","Andrews Elementary","Beauty for Ashes","Behind the Veil Ministry","Berachah Church","Berkley Village Apartments","Bethel Temple","Booker Elementary","Bread for Life","Buckroe Baptist Church",
            "C.H.P. Lafayette Village","C.H.P. Rivermede","C.H.P. Woods @ Yorktown", "Cary Elementary","Checed Warwick","Christ Temple Holiness Church","Coastal Community Church","Deeper Life Assembly","Doris Miller Center","Dunamis Christian Center",
            "Emmanuel House Inc.","Empowered Believers Christian","First Baptist - Jefferson Park","First Baptist Denbigh","Five Loaves","Forrest Elementary","Garden of Prayer","Gleaning Baptist Church","Greater Bethlehem Christ. As.","Greater Emmanuel Ministries",
            "Greater Works Ministries","Grove Christian Outreach","Holy Tabernacle Church","Hope House Ministries","Ivy Baptist Church","Ivy Farms Church","JTC Lifechanging Center Inc.","Just-Us-Kidz Inc.","Langley Elementary","Langley Village","Lexington Commons",
            "Lifeline Full Gospel","Living Faith Christian Center","Living Waters Family Outreach","Living Waters Way of the Cross","Magruder Elementary","Mercy Seat Bapt.","Mt. Calvary SDA Church","Mt. Calvary Baptist","New Beginnings","New Bethel International",
            "New Hope Baptist Church","New Life Ministry Center","Newport News Housing","Oasis of Life","Open Door Full Gospel","Operation Breaking Through","Peninsula Hispanic SDA","Perfecting Saints Ministries","Pinecroft","Pocahontas Temple","Poquoson",
            "Salem UMC","Salvation Army (Hampton)","Salvation Army (Williamsburg)","Seton Manor","Smith Elementary","Spirit of Truth Ministries","Sr. Christian Village of E. VA.","St. James Deliverance","St. John Baptist Church","St. Marks UMC","St. Timothy Church",
            "Surry Community Center 1","Tarrant Elementary","Temple of Life / New Life","Temple of Peace","Temple of Refuge","Triumph Christian Center","Tyler Elementary","W.M. Ratley Road Ahead","Warwick Assembly of God","White Marsh Baptist Church",
            "Williamsburg/Blayton Building","Williamsburg/Burnt Ordinary","Williamsburg/Katherine Circle","Williamsburg/Mimosa Woods","Williamsburg/Sylvia Brown","World Outreach Worship Center","YWCA","Zion Prospect Baptist Church"};
    private String query = "";
    
    static final String INITIAL_QUERY = "SELECT Customer_ID, First_Name, Last_Name, City, Zip_Code, Phone_Number FROM jos_fb_customer WHERE Customer_ID = 'a';";
    static final String SIZE_DATABASE_QUERY = "SELECT COUNT(Customer_ID) FROM jos_fb_customer;";
    
    
    public GeneratePdfPanel(Connection connL2, queryQue que, boolean online2, boolean visible, FeastMainFrame f) {
        super("PDF Generator");
        connL = connL2;
        online = online2;
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
        model = new PDFTableModel( connR, connL, online, INITIAL_QUERY );
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
                setSize(650,300);
                scroll.setVisible(true);
                pdfButton.setVisible(true);
                model.setQuery( "SELECT * FROM jos_fb_agency WHERE Agency_Name = \'"+agency+"\'");
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
        private Statement stmtL;

        private Statement stmtR;

        private ResultSetMetaData metaData;

        private boolean connectedToDatabase = false;

        private static final long serialVersionUID = 1L;


        public PDFTableModel(
            Connection connR,
            Connection connL,
            boolean online,
            String query1 )
        {
            connectedToDatabase = online;
            try
            {
                System.out.println( "Test connection local: "
                    + connL.toString() );
                stmtL = connL.createStatement();
                if ( connectedToDatabase )
                {
                    System.out.println("Here");
                    stmtR = connR.createStatement();
                }
                query = query1;
                setQuery( query );
            }
            catch ( SQLException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        private void setQuery( String query1 )
        {
            try
            {
                System.out.println("IM SETTING A QUERY: "+query);
                System.out.println("Set Query");
                query = query1;
                if ( online )
                {
                    result = stmtR.executeQuery( query );
                    metaData = result.getMetaData();
                    result.last();
                    numberOfRows = result.getRow();
                }
                else
                {
                    result = stmtL.executeQuery( SIZE_DATABASE_QUERY );
                    numberOfRows = result.getInt( 1 );
                    result = stmtL.executeQuery( query );
                    metaData = result.getMetaData();
                }

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
                if ( online )
                {
                    result = stmtR.executeQuery( query );
                    result.absolute( r + 1 );
                }
                else
                {

                    String SELECT_ONE_ROW = "SELECT * FROM jos_fb_customer LIMIT 1 OFFSET "
                        + r + ";";
                    result = stmtL.executeQuery( SELECT_ONE_ROW );
                }

                // System.out.println(r+1);
                return result.getObject( col + 1 );
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
                return true;
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
