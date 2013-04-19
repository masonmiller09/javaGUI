import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.rmi.NoSuchObjectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;


public class PdfGenerator
{
    private static ArrayList<Object> auto;


    public static void populateSDIForm(
        Object[] customerInfo,
        Object[] agencyInfo )
    {
        String pdfTemplate = "Editable_SDI_Form.pdf";
        String newFile = "C:\\Windows\\Temp\\Feast\\populated_SDI_Form.pdf";
        PdfReader pdfReader;
        try
        {
            pdfReader = new PdfReader( pdfTemplate );
            PdfStamper pdfStamper = new PdfStamper( pdfReader,
                new FileOutputStream( newFile ) );
            AcroFields pdfFormFields = pdfStamper.getAcroFields();
            pdfFormFields.setField( "Agency_Name", agencyInfo[1].toString() );
            pdfFormFields.setField( "Account_Number", agencyInfo[0].toString() );
            pdfFormFields.setField( "Applicant_Name", customerInfo[3] + " "
                + customerInfo[2] );
            pdfFormFields.setField( "Street_Address", (String)customerInfo[4] );
            pdfFormFields.setField( "Apartment_Number", (String)customerInfo[5]
                + "" );
            pdfFormFields.setField( "City", (String)customerInfo[6] );
            pdfFormFields.setField( "Zipcode", customerInfo[7].toString() );
            pdfFormFields.setField( "Phone_Number", customerInfo[8].toString() );
            pdfFormFields.setField( "Num_Children", customerInfo[9].toString() );
            pdfFormFields.setField( "Num_Adults", customerInfo[10].toString() );
            pdfFormFields.setField( "Num_Seniors", customerInfo[11].toString() );
            Integer n = (Integer)customerInfo[9] + (Integer)customerInfo[10]
                + (Integer)customerInfo[11];
            pdfFormFields.setField( "Total_Household", n.toString() );
            if ( (Boolean)customerInfo[12] == false )
            {
                pdfFormFields.setField( "Food_Stamps", "0" );
            }
            else
            {
                pdfFormFields.setField( "Food_Stamps", "Yes" );
            }
            if ( (Boolean)customerInfo[13] == false )
            {
                pdfFormFields.setField( "TANF", "0" );
            }
            else
            {
                pdfFormFields.setField( "TANF", "Yes" );
            }
            if ( (Boolean)customerInfo[14] == false )
            {
                pdfFormFields.setField( "SSI", "0" );
            }
            else
            {
                pdfFormFields.setField( "SSI", "Yes" );
            }
            if ( (Boolean)customerInfo[15] == false )
            {
                pdfFormFields.setField( "Medicaid", "0" );
            }
            else
            {
                pdfFormFields.setField( "Medicaid", "Yes" );
            }
            if ( (Boolean)customerInfo[17] == true )
            {
                pdfFormFields.setField( "Weekly_Income",
                    customerInfo[16].toString() );
            }
            else if ( (Boolean)customerInfo[18] == true )
            {
                pdfFormFields.setField( "Monthly_Income",
                    customerInfo[16].toString() );
            }
            else
            {
                pdfFormFields.setField( "Annual_Income",
                    customerInfo[16].toString() );
            }
            pdfFormFields.setField( "Signature_1", "" );
            pdfFormFields.setField( "Date_1", "" );
            pdfFormFields.setField( "Signature_2", "" );
            pdfFormFields.setField( "Date_2", "" );
            pdfFormFields.setField( "Signature_3", "" );
            pdfFormFields.setField( "Date_3", "" );
            pdfFormFields.setField( "Authorized", customerInfo[3] + " "
                + customerInfo[2] );
            pdfStamper.setFormFlattening( false );
            pdfStamper.close();
            File pdfFile = new File( "C:\\Windows\\Temp\\Feast\\populated_SDI_Form.pdf" );
            if ( Desktop.isDesktopSupported() )
            {
                Desktop.getDesktop().open( pdfFile );
            }
            else
            {
                System.out.println( "Awt Desktop is not supported!" );
            }
        }
        catch ( DocumentException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog( null,
                "Please close the open PDF Form \"populated_SDI_Form.pdf\" and try again.\nPlease use the PDF Generator button to make multiple PDFs.",
                "Generate PDF Error",
                JOptionPane.INFORMATION_MESSAGE );
            e.printStackTrace();
        }
    }


