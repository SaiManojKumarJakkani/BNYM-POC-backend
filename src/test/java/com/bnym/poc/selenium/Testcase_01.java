package com.bnym.poc.selenium;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.DependsOn;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Random; 
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import junit.framework.Assert;

@TestMethodOrder(OrderAnnotation.class)
public class Testcase_01 {
	int c=0;
	static ChromeDriver fd=null;
	@BeforeClass
	public static void startAll(){
		System.setProperty("webdriver.chrome.driver","C:\\Users\\JakkaniSaiVinodKumar\\Desktop\\Source\\BNYM\\Final\\BNYM-POC-backend\\Drivers\\chromedriver.exe");
		fd=new ChromeDriver();
		fd.manage().window().maximize();
		fd.get("http://localhost:4200/");
		fd.findElementByXPath("/html/body/app-root/html/body/nav/a[3]").click();
	}
	@Before
	public void setup() throws InterruptedException {
		Thread.sleep(1000);

	}
	@AfterClass
	public static void teardown() {
		fd.close();
	}
	
	//Upload File
	@Test
	public void a() throws InterruptedException {
		System.out.println("Uploading a file");
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[2]/div/button").click();
		Thread.sleep(1000);
		List<WebElement> beforeTableList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		int beforeTableSize= beforeTableList.size();
		WebDriverWait mywait= new WebDriverWait(fd,10);
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='fileUpload']")));
		WebElement fp=fd.findElementByXPath("//*[@id='fileUpload']");
		//fp.sendKeys(System.getProperty("user.dir")+"\\Location.xlsx");
		fp.sendKeys(System.getProperty("user.dir")+"\\Location - Copy.xlsx");
		Thread.sleep(2000);
		fd.findElementByXPath("/html/body/ngb-modal-window/div/div/div[3]/button[1]").click();
		fd.switchTo().alert().accept();
		Thread.sleep(4000);
		List<WebElement> afterTableList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		int afterTableSize= afterTableList.size();
		String message = fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[1]/h4").getText();
 		
