import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;


public class permLog
{
    final String directory = "C:/Windows/Temp/Feast";

    final String permLog = "feastLOG.txt";

    File file;

    File dir;

    BufferedWriter bw;


    public void LogFileExist()
    {
        dir = new File( directory );
        System.out.println( "directory check: " + dir.mkdir() );
        file = new File( dir, permLog );
        try
        {
            if ( !file.exists() )
            {
                file.createNewFile();
                System.out.println( "File check: " + file );
                bw = new BufferedWriter( new FileWriter( file, true ) );
                bw.write( "Permanent FEAST Database Log File: " + "\r\n"
                    + "\r\n" );
                bw.close();
            }
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void logQuery( String q )
    {
        Date date = new Date();
        try
        {
            bw = new BufferedWriter( new FileWriter( file, true ) );
            bw.write( "Query executed on: " + date.toString() + "\r\n" );
            bw.write( q + "\r\n" + "\r\n" );
            bw.close();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
