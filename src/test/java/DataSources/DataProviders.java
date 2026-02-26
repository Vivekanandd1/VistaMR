package DataSources;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import com.github.javafaker.File;

public class DataProviders {

	String[] cred;
	static String host = "localhost";
	static String port = "3306";

	DataFormatter formatter = new DataFormatter();

	@DataProvider(name = "driver")
	public Object[][] DataExcel() throws IOException {
		 try (InputStream is = getClass()
			        .getClassLoader()
			        .getResourceAsStream("DataSet.xlsx");

			XSSFWorkbook wb = new XSSFWorkbook(is);){

		        XSSFSheet sheet = wb.getSheetAt(0);
		        int rowcount = sheet.getPhysicalNumberOfRows();
		        int colcount = sheet.getRow(0).getLastCellNum();

		        System.out.println("Rowcount: " + rowcount);
		        System.out.println("Colcount: " + colcount);

		        Object data[][] = new Object[rowcount - 1][colcount];
		        DataFormatter formatter = new DataFormatter();

		        for (int i = 1; i < rowcount; i++) {
		            XSSFRow row = sheet.getRow(i);
		            for (int j = 0; j < colcount; j++) {
		                XSSFCell cell = row.getCell(j);
		                data[i - 1][j] = formatter.formatCellValue(cell);
		            }
		        }
		        return data;
		    }
	}

	@DataProvider(name = "CredsDB")
	public Object[][] DataDB() throws IOException, SQLException {
		List<Object[]> dataList = new ArrayList<>();
		Connection con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/UserCred", "root", "Root");
		Statement s = con.createStatement();
		ResultSet rs = s.executeQuery("select * from UserInfo where name='Automation Request'");
		while (rs.next()) {
			//dataList.add(new String[] { rs.getString("Email"), rs.getString("Password"), rs.getString("Name") });
			/*Name is getting from faker library*/
			dataList.add(new String[] { rs.getString("Email"), rs.getString("Password") });
		}
		 return dataList.toArray(new Object[0][]);
	}

}