    public static void populateSDIForm(
        Object[] customerInfo,
        Object[] agencyInfo,
        int population )
    {
        String pdfTemplate = "Editable_SDI_Form.pdf";
        String newFile = "C:\\Windows\\Temp\\Feast\\populated_SDI_Form_"
            + population + ".pdf";
        PdfReader pdfReader;
        try
        {
            pdfReader = new PdfReader( pdfTemplate );
            PdfStamper pdfStamper = new PdfStamper( pdfReader,
                new FileOutputStream( newFile ) );
            AcroFields pdfFormFields = pdfStamper.getAcroFields();
            pdfFormFields.setField( "Agency_Name", agencyInfo[1].toString() );
            pdfFormFields.setField( "Account_Number", agencyInfo[0].toString() );
            pdfFormFields.setField( "Applicant_Name", customerInfo[3] + " "
                + customerInfo[2] );
            pdfFormFields.setField( "Street_Address", (String)customerInfo[4] );
            pdfFormFields.setField( "Apartment_Number", (String)customerInfo[5]
                + "" );
            pdfFormFields.setField( "City", (String)customerInfo[6] );
            pdfFormFields.setField( "Zipcode", customerInfo[7].toString() );
            pdfFormFields.setField( "Phone_Number", customerInfo[8].toString() );
            pdfFormFields.setField( "Num_Children", customerInfo[9].toString() );
            pdfFormFields.setField( "Num_Adults", customerInfo[10].toString() );
            pdfFormFields.setField( "Num_Seniors", customerInfo[11].toString() );
            Integer n = (Integer)customerInfo[9] + (Integer)customerInfo[10]
                + (Integer)customerInfo[11];
            pdfFormFields.setField( "Total_Household", n.toString() );
            if ( (Boolean)customerInfo[12] == false )
            {
                pdfFormFields.setField( "Food_Stamps", "0" );
            }
            else
            {
                pdfFormFields.setField( "Food_Stamps", "Yes" );
            }
            if ( (Boolean)customerInfo[13] == false )
            {
                pdfFormFields.setField( "TANF", "0" );
            }
            else
            {
                pdfFormFields.setField( "TANF", "Yes" );
            }
            if ( (Boolean)customerInfo[14] == false )
            {
                pdfFormFields.setField( "SSI", "0" );
            }
            else
            {
                pdfFormFields.setField( "SSI", "Yes" );
            }
            if ( (Boolean)customerInfo[15] == false )
            {
                pdfFormFields.setField( "Medicaid", "0" );
            }
            else
            {
                pdfFormFields.setField( "Medicaid", "Yes" );
            }
            if ( (Boolean)customerInfo[17] == true )
            {
                pdfFormFields.setField( "Weekly_Income",
                    customerInfo[16].toString() );
            }
            else if ( (Boolean)customerInfo[18] == true )
            {
                pdfFormFields.setField( "Monthly_Income",
                    customerInfo[16].toString() );
            }
            else
            {
                pdfFormFields.setField( "Annual_Income",
                    customerInfo[16].toString() );
            }
            pdfFormFields.setField( "Signature_1", "" );
            pdfFormFields.setField( "Date_1", "" );
            pdfFormFields.setField( "Signature_2", "" );
            pdfFormFields.setField( "Date_2", "" );
            pdfFormFields.setField( "Signature_3", "" );
            pdfFormFields.setField( "Date_3", "" );
            pdfFormFields.setField( "Authorized", customerInfo[3] + " "
                + customerInfo[2] );
            pdfStamper.setFormFlattening( false );
            pdfStamper.close();
            System.out.println( "PRINTED" );
        }
        catch ( DocumentException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void mergePdfFiles(int population)
    {
        List<String> list = new ArrayList<String>();
        try {
            // Source pdfs
            while(population > 0) {
                String fileName = "C:\\Windows\\Temp\\Feast\\populated_SDI_Form_" + (population-1) + ".pdf";
                list.add(fileName);
                population--;
            }
            System.out.println("List size: "+list.size()+" Population: "+population);
            // Resulting pdf
            OutputStream out = new FileOutputStream(new File("C:\\Windows\\Temp\\Feast\\result.pdf"));

            try
            {
                doMerge(list, out);
            }
            catch ( DocumentException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void doMerge(
        List<String> list,
        OutputStream outputStream ) throws DocumentException, IOException
    {
        Document document = new Document();
        PdfCopy writer = new PdfCopy( document, outputStream );
        document.open();
        PdfReader ReadInputPDF;
        int number_of_pages;
        
        for(int i= 0; i < list.size(); i++)
        {
            ReadInputPDF = new PdfReader(list.get( i ));
            number_of_pages = ReadInputPDF.getNumberOfPages();
            for(int page = 0; page < number_of_pages; )
            {
                writer.addPage( writer.getImportedPage( ReadInputPDF, ++page ) );
            }
        }
        
        File pdfFile = new File("C:\\Windows\\Temp\\Feast\\result.pdf");
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(pdfFile);
        } else {
            System.out.println("Awt Desktop is not supported!");
        }
        outputStream.flush();
        document.close();
        outputStream.close();
    }
}
