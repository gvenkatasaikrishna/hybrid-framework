package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static WebDriver driver;
	public static Properties conpro;
	
	//method for launching browser
public static WebDriver startBrowser() throws Throwable
{
	conpro = new Properties();
	conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));
	if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
	{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		
	}
	else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
	{
		driver = new FirefoxDriver();
	}
	return driver;
}
//method for launching url
public static void openUrl()
{
	driver.get(conpro.getProperty("Url"));
}
//method for wait for any webelement
public static void waitForElement(String LocatorType,String LocatorValue,String TestData)
{
	WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
	}
}
//method for any text box 
public static void typeAction(String LocatorType,String LocatorValue,String TestData)
{
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).clear();
		driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).clear();
		driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).clear();
		driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
	}
			
}
//method for any button,images,links,radio buttons
public static void clickAction(String LocatorType,String LocatorValue)
{
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).click();
	}
}
//method for validate title
public static void validateTitle(String Expected_title)
{
	String Actual_Title = driver.getTitle();
	try {
	Assert.assertEquals(Actual_Title, Expected_title,"Title is not Matching");
	}catch(AssertionError a)
	{
		System.out.println(a.getMessage());
	}
}
//method for close browser
public static void closeBrowser()
{
	driver.quit();
}
//method for date and time
public static String generateDate()
{
Date date = new Date();
DateFormat df = new SimpleDateFormat("YYYY-MM-dd hh-mm");
return df.format(date);

}
//method for listboxes
public static void dropDownAction(String LocatorType,String LocatorValue,String TestData)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
		element.selectByIndex(value);
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.name(LocatorValue)));
		element.selectByIndex(value);
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.id(LocatorValue)));
		element.selectByIndex(value);
	}
}
//method for capture stock number
public static void captureStock(String LocatorType,String LocatorValue)throws Throwable
{
	String stockNum="";
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		stockNum=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");	
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		stockNum= driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		stockNum= driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	//create note pad file into capture data folder
	FileWriter fw = new FileWriter("./CaptureData/StockNumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(stockNum);
	bw.flush();
	bw.close();
}
//method for stock table
public static void stocktable() throws Throwable
{
	//read stock number from above note pad
	FileReader fr = new FileReader("./CaptureData/StockNumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(Exp_data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Act_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr/td[8]/div/span/span")).getText();
	Reporter.log(Exp_data+"    "+Act_data,true);
	try {
		Assert.assertEquals(Act_data, Exp_data,"Stock number not matching");
	}catch(AssertionError a)
	{
		System.out.println(a.getMessage());
	}
	br.close();
}
//method for capture supplier number
public static void capturesupplier(String LocatorType, String LocatorValue)throws Throwable
{
String suppliernum="";
if(LocatorType.equalsIgnoreCase("xpath"))
{
	suppliernum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
}
if(LocatorType.equalsIgnoreCase("name"))
{
	suppliernum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
}
if(LocatorType.equalsIgnoreCase("id"))
{
	suppliernum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
}
//create note pad file into capture data folder
FileWriter fw = new FileWriter("./CaptureData/SupplierNumber.txt");
BufferedWriter bw = new BufferedWriter(fw);
bw.write(suppliernum);
bw.flush();
bw.close();
}
//method for supplier table
public static void suppliertable() throws Throwable
{
	//read supplier number from above note pad
	FileReader fr = new FileReader("./CaptureData/SupplierNumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String exp_data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(exp_data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Actual_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
	Reporter.log(exp_data+"  "+Actual_data,true);
	try {
		Assert.assertEquals(Actual_data, exp_data, "Supplier number is not matching");
	}catch(AssertionError a)
	{
		System.out.println(a.getMessage());
	}
	br.close();
}
//method for capture customer number
public static void capturecustomernumber(String LocatorType,String Locatorvalue) throws Throwable
{
	String Customernum="";
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		Customernum = driver.findElement(By.xpath(Locatorvalue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		Customernum = driver.findElement(By.id(Locatorvalue)).getAttribute("value");
	}
	//create a notepad file into capture data folder
	FileWriter fw = new FileWriter("./CaptureData/customerNumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(Customernum);
	bw.flush();
	bw.close();	
}
public static void customertable() throws Throwable
{
	//read supplier number from above note pad
	FileReader fr = new FileReader("./CaptureData/Customernumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String exp_data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("Search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("Search-textbox"))).sendKeys(exp_data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Actual_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
	Reporter.log(exp_data+"  "+Actual_data,true);
	try {
		Assert.assertEquals(Actual_data, exp_data, "Customer number is not matching");
	}catch(AssertionError a)
	{
		System.out.println(a.getMessage());
	}
	br.close();
}
}
