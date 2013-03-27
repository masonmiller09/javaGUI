import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.NoSuchObjectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


public class GUIFrameMatt extends JFrame
{
    private JPanel panel, paneTop, paneBottom;

    private JTextField searchBox;

    private JLabel searchLabel;

    private JComboBox<String> searchOptions;

    private JButton searchButton, viewButton, reportButton;

    private JTable table;

    private Vector data, row, fulldata;

    private String[] test = new String[3];

    private Vector<String> columns, fullcolumns;

    private Object[] currentInfo;

    private String[] cbList = { "", "Agency", "First_Name", "Last_Name",
        "Zipcode" };

    public static Connection conn = null;
    
   // public static Connection connL = null;

    public Statement stmt = null;

    public ResultSet result = null;

    int population = 0;

    static String url = "jdbc:mysql://lampa.vf.cnu.edu:3306/";

    static String dbName = "feastdb";

    private static String driverL = "org.sqlite.JDBC";

    private static String urlL = "jdbc:sqlite:";

    private static String dbNameL = "feast.db";

    static String driver = "com.mysql.jdbc.Driver";

    static String userName = "root";

    static String password = "lampa";

    String searching = "";

    private int num = 0;

    static FBDatabase fbd;

    static functions f;
    
    static SampleTableModel model;