		if(message.contains("Uploaded Successfully and All")) {
 			//System.out.println(message);
			//System.out.println(StringUtils.difference("Uploaded Successfully and All  ", message).replace(" records got inserted!!", ""));
 			int noOfRecords= Integer.valueOf((StringUtils.difference("Uploaded Successfully and All  ", message).replace(" records got inserted!!", "")));
 			Assert.assertEquals(noOfRecords,(afterTableSize-beforeTableSize));
 		}
 		else if(message.contains(" records got inserted and ")) {
 			//System.out.println(message);
			char[] chars = message.toCharArray();
			StringBuilder sb = new StringBuilder();
			for(char c : chars){
			    if(Character.isDigit(c)){
			        sb.append(c);
			    }
			    else 
			      	break;
			    }
		 	int noOfRecords= Integer.valueOf(sb.toString());
  			Assert.assertEquals(noOfRecords,(afterTableSize-beforeTableSize));
 		}
 		else {
 			//System.out.println(message);
 			Assert.assertEquals(0,(afterTableSize-beforeTableSize));
 		}		
	}
	
	//Create a record
	@Test()
	public void b() throws InterruptedException {
		System.out.println("Creating a record");
		String actualLocation=" Reserve Bank of India";
		//String actualLocation="State Bank Of India";

		String actualNormalized="Bank of Delhi";
		//String actualNormalized="State Bank Of Hyderabad";

		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[2]/button").click();
		Thread.sleep(500);
		fd.findElementByXPath("//*[@id='locationName']").sendKeys(actualLocation);
		fd.findElementByXPath("//*[@id='normalizedLocation']").sendKeys(actualNormalized);
		fd.findElementByXPath("/html/body/ngb-modal-window/div/div/div[2]/form/div[3]/button").submit();
		WebDriverWait mywait= new WebDriverWait(fd,10);
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[1]/h4")));
		Thread.sleep(1000);
		if(fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[1]/h4").equals("Successfully saved the data!")) {
		List<WebElement> TestCompList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		int tableSize= TestCompList.size();
		
		//System.out.println(tableSize);
		String expectedLocation=fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(tableSize)+"]/td[5]").getText();
		String expectedNormalized=fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(tableSize)+"]/td[6]").getText();
		//System.out.println(expectedLocation+" = "+actualLocation);
		//System.out.println(expectedNormalized+" = "+actualNormalized);

		Assert.assertEquals(expectedLocation,actualLocation);   
		Assert.assertEquals(expectedNormalized,actualNormalized);   
		}
		else {
			//System.out.println("...................");
			Assert.assertEquals(true, true);
		}

	}
	
	//Edit File
	@Test
	public void c() throws InterruptedException {
		System.out.println("Editing a record");
		String expectedNormalized="Bank Of India";
		//String expectedNormalized="State Bank Of India";
		Thread.sleep(1000);
		List<WebElement> TestCompList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		int x= TestCompList.size();
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(x)+"]/td[3]").click();
		Thread.sleep(1000);
		fd.findElementByXPath("//*[@id='normalized']").clear();
		fd.findElementByXPath("//*[@id='normalized']").sendKeys(expectedNormalized);
		fd.findElementByXPath("/html/body/ngb-modal-window/div/div/div[2]/form/div[3]/button").submit();
		WebDriverWait mywait= new WebDriverWait(fd,10);
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[1]/h4")));
		Thread.sleep(500);
		if(fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[1]/h4").equals("Successfully saved the data!")) {
		String actualNormalized= fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(x)+"]/td[6]").getText();
		Assert.assertEquals(expectedNormalized,actualNormalized);
		}
	}
	
	//Some data to In Approval
	@Test
	public void d() throws InterruptedException {
		System.out.println("Moving some records to InApproval");
		Thread.sleep(2000);
		List<WebElement> TestCompList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		int x= TestCompList.size();
		if(x!=0) {
		//System.out.println(x);
        Random rand = new Random(); 
        List<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<5;i++)
		{
	        int r = rand.nextInt(x);
			//System.out.println(r);
			if((fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(r+1)+"]/td[4]").getText()).equals("IN_DRAFT")) {
	        if(l.contains(r)) {
	        	l.remove(l.indexOf(r));
	        	i=i-2;
	        	}
	        else
	        	l.add(r);
	        if(r==4)
	        	fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr[5]/td[2]").click();
	        else
	        	fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(r+1)+"]/td[2]").click();
			}
			Thread.sleep(2000);
		}
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/div/button").click();
		if(l.size()!=0) {
		Thread.sleep(1000);
		fd.switchTo().alert().accept();
		Thread.sleep(2000);
		//for(int a:l)
			//System.out.println("List "+a);
		WebDriverWait mywait= new WebDriverWait(fd,30);
		mywait.until(ExpectedConditions.textToBe(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[1]/h4"),"Sucessfully moved the data for Approval!"));
		Thread.sleep(1000);
		for(int a:l)
		{	
			//System.out.println(a+1);
			Thread.sleep(1000);
			String actualNormalized=fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(a+1)+"]/td[4]").getText();
			//System.out.println(actualNormalized);

			Assert.assertEquals("IN_APPROVAL",actualNormalized);

		}
		}
		}

	}
	
	//Approve a record
	@Test
	public void e() throws InterruptedException {
		System.out.println("Approving a record");
		
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[1]/div/div[2]/a").click();
		Thread.sleep(2000);
		List<WebElement> TestList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr/td[1]"));
		//System.out.println(TestList.size());
		int x= TestList.size();
		if(x!=0) {
		String approveLocation= fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr[1]/td[3]").getText();

		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr[1]/td[5]/button[1]").click();
		fd.switchTo().alert().accept();
		WebDriverWait mywait= new WebDriverWait(fd,30);
		mywait.until(ExpectedConditions.textToBe(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[1]/h4"),"Sucessfully APPROVED the data!"));
		Thread.sleep(1000);
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[1]/div/div[1]/a").click();
		Thread.sleep(500);
		List<WebElement> TestCompList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[5]"));
		//System.out.println(TestCompList.size());
		for(int i=-1;i<TestCompList.size()-1;i++) {
			//System.out.println(TestCompList.get(i+1).getText()+" = "+approveLocation);
			if(TestCompList.get(i+1).getText().equals(approveLocation)) {
			Assert.assertEquals("APPROVED",fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(i+2)+"]/td[4]").getText());
			break;
			}
		}
		}	
	}
	
	//Reject a record
	@Test
	public void f() throws InterruptedException {
		System.out.println("Reject a record");
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[1]/div/div[2]/a").click();
		Thread.sleep(2000);
		List<WebElement> TestList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr/td[1]"));
		//System.out.println(TestList.size());
		int x= TestList.size();
		if(x!=0) {
		String approveLocation= fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr[1]/td[3]").getText();

		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr[1]/td[5]/button[2]").click();
		Thread.sleep(500);
		fd.findElementByXPath("//*[@id='rejectionNotes']").sendKeys("Better luck next time!!");
		fd.findElementByXPath("/html/body/ngb-modal-window/div/div/div[2]/form/div[2]/button").click();
		WebDriverWait mywait= new WebDriverWait(fd,30);
		mywait.until(ExpectedConditions.textToBe(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[1]/h4"),"Sucessfully REJECTED the data!"));
		Thread.sleep(1000);
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[1]/div/div[1]/a").click();
		Thread.sleep(500);
		List<WebElement> TestCompList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[5]"));
		//System.out.println(TestCompList.size());
		for(int i=-1;i<TestCompList.size()-1;i++) {
			//System.out.println(TestCompList.get(i+1).getText()+" = "+approveLocation);
			if(TestCompList.get(i+1).getText().equals(approveLocation)) {
			Assert.assertEquals("REJECTED",fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(i+2)+"]/td[4]").getText());
			break;
			}
		}
		}	
	}
	
	//Approve all records
	@Test
	public void g() throws InterruptedException {
		System.out.println("Approving all the records");
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[1]/div/div[2]/a").click();
		Thread.sleep(2000);
		List<WebElement> TestList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr/td[1]"));
		int x= TestList.size();
		if(x!=0) {
        Random rand = new Random(); 
        List<String> l=new ArrayList<String>();
		for(int i=0;i<3;i++)
		{
	        int r = rand.nextInt(x);
			System.out.println(r);
	        if(l.contains(fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr["+(r+1)+"]/td[3]").getText()))
	        	l.remove(l.indexOf(fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr["+(r+1)+"]/td[3]").getText()));
	        else
	        	l.add(fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr["+(r+1)+"]/td[3]").getText());
			fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr["+(r+1)+"]/td[2]").click();
		}
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[3]/button[1]").click();
		fd.switchTo().alert().accept();
		WebDriverWait mywait= new WebDriverWait(fd,30);
		mywait.until(ExpectedConditions.textToBe(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[1]/h4"),"Sucessfully APPROVED the data!"));
		Thread.sleep(1000);
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[1]/div/div[1]/a").click();
		Thread.sleep(500);
		List<WebElement> TestCompList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		//System.out.println(TestCompList.size());
		for(String a:l)
		{	
			//System.out.println("Check = "+a);
			for(int i=1;i<=TestCompList.size();i++) {
				String b=fd.findElement(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+i+"]/td[5]")).getText();
				if((b).equals(a)) {
					String status=fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+i+"]/td[4]").getText();
					Assert.assertEquals("APPROVED",status);
					break;
				}
			}

		}
		}

	}
}