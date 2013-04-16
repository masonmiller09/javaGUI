import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


public class DistributionInputPanel extends JFrame {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //static FBDatabase fbd;
    FeastMainFrame fmf;
    public static boolean online;
    public static Connection connL = null;
    public static Statement stmtL;
    public ResultSet result = null;
    JPanel panel1,panel2,panel3,panel4,panel5,panel6,panel;
    JLabel agencyLabel,dateLabel,agencyRepLabel,monthLabel,dayLabel,yearLabel,customerIDLabel1,customerIDLabel2;
    JComboBox agencyBox,agencyRepBox,monthBox,dayBox;
    JTextField customerBox,yearBox;
    JButton distributionButton,clearButton,mainPage;
    private String[] monthList = {"","01","02","03","04","05","06","07","08","09","10","11","12"};
    private String[] agencyOptions /*= {"","Agape Cathedral Center","An Acheivable Dream","Andrews Elementary","Beauty for Ashes","Behind the Veil Ministry","Berachah Church","Berkley Village Apartments","Bethel Temple","Booker Elementary","Bread for Life","Buckroe Baptist Church",
                                        "C.H.P. Lafayette Village","C.H.P. Rivermede","C.H.P. Woods @ Yorktown", "Cary Elementary","Checed Warwick","Christ Temple Holiness Church","Coastal Community Church","Deeper Life Assembly","Doris Miller Center","Dunamis Christian Center",
                                        "Emmanuel House Inc.","Empowered Believers Christian","First Baptist - Jefferson Park","First Baptist Denbigh","Five Loaves","Forrest Elementary","Garden of Prayer","Gleaning Baptist Church","Greater Bethlehem Christ. As.","Greater Emmanuel Ministries",
                                        "Greater Works Ministries","Grove Christian Outreach","Holy Tabernacle Church","Hope House Ministries","Ivy Baptist Church","Ivy Farms Church","JTC Lifechanging Center Inc.","Just-Us-Kidz Inc.","Langley Elementary","Langley Village","Lexington Commons",
                                        "Lifeline Full Gospel","Living Faith Christian Center","Living Waters Family Outreach","Living Waters Way of the Cross","Magruder Elementary","Mercy Seat Bapt.","Mt. Calvary SDA Church","Mt. Calvary Baptist","New Beginnings","New Bethel International",
                                        "New Hope Baptist Church","New Life Ministry Center","Newport News Housing","Oasis of Life","Open Door Full Gospel","Operation Breaking Through","Peninsula Hispanic SDA","Perfecting Saints Ministries","Pinecroft","Pocahontas Temple","Poquoson",
                                        "Salem UMC","Salvation Army (Hampton)","Salvation Army (Williamsburg)","Seton Manor","Smith Elementary","Spirit of Truth Ministries","Sr. Christian Village of E. VA.","St. James Deliverance","St. John Baptist Church","St. Marks UMC","St. Timothy Church",
                                        "Surry Community Center 1","Tarrant Elementary","Temple of Life / New Life","Temple of Peace","Temple of Refuge","Triumph Christian Center","Tyler Elementary","W.M. Ratley Road Ahead","Warwick Assembly of God","White Marsh Baptist Church",
                                        "Williamsburg/Blayton Building","Williamsburg/Burnt Ordinary","Williamsburg/Katherine Circle","Williamsburg/Mimosa Woods","Williamsburg/Sylvia Brown","World Outreach Worship Center","YWCA","Zion Prospect Baptist Church"}*/;
    private String[] agencyRepOptions /*= {"","Frank Blank","Jaymie Tetreault","John Gregory","John Tester","Mandy Fitzgerald","Riley Little","Walker Riley"}*/;

    static final String INITIAL_QUERY = "SELECT Customer_ID, First_Name, Last_Name, City, Zip_Code, Phone_Number FROM jos_fb_customer WHERE Customer_ID LIKE '1%' ORDER BY Customer_ID ASC;";
    static final String SIZE_DATABASE_QUERY = "SELECT COUNT(Customer_ID) FROM jos_fb_customer;";
    private String query = "",prevMonth;
    private int agencySize;

