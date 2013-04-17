
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.NoSuchObjectException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SDIForm extends JFrame {
	Object[] currentInfo = new Object[21];
	JPanel panelRow0, panelRow1, panelRow2, panelRow3, panelRow4, panelRow5,
			panelRow6, panelRow7, panelRow8, panelRow9, panelRow10, panelRow11,
			panelRow12, panel;
	JTextField idField, agencyField, firstNameField, lastNameField,
			addressField, apartmentNumberField, cityField, zipField,
			phoneField, numChildrenField, numAdultsField, numSeniorsField,
			totInHouseholdField, hhIncomeField, yearBox;
	JLabel idLabel, fNameLabel, lNameLabel, addressLabel, apartmentNumberlabel,
			cityLabel, zipLabel, phoneLabel, numChildrenLabel, numAdultsLabel,
			numSeniorsLabel, totInHouseholdLabel, qualifierLabel,
			hhIncomeLabel, agencyLabel, agencyRepLabel, dateLabel, monthLabel,
			dayLabel, yearLabel, missingFieldsLabel;
	JCheckBox foodstamps, tanf, ssi, medicaid, weekly, monthly, annually;
	JButton search, update, populate, insert, mainPage,clearForm;
	JComboBox agencyBox,agencyRepBox,monthBox,dayBox;
	private String[] monthList = {"","01","02","03","04","05","06","07","08","09","10","11","12"};
    private String[] agencyOptions/* = {"","Agape Cathedral Center","An Acheivable Dream","Andrews Elementary","Beauty for Ashes","Behind the Veil Ministry","Berachah Church","Berkley Village Apartments","Bethel Temple","Booker Elementary","Bread for Life","Buckroe Baptist Church",
    									"C.H.P. Lafayette Village","C.H.P. Rivermede","C.H.P. Woods @ Yorktown", "Cary Elementary","Checed Warwick","Christ Temple Holiness Church","Coastal Community Church","Deeper Life Assembly","Doris Miller Center","Dunamis Christian Center",
    									"Emmanuel House Inc.","Empowered Believers Christian","First Baptist - Jefferson Park","First Baptist Denbigh","Five Loaves","Forrest Elementary","Garden of Prayer","Gleaning Baptist Church","Greater Bethlehem Christ. As.","Greater Emmanuel Ministries",
    									"Greater Works Ministries","Grove Christian Outreach","Holy Tabernacle Church","Hope House Ministries","Ivy Baptist Church","Ivy Farms Church","JTC Lifechanging Center Inc.","Just-Us-Kidz Inc.","Langley Elementary","Langley Village","Lexington Commons",
    									"Lifeline Full Gospel","Living Faith Christian Center","Living Waters Family Outreach","Living Waters Way of the Cross","Magruder Elementary","Mercy Seat Bapt.","Mt. Calvary SDA Church","Mt. Calvary Baptist","New Beginnings","New Bethel International",
    									"New Hope Baptist Church","New Life Ministry Center","Newport News Housing","Oasis of Life","Open Door Full Gospel","Operation Breaking Through","Peninsula Hispanic SDA","Perfecting Saints Ministries","Pinecroft","Pocahontas Temple","Poquoson",
    									"Salem UMC","Salvation Army (Hampton)","Salvation Army (Williamsburg)","Seton Manor","Smith Elementary","Spirit of Truth Ministries","Sr. Christian Village of E. VA.","St. James Deliverance","St. John Baptist Church","St. Marks UMC","St. Timothy Church",
    									"Surry Community Center 1","Tarrant Elementary","Temple of Life / New Life","Temple of Peace","Temple of Refuge","Triumph Christian Center","Tyler Elementary","W.M. Ratley Road Ahead","Warwick Assembly of God","White Marsh Baptist Church",
    									"Williamsburg/Blayton Building","Williamsburg/Burnt Ordinary","Williamsburg/Katherine Circle","Williamsburg/Mimosa Woods","Williamsburg/Sylvia Brown","World Outreach Worship Center","YWCA","Zion Prospect Baptist Church"}*/;
	private String[] agencyRepOptions/* = {"","Frank Blank","Jaymie Tetreault","John Gregory","John Tester","Mandy Fitzgerald","Riley Little","Walker Riley"}*/;
    private String prevMonth;
    FeastMainFrame fmf;
    private String AGENCY_QUERY = "SELECT 'Agency_Name' FROM jos_fb_agency ORDER BY Agency_Name ASC;";
    
	boolean addNew = true;
	boolean online;
	Connection connL;
	Statement stmtL = null;
    public ResultSet result = null;
	queryQue que;
    private int agencySize;

	public SDIForm( Connection conL, boolean addN, queryQue q, boolean isConnect, FeastMainFrame f) {
	    System.out.println("SDI FORM");
	    addNew = addN;
	    online = isConnect;
	    connL = conL;
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
                System.out.println("aName: " + agencyOptions[i]);
                result.next();
            }
            result = stmtL.executeQuery( "SELECT COUNT(*), AgencyRep_ID FROM jos_fb_agencyrep;");
            agencySize = result.getInt(1);
            System.out.println("agency Size: " + agencySize);
            agencyRepOptions = new String[agencySize+1];
            agencyRepOptions[0] = "";
            result = stmtL.executeQuery( "SELECT Rep_LName, Rep_FName FROM jos_fb_agencyrep ORDER By Rep_FName ASC;");
            result.next();
            for (int i = 1; i <= agencySize; i++){
                agencyRepOptions[i] = result.getString("Rep_FName")+" "+result.getString( "Rep_LName" );
                System.out.println("aName: " + agencyRepOptions[i]);
                result.next();
            }
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        initComponents();
        setTitle("SDI form");
		fmf = f;
		setSize(575, 600);
		setLocationRelativeTo(null);
		try
        {
            stmtL = connL.createStatement();
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		que = q;
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
                if (!addNew) {
                    fmf.setVisible( false );
                }
                else {
                    fmf.setVisible( true );
                }
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

	private void initComponents() {
		panelRow0 = new JPanel(new FlowLayout());
		panelRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelRow10 = new JPanel(new FlowLayout());
		panelRow11 = new JPanel(new FlowLayout());
		panelRow12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel = new JPanel(new GridLayout(0, 1));

		// Text Fields
		firstNameField = new JTextField(12);
		lastNameField = new JTextField(12);
		addressField = new JTextField(20);
		apartmentNumberField = new JTextField(5);
		cityField = new JTextField(12);
		zipField = new JTextField(5);
		phoneField = new JTextField(10);
		numChildrenField = new JTextField(2);
		numAdultsField = new JTextField(2);
		numSeniorsField = new JTextField(2);
		totInHouseholdField = new JTextField(2);
		hhIncomeField = new JTextField(10);
		idField = new JTextField(10);
		yearBox = new JTextField(4);

		// JLabels
		fNameLabel = new JLabel("*Last Name:");
		lNameLabel = new JLabel("*First Name:");
		addressLabel = new JLabel("*Address:");
		apartmentNumberlabel = new JLabel("Apartment Number:");
		cityLabel = new JLabel("*City:");
		zipLabel = new JLabel("*Zip Code:");
		phoneLabel = new JLabel("Phone:");
		numChildrenLabel = new JLabel("*# of Children (0-17):");
		numAdultsLabel = new JLabel("*# of Adults (18-64):");
		numSeniorsLabel = new JLabel("*# of Seniors (65+):");
		totInHouseholdLabel = new JLabel("Total in Household:");
		qualifierLabel = new JLabel(
				"Automatic Household Qualifiers - Mark all that apply");
		hhIncomeLabel = new JLabel(
				"*Household Income (Select frequency of income)");
		idLabel = new JLabel("Customer ID:");
		agencyLabel = new JLabel("*Select Agency:");
		agencyRepLabel = new JLabel("*Select Agency Rep:");
		dateLabel = new JLabel("*Date:");
		monthLabel = new JLabel("M");
		dayLabel = new JLabel("D");
		yearLabel = new JLabel("Year (YYYY):");
		missingFieldsLabel = new JLabel("Required Fields marked by *");

		// checkboxes
		foodstamps = new JCheckBox("FOOD STAMPS/SNAP");
		tanf = new JCheckBox("TANF");
		ssi = new JCheckBox("SSI");
		medicaid = new JCheckBox("MEDICAID");
		weekly = new JCheckBox("weekly");
		monthly = new JCheckBox("monthly");
		annually = new JCheckBox("annually");

		// Buttons
		search = new JButton("Search");
        update = new JButton("Update Customer");
        populate = new JButton("Populate PDF Form");
        insert = new JButton("Add Customer");
        clearForm = new JButton("Clear Form");
        
        //JComboBoxes
        agencyBox = new JComboBox(agencyOptions);
        agencyRepBox = new JComboBox(agencyRepOptions);
        monthBox = new JComboBox(monthList);
        
//Make sure it's working
        System.out.println("add New: " + addNew);
		if(!addNew){
	        mainPage = new JButton("Return to Search");
		    System.out.println("Og buttons");
		    update.setVisible( true );
		    populate.setVisible( true );
		    idLabel.setVisible(true);
		    idField.setVisible(true);
		    idField.setEnabled( false );
		    insert.setVisible(false);
		    agencyLabel.setVisible(true);
		    agencyRepLabel.setVisible(false);
		    dateLabel.setVisible(false);
		    agencyBox.setVisible(true);
		    agencyRepBox.setVisible(false);
		    monthLabel.setVisible(false);
		    monthBox.setVisible(false);
		    yearLabel.setVisible(false);
		    yearBox.setVisible(false);
            dayLabel.setVisible(false);
            clearForm.setVisible(false);
            missingFieldsLabel.setVisible(false);
		}
		else{
		    System.out.println("Insert Button only");
	        mainPage = new JButton("Return to Main Menu");
            update.setVisible(false );
            populate.setVisible( false );
            idLabel.setVisible(false);
            idField.setVisible(false);
            agencyLabel.setVisible(true);
		    agencyRepLabel.setVisible(true);
		    dateLabel.setVisible(true);
            insert.setVisible(true);
            agencyBox.setVisible(true);
            agencyRepBox.setVisible(true);
            dayLabel.setVisible(false);
            monthLabel.setVisible(true);
		    monthBox.setVisible(true);
		    yearLabel.setVisible(true);
		    yearBox.setVisible(true);
            clearForm.setVisible(true);
            missingFieldsLabel.setVisible(true);
		}
		
		insert.addActionListener(new ActionListener(){
		   @Override
		   public void actionPerformed(ActionEvent evt) {
		       if (firstNameField.getText() == "" || lastNameField.getText() == "" || addressField.getText() == "" || cityField.getText() == "" ||
                               zipField.getText() == "" || numChildrenField.getText() == "" || numAdultsField.getText() == "" || numSeniorsField.getText() == "" ||
                               hhIncomeField.getText() == "" || agencyBox.getSelectedItem() == "" || agencyRepBox.getSelectedItem() == "" || monthBox.getSelectedItem() == "" ||
                               yearBox.getText() == "") {
                   displayFailedDialog();
               }
               else {
                   try
                   {
                       updateCurrentArray();
                       int n = Integer.valueOf( currentInfo[9].toString()) + Integer.valueOf( currentInfo[10].toString()) + Integer.valueOf( currentInfo[11].toString());
                       FBcustomerRS fbc = new FBcustomerRS( connL, online, currentInfo[2].toString(),currentInfo[3].toString(),currentInfo[4].toString(),currentInfo[5].toString(),currentInfo[6].toString(),
                              Integer.valueOf( currentInfo[7].toString()), Long.valueOf( currentInfo[8].toString()),
                              Integer.valueOf( currentInfo[9].toString()),
                              Integer.valueOf( currentInfo[10].toString()),
                              Integer.valueOf( currentInfo[11].toString()),
                              n,
                              ((Boolean)currentInfo[12]? 1:0),
                              ( (Boolean)currentInfo[13]? 1:0),
                              ( (Boolean) currentInfo[14]? 1:0),
                              ( (Boolean) currentInfo[15]? 1:0),
                              Integer.valueOf( currentInfo[16].toString()),
                              ( (Boolean) currentInfo[17]? 1:0),
                              ( (Boolean) currentInfo[18]? 1:0),
                              ( (Boolean) currentInfo[19]? 1:0),
                              ( (Boolean) currentInfo[20]? 1:0));
                       que.addToQue( fbc.getCreateCustomerQuery());
                       System.out.println("First Name: "+currentInfo[3]+" Last Name: "+currentInfo[2]);
                       String q = "SELECT * FROM jos_fb_customer WHERE First_Name = '"+currentInfo[3]+"' AND Last_Name = '"+currentInfo[2]+"';";
                       System.out.println(q);
                       result = stmtL.executeQuery(q);
                       if (result.next()){
                           displayConfirmDialog();
                       }
                       else {
                           displayFailedDialog();
                       }
                       result = stmtL.executeQuery("SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '"+agencyBox.getSelectedItem()+"';");
                       String agencyid = "";
                       if (result.next()) {
                           agencyid = result.getString("Acct_Num");
                       }
                       System.out.println("RepFirstName: "+agencyRepBox.getSelectedItem().toString().substring( 0, agencyRepBox.getSelectedItem().toString().indexOf( " " ) )+ " RepLastName: "+agencyRepBox.getSelectedItem().toString().substring(agencyRepBox.getSelectedItem().toString().indexOf( " " )+1, agencyRepBox.getSelectedItem().toString().length() ));
                       result = stmtL.executeQuery("SELECT AgencyRep_ID FROM jos_fb_agencyrep WHERE Rep_FName = '"+agencyRepBox.getSelectedItem().toString().substring( 0, agencyRepBox.getSelectedItem().toString().indexOf( " " ) ) + 
                           "' AND Rep_LName = '" +agencyRepBox.getSelectedItem().toString().substring(agencyRepBox.getSelectedItem().toString().indexOf( " " )+1, agencyRepBox.getSelectedItem().toString().length() ) + "';");
                       String agencyrepid = "";
                       if (result.next()) {
                           agencyrepid = result.getString( "AgencyRep_ID" );
                       }
                       result = stmtL.executeQuery("SELECT Customer_ID FROM jos_fb_customer WHERE Last_Name = '"+currentInfo[2]+"' AND First_Name = '"+currentInfo[3]+"' AND Street_Address = '"+currentInfo[4]+"';");
                       int customerid = 0;
                       if (result.next()) {
                           customerid = result.getInt( "Customer_ID" );
                       }
                       String date = "" + yearBox.getText()+"-"+monthBox.getSelectedItem()+"-"+dayBox.getSelectedItem();
                       System.out.println("Acct_Num: "+ agencyid + " Agency_RepID: "+agencyrepid +" Date: "+date);
                       FBMonthlyDistribution dist = new FBMonthlyDistribution(connL,false, customerid ,agencyid,Integer.parseInt(agencyrepid),date, que);
                       que.addToQue( dist.createQuery() );
                       result = stmtL.executeQuery("SELECT Customer_ID, theDate FROM jos_fb_monthlydist WHERE Customer_ID = "+customerid+" AND theDate = '"+date+"';");
                       if (result.next()){
                           displayConfirmDialog();
                       }
                       else {
                           displayFailedDialog();
                       }
                       firstNameField.setText("");
                       lastNameField.setText("");
                       addressField.setText("");
                       apartmentNumberField.setText("");
                       cityField.setText("");
                       zipField.setText("");
                       phoneField.setText("");
                       numChildrenField.setText("");
                       numAdultsField.setText("");
                       numSeniorsField.setText("");
                       totInHouseholdField.setText("");
                       hhIncomeField.setText("");
                       idField.setText("");
                       yearBox.setText("");
                       foodstamps.setSelected(false);
                       tanf.setSelected(false);
                       ssi.setSelected(false);
                       medicaid.setSelected(false);
                       weekly.setSelected(false);
                       monthly.setSelected(false);
                       annually.setSelected(false);
                       agencyBox.setSelectedIndex(0);
                       agencyRepBox.setSelectedIndex(0);
                       monthBox.setSelectedIndex(0);
                   }
                   catch ( NumberFormatException e )
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch ( SQLException e )
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
               }
		   }
		});
		
		/*search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String id = idField.getText();
				if (id.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please, enter a customer ID",
							"Customer Search Error",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					try {
					   
					        currentInfo = functions.retrieveUser(Integer.parseInt(id), online);
					        System.out.println("Setting the fields");
						firstNameField.setText((String) currentInfo[2]);
						lastNameField.setText((String) currentInfo[3]);
						addressField.setText((String) currentInfo[4]);
						apartmentNumberField.setText((String) currentInfo[5]);
						cityField.setText((String) currentInfo[6]);
						zipField.setText(currentInfo[7].toString());
						phoneField.setText(currentInfo[8].toString());
						numChildrenField.setText(currentInfo[9].toString());
						numAdultsField.setText(currentInfo[10].toString());
						numSeniorsField.setText(currentInfo[11].toString());
						Integer n = (Integer) currentInfo[9] + (Integer) currentInfo[10]
								+ (Integer) currentInfo[11];
						totInHouseholdField.setText(n.toString());
						foodstamps.setSelected((Boolean) currentInfo[12]);
						tanf.setSelected((Boolean) currentInfo[13]);
						ssi.setSelected((Boolean) currentInfo[14]);
						medicaid.setSelected((Boolean) currentInfo[15]);
						hhIncomeField.setText(currentInfo[16].toString());
						weekly.setSelected((Boolean) currentInfo[17]);
						monthly.setSelected((Boolean) currentInfo[18]);
						annually.setSelected((Boolean) currentInfo[19]);
						// TODO set offender field once created in GUI

					} catch (NoSuchObjectException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,
								"There is no user: \"" + id
										+ "\" in the database",
								"Customer Search Error",
								JOptionPane.INFORMATION_MESSAGE);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});*/
		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//0 == yes; 1==no;
				int n = JOptionPane.showConfirmDialog(
					    null,
					    "Are you sure you would like to update the current customer?",
					    "Customer Update",
					    JOptionPane.YES_NO_OPTION);
				if(n==0){
					updateCurrentArray();
					Integer i = Integer.parseInt( idField.getText() );
					//i.parseInt( idField.getText() );
					System.out.println("idField:" +idField.getText());
					System.out.println("cust ID: " + i);
					functions.updateCustomer(
					    i,
					    currentInfo,
					    online,
					    que,
					    connL);
				}
			}
		});
		populate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    
				String id = idField.getText();
				int one = 1;
				if (id.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please, enter a customer ID",
							"Customer Search Error",
							JOptionPane.INFORMATION_MESSAGE);
				}

				else if(agencyBox.getSelectedItem().equals(""))
				{
				    JOptionPane.showMessageDialog(null,
                        "Please, select an Agency",
                        "Generate PDF Error",
                        JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					try {
					        result = stmtL.executeQuery( "SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '" + agencyBox.getSelectedItem() + "';" );
					        String acctNum = "";
					        Object[] agencyInfo = new Object[2];
					        if(result.next())
					        {
					            acctNum = result.getString( "Acct_Num" );
					            agencyInfo[0] = acctNum;
					            agencyInfo[1] = agencyBox.getSelectedItem();
					        }
					        currentInfo = functions.retrieveUser(Integer.parseInt(id), online, connL);
	                       // System.out.println("Retrieving Agency");
	                       // System.out.println("Retrieving Agent");
					   
						PdfGenerator.populateSDIForm(currentInfo, agencyInfo);
					} 
					catch (NoSuchObjectException o) {
						// TODO Auto-generated catch block
					    o.printStackTrace();
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
                    catch ( SQLException m )
                    {
                        // TODO Auto-generated catch block
                        m.printStackTrace();
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
			    	dayBox = new JComboBox(days);
					panelRow11.remove(yearLabel);
					panelRow11.remove(yearBox);
					dayLabel.setVisible(true);
					panelRow11.add(dayBox);
					panelRow11.add(yearLabel);
					panelRow11.add(yearBox);
					setSize(630, 600);
		            revalidate();
		    	}
		    	else if (m == "") {
		    		dayLabel.setVisible(false);
					panelRow11.remove(dayBox);
					setSize(575, 600);
	                revalidate();
		    	}
		    	else {
		    		panelRow11.remove(yearLabel);
					panelRow11.remove(yearBox);
					panelRow11.remove(dayBox);
                    dayBox = new JComboBox(days);
					dayLabel.setVisible(true);
					panelRow11.add(dayBox);
					panelRow11.add(yearLabel);
					panelRow11.add(yearBox);
					setSize(630, 600);
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
        	    if (!addNew) {
        	        fmf.setVisible( false );
        	    }
        	    else {
                    fmf.setVisible( true );
        	    }
        	    fmf.customerSearchField.setText( "" );
                dispose();
        	}
        });
		clearForm.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        firstNameField.setText("");
		        lastNameField.setText("");
		        addressField.setText("");
				apartmentNumberField.setText("");
				cityField.setText("");
				zipField.setText("");
				phoneField.setText("");
				numChildrenField.setText("");
				numAdultsField.setText("");
				numSeniorsField.setText("");
				totInHouseholdField.setText("");
				hhIncomeField.setText("");
				idField.setText("");
				yearBox.setText("");
				foodstamps.setSelected(false);
				tanf.setSelected(false);
				ssi.setSelected(false);
				medicaid.setSelected(false);
				weekly.setSelected(false);
				monthly.setSelected(false);
				annually.setSelected(false);
				agencyBox.setSelectedIndex(0);
				agencyRepBox.setSelectedIndex(0);
				monthBox.setSelectedIndex(0);
		    }
		});

		panelRow0.add(idLabel);
		panelRow0.add(idField);
		//panelRow0.add(search);
		panelRow0.add(agencyLabel);
		panelRow0.add(agencyBox);
		panelRow11.add(agencyRepLabel);
		panelRow11.add(agencyRepBox);
		panelRow11.add(dateLabel);
		panelRow11.add(monthLabel);
		panelRow11.add(monthBox);
		panelRow11.add(dayLabel);
		//panelRow11.add(dayBox);
		panelRow11.add(yearLabel);
		panelRow11.add(yearBox);
		panelRow1.add(fNameLabel);
		panelRow1.add(firstNameField);
		panelRow1.add(lNameLabel);
		panelRow1.add(lastNameField);
		panelRow2.add(addressLabel);
		panelRow2.add(addressField);
		panelRow2.add(apartmentNumberlabel);
		panelRow2.add(apartmentNumberField);
		panelRow3.add(cityLabel);
		panelRow3.add(cityField);
		panelRow3.add(zipLabel);
		panelRow3.add(zipField);
		panelRow3.add(phoneLabel);
		panelRow3.add(phoneField);
		panelRow4.add(numChildrenLabel);
		panelRow4.add(numChildrenField);
		panelRow4.add(numAdultsLabel);
		panelRow4.add(numAdultsField);
		panelRow4.add(numSeniorsLabel);
		panelRow4.add(numSeniorsField);
		panelRow5.add(totInHouseholdLabel);
		panelRow5.add(totInHouseholdField);
		panelRow6.add(qualifierLabel);
		panelRow7.add(foodstamps);
		panelRow7.add(tanf);
		panelRow7.add(ssi);
		panelRow7.add(medicaid);
		panelRow8.add(hhIncomeLabel);
		panelRow8.add(hhIncomeField);
		panelRow9.add(weekly);
		panelRow9.add(monthly);
		panelRow9.add(annually);
		panelRow12.add(missingFieldsLabel);
		panelRow10.add(update);
		panelRow10.add(populate);
		panelRow10.add(insert);
		panelRow10.add(clearForm);
		panelRow10.add(mainPage);

		panel.add(panelRow0);
		panel.add(panelRow11);
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		panel.add(panelRow1);
		panel.add(panelRow2);
		panel.add(panelRow3);
		panel.add(panelRow4);
		panel.add(panelRow5);
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		panel.add(panelRow6);
		panel.add(panelRow7);
		panel.add(panelRow8);
		panel.add(panelRow9);
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		panel.add(panelRow12);
		panel.add(panelRow10);
		this.add(panel);
	}

	protected void updateCurrentArray() {
		currentInfo[2]=firstNameField.getText();
		currentInfo[3]=lastNameField.getText();
		currentInfo[4]=addressField.getText();
		currentInfo[5]=apartmentNumberField.getText();
		currentInfo[6]=cityField.getText();
		currentInfo[7]=zipField.getText();
		currentInfo[8]=phoneField.getText();
		currentInfo[9]=numChildrenField.getText();
		currentInfo[10]=numAdultsField.getText();
		currentInfo[11]=numSeniorsField.getText();
		currentInfo[12]=foodstamps.isSelected();
		currentInfo[13]=tanf.isSelected();
		currentInfo[14]=ssi.isSelected();
		currentInfo[15]=medicaid.isSelected();
		currentInfo[16]=Integer.parseInt(hhIncomeField.getText());
		currentInfo[17]=weekly.isSelected();
		currentInfo[18]=monthly.isSelected();
		currentInfo[19]=annually.isSelected();
		currentInfo[20]= false;
		
	}
	private void displayConfirmDialog()
    {
        JOptionPane.showMessageDialog(this,"Customer was added!");
    }
    private void displayFailedDialog() {
        JOptionPane.showMessageDialog( this, "Customer failed to be added. \nPlease make sure to fill in all fields" );
    }
/*
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SDIForm sdi = new SDIForm();
				sdi.setVisible(true);
			}
		});
	}
	*/

}
