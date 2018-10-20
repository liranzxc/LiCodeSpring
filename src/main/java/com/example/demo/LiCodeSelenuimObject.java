package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LiCodeSelenuimObject implements LiCodeControllerInterface, Finals {

	private ChromeOptions options;
	private WebDriver driver;
	private Actions action;

	public LiCodeSelenuimObject() {
		if (!init()) {
			throw new ExceptionInInitializerError();
		}
	}

	@Override
	public Integer upload_image_to_group_facebook_auto(PostRequest postrequest, String group_name) throws Exception {
		// TODO Auto-generated method stub

		try {

			if (!GoTo(facebook)) {

				throw new Exception("Go to Fail");
			}
			if (!Login(postrequest.getEmail(),postrequest.getPassword())) {
				throw new Exception("Login Fail");
			}

			if (!OpenGroupPage(group_name)) {
				throw new Exception("OpenGroupPage Fail");
			}

			Thread.sleep(3000);
			
			if (!Upload_group(postrequest)) {
				throw new Exception("Upload_group Fail");
			}

			
			return 200;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 404;

		}
		finally
		{
			quit();
		}

	}

	@Override
	public Integer upload_image_to_wall_auto(PostRequest postrequest) {
		// TODO Auto-generated method stub
		try {

			if (!GoTo(facebook)) {

				throw new Exception("Go to Fail");
			}
			if (!Login(postrequest.getEmail(),postrequest.getPassword())) {
				throw new Exception("Login Fail");
			}

			if (!Upload_wall(postrequest)) {
				throw new Exception("upload wall Fail");
			}

			
			return 200;
		} catch (Exception e) {
			e.printStackTrace();
			return 404;

		}
		finally
		{
			quit();
		}
	}

	@Override
	public ResponseEntity<Status> isGroupNameValid(group_valid group_valid) {
		// TODO Auto-generated method stub
		try {
			if (!GoTo(facebook)) {
				throw new Exception("Fail go to");
			}
			if (!Login(group_valid.getEmail(),group_valid.getPassword())) {
				throw new Exception("Fail Login");
			}

			if (!OpenGroupPage(group_valid.getGroupname())) {
				throw new Exception("Fail Open group page");
			}
			
			String onlyword = driver.getTitle();
			
			System.out.println("title " + driver.getTitle());
			if(onlyword.contains("("))
			{
				onlyword = onlyword.replaceAll("[\\d.]", "");
				onlyword = onlyword.replace("(", "");
				onlyword = onlyword.replace(")", "");
				onlyword = onlyword.trim();

			}
			System.out.println("****");

			System.out.println(onlyword);

			
			if(!(onlyword).equals(group_valid.getGroupname()))
			{
				throw new Exception("Fail Open group page");

			}

			return new ResponseEntity<Status>(new Status("group valid", "200"), HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
			return new ResponseEntity<Status>(new Status("group not valid", "404"), HttpStatus.OK);

		}
		finally
		{
			quit();
		}
	}


	@Override
	public boolean GoTo(String url) {
		// TODO Auto-generated method stub

		try {
			driver.get(facebook);

			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.urlToBe(facebook));

			return true;

		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		try {
			options = new ChromeOptions();
	
			// options.addArguments("test-type");
			options.addArguments("--disable-popup-blocking");
			options.addArguments("--disable-notifications");
			options.addArguments("--headless");
			
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("browser", "Chrome");
			caps.setCapability("browser_version", "62.0");
			caps.setCapability("os", "Windows");
			caps.setCapability("os_version", "10");
			caps.setCapability("resolution", "1024x768");
			caps.setCapability("browserstack.debug", true);
			caps.setCapability(ChromeOptions.CAPABILITY, options);

			System.setProperty("webdriver.chrome.driver", "src\\main\\java\\com\\example\\demo\\chromedriver.exe");

			// driver = new RemoteWebDriver(new URL(URL), caps);

			driver = new ChromeDriver(options);

			// ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
			action = new Actions(driver);
			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean clickTabTimes(int i) {
		// TODO Auto-generated method stub
		try {
			for (int j = 0; j < i; j++) {
				action.sendKeys(Keys.TAB).build().perform();
			}
			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean Upload_group(PostRequest postrequest) {
		// TODO Auto-generated method stub
		try {

			String linkTextxpath = "//a[@label='Write Post']";

			driver.findElement(By.xpath(linkTextxpath)).click();
			Thread.sleep(1000);
			WebElement uploadPhoto = driver.findElement(By.name("composer_photo[]"));

			System.out.println("Found UploadPhoto element");

			// get img from url
			URL urlInput = new URL(postrequest.getUrl());
			BufferedImage urlImage = ImageIO.read(urlInput);
			File outputfile = new File("image.jpg");
			ImageIO.write(urlImage, "jpg", outputfile);
			uploadPhoto.sendKeys(outputfile.getAbsolutePath());

			Thread.sleep(15000); // wait for upload

			WebElement span = driver.findElement(By.cssSelector("[data-testid='status-attachment-mentions-input']"));
			span.click();
			span.sendKeys(postrequest.getText());

			clickTabTimes(17); // move to post button

			action.sendKeys(Keys.RETURN).build().perform(); // post

			Thread.sleep(1000);

			return true;

		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean OpenGroupPage(String groupname) {
		// TODO Auto-generated method stub
		try {

			try {
				WebElement Searchtext = driver.findElement(By.name("q"));
				Searchtext.sendKeys(groupname);

			} catch (org.openqa.selenium.StaleElementReferenceException ex) {
				WebElement Searchtext = driver.findElement(By.name("q"));
				Searchtext.sendKeys(groupname);

			}

			Thread.sleep(2000);
			WebElement searchbutton = driver.findElement(By.xpath("//button[contains(@aria-label,'Search')]"));
			searchbutton.submit();

			Thread.sleep(2000);

			WebElement grouplink = driver.findElement(By.partialLinkText(groupname));
			grouplink.click();

			Thread.sleep(9000);

			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean quit() {
		// TODO Auto-generated method stub

		try {
			driver.close();
			driver.quit();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public boolean Upload_wall(PostRequest postrequest) throws InterruptedException {
		// TODO Auto-generated method stub
		Thread.sleep(2000);
		try {

			// open dialog

			WebElement Compose_Post = driver.findElement(By.cssSelector("[data-attachment-type='STATUS']"));

			Compose_Post.click();

			Thread.sleep(2000);

			WebElement span = driver.findElement(By.cssSelector("[data-testid='status-attachment-mentions-input']"));
			span.click();
			span.sendKeys(postrequest.getText());

			WebElement uploadPhoto = driver.findElement(By.name("composer_photo[]"));

			System.out.println("Found UploadPhoto element");

			// get img from url
			URL urlInput = new URL(postrequest.getUrl());
			BufferedImage urlImage = ImageIO.read(urlInput);
			File outputfile = new File("image.jpg");
			ImageIO.write(urlImage, "jpg", outputfile);
			uploadPhoto.sendKeys(outputfile.getAbsolutePath());

			Thread.sleep(5000); // wait for upload

			System.out.println("done upload");

			clickTabTimes(27); // move to post button

			action.sendKeys(Keys.RETURN).build().perform(); // post

			Thread.sleep(1000);

			return true;

		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean Login(String email_text , String password_text) {
		// TODO Auto-generated method stub
		try {

			WebElement email = driver.findElement(By.id("email"));

			email.sendKeys(email_text);

			action.sendKeys(Keys.TAB).build().perform();

			driver.switchTo().activeElement().sendKeys(password_text);

			action.sendKeys(Keys.TAB).build().perform();

			action.sendKeys(Keys.RETURN).build().perform();

			action.sendKeys(Keys.RETURN).build().perform();

			return true;

		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ResponseEntity<Status> isLoginValid(Credits credits) {
		// TODO Auto-generated method stub
		try {

			if (!GoTo(facebook)) {

				throw new Exception("Go to Fail");
			}
			if (!Login(credits.getEmail(),credits.getPassword())) {
				throw new Exception("Login Fail");
			}
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.urlToBe(facebook));
			
			return new ResponseEntity<Status>(new Status("login succesfully","200"),HttpStatus.OK);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<Status>(new Status("login fail","404")
					,HttpStatus.BAD_REQUEST);

		}
		finally
		{
			quit();
		}
		
	   
	}

}
