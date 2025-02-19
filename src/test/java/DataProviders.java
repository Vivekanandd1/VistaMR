import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviders {
	
	DataFormatter formatter = new DataFormatter();
	
	@Test(dataProvider = "driver")
	public void runner(String name,String email,String Case) {
		System.out.println("My name is "+name+" and my email is "+email+" with case id "+ Case);
	}
	
	
	
	@DataProvider(name="driver")
	public Object[][] DataExcel() throws IOException {
		FileInputStream fis = new FileInputStream("C:\\Users\\Admin\\Desktop\\Dataset.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);
		int rowcount = sheet.getPhysicalNumberOfRows();
		XSSFRow row = sheet.getRow(0);
		int colcount = row.getLastCellNum();
		System.out.println(colcount);
		System.out.println(rowcount);
		//-1
		Object data[][] = new Object[rowcount-1][rowcount];
		for(int i=0;i<rowcount-1;i++) {
			System.out.println("Outerloop");
			row = sheet.getRow(i+1);
			for (int j=0;j<colcount;j++) {
			System.out.println(row.getCell(j));
			
			XSSFCell cell= row.getCell(j);
			data[i][j] = formatter.formatCellValue(cell);
			
			}
			
		}
		return data;
	}

}
