import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics;
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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;


public class GUIFrame extends JFrame
{
    private JPanel panel, paneTop, paneBottom;

    private JTextField searchBox, year;

    private JLabel searchLabel,monthLabel,dayLabel,yearLabel;

    private StatusBar statusBar;

    private JComboBox<String> searchOptions, month, day;

    private JButton searchButton, viewButton, reportButton, distributionButton;

    private JTable table;

    private ArrayList<Integer> selected;

    private Object[] currentInfo;

    private String[] cbList = { "", "Agency", "Customer", "Customer ID", "Distribution Date" };
    
    private String[] monthList = {"","01","02","03","04","05","06","07","08","09","10","11","12"};

    int population = 0;

    public static boolean online;

    public static Connection connL = null;

    public static Connection connR = null;

    public Statement stmt = null;

    public ResultSet result = null;

    private int numberOfRows;

    private FeastTableModel model;

    private ListSelectionModel listModel;

    String searching = "";

    static FBDatabase fbd;

    static functions f;
    
    static final String INITIAL_QUERY = "SELECT Customer_ID, Last_Name, First_Name, Street_Address, Apartment_Number, City, Zip_Code, Phone_Number FROM jos_fb_customer WHERE Customer_ID LIKE 'a';";

    static final String DEFAULT_QUERY = "SELECT Customer_ID, Last_Name, First_Name, Street_Address, Apartment_Number, City, Zip_Code, Phone_Number FROM jos_fb_customer;";

    static final String SIZE_DATABASE_QUERY = "SELECT COUNT(Customer_ID) FROM jos_fb_customer;";

    static final String FULL_DATA_QUERY = "SELECT * FROM jos_fb_customer;";
    
    private String previous,prevMonth;


    public GUIFrame()
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
        // JMenuItem delete = file.add( "Delete" );
        JMenuItem print = file.add( "Printable DB" );
        f = new functions( connR, connL );
        JMenuItem exit = file.add( "Exit" );
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
        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        panel = new JPanel();
        panel.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
        searchBox = new JTextField( 10 );
        searchButton = new JButton( "Search" );
        searchLabel = new JLabel( "Search by: " );
        searchOptions = new JComboBox( cbList );
	month = new JComboBox(monthList);
	year = new JTextField(4);
    	final int limit = 4;
        panel.add( searchLabel, BorderLayout.EAST );
        panel.add( searchOptions, BorderLayout.EAST );
        paneTop.add( panel, BorderLayout.EAST );
        paneBottom = new JPanel();
        paneBottom.setLayout( new BorderLayout() );
        statusBar = new StatusBar( connR, connL );
        paneBottom.add( statusBar, BorderLayout.SOUTH );
        JPanel paneBottomBody = new JPanel();
        paneBottom.add( paneBottomBody, BorderLayout.NORTH );
        paneBottom.add( new JSeparator( SwingConstants.HORIZONTAL ) );
        viewButton = new JButton( "View" );
        reportButton = new JButton( "Report" );
        distributionButton = new JButton( "Distributions" );
        paneBottomBody.add( viewButton );
        paneBottomBody.add( reportButton );
        paneBottomBody.add( distributionButton );
        add( paneBottom, BorderLayout.SOUTH );
        model = new FeastTableModel( connR, connL, online, INITIAL_QUERY );
        table = new JTable( model );
        listModel = table.getSelectionModel();
        listModel.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        listModel.addListSelectionListener( new SharedListSelectionHandler() );
        table.setSelectionModel( listModel );
        table.setPreferredScrollableViewportSize( table.getPreferredSize() );
        table.setFillsViewportHeight( true );
        JScrollPane scrollPane = new JScrollPane( table );
        add( scrollPane );

