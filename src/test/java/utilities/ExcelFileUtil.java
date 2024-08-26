package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelFileUtil {
	XSSFWorkbook wb;
	//constructor for reading excel path
	public ExcelFileUtil(String Excelpath) throws Throwable
	{
		FileInputStream fi = new FileInputStream(Excelpath);
		wb = new XSSFWorkbook(fi);
	}
	//method for counting no of rows in a sheet
	public int rowCount(String sheetName)
	{
		return wb.getSheet(sheetName).getLastRowNum();
	}
	//method for reading data from cell
	public String getcelldata(String sheetName,int row,int cell)
	{
		String data="";
		if(wb.getSheet(sheetName).getRow(row).getCell(cell).getCellType()==CellType.NUMERIC)
		{
			int celldata = (int) wb.getSheet(sheetName).getRow(row).getCell(cell).getNumericCellValue();
			data = String.valueOf(celldata);
		}
		else {
			data = wb.getSheet(sheetName).getRow(row).getCell(cell).getStringCellValue();
			
		}
		return data;
	}
	//method for writing status into another wb
	public void setcelldata(String sheetName,int row,int cell,String status,String WriteExcel) throws Throwable
	{
		//get sheet from wb
		XSSFSheet ws = wb.getSheet(sheetName);
		//get row from sheet
		XSSFRow rownum = ws.getRow(row);
		//create cell in row
		XSSFCell cellnum = rownum.createCell(cell);
		//write status in cell
		cellnum.setCellValue(status);
		if(status.equalsIgnoreCase("pass"))
		{
			XSSFCellStyle style = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.GREEN.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(cell).setCellStyle(style);
		}
		else if(status.equalsIgnoreCase("fail"))
		{
			XSSFCellStyle style = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.RED.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(cell).setCellStyle(style);
		}
		else if(status.equalsIgnoreCase("blocked"))
		{
			XSSFCellStyle style = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.BLUE.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(cell).setCellStyle(style);
		}
		FileOutputStream fo = new FileOutputStream(WriteExcel);
		wb.write(fo);
		
	}
	

}
