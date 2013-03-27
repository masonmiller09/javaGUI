import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.NoSuchObjectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


public class PdfGenerator {
	private static ArrayList<Object>auto;
	public static void populateSDIForm(Object[] customerInfo, Object[] agencyInfo, Object[] agentInfo)
    {
        String pdfTemplate = "Editable_SDI_Form.pdf";
        String newFile = "populated_SDI_Form.pdf";
        PdfReader pdfReader;
		try {
			pdfReader = new PdfReader(pdfTemplate);
	        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(newFile));
	        AcroFields pdfFormFields = pdfStamper.getAcroFields();
	        pdfFormFields.setField("Agency_Name", agencyInfo[3].toString()); 
	        pdfFormFields.setField("Account_Number", agencyInfo[2].toString());
            pdfFormFields.setField("Agency_Rep", agentInfo[4] + " " + agentInfo[3]);
            pdfFormFields.setField("Applicant_Name", customerInfo[3]+" "+customerInfo[2]);
            pdfFormFields.setField("Street_Address", (String) customerInfo[4]);
            pdfFormFields.setField("Apartment_Number", (String) customerInfo[5]+"");
            pdfFormFields.setField("City", (String) customerInfo[6]);
            pdfFormFields.setField("Zipcode", customerInfo[7].toString());
            pdfFormFields.setField("Phone_Number", customerInfo[8].toString());
            pdfFormFields.setField("Num_Children", customerInfo[9].toString());
            pdfFormFields.setField("Num_Adults", customerInfo[10].toString());
            pdfFormFields.setField("Num_Seniors", customerInfo[11].toString());
			Integer n = (Integer) customerInfo[9] + (Integer) customerInfo[10] + (Integer) customerInfo[11];
            pdfFormFields.setField("Total_Household", n.toString());
            if ((Boolean) customerInfo[12] == false) {
                pdfFormFields.setField("Food_Stamps", "0"); 
            }
            else {
                pdfFormFields.setField("Food_Stamps", "Yes"); 
            }
            if ((Boolean) customerInfo[13] == false) {
    	        pdfFormFields.setField("TANF", "0");
            }
            else {
    	        pdfFormFields.setField("TANF", "Yes");
            }
            if ((Boolean) customerInfo[14] == false) {
                pdfFormFields.setField("SSI", "0");
            }
            else {
                pdfFormFields.setField("SSI", "Yes");
            }
            if ((Boolean) customerInfo[15] == false) {
                pdfFormFields.setField("Medicaid", "0");
            }
            else {
                pdfFormFields.setField("Medicaid", "Yes");
            }
            if ((Boolean) customerInfo[17] == true) {
            	pdfFormFields.setField("Weekly_Income", customerInfo[16].toString());
            }
            else if ((Boolean) customerInfo[18] == true) {
            	pdfFormFields.setField("Monthly_Income", customerInfo[16].toString());
            }
            else {
            	pdfFormFields.setField("Annual_Income", customerInfo[16].toString());
            }
            pdfFormFields.setField("Signature_1", "");
            pdfFormFields.setField("Date_1", "");
            pdfFormFields.setField("Signature_2", "");
            pdfFormFields.setField("Date_2", "");
            pdfFormFields.setField("Signature_3", "");
            pdfFormFields.setField("Date_3", "");
            pdfFormFields.setField("Applicant_authorizing", agentInfo[4] + " " + agentInfo[3]);
            pdfFormFields.setField("Authorized", customerInfo[3]+" "+customerInfo[2]); 
            pdfStamper.setFormFlattening(false);
            pdfStamper.close();	 
		} 
		catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
    }
	
	public static void populateSDIForm(Object[] customerInfo, Object[] agencyInfo, Object[] agentInfo, int population)
    {
        String pdfTemplate = "Editable_SDI_Form.pdf";
        String newFile = "populated_SDI_Form_"+population+".pdf";
        PdfReader pdfReader;
		try {
			pdfReader = new PdfReader(pdfTemplate);
	        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(newFile));
	        AcroFields pdfFormFields = pdfStamper.getAcroFields();
	        pdfFormFields.setField("Agency_Name", agencyInfo[3].toString()); 
	        pdfFormFields.setField("Account_Number", agencyInfo[2].toString());
            pdfFormFields.setField("Agency_Rep", agentInfo[4] + " " + agentInfo[3]);
            pdfFormFields.setField("Applicant_Name", customerInfo[2]+" "+customerInfo[3]);
            pdfFormFields.setField("Street_Address", (String) customerInfo[4]);
            pdfFormFields.setField("Apartment_Number", (String) customerInfo[5]+"");
            pdfFormFields.setField("City", (String) customerInfo[6]);
            pdfFormFields.setField("Zipcode", customerInfo[7].toString());
            pdfFormFields.setField("Phone_Number", customerInfo[8].toString());
            pdfFormFields.setField("Num_Children", customerInfo[9].toString());
            pdfFormFields.setField("Num_Adults", customerInfo[10].toString());
            pdfFormFields.setField("Num_Seniors", customerInfo[11].toString());
			Integer n = (Integer) customerInfo[9] + (Integer) customerInfo[10] + (Integer) customerInfo[11];
            pdfFormFields.setField("Total_Household", n.toString());
            if ((Boolean) customerInfo[12] == false) {
                pdfFormFields.setField("Food_Stamps", "0"); 
            }
            else {
                pdfFormFields.setField("Food_Stamps", "Yes"); 
            }
            if ((Boolean) customerInfo[13] == false) {
    	        pdfFormFields.setField("TANF", "0");
            }
            else {
    	        pdfFormFields.setField("TANF", "Yes");
            }
            if ((Boolean) customerInfo[14] == false) {
                pdfFormFields.setField("SSI", "0");
            }
            else {
                pdfFormFields.setField("SSI", "Yes");
            }
            if ((Boolean) customerInfo[15] == false) {
                pdfFormFields.setField("Medicaid", "0");
            }
            else {
                pdfFormFields.setField("Medicaid", "Yes");
            }
            if ((Boolean) customerInfo[17] == true) {
            	pdfFormFields.setField("Weekly_Income", customerInfo[16].toString());
            }
            else if ((Boolean) customerInfo[18] == true) {
            	pdfFormFields.setField("Monthly_Income", customerInfo[16].toString());
            }
            else {
            	pdfFormFields.setField("Annual_Income", customerInfo[16].toString());
            }
            pdfFormFields.setField("Signature_1", "");
            pdfFormFields.setField("Date_1", "");
            pdfFormFields.setField("Signature_2", "");
            pdfFormFields.setField("Date_2", "");
            pdfFormFields.setField("Signature_3", "");
            pdfFormFields.setField("Date_3", "");
            pdfFormFields.setField("Applicant_authorizing", agentInfo[4] + " " + agentInfo[3]);
            pdfFormFields.setField("Authorized", customerInfo[3]+" "+customerInfo[2]); 
            pdfStamper.setFormFlattening(false);
            pdfStamper.close();	 
		} 
		catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
    }
	
	public static void autoComplete(final JTextField Field, String section) {
		Connection conn = null;
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
        final JComboBox Box = new JComboBox(model) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
		String request = new String(
				"SELECT * FROM feastdb.fb_customer WHERE "+ section +" LIKE  \""
						+ Field.getText() + "%\"");
		try {
			String url = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
			String dbName = "feastdb";
			String driver = "com.mysql.jdbc.Driver";
			String userName = "root";
			String password = "lampa";
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			System.out.println("Connected to the database");
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(request);
			if (!result.isBeforeFirst()) {
				throw new NoSuchObjectException(new String(
						"The object being searched for :" + Field.getText() + " is not in the database"));
			}
			while (result.next()) {
				if (section.equals("Last_Name")) {
					String test = result.getString(1);
					auto.add(result.getString(2));
					String test2 = result.getString(3);
					String test3 = result.getString(4);
					String test4 = result.getString(5);
					String test5 = result.getString(6);
					int test6 = result.getInt(7);
					BigDecimal test7 = result.getBigDecimal(8);
					int test8 = result.getInt(9);
					int test9 = result.getInt(10);
					int test10 = result.getInt(11);
					Boolean test11 = result.getBoolean(12);
					Boolean test12 = result.getBoolean(13);
					Boolean test13 = result.getBoolean(14);
					Boolean test14 = result.getBoolean(15);
					int test15 = result.getInt(16);
					Boolean test16 = result.getBoolean(17);
					Boolean test17 = result.getBoolean(18);
					Boolean test18 = result.getBoolean(19);
					Boolean test19 = result.getBoolean(20);
				}
				if (section.equals("First_Name")){
					auto.add(result.getString(3));
				}
				if (section.equals("Street_Address")){
					auto.add(result.getString(4));
				}
				if (section.equals("Apartment_Number")){
					auto.add(result.getString(5));
				}
				if (section.equals("City")){
					auto.add(result.getString(6));
				}
				if (section.equals("Zip_Code")) {
					auto.add(result.getInt(7));
				}
			}
			for (int i = 0; i < auto.size(); i++) {
				System.out.println(auto.get(i));
			}
			Field.getDocument().addDocumentListener(new DocumentListener() {
	            public void insertUpdate(DocumentEvent e) {
	                updateList();
	            }

	            public void removeUpdate(DocumentEvent e) {
	                updateList();
	            }

	            public void changedUpdate(DocumentEvent e) {
	                updateList();
	            }

	            private void updateList() {
	                setAdjusting(Box, true);
	                model.removeAllElements();
	                String input = Field.getText();
	                if (!input.isEmpty()) {
	                    for (Object item : auto) {
	                        if (((String) item).toLowerCase().startsWith(input.toLowerCase())) {
	                            model.addElement(item);
	                        }
	                    }
	                }
	                Box.setPopupVisible(model.getSize() > 0);
	                setAdjusting(Box, false);
	            }
	        });
			Field.setLayout(new BorderLayout());
			Field.add(Box, BorderLayout.SOUTH);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static boolean isAdjusting(JComboBox cbInput) {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }

    private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }
}
