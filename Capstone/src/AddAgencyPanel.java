import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class AddAgencyPanel extends JFrame{
    JPanel panel,panel1,panel2,panel3;
    JLabel agencyNameLabel,agencyIDLabel;
    JTextField agencyNameBox,agencyIDBox;
    JButton addAgencyButton,mainPage;
    static FBDatabase fbd;
    public static boolean online;
    public static Connection connL = null;
    public static Connection connR = null;
    public ResultSet result = null;
    FeastMainFrame fmf;
    
    public AddAgencyPanel(Connection connL2, queryQue que, boolean online2, boolean visible, FeastMainFrame f) {
        super("Add Agency");
        setSize(350,175);
        connL = connL2;
        online = online2;
        initComponents();
        fmf = f;
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
        
        //JLabels
        agencyNameLabel = new JLabel("Agency Name:*");
        agencyIDLabel = new JLabel("Agency ID:*");
        
        //JTextFields
        agencyNameBox = new JTextField(30);
        agencyIDBox = new JTextField(30);
        
        //JButtons
        addAgencyButton = new JButton("Add Agency");
        mainPage = new JButton("Return to Main Menu");
        
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
        
        panel1.add(agencyNameLabel,BorderLayout.EAST);
        panel1.add(agencyNameBox,BorderLayout.EAST);
        panel2.add(agencyIDLabel,BorderLayout.EAST);
        panel2.add(agencyIDBox,BorderLayout.EAST);
        panel3.add(addAgencyButton,BorderLayout.EAST);
        panel3.add(mainPage,BorderLayout.EAST);
        
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        this.add(panel);
    }
}
