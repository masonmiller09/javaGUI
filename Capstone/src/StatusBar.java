import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class StatusBar extends JLabel
{	
	boolean isUpdating = false;
	private Connection connL = null;
	
	public StatusBar(Connection connL, boolean update)
	{
		super();
		super.setPreferredSize(new Dimension(100, 16));
		isUpdating = update;
	}
	
	public void setMessage(String message)
	{
		//if()
		setText(" " + message);
	}
	
	public void refresh(boolean update)
	{
	    isUpdating = update;
	    if(isUpdating)
        {
            setMessage("Updating...");
        }
        else
        {
            setMessage("Local");
        }
	}
	
	public void playMessage(String message)
	{
		setText(message);
		Timer t = new Timer();
		t.schedule(new TimerTask()
		{
			public void run()
			{
				if(isUpdating)
				{
					setMessage("Updating");
				}
				else
				{
					setMessage("Local");
				}
			}
		}, 5000);
	}
}
