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
        Object[] agencyInfo,
        Object[] agentInfo )
    {
        String pdfTemplate = "Editable_SDI_Form.pdf";
        String newFile = "populated_SDI_Form.pdf";
        PdfReader pdfReader;
        try
        {
            pdfReader = new PdfReader( pdfTemplate );
            PdfStamper pdfStamper = new PdfStamper( pdfReader,
                new FileOutputStream( newFile ) );
            AcroFields pdfFormFields = pdfStamper.getAcroFields();
            pdfFormFields.setField( "Agency_Name", agencyInfo[3].toString() );
            pdfFormFields.setField( "Account_Number", agencyInfo[2].toString() );
            pdfFormFields.setField( "Agency_Rep", agentInfo[4] + " "
                + agentInfo[3] );
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
            pdfFormFields.setField( "Applicant_authorizing", agentInfo[4] + " "
                + agentInfo[3] );
            pdfFormFields.setField( "Authorized", customerInfo[3] + " "
                + customerInfo[2] );
            pdfStamper.setFormFlattening( false );
            pdfStamper.close();
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


    public static void populateSDIForm(
        Object[] customerInfo,
        Object[] agencyInfo,
        Object[] agentInfo,
        int population )
    {
        String pdfTemplate = "Editable_SDI_Form.pdf";
        String newFile = "populated_SDI_Form_" + population + ".pdf";
        PdfReader pdfReader;
        try
        {
            pdfReader = new PdfReader( pdfTemplate );
            PdfStamper pdfStamper = new PdfStamper( pdfReader,
                new FileOutputStream( newFile ) );
            AcroFields pdfFormFields = pdfStamper.getAcroFields();
            pdfFormFields.setField( "Agency_Name", agencyInfo[3].toString() );
            pdfFormFields.setField( "Account_Number", agencyInfo[2].toString() );
            pdfFormFields.setField( "Agency_Rep", agentInfo[4] + " "
                + agentInfo[3] );
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
            pdfFormFields.setField( "Applicant_authorizing", agentInfo[4] + " "
                + agentInfo[3] );
            pdfFormFields.setField( "Authorized", customerInfo[3] + " "
                + customerInfo[2] );
            pdfStamper.setFormFlattening( false );
            pdfStamper.close();
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
	
	public static void mergePdfFiles(int population) {
		List<InputStream> list = new ArrayList<InputStream>();
		try {
            // Source pdfs
			for (int i = 0; i < population; i++) {
	            list.add(new FileInputStream(new File("populated_SDI_Form_"+i+".pdf")));
			}
			System.out.println("List size: "+list.size()+" Population: "+population);
            // Resulting pdf
            OutputStream out = new FileOutputStream(new File("result.pdf"));

            doMerge(list, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private static void doMerge(List<InputStream> list, OutputStream out) {
		
		try {
			Document document = null;
	        PdfCopy writer = null;
	       
	        for (InputStream in : list) {
	            PdfReader reader = new PdfReader(in);
	            if (in == list.get(0)) {
	            	document = new Document(reader.getPageSizeWithRotation(1));
	            	writer = new PdfCopy(document, out);
	            	document.open();
	            }
	            System.out.println("I'm in");
	            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
	                document.newPage();
	                //import the page from source pdf
	                PdfImportedPage page = writer.getImportedPage(reader, i);
	                //add the page to the destination pdf
	                writer.addPage(page);
	            }
	            System.out.println("About to get shit");
	            writer.copyAcroForm(reader);
	            in.close();
	        }
	        File pdfFile = new File("result.pdf");
            if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(pdfFile);
			} else {
				System.out.println("Awt Desktop is not supported!");
			}
	        out.flush();
	        document.close();
	        out.close();
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