    public DistributionInputPanel(Connection connL2, queryQue que, boolean online2, boolean visible, FeastMainFrame f) {
        super("Distribution Input");
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
        fmf = f;
        setSize(500,300);
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
        panel = new JPanel(new GridLayout(0,1));
        panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel5 = new JPanel(new FlowLayout());
        panel6 = new JPanel(new FlowLayout());
        
        //JLabels

        agencyLabel = new JLabel("Select Agency:*");
        agencyRepLabel = new JLabel("Select Agency Rep:*");
        dateLabel = new JLabel("Date:*");
        monthLabel = new JLabel("M");
        dayLabel = new JLabel("D");
        yearLabel = new JLabel("Year(YYYY)");
        customerIDLabel1 = new JLabel("Enter all Customer ID's separated by a comma and a space. ");
        customerIDLabel2 = new JLabel("(Ex: 1, 4, 54, 23)");
        Font newLabelFont=new Font(customerIDLabel2.getFont().getName(),~Font.BOLD,customerIDLabel2.getFont().getSize());
        customerIDLabel2.setFont(newLabelFont);
        
        //ComboBoxes
        agencyBox = new JComboBox(agencyOptions);
        agencyRepBox = new JComboBox(agencyRepOptions);
        monthBox = new JComboBox(monthList);
        
        //JTextFields
        yearBox = new JTextField(4);
        customerBox = new JTextField(40);
        
        //JButtons
        distributionButton = new JButton("Add Distribution");
        clearButton = new JButton("Clear Form");
        mainPage = new JButton("Return to Main Menu");
        
        //Listeners
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
                if (prevMonth == null || prevMonth == "") {
                    dayBox = new JComboBox(days);
                    panel3.remove(yearLabel);
                    panel3.remove(yearBox);
                    panel3.add(dayLabel,BorderLayout.EAST);
                    panel3.add(dayBox,BorderLayout.EAST);
                    panel3.add(yearLabel,BorderLayout.EAST);
                    panel3.add(yearBox,BorderLayout.EAST);
                    revalidate();
                }
                else if (m == "") {
                    panel3.remove(dayLabel);
                    panel3.remove(dayBox);
                    revalidate();
                }
                else {
                    dayBox = new JComboBox(days);
                    panel.removeAll();
                    panel3.add(dateLabel,BorderLayout.EAST);
                    panel3.add(monthLabel,BorderLayout.EAST);
                    panel3.add(monthBox,BorderLayout.EAST);
                    panel3.add(dayLabel,BorderLayout.EAST);
                    panel3.add(dayBox,BorderLayout.EAST);
                    panel3.add(yearLabel,BorderLayout.EAST);
                    panel3.add(yearBox,BorderLayout.EAST);
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
                fmf.setVisible( true );
                fmf.customerSearchField.setText( "" );
                dispose();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                yearBox.setText("");
                agencyBox.setSelectedIndex(0);
                monthBox.setSelectedIndex(0);
                agencyRepBox.setSelectedIndex(0);
                customerBox.setText("");
                prevMonth = null;
            }
        });
        
        panel1.add(agencyLabel,BorderLayout.EAST);
        panel1.add(agencyBox,BorderLayout.EAST);
        panel2.add(agencyRepLabel,BorderLayout.EAST);
        panel2.add(agencyRepBox,BorderLayout.EAST);
        panel3.add(dateLabel,BorderLayout.EAST);
        panel3.add(monthLabel,BorderLayout.EAST);
        panel3.add(monthBox,BorderLayout.EAST);
        panel3.add(yearLabel,BorderLayout.EAST);
        panel3.add(yearBox,BorderLayout.EAST);
        panel4.add(customerIDLabel1,BorderLayout.EAST);
        panel4.add(customerIDLabel2,BorderLayout.EAST);
        panel5.add(customerBox,BorderLayout.EAST);
        panel6.add(distributionButton,BorderLayout.EAST);
        panel6.add(clearButton,BorderLayout.EAST);
        panel6.add(mainPage,BorderLayout.EAST);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);
        panel.add(panel5);
        //panel.add(scroll);
        panel.add(panel6,BorderLayout.SOUTH);
        this.add(panel);
    }
}
