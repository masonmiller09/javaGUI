import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException; 
import java.util.Date;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class queryQue
{

    final String directory = "C:/Windows/Temp/Feast";

    final String que = "DB_Update_Que.txt";

    File file;

    File dir;

    BufferedWriter bw;
    
    
    public void queFileExist()
    {
        dir = new File( directory );
        System.out.println( "directory check: " + dir.mkdir() );
        file = new File( dir, que );
        try
        {
            if ( !file.exists() )
            {
                file.createNewFile();
                System.out.println( "File check: " + file );
                bw = new BufferedWriter( new FileWriter( file, true ) );
                bw.close();
            }
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }
    
    public void addToQue( String q )
    {
        try
        {
           // String[] pieces = q.split( " " );
            bw = new BufferedWriter( new FileWriter( file, true ) );
            bw.write( q + "\r\n");
            bw.close();
            
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public void clearQue() throws IOException
    {
        bw = new BufferedWriter( new FileWriter( file, false ) );
        bw.write("");
        bw.close();
    }
    
}