        editF.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                // Put in new View code
            }
        } );

        insert.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                System.out.println( "Insert Attempted" );
                SDIForm sdi = new SDIForm( connR,
                    connL,
                    true,
                    fbd.que,
                    fbd.isConnected );
                sdi.setVisible( true );
            }
        } );

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
                // f.printableDB( online, directory, data );
            }
        } );
        /*
         * delete.addActionListener( new ActionListener() {
         * 
         * @Override public void actionPerformed( ActionEvent arg0 ) { for ( int
         * i = 0; i < data.size(); i++ ) { row = (Vector)data.get( i );
         * 
         * if ( (Boolean)row.get( 0 ) ) { System.out.println("delete test: " +
         * row.get(1)); f.delete( fbd.isConnected, (Integer)row.get( 1 ),
         * fbd.que ); } } model.refresh(); } } );
         */
        exit.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                int result = displayExitDialog();
                if ( result == JOptionPane.YES_OPTION )
                {
                    System.exit( 0 );
                }
            }
        } );
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
                    System.exit( 0 );
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

        searchBox.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                String text = searchBox.getText();
                String option = (String)searchOptions.getSelectedItem();
                String request;
                if ( option == "Agency" )
                {
                    request = new String( "SELECT * FROM jos_fb_agency WHERE Agency_Name LIKE \'"
                        + text + "%\'" );
                }
                else if (option == "Customer ID") {
	            	request = new String ("SELECT * FROM jos_fb_customer WHERE Customer_ID LIKE \'"+ text +"%\'");
	        }
	        
                else if ( text == "" )
                {
                    request = DEFAULT_QUERY;
                }
                else
                {
                    request = new String( "SELECT * FROM jos_fb_customer WHERE First_Name LIKE  \'"
                        + text + "%\' or Last_Name LIKE \'" + text + "%\'" );
                }
                try
                {
                    model.setQuery( request );
                }
                catch ( Exception e1 )
                {
                    JOptionPane.showMessageDialog( null,
                        e1.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE );
                    try
                    {
                        model.setQuery( DEFAULT_QUERY );
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
        } );
        searchButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                String text = searchBox.getText();
                String option = (String)searchOptions.getSelectedItem();
                String request;
                if ( option == "Agency" )
                {
                    request = new String( "SELECT * FROM jos_fb_agency WHERE Agency_Name LIKE \'"
                        + text + "%\'" );
                }
                else if (option == "Customer ID") {
                    request = new String ("SELECT * FROM jos_fb_customer WHERE Customer_ID LIKE \'"+ text +"%\'");
                }
                else if (option == "Distribution Date") {
	            String y = year.getText();
	            String m = (String)month.getSelectedItem();
	            String d = (String)month.getSelectedItem();
	            request = new String ("SELECT * FROM jos_fb_monthlyDist WHERE Date = \'"+y+"-"+m+"-"+d+"\'")
	        }
                else if ( text == "" || option == "" )
                {
                    request = DEFAULT_QUERY;
                }
                else
                {
                    request = new String( "SELECT * FROM jos_fb_customer WHERE First_Name LIKE  \'"
                        + text + "%\' or Last_Name LIKE \'" + text + "%\'" );
                }
                try
                {
                    model.setQuery( request );
                }
                catch ( Exception e1 )
                {
                    JOptionPane.showMessageDialog( null,
                        e1.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE );
                    try
                    {
                        model.setQuery( DEFAULT_QUERY );
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
        } );

        viewButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                int shift = 0;
                int move;
                try
                {
                    move = result.getRow() - 1;
                    result.relative( -move );
                }
                catch ( SQLException e2 )
                {
                    e2.printStackTrace();
                }
                for ( int i = 0; i < numberOfRows; i++ )
                {
                    try
                    {
                        for ( int j = 0; j < selected.size(); j++ )
                        {
                            if ( result.getRow() == selected.get( j ) )
                            {
                                Integer id = result.getInt( 1 );
                                SDIForm sdi = new SDIForm( connR,
                                    connL,
                                    false,
                                    fbd.que,
                                    fbd.isConnected );
                                try
                                {
                                    currentInfo = functions.retrieveUser( id,
                                        fbd.isConnected );
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
                                    l.printStackTrace();
                                }
                                sdi.setLocation( 500 + shift, 250 + shift );
                                shift += 20;
                                sdi.setVisible( true );
                            }
                        }
                        result.next();
                    }
                    catch ( SQLException e1 )
                    {
                        e1.printStackTrace();
                    }
                }
            }
        } );

        reportButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                int move;
                try
                {
                   if(!online)
                    {
                       move = selected.get(0);
                       String SELECT_ONE_ROW = "SELECT * FROM jos_fb_customer LIMIT 1 OFFSET "
                           + move + ";";
                        result = stmt.executeQuery( SELECT_ONE_ROW );
                        for(int i = 0; i < selected.size(); i++){
                            System.out.println("indexes: " + selected.get( i ));
                        }                        
                        System.out.println("move: " + move);
                    }
                    else{
                    move = result.getRow();
                    System.out.println("move: " + move);
                    result.relative( -move );
                    }
                }
                catch ( SQLException e2 )
                {
                    e2.printStackTrace();
                }
                for ( int i = 0; i < numberOfRows; i++ )
                {
                    try
                    {
                        if ( selected != null )
                        {
                            for ( int j = 0; j < selected.size(); j++ )
                            {
                                if ( result.getRow() == selected.get( j ) )
                                {
                                    Integer id = (Integer)result.getInt( 1 );
                                    int one = 1;
                                    try
                                    {
                                        currentInfo = functions.retrieveUser( id,
                                            fbd.isConnected );
                                        System.out.println( "Retrieving Agency" );
                                        Object[] agencyInfo = functions.retrieveAgency( one,
                                            fbd.isConnected );
                                        System.out.println( "Retrieving Agent" );
                                        Object[] agentInfo = functions.retrieveAgent( one,
                                            fbd.isConnected );
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
                    catch ( Exception b )
                    {
                        b.printStackTrace();
                    }
                }
                PdfGenerator.mergePdfFiles( population );
                population = 0;
                population = 0;
            }
        } );

        distributionButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                int res = displayDistributionDialog();
                if ( res == JOptionPane.YES_OPTION )
                {
                    FeastDistribution dist = new FeastDistribution();
                    dist.setVisible( true );
                }
                if ( res == JOptionPane.NO_OPTION )
                {
                    FeastDistribution dist = new FeastDistribution( "load" );
                    dist.setVisible( true );
                }
                if ( res == JOptionPane.CANCEL_OPTION )
                {

                }
            }
        } );
        
        searchOptions.addActionListener (new ActionListener () {
	        public void actionPerformed(ActionEvent e) {
	            String option = (String)searchOptions.getSelectedItem();
	            if (option == "Customer" || option == "Customer ID" || option == "Agency") {
	            	if (previous == null) {
		            	panel.add(searchBox, BorderLayout.EAST);
		            	panel.add(searchButton,BorderLayout.EAST);
		            	searchBox.setFont(new Font("Verdana",Font.PLAIN,12));
		            	revalidate();
	            	}
	            	else if (previous == "Distribution Date"){
	            		panel.removeAll();
	            		panel.add(searchLabel,BorderLayout.EAST);
	            		panel.add(searchOptions,BorderLayout.EAST);
	            		panel.add(searchBox,BorderLayout.EAST);
	            		panel.add(searchButton,BorderLayout.EAST);
	            		revalidate();
	            	}
	            	else {
	            		revalidate();
	            	}
	            }
	            else if (option == "Distribution Date") {
	            	if (previous == null) {
		            	panel.add(monthLabel,BorderLayout.EAST);
		            	panel.add(month,BorderLayout.EAST);
		            	panel.add(yearLabel,BorderLayout.EAST);
		            	panel.add(year, BorderLayout.EAST);
		            	panel.add(searchButton,BorderLayout.EAST);
		            	revalidate();
	            	}
	            	else if (previous == "Customer" || previous == "Customer ID" || previous == "Agency") {
		            	panel.removeAll();
		            	panel.add(searchLabel,BorderLayout.EAST);
		            	panel.add(searchOptions,BorderLayout.EAST);
		            	panel.add(monthLabel,BorderLayout.EAST);
		            	panel.add(month,BorderLayout.EAST);
		            	panel.add(yearLabel,BorderLayout.EAST);
		            	panel.add(year, BorderLayout.EAST);
		            	panel.add(searchButton,BorderLayout.EAST);
		            	revalidate();
	            	}
	            	else {
	            		revalidate();
	            	}
	            }
	            else {
	            	revalidate();
	            }
	    	previous = option;
	        }
	    });
        
        month.addActionListener(new ActionListener () {
	    public void actionPerformed(ActionEvent l) {
	    	String m = (String)month.getSelectedItem();
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
	    	day = new JComboBox(days);
	    	if (prevMonth == null) {
			panel.remove(yearLabel);
			panel.remove(year);
			panel.remove(searchButton);
			panel.add(dayLabel,BorderLayout.EAST);
			panel.add(day,BorderLayout.EAST);
			panel.add(yearLabel,BorderLayout.EAST);
			panel.add(year,BorderLayout.EAST);
		panel.add(searchButton,BorderLayout.EAST);
			revalidate();
	    	}
	    	else {
	    		panel.removeAll();
	    	panel.add(searchLabel,BorderLayout.EAST);
	    	panel.add(searchOptions,BorderLayout.EAST);
	    	panel.add(monthLabel,BorderLayout.EAST);
	    	panel.add(month,BorderLayout.EAST);
	    	panel.add(dayLabel,BorderLayout.EAST);
			panel.add(day,BorderLayout.EAST);
			panel.add(yearLabel,BorderLayout.EAST);
			panel.add(year,BorderLayout.EAST);
		panel.add(searchButton,BorderLayout.EAST);
			revalidate();
	    	}
	    	prevMonth = m;
	    }
    	});
    	
    	year.setDocument(new PlainDocument(){
    	    @Override
    	    public void insertString(int offs, String str, AttributeSet a)
    	            throws BadLocationException {
    	        if(getLength() + str.length() <= limit)
    	            super.insertString(offs, str, a);
    	    }
    	});
    	
    	year.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
            	String y = year.getText();
            	String m = (String)month.getSelectedItem();
            	String d = (String)month.getSelectedItem();
            	String request = new String ("SELECT * FROM jos_fb_monthlyDist WHERE Date = \'"+y+"-"+m+"-"+d+"\'");
            	table.setOpaque(true);
            	try { 
	            	model.setQuery(request); 
	            	//table.setOpaque(true);
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

    }


    public static void main( String[] args )
    {
        fbd = new FBDatabase();
        connR = fbd.connR;
        connL = fbd.connL;
        online = fbd.isConnected;

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


    private int displayDistributionDialog()
    {
        Object[] options = { "Create New", "Load", "Cancel" };
        int result = JOptionPane.showOptionDialog( this,
            "Would you like to create a new Distribution or Load a Previous one?",
            "Distributions",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2] );
        return result;
    }


    class FeastTableModel extends AbstractTableModel
    {
        /**
		 * 
		 */
        private Statement stmtL;

        private Statement stmtR;

        private ResultSetMetaData metaData;

        private boolean connectedToDatabase = false;

        private static final long serialVersionUID = 1L;


        public FeastTableModel(
            Connection connR,
            Connection connL,
            boolean online,
            String query )
        {
            connectedToDatabase = online;
            try
            {
                System.out.println( "Test connection local: "
                    + connL.toString() );
                stmtL = connL.createStatement();
                if ( connectedToDatabase )
                {
                    stmtR = connR.createStatement();
                }
                setQuery( query );
            }
            catch ( SQLException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        private void setQuery( String query )
        {
            try
            {
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

                // vcxSystem.out.println();
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
                    result = stmtR.executeQuery( DEFAULT_QUERY );
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


    class SharedListSelectionHandler implements ListSelectionListener
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
