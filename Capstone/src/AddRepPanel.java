import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class AddRepPanel extends JFrame {
    JPanel panel,panel0,panel1,panel2,panel3;
    JLabel repFirstNameLabel,repLastNameLabel,agencyLabel;
    JTextField repFirstNameBox,repLastNameBox;
    FeastMainFrame fmf;
    JButton addRepButton,mainPage;
    JComboBox agencyBox;
    private String[] agencyOptions/* = {"","Agape Cathedral Center","An Acheivable Dream","Andrews Elementary","Beauty for Ashes","Behind the Veil Ministry","Berachah Church","Berkley Village Apartments","Bethel Temple","Booker Elementary","Bread for Life","Buckroe Baptist Church",
            "C.H.P. Lafayette Village","C.H.P. Rivermede","C.H.P. Woods @ Yorktown", "Cary Elementary","Checed Warwick","Christ Temple Holiness Church","Coastal Community Church","Deeper Life Assembly","Doris Miller Center","Dunamis Christian Center",
            "Emmanuel House Inc.","Empowered Believers Christian","First Baptist - Jefferson Park","First Baptist Denbigh","Five Loaves","Forrest Elementary","Garden of Prayer","Gleaning Baptist Church","Greater Bethlehem Christ. As.","Greater Emmanuel Ministries",
            "Greater Works Ministries","Grove Christian Outreach","Holy Tabernacle Church","Hope House Ministries","Ivy Baptist Church","Ivy Farms Church","JTC Lifechanging Center Inc.","Just-Us-Kidz Inc.","Langley Elementary","Langley Village","Lexington Commons",
            "Lifeline Full Gospel","Living Faith Christian Center","Living Waters Family Outreach","Living Waters Way of the Cross","Magruder Elementary","Mercy Seat Bapt.","Mt. Calvary SDA Church","Mt. Calvary Baptist","New Beginnings","New Bethel International",
            "New Hope Baptist Church","New Life Ministry Center","Newport News Housing","Oasis of Life","Open Door Full Gospel","Operation Breaking Through","Peninsula Hispanic SDA","Perfecting Saints Ministries","Pinecroft","Pocahontas Temple","Poquoson",
            "Salem UMC","Salvation Army (Hampton)","Salvation Army (Williamsburg)","Seton Manor","Smith Elementary","Spirit of Truth Ministries","Sr. Christian Village of E. VA.","St. James Deliverance","St. John Baptist Church","St. Marks UMC","St. Timothy Church",
            "Surry Community Center 1","Tarrant Elementary","Temple of Life / New Life","Temple of Peace","Temple of Refuge","Triumph Christian Center","Tyler Elementary","W.M. Ratley Road Ahead","Warwick Assembly of God","White Marsh Baptist Church",
            "Williamsburg/Blayton Building","Williamsburg/Burnt Ordinary","Williamsburg/Katherine Circle","Williamsburg/Mimosa Woods","Williamsburg/Sylvia Brown","World Outreach Worship Center","YWCA","Zion Prospect Baptist Church"}*/;
    static FBDatabase fbd;
    public static boolean online;
    public static Connection connL = null;
    public static Statement stmtL;
    public ResultSet result = null;
    public Integer agencySize;
    
    public AddRepPanel(Connection connL2, queryQue que, boolean online2, boolean visible, FeastMainFrame f) {
        super("Add Agency Representative");
        setSize(370,220);
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
                System.out.println("aName: " + agencyOptions[i]);
                result.next();
            }
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fmf = f;
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
        panel0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel3 = new JPanel(new FlowLayout());
        panel = new JPanel(new GridLayout(0,1));
        
        //JLabels
        agencyLabel = new JLabel("*Select Agency:");
        repFirstNameLabel = new JLabel("*First Name:");
        repLastNameLabel = new JLabel("*Last Name:");
        
        //JTextFields
        repFirstNameBox = new JTextField(30);
        repLastNameBox = new JTextField(30);
        
        //JButtons
        addRepButton = new JButton("Add Representative");
        mainPage = new JButton("Return to Main Menu");
        
        //JComboBox
        agencyBox = new JComboBox(agencyOptions);
        
        //Listeners
        addRepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String agency = (String)agencyBox.getSelectedItem();
                String firstname = repFirstNameBox.getText();
                String lastname = repLastNameBox.getText();
                String accountnumber = "";
                String QUERY = ("SELECT Acct_Num FROM jos_fb_agency WHERE Agency_Name = '"+agency+"';");
                if (agency == "" || firstname == "" || lastname == "") {
                    displayFailedDialog();
                }
                else {
                    try
                    {
                        result = stmtL.executeQuery(QUERY);
                        accountnumber = result.getString(1);
                        System.out.println("AGENCY: "+agency+" ACCOUNT NUMBER: "+accountnumber);
                        FBAgencyRep rep = new FBAgencyRep(connL,accountnumber,lastname,firstname);
                        String q = "SELECT * FROM jos_fb_agencyrep WHERE Rep_FName = '"+firstname+"' AND Rep_LName = '"+lastname+"';";
                        result = stmtL.executeQuery(q);
                        if (result.next()){
                            displayConfirmDialog();
                        }
                        else {
                            displayFailedDialog();
                        }
                    }
                    catch ( SQLException e1 )
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
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
        panel0.add(agencyLabel,BorderLayout.EAST);
        panel0.add(agencyBox,BorderLayout.EAST);
        panel1.add(repFirstNameLabel,BorderLayout.EAST);
        panel1.add(repFirstNameBox,BorderLayout.EAST);
        panel2.add(repLastNameLabel,BorderLayout.EAST);
        panel2.add(repLastNameBox,BorderLayout.EAST);
        panel3.add(addRepButton,BorderLayout.EAST);
        panel3.add(mainPage,BorderLayout.EAST);
        
        panel.add(panel0);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        this.add(panel);
    }
    private void displayConfirmDialog()
    {
        JOptionPane.showMessageDialog(this,"Agency Rep was added!");
    }
    private void displayFailedDialog() {
        JOptionPane.showMessageDialog( this, "Agency Representative failed to be added. \nPlease make sure to fill in all fields" );
    }
}
