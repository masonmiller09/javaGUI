import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class StatusBar extends JLabel
{	
	boolean isConnected = false;
	private String urlR = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
	private String dbNameR = "feastdb";
	private String driverR = "com.mysql.jdbc.Driver";
	private String userNameR = "root";
	private String passwordR = "lampa";
	private Connection connR = null;
	private Connection connL = null;
	
	public StatusBar(Connection connR, Connection connL)
	{
		super();
		super.setPreferredSize(new Dimension(100, 16));
		isConnected = canConnect();
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask()
		{
			public void run()
			{
				if(canConnect())
				{
					setMessage("Online");
				}
				else
				{
					setMessage("Local");
				}
			}
		}, 0, 10000L);
	}
	
	public void setMessage(String message)
	{
		//if()
		setText(" " + message);
	}
	
	public void playMessage(String message)
	{
		setText(message);
		Timer t = new Timer();
		t.schedule(new TimerTask()
		{
			public void run()
			{
				if(canConnect())
				{
					setMessage("Online");
				}
				else
				{
					setMessage("Local");
				}
			}
		}, 5000);
	}
	public boolean canConnect()
	{
		isConnected = false;
		try
		{
			Class.forName(driverR).newInstance();
			connR = DriverManager.getConnection(urlR + dbNameR, userNameR, passwordR);
			isConnected = true;
		}
		catch(Exception e)
		{
			// e.printStackTrace();
		}
		return isConnected;
	}
}
