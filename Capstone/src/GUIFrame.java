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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class GUIFrame extends JFrame
{
	private JPanel panel;
	private JPanel paneTop;
	private JPanel paneBottom;
	private JTextField searchBox;
	private JLabel searchLabel;
	private JComboBox<String> searchOptions;
	private JButton searchButton;
	private JButton viewButton;
	private JButton reportButton;
	private JTable table;
	private Vector columns;
	private Vector data;
	private Vector row;
	private String[] cbList = { "", "Agency", "First_Name", "Last_Name", "Zipcode" };
	public Connection conn = null;
	public Statement stmt = null;
	public ResultSet result = null;
	String url = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
	String dbName = "feastdb";
	String driver = "com.mysql.jdbc.Driver";
	String userName = "root";
	String password = "lampa";
	String searching = "";
	private int num = 0;

	public GUIFrame()
	{
		super("FEAST");
		paneTop = new JPanel();
		add(paneTop, BorderLayout.NORTH);
		setSize(1500, 700);
		JMenuBar main = new JMenuBar();
		setJMenuBar(main);
		JMenu file = new JMenu("File");
		file.enable(true);
		JMenuItem insert = file.add("Insert");
		JMenuItem delete = file.add("Delete");
		JMenuItem exit = file.add("Exit");
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int result = displayExitDialog();
				if(result == JOptionPane.YES_OPTION)
				{
					dispose();
				}
			}
		});
		JMenu edit = new JMenu("Edit");
		edit.enable(true);
		JMenuItem editF = edit.add("Edit");
		JMenu view = new JMenu("View");
		JMenu viewBy = new JMenu("View by");
		JMenuItem vAgency = viewBy.add("Agency");
		JMenuItem vZipcode = viewBy.add("City");
		JMenuItem vCity = viewBy.add("Zipcode");
		view.add(viewBy);
		view.enable(true);
		main.add(file);
		main.add(edit);
		main.add(view);
		addWindowListener(new WindowListener()
		{
			public void windowActivated(WindowEvent arg0)
			{
			}

			public void windowClosed(WindowEvent arg0)
			{
			}

			public void windowClosing(WindowEvent arg0)
			{
				int result = displayExitDialog();
				if(result == JOptionPane.YES_OPTION)
				{
					dispose();
				}
			}

			@Override
			public void windowDeactivated(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void windowIconified(WindowEvent arg0)
			{
			}

			public void windowOpened(WindowEvent arg0)
			{
			}
		});
		// setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		searchBox = new JTextField(10);
		searchBox.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_DELETE)
				{
					searching = searching.substring(0, searching.length() - 1);
				}
				else if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String text = searchBox.getText();
					String option = (String) searchOptions.getSelectedItem();
					Search(option, text);
				}
				else
				{
					char c = e.getKeyChar();
					searching += c;
					if(num == 0)
					{

					}
				}
			}
		});
		searchButton = new JButton("Search");
		searchButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				String text = searchBox.getText();
				String option = (String) searchOptions.getSelectedItem();
				Search(option, text);

			}
		});
		searchLabel = new JLabel("Search by: ");
		searchOptions = new JComboBox(cbList);
		panel.add(searchLabel, BorderLayout.EAST);
		panel.add(searchOptions, BorderLayout.EAST);
		panel.add(searchBox, BorderLayout.EAST);
		panel.add(searchButton, BorderLayout.EAST);
		searchBox.setFont(new Font("Verdana", Font.PLAIN, 12));
		paneTop.add(panel, BorderLayout.EAST);
		paneBottom = new JPanel();
		viewButton = new JButton("View");
		reportButton = new JButton("Report");
		paneBottom.add(viewButton);
		paneBottom.add(reportButton);
		add(paneBottom, BorderLayout.SOUTH);
		try
		{
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			stmt = conn.createStatement();
			result = stmt.executeQuery("SELECT * FROM fb_customer");
			ResultSetMetaData md = result.getMetaData();
			int columnCount = md.getColumnCount();
			columns = new Vector(columnCount);
			for(int i = 1; i <= columnCount; i++)
			{
				columns.add(md.getColumnName(i));
			}
			data = new Vector();
			data.add(columns);
			while(result.next())
			{
				row = new Vector(columnCount);
				for(int i = 1; i <= columnCount; i++)
				{
					row.add(result.getString(i));
				}
				data.add(row);
			}
			table = new JTable(data, columns);
			JScrollPane scrollPane = new JScrollPane(table);
			add(scrollPane);
			add(table);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		JFrame GUI = new GUIFrame();
		GUI.setLocationRelativeTo(null);
		GUI.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		GUI.setVisible(true);
	}

	private int displayExitDialog()
	{
		int result = JOptionPane.showConfirmDialog(this, "Are you sure?", "Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		return result;
	}

	public void Search(String option, String text)
	{
		try
		{
			result = stmt.executeQuery("SELECT * FROM fb_customer WHERE " + option + "= '" + text + "'");
			ResultSetMetaData md = result.getMetaData();
			int columnCount = md.getColumnCount();
			columns = new Vector(columnCount);
			for(int i = 1; i <= columnCount; i++)
			{
				columns.add(md.getColumnName(i));
			}
			data = new Vector();
			data.add(columns);
			while(result.next())
			{
				row = new Vector(columnCount);
				for(int i = 1; i <= columnCount; i++)
				{
					row.add(result.getString(i));
				}
				data.add(row);
			}
			table = new JTable(data, columns);
			System.out.println("Here");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
