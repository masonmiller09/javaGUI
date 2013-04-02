import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.NumericShaper;
import java.math.BigDecimal;
import java.rmi.NoSuchObjectException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.sql.Connection;
import java.sql.SQLException;

public class SDIForm extends JFrame {
	Object[] currentInfo = new Object[21];
	JPanel panelRow0, panelRow1, panelRow2, panelRow3, panelRow4, panelRow5,
			panelRow6, panelRow7, panelRow8, panelRow9, panelRow10, panel;
	JTextField idField, agencyField, firstNameField, lastNameField,
			addressField, apartmentNumberField, cityField, zipField,
			phoneField, numChildrenField, numAdultsField, numSeniorsField,
			totInHouseholdField, hhIncomeField;
	JLabel idLabel, fNameLabel, lNameLabel, addressLabel, apartmentNumberlabel,
			cityLabel, zipLabel, phoneLabel, numChildrenLabel, numAdultsLabel,
			numSeniorsLabel, totInHouseholdLabel, qualifierLabel,
			hhIncomeLabel;
	JCheckBox foodstamps, tanf, ssi, medicaid, weekly, monthly, annually;
	JButton search, update, populate, insert;
	
	boolean addNew = true;
	boolean online;
	Connection connR;
	Connection connL;
	queryQue que;

	public SDIForm(Connection conR, Connection conL, boolean addN, queryQue q, boolean isConnect) {
	    addNew = addN;
	    online = isConnect;
	    initComponents();
		setTitle("SDI form");
		setSize(550, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		connR = conR;
		connL = conL;
		que = q;
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
		panel = new JPanel(new GridLayout(0, 1));

		// Text Fields
		firstNameField = new JTextField(12);
		firstNameField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                //PdfGenerator.autoComplete(firstNameField, "Last_Name", conn);
            }
        });
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

		// JLabels
		fNameLabel = new JLabel("Last Name:");
		lNameLabel = new JLabel("First Name");
		addressLabel = new JLabel("Address:");
		apartmentNumberlabel = new JLabel("Apartment Number:");
		cityLabel = new JLabel("City:");
		zipLabel = new JLabel("Zip Code:");
		phoneLabel = new JLabel("Phone (optional):");
		numChildrenLabel = new JLabel("# of Children (0-17):");
		numAdultsLabel = new JLabel("# of Adults (18-64):");
		numSeniorsLabel = new JLabel("# of Seniors (65+):");
		totInHouseholdLabel = new JLabel("Total in Household:");
		qualifierLabel = new JLabel(
				"Automatic Household Qualifiers - Mark all that apply");
		hhIncomeLabel = new JLabel(
				"Household Income (Select frequency of income)");
		idLabel = new JLabel("Customer ID:");

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
        populate = new JButton("Populate SDI Form");
        insert = new JButton("Insert");
//Make sure it's working
        System.out.println("add New: " + addNew);
		if(!addNew){
		    System.out.println("Og buttons");
		    search.setVisible( true );
		    update.setVisible( true );
		    populate.setVisible( true );
		    idField.enable();
		    insert.setVisible(false);
		}
		else{
		    System.out.println("Insert Button only");
		    search.setVisible( false );
            update.setVisible(false );
            populate.setVisible( false );
            idField.disable();
            insert.setVisible(true);
		}
		
		insert.addActionListener(new ActionListener(){
		   @Override
		   public void actionPerformed(ActionEvent evt) {
		       updateCurrentArray();

                   int n = Integer.valueOf( currentInfo[9].toString()) + Integer.valueOf( currentInfo[10].toString()) + Integer.valueOf( currentInfo[11].toString());
                   try
                {
                    FBcustomerRS fbc = new FBcustomerRS(connR, connL, online, currentInfo[2].toString(),currentInfo[3].toString(),currentInfo[4].toString(),currentInfo[5].toString(),currentInfo[6].toString(),
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
                    dispose();
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
		});
		
		search.addActionListener(new ActionListener() {
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
		});
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
					System.out.println("cust ID: " + (Integer)currentInfo[1]);
					functions.updateCustomer(
					    (Integer)currentInfo[1],
					    currentInfo,
					    online,
					    que);
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
				} else {
					try {
					    
					        currentInfo = functions.retrieveUser(Integer.parseInt(id), online);
	                       // System.out.println("Retrieving Agency");
	                        Object [] agencyInfo = functions.retrieveAgency(one, online);
	                       // System.out.println("Retrieving Agent");
	                        Object [] agentInfo = functions.retrieveAgent(one, online);
					   
						PdfGenerator.populateSDIForm(currentInfo,agencyInfo,agentInfo);
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
		});

		panelRow0.add(idLabel);
		panelRow0.add(idField);
		panelRow0.add(search);
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
		panelRow10.add(update);
		panelRow10.add(populate);
		panelRow10.add(insert);

		panel.add(panelRow0);
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
