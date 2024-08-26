package driverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	WebDriver driver;
	String inputpath = "./FileInput/controller.xlsx";
	String outputpath = "./FileOutput/Results.xlsx";
	String Tcsheet = "MasterTestCase";
	ExtentReports reports;
	ExtentTest logger;
	public void startTest() throws Throwable
	{
		String Module_Status="";
		String Module_New="";
		//create object for excel file util class
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		//iterate all rows in tcsheet
		for(int i=1;i<=xl.rowCount(Tcsheet);i++)
		{
			if(xl.getcelldata(Tcsheet, i, 2).equalsIgnoreCase("Y"))
			{
				//read module cell for tcsheet
				String Tcmodule = xl.getcelldata(Tcsheet, i, 1);
				//define path of html
				reports = new ExtentReports("./target/Reports/"+Tcmodule+FunctionLibrary.generateDate()+".html");
				logger = reports.startTest(Tcmodule);
				//iterate all rows in Tcmodule
				for(int j=1;j<=xl.rowCount(Tcmodule);j++)
				{
					//read cell from tcmodule
					String Description = xl.getcelldata(Tcmodule, j, 0);
					String ObjectType = xl.getcelldata(Tcmodule, j, 1);
					String LocatorType =xl.getcelldata(Tcmodule, j, 2);
					String LocatorValue=xl.getcelldata(Tcmodule, j, 3);
					String TestData = xl.getcelldata(Tcmodule, j, 4);
					try {
						if(ObjectType.equalsIgnoreCase("startBrowser"))
						{
							driver = FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(LocatorType, LocatorValue, TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(LocatorType, LocatorValue, TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(LocatorType, LocatorValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("dropDownAction"))
						{
							FunctionLibrary.dropDownAction(LocatorType, LocatorValue, TestData);
							logger.log(LogStatus.INFO, Description);
							
						}
						if(ObjectType.equalsIgnoreCase("captureStock"))
						{
							FunctionLibrary.captureStock(LocatorType, LocatorValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("stocktable"))
						{
							FunctionLibrary.stocktable();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("capturesupplier"))
						{
							FunctionLibrary.capturesupplier(LocatorType, LocatorValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("suppliertable"))
						{
							FunctionLibrary.suppliertable();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("capturecustomernumber"))
						{
							FunctionLibrary.capturecustomernumber(LocatorType, LocatorValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("customertable"))
						{
							FunctionLibrary.customertable();
							logger.log(LogStatus.INFO, Description);
						}
						
						//write as pass into status cell in tcmodule sheet
						xl.setcelldata(Tcmodule, j, 5, "pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_Status="True";
					}catch(Exception e)
					{
					System.out.println(e.getMessage());
					//write fail into status cell in tc module sheet
					xl.setcelldata(Tcmodule, j, 5, "Fail", outputpath);
					logger.log(LogStatus.FAIL, Description);
					Module_Status="False";
					}
					if(Module_Status.equalsIgnoreCase("True"))
					{
						xl.setcelldata(Tcsheet, i, 3, "pass", outputpath);
						
					}
					reports.endTest(logger);
					reports.flush();
				}
				if(Module_New.equalsIgnoreCase("False"))
				{
					xl.setcelldata(Tcsheet, i, 3, "fail", outputpath);
				}
			}
			else
			{
				//write as blocked into status cell for testcases falg to N
				xl.setcelldata(Tcsheet, i, 3, "Blocked", outputpath);
			}
		}
	}

}