    public GUIFrameMatt()
    {

        super( "FEAST" );
        paneTop = new JPanel();
        add( paneTop, BorderLayout.NORTH );
        setSize( 1500, 700 );
        JMenuBar main = new JMenuBar();
        setJMenuBar( main );
        JMenu file = new JMenu( "File" );
        file.enable( true );
        JMenuItem insert = file.add( "Insert" );
        JMenuItem delete = file.add( "Delete" );
        JMenuItem print = file.add( "Printable DB" );
        f = new functions();
        
        insert.addActionListener( new ActionListener()
        {
           @Override
           public void actionPerformed( ActionEvent arg0)
           {
               System.out.println("Insert Attempted");
               SDIForm sdi = new SDIForm(conn, true);
               sdi.setVisible( true );
           }
        });
        
        print.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode( 1 );
                jfc.setApproveButtonText( "Directory..." );
                String directory = "";
                jfc.showDialog( searchBox, null );
                directory = jfc.getSelectedFile().getAbsolutePath();
                //f.printableDB( conn, directory, data );
            }
        } );

        delete.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                for ( int i = 0; i < data.size(); i++ )
                {
                    row = (Vector)data.get( i );
                    if ( (Boolean)row.get( 0 ) )
                    {
                        f.delete( conn, (Integer)row.get( 1 ), fbd.que );
                        model.refresh();

                    }
                }
            }
        } );

        JMenuItem exit = file.add( "Exit" );
        exit.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                int result = displayExitDialog();
                if ( result == JOptionPane.YES_OPTION )
                {
                    System.out.println("Exit attempted");
                    fbd.timer = false;
                    System.out.println("guiframe timer: " + fbd.timer);
                    dispose();
                    
                }
            }
        } );
        JMenu edit = new JMenu( "Edit" );
        edit.enable( true );
        JMenuItem editF = edit.add( "Edit" );
        JMenu view = new JMenu( "View" );
        JMenu viewBy = new JMenu( "View by" );
        viewBy.add( "Agency" );
        viewBy.add( "City" );
        viewBy.add( "Zipcode" );
        view.add( viewBy );
        view.enable( true );
        main.add( file );
        main.add( edit );
        main.add( view );
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
                int result = displayExitDialog();
                if ( result == JOptionPane.YES_OPTION )
                {
                    dispose();
                }
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
        // setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
        searchBox = new JTextField( 10 );
        searchBox.addKeyListener( new KeyAdapter()
        {
            @Override
            public void keyPressed( KeyEvent e )
            {
                if ( e.getKeyCode() == KeyEvent.VK_DELETE )
                {
                    searching = searching.substring( 0, searching.length() - 1 );
                }
                else if ( e.getKeyCode() == KeyEvent.VK_ENTER )
                {
                    String text = searchBox.getText();
                    String option = (String)searchOptions.getSelectedItem();
                }
                else
                {
                    char c = e.getKeyChar();
                    searching += c;
                    if ( num == 0 )
                    {

                    }
                }
            }
        } );
        searchButton = new JButton( "Search" );
        searchButton.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent e )
            {
                String text = searchBox.getText();
                String option = (String)searchOptions.getSelectedItem();
            }
        } );
        searchLabel = new JLabel( "Search by: " );
        searchOptions = new JComboBox( cbList );
        panel.add( searchLabel, BorderLayout.EAST );
        panel.add( searchOptions, BorderLayout.EAST );
        panel.add( searchBox, BorderLayout.EAST );
        panel.add( searchButton, BorderLayout.EAST );
        searchBox.setFont( new Font( "Verdana", Font.PLAIN, 12 ) );
        paneTop.add( panel, BorderLayout.EAST );
        paneBottom = new JPanel();
        viewButton = new JButton( "View" );
        viewButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                for ( int i = 0; i < data.size(); i++ )
                {
                    row = (Vector)data.get( i );
                    if ( (Boolean)row.get( 0 ) )
                    {
                        Integer id = (Integer)row.get( 1 );
                        SDIForm sdi = new SDIForm(conn, false);
                        try
                        {
                            currentInfo = functions.retrieveUser( id, conn );
                            sdi.idField.setText( currentInfo[1].toString() );
                            sdi.firstNameField.setText( (String)currentInfo[2] );
                            sdi.lastNameField.setText( (String)currentInfo[3] );
                            sdi.addressField.setText( (String)currentInfo[4] );
                            sdi.apartmentNumberField.setText( (String)currentInfo[5] );
                            sdi.cityField.setText( (String)currentInfo[6] );
                            sdi.zipField.setText( currentInfo[7].toString() );
                            sdi.phoneField.setText( currentInfo[8].toString() );
                            sdi.numChildrenField.setText( currentInfo[9].toString() );
                            sdi.numAdultsField.setText( currentInfo[10].toString() );
                            sdi.numSeniorsField.setText( currentInfo[11].toString() );
                            Integer n = (Integer)currentInfo[9]
                                + (Integer)currentInfo[10]
                                + (Integer)currentInfo[11];
                            sdi.totInHouseholdField.setText( n.toString() );
                            sdi.foodstamps.setSelected( (Boolean)currentInfo[12] );
                            sdi.tanf.setSelected( (Boolean)currentInfo[13] );
                            sdi.ssi.setSelected( (Boolean)currentInfo[14] );
                            sdi.medicaid.setSelected( (Boolean)currentInfo[15] );
                            sdi.hhIncomeField.setText( currentInfo[16].toString() );
                            sdi.weekly.setSelected( (Boolean)currentInfo[17] );
                            sdi.monthly.setSelected( (Boolean)currentInfo[18] );
                            sdi.annually.setSelected( (Boolean)currentInfo[19] );
                        }
                        catch ( NoSuchObjectException l )
                        {
                            // TODO Auto-generated catch block
                            l.printStackTrace();
                        }
                        sdi.setVisible( true );
                    }
                }
            }
        } );
        reportButton = new JButton( "Report" );
        reportButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                for ( int i = 0; i < data.size(); i++ )
                {
                    row = (Vector)data.get( i );
                    if ( (Boolean)row.get( 0 ) )
                    {
                        Integer id = (Integer)row.get( 1 );
                        int one = 1;
                        if ( id.equals( "" ) )
                        {
                            JOptionPane.showMessageDialog( null,
                                "Please, enter a customer ID",
                                "Customer Search Error",
                                JOptionPane.INFORMATION_MESSAGE );
                        }
                        else
                        {
                            try
                            {
                                currentInfo = functions.retrieveUser( id, conn );
                                System.out.println( "Retrieving Agency" );
                                Object[] agencyInfo = functions.retrieveAgency( one, conn );
                                System.out.println( "Retrieving Agent" );
                                Object[] agentInfo = functions.retrieveAgent( one, conn );
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
                population = 0;
            }
        } );
        paneBottom.add( viewButton );
        paneBottom.add( reportButton );
        add( paneBottom, BorderLayout.SOUTH );
        model = new SampleTableModel();
        table = new JTable( model );
        table.setPreferredScrollableViewportSize( table.getPreferredSize() );
        table.setFillsViewportHeight( true );
        JScrollPane scrollPane = new JScrollPane( table );
        add( scrollPane );
    }


    public static void main( String[] args )
    {
        fbd = new FBDatabase();
        fbd.main( null );

        try
        {
            Class.forName( driver ).newInstance();
            Class.forName( driverL ).newInstance();
            if ( fbd.isConnected )
            {
                conn = DriverManager.getConnection( url + dbName,
                    userName,
                    password );
            }
            else
            {
                conn = DriverManager.getConnection( urlL + dbNameL,
                    userName,
                    password );
            }
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( InstantiationException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IllegalAccessException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( ClassNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JFrame GUI = new GUIFrame();
        GUI.setLocationRelativeTo( null );
        GUI.setVisible( true );

    }


    private int displayExitDialog()
    {
        int result = JOptionPane.showConfirmDialog( this,
            "Are you sure?",
            "Exit?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE );
        return result;
    }


    class SampleTableModel extends AbstractTableModel
    {
        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;


        public SampleTableModel()
        {
            try
            {
                System.out.println( "building local table" );
                stmt = conn.createStatement();
                result = stmt.executeQuery( "SELECT * FROM fb_customer" );
                ResultSetMetaData md = result.getMetaData();
                int columnCount = md.getColumnCount();
                columns = new Vector( 8 );
                fullcolumns = new Vector( columnCount );
                columns.addElement( "Selected" );
                fullcolumns.addElement( "Selected" );
                int m = 0;
                for ( int i = 1; i <= columnCount; i++ )
                {
                    if ( m < 8 )
                    {
                        columns.addElement( md.getColumnName( i ) );
                        m++;
                    }
                    fullcolumns.addElement( md.getColumnName( i ) );
                }
                data = new Vector();
                fulldata = new Vector();
                while ( result.next() )
                {
                    row = new Vector( columnCount );
                    row.addElement( new Boolean( false ) );
                    for ( int i = 1; i <= columnCount; i++ )
                    {
                        row.addElement( result.getObject( i ) );
                    }
                    data.addElement( row );
                    fulldata.addElement( row );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }


        public void refresh()
        {
            try
            {
                stmt = conn.createStatement();
                result = stmt.executeQuery( "SELECT * FROM fb_customer" );
                ResultSetMetaData md = result.getMetaData();
                int columnCount = md.getColumnCount();
                columns = new Vector( 8 );
                fullcolumns = new Vector( columnCount );
                columns.addElement( "Selected" );
                fullcolumns.addElement( "Selected" );
                int m = 0;
                for ( int i = 1; i <= columnCount; i++ )
                {
                    if ( m < 8 )
                    {
                        columns.addElement( md.getColumnName( i ) );
                        m++;
                    }
                    fullcolumns.addElement( md.getColumnName( i ) );
                }
                data = new Vector();
                fulldata = new Vector();
                while ( result.next() )
                {
                    row = new Vector( columnCount );
                    row.addElement( new Boolean( false ) );
                    for ( int i = 1; i <= columnCount; i++ )
                    {
                        row.addElement( result.getObject( i ) );
                    }
                    data.addElement( row );
                    fulldata.addElement( row );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }


        @Override
        public int getColumnCount()
        {
            return columns.size();
        }


        @Override
        public int getRowCount()
        {
            return data.size();
        }


        @Override
        public String getColumnName( int column )
        {
            return columns.get( column );
        }


        @Override
        public Object getValueAt( int r, int col )
        {
            row = (Vector)data.get( r );
            return row.get( col );
        }


        public Class<?> getColumnClass( int c )
        {
            return getValueAt( 1, c ).getClass();
        }


        public void setValueAt( Object value, int row, int column )
        {
            ( (Vector)data.get( row ) ).set( column, value );
            fireTableCellUpdated( row, column );
        }


        public boolean isCellEditable( int row, int column )
        {
            return true;
        }
    }
}
