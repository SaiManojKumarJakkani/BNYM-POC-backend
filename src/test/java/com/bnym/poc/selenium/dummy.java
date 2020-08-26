package com.bnym.poc.selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.Assert;

public class dummy {
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
		Thread.sleep(2000);

	}
	@AfterClass
	public static void teardown() {
		fd.close();
	}
	@Ignore
	@Test
	public void a() throws InterruptedException {
		//WebDriverWait(fd,20).until(EC.element_to_be_clickable((By.XPATH,"//input[starts-with(@id,'undefined-undefined-E-mail-')]")))
	    //WebElement fU = fd.findElement(By.xpath("//input[@id='fileUpload']"));
	    //fU.sendKeys("C:\\Users\\JakkaniSaiVinodKumar\\Desktop\\Source\\BNYM\\Location.xlsx");
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[2]/div/button").click();
		Thread.sleep(1000);
		List<WebElement> beforeTableList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		int beforeTableSize= beforeTableList.size();
		WebDriverWait mywait= new WebDriverWait(fd,10);
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='fileUpload']")));
		WebElement fp=fd.findElementByXPath("//*[@id='fileUpload']");
		fp.sendKeys(System.getProperty("user.dir")+"\\Location.xlsx");
		Thread.sleep(2000);
		fd.findElementByXPath("/html/body/ngb-modal-window/div/div/div[3]/button[1]").click();
		fd.switchTo().alert().accept();
		Thread.sleep(4000);
		List<WebElement> afterTableList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		int afterTableSize= afterTableList.size();
		String message = fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[1]/h4").getText();
 		
		if(message.contains("Uploaded Successfully and All")) {
 			System.out.println(message);
			System.out.println(StringUtils.difference("Uploaded Successfully and All  ", message).replace(" records got inserted!!", ""));
 			int noOfRecords= Integer.valueOf((StringUtils.difference("Uploaded Successfully and All  ", message).replace(" records got inserted!!", "")));
 			Assert.assertEquals(noOfRecords,(afterTableSize-beforeTableSize));
 		}
 		else if(message.contains(" records got inserted and ")) {
 			System.out.println(message);
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
 			System.out.println(message);
 			Assert.assertEquals(0,(afterTableSize-beforeTableSize));
 		}		
	}
	@Ignore
	@Test
	public void ApprovalTest() throws InterruptedException {
		System.out.println("InApproval");
		Thread.sleep(1000);
		List<WebElement> TestCompList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[1]"));
		int x= TestCompList.size();
		if(x!=0) {
		
		System.out.println(x);
        Random rand = new Random(); 
        List<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<5;i++)
		{
	        int r = rand.nextInt(x);
			System.out.println(r);
			if((fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(r+1)+"]/td[4]").getText()).equals("IN_DRAFT")) {
				//System.out.println(fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(r+1)+"]/td[4]").getText());
	        if(l.contains(r)) {
	        	l.remove(l.indexOf(r));
	        	i=i-2;
	        	}
	        else
	        	l.add(r);
	        if(r==3)
	        	fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr[4]/td[2]").click();
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
		for(int a:l)
			System.out.println("List "+a);
		WebDriverWait mywait= new WebDriverWait(fd,30);
		mywait.until(ExpectedConditions.textToBe(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[1]/h4"),"Sucessfully moved the data for Approval!"));
		Thread.sleep(1000);
		for(int a:l)
		{	
			System.out.println(a+1);
			Thread.sleep(1000);
			String actualNormalized=fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(a+1)+"]/td[4]").getText();
			System.out.println(actualNormalized);

			Assert.assertEquals("IN_APPROVAL",actualNormalized);

		}
		}
		}

	}
	@Test
	public void e() throws InterruptedException {
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[1]/div/div[2]/a").click();
		Thread.sleep(5000);
		List<WebElement> TestList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr/td[1]"));

		System.out.println(TestList.size());
		int x= TestList.size();
		if(x!=0) {
		System.out.println("Hiiiii");
//		if(TestList.size()==0) {}
//		else {
		//WebDriverWait wait= new WebDriverWait(fd,10);
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr[1]/td[3]")));
		String approveLocation= fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr[1]/td[3]").getText();

		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[2]/div/table/tbody/tr[1]/td[5]/button[2]").click();
		Thread.sleep(500);
		fd.findElementByXPath("//*[@id='rejectionNotes']").sendKeys("Better luck next time!!");
		fd.findElementByXPath("/html/body/ngb-modal-window/div/div/div[2]/form/div[2]/button").click();
		WebDriverWait mywait= new WebDriverWait(fd,30);
		mywait.until(ExpectedConditions.textToBe(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allapproval/html/body/div/div[1]/h4"),"Sucessfully REJECTED the data!"));
		fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[1]/div/div[1]/a").click();
		Thread.sleep(500);
		List<WebElement> TestCompList=fd.findElements(By.xpath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr/td[5]"));
		System.out.println(TestCompList.size());
		for(int i=-1;i<TestCompList.size()-1;i++) {
			System.out.println(TestCompList.get(i+1).getText()+" = "+approveLocation);
			if(TestCompList.get(i+1).getText().equals(approveLocation)) {
			Assert.assertEquals("REJECTED",fd.findElementByXPath("/html/body/app-root/html/app-home/html/body/div[2]/app-allstaging/html/body/div/div[3]/div/table/tbody/tr["+(i+2)+"]/td[4]").getText());
			break;
			}
		}
		}	

	}
}