
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.NoSuchObjectException;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;


public class GUIFrame extends JFrame {
	private JPanel panel,paneTop,paneBottom;
	private JTextField searchBox;
	private JLabel searchLabel;
	private StatusBar statusBar;
	private JComboBox<String> searchOptions;
	private JButton searchButton,viewButton,reportButton;
	private JTable table;
	private ArrayList<Integer> selected;
	private Object[] currentInfo;
	private String[] cbList = {"", "Agency", "First_Name", "Last_Name", "Zipcode"};
    int population = 0;
    String url = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
    String dbName = "feastdb";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "root";
    String password = "lampa";
	private int numberOfRows;
    private FeastTableModel model; 
    private ListSelectionModel listModel;
	private ResultSet result;
    String searching = "";
    static final String DEFAULT_QUERY = "SELECT Customer_ID, Last_Name, First_Name, Street_Address, Apartment_Number, City, Zip_Code, Phone_Number FROM fb_customer";
    static final String FULL_DATA_QUERY = "SELECT * FROM fb_customer";
    private FBDatabase database = new FBDatabase();
    private Connection connR;
    private Connection connL;
    
	public GUIFrame() {
		super("FEAST");
		connR = database.connR;
		connL = database.connL;
		paneTop= new JPanel();
		add(paneTop, BorderLayout.NORTH);
		setSize(1500,700);
		JMenuBar main = new JMenuBar();
		setJMenuBar(main);
		JMenu file = new JMenu("File");
		file.enable(true);
		file.add("Insert");
		file.add("Delete");
		JMenuItem exit = file.add("Exit");
		exit.addActionListener(
				new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						int result = displayExitDialog();
						if(result == JOptionPane.YES_OPTION){
							dispose();
							System.exit(0);
						}
					}
				});
		JMenu edit = new JMenu("Edit");
		edit.enable(true);
		JMenuItem editF = edit.add("Edit");
		JMenu view = new JMenu("View");
		JMenu viewBy = new JMenu("View by");
		viewBy.add("Agency");
		viewBy.add("City");
		viewBy.add("Zipcode");
		view.add(viewBy);
		view.enable(true);
		main.add(file);
		main.add(edit);
		main.add(view);
		addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0){}
			public void windowClosed(WindowEvent arg0){}
			public void windowClosing(WindowEvent arg0){
				int result = displayExitDialog();
				if(result == JOptionPane.YES_OPTION){
					dispose();
					System.exit(0);
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
			public void windowOpened(WindowEvent arg0){}
		});
		//setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		panel = new JPanel();
	    panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	    searchBox = new JTextField(10);
	    searchBox.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
    			String text = searchBox.getText();
	            String option = (String)searchOptions.getSelectedItem();
	            String request;
	            if (option == "Agency") {
	            	request = new String ("SELECT * FROM feastdb.fb_agency WHERE Agency_Name LIKE \'"+ text +"%\'");
	            }
	            else if (text == "") {
	            	request = DEFAULT_QUERY;
	            }
	            else {
		            request = new String("SELECT * FROM feastdb.fb_customer WHERE "+option+" LIKE  \'"+ text + "%\'");
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
	            	request = new String ("SELECT * FROM feastdb.fb_agency WHERE Agency_Name LIKE \'"+ text +"%\'");
	            }
	            else if (text == "" || option == "") {
	            	request = DEFAULT_QUERY;
	            }
	            else {
		            request = new String("SELECT * FROM feastdb.fb_customer WHERE "+option+" LIKE  \'"+ text + "%\'");
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
	    searchLabel = new JLabel("Search by: ");
	    searchOptions = new JComboBox(cbList);
	    panel.add(searchLabel, BorderLayout.EAST);
	    panel.add(searchOptions, BorderLayout.EAST);
	    panel.add(searchBox, BorderLayout.EAST);
	    panel.add(searchButton,BorderLayout.EAST);
	    searchBox.setFont(new Font("Verdana",Font.PLAIN,12));
	    paneTop.add(panel, BorderLayout.EAST);
	    paneBottom = new JPanel();
	    paneBottom.setLayout(new BorderLayout());
	    viewButton = new JButton("View");
	    viewButton.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		statusBar.playMessage("Opening Customer Information");
	    		int shift = 0;
	    		int move;
				try {
					move = result.getRow()-1;
					result.relative(-move);
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
	    		for (int i = 0; i < numberOfRows; i++) {
	    			try {
	    				for (int j = 0; j < selected.size(); j++) {
	    					if (result.getRow() == selected.get(j)) {
								Integer id = result.getInt(1);
								SDIForm sdi = new SDIForm();
								try {
									currentInfo = functions.retrieveUser(id);
									sdi.idField.setText(currentInfo[1].toString());
									sdi.firstNameField.setText((String) currentInfo[2]);
									sdi.lastNameField.setText((String) currentInfo[3]);
									sdi.addressField.setText((String) currentInfo[4]);
									sdi.apartmentNumberField.setText((String) currentInfo[5]);
									sdi.cityField.setText((String) currentInfo[6]);
									sdi.zipField.setText(currentInfo[7].toString());
									sdi.phoneField.setText(currentInfo[8].toString());
									sdi.numChildrenField.setText(currentInfo[9].toString());
									sdi.numAdultsField.setText(currentInfo[10].toString());
									sdi.numSeniorsField.setText(currentInfo[11].toString());
									Integer n = (Integer) currentInfo[9] + (Integer) currentInfo[10]
											+ (Integer) currentInfo[11];
									sdi.totInHouseholdField.setText(n.toString());
									sdi.foodstamps.setSelected((Boolean) currentInfo[12]);
									sdi.tanf.setSelected((Boolean) currentInfo[13]);
									sdi.ssi.setSelected((Boolean) currentInfo[14]);
									sdi.medicaid.setSelected((Boolean) currentInfo[15]);
									sdi.hhIncomeField.setText(currentInfo[16].toString());
									sdi.weekly.setSelected((Boolean) currentInfo[17]);
									sdi.monthly.setSelected((Boolean) currentInfo[18]);
									sdi.annually.setSelected((Boolean) currentInfo[19]);
								} 
								catch (NoSuchObjectException l) {
									l.printStackTrace();
								}
								sdi.setLocation(500+shift,250+shift);
								shift+=20;
								sdi.setVisible(true);
							}
	    				}
    					result.next();
					} 
	    			catch (SQLException e1) {
						e1.printStackTrace();
					}
	    		}
	    	}
	    });
	    reportButton = new JButton("Report");
	    reportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				statusBar.playMessage("Generating Report");
				int move;
				try {
					move = result.getRow()-1;
					result.relative(-move);
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
	    		for (int i = 0; i < numberOfRows; i++) {
	    			try {
	    				for (int j = 0; j < selected.size(); j++) {
	    					if (result.getRow() == selected.get(j)) {
	    						Integer id = (Integer) selected.get(j);
	    						int one = 1;
								try {
									currentInfo = functions.retrieveUser(id);
									System.out.println("Retrieving Agency");
									Object [] agencyInfo = functions.retrieveAgency(one);
									System.out.println("Retrieving Agent");
									Object [] agentInfo = functions.retrieveAgent(one);
									PdfGenerator.populateSDIForm(currentInfo,agencyInfo,agentInfo,population);
									population++;
								} 
								catch (NoSuchObjectException o) {
									// TODO Auto-generated catch block
									JOptionPane.showMessageDialog(null,
											"There is no user: \"" + id
													+ "\" in the database",
											"Customer Search Error",
											JOptionPane.INFORMATION_MESSAGE);
								} 
								catch (NumberFormatException o) {
								// TODO Auto-generated catch block
									o.printStackTrace();
								}
	    					}
	    				}
	    				result.next();
	    			}
	    			catch (Exception b) {
	    				b.printStackTrace();
	    			}
				}
				population = 0;
			}
		});
	    statusBar = new StatusBar(connR, connL, database.isConnected);
	    paneBottom.add(statusBar, BorderLayout.SOUTH);
	    
	    JPanel paneBottomBody = new JPanel();
	    paneBottom.add(paneBottomBody, BorderLayout.NORTH);
	    paneBottom.add(new JSeparator(SwingConstants.HORIZONTAL));
	    paneBottomBody.add(viewButton);
	    paneBottomBody.add(reportButton);
	    add(paneBottom, BorderLayout.SOUTH);
	    model = new FeastTableModel(driver,url+dbName,userName,password, DEFAULT_QUERY);
	    table = new JTable(model);
	    listModel = table.getSelectionModel();
	    listModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listModel.addListSelectionListener(new SharedListSelectionHandler());
        table.setSelectionModel(listModel);
	    table.setPreferredScrollableViewportSize(table.getPreferredSize());
	    table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
	    add(scrollPane);
	}
	public static void main(String [] args)
	{
		JFrame GUI = new GUIFrame();
		GUI.setLocationRelativeTo(null);
		GUI.setVisible(true);
	}

	private int displayExitDialog() {
		int result = JOptionPane.showConfirmDialog(this, "Are you sure?", "Exit?", JOptionPane.YES_NO_OPTION , JOptionPane.WARNING_MESSAGE);
		return result;
	}
	
	class FeastTableModel extends AbstractTableModel {
		/**
		 * 
		 */
		private Connection conn;
		private Statement stmt;
		private ResultSetMetaData metaData;
		private boolean connectedToDatabase = false;
		private static final long serialVersionUID = 1L;
		
		public FeastTableModel(String driver, String url, String username, String password, String query) {
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
}
