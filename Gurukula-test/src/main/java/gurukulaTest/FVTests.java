package gurukulaTest;

import static org.testng.AssertJUnit.assertEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import baseclass.CommonMethods;

public class FVTests {

	public static WebDriver driver;
	public static String branchIdCreated;

	public static WebDriver initDriver() {
		System.setProperty("webdriver.gecko.driver",
				"geckodriver-v0.24.0-win64/geckodriver.exe");

		driver = new FirefoxDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		return driver;
	}

	@BeforeMethod
	// Test(priority = 1)
	@Parameters({ "appUrl", "username", "password" })
	public void loginToApp(String appUrl, String username, String password) {
		String url = appUrl;
		String uname = username;
		String pswd = password;
		// Launch and login with given/valid credentials
		baseclass.CommonMethods.login(url, uname, pswd);
	}

	@Test(priority = 1, description = "Login the app with valid credentials ")
	public void validateUserLoggedIn() {
		String actualMsg = driver
				.findElement(
						(By.xpath("//div[@class='alert alert-success ng-scope ng-binding']")))
				.getText();
		System.out.println("actualMsg  " + actualMsg);
		String expectedMsg = "You are logged in as user \"admin\".";
		System.out.println("expectedMsg  " + expectedMsg);
		// Validate the login message
		assertEquals(expectedMsg, actualMsg);

	}

	@Test(priority = 2, description = "Login the app with invalid credentials ")
	@Parameters({ "appUrl", "username", "password2" })
	public void validateUserNotLoggedIn(String appUrl, String username,
			String password2) {
		baseclass.CommonMethods.logout();
		driver.quit();
		String url = appUrl;
		String uname = username;
		String pswd = password2;
		baseclass.CommonMethods.login(url, uname, pswd);
		String expectedMsg = "Authentication failed! Please check your credentials and try again.";
		CommonMethods.verifyError(expectedMsg);

	}

	@Test(priority = 3, description = "Register new user ")
	@Parameters({ "appUrl", "usernamenew", "emailnew", "passwordnew" })
	public void registerUser(String appUrl, String usernamenew,
			String emailnew, String passwordnew) {
		// baseclass.CommonMethods.launchApp(appUrl);
		baseclass.CommonMethods.logout();
		WebElement element = driver.findElement(By
				.linkText("Register a new account"));
		element.click();
		WebElement name = driver.findElement(By.name("login"));
		WebElement email = driver.findElement(By.name("email"));
		WebElement pswd = driver.findElement(By.name("password"));
		WebElement confirmPswd = driver.findElement(By.name("confirmPassword"));
		baseclass.CommonMethods.enterData(usernamenew, name); // Enter Name
		baseclass.CommonMethods.enterData(emailnew, email); // Enter Email
		baseclass.CommonMethods.enterData(passwordnew, pswd); // Enter password
		baseclass.CommonMethods.enterData(passwordnew, confirmPswd); // Confirm
		// password
		driver.findElement(By.cssSelector(".btn")).click();
		String expectedMsg = "User Registered";
		CommonMethods.verifyError(expectedMsg);

	}

	@Test(priority = 4, description = "Create a new branch  ")
	@Parameters({ "branchName", "branchCode" })
	public static void createBranch(String branchName, String branchCode) {
		// Navigate to Entities--> branch menu
		baseclass.CommonMethods.navEntitiesBranch();
		// Create a new branch
		driver.findElement(
				By.xpath("//span[@class='ng-scope' and contains(.,'Create a new Branch')]"))
				.click();
		WebElement name = driver.findElement(By.name("name"));
		WebElement code = driver.findElement(By.name("code"));
		baseclass.CommonMethods.enterData(branchName, name);
		baseclass.CommonMethods.enterData(branchCode, code);
		driver.findElement(By.xpath("//span[text()='Save']")).click();
		String branch = driver.findElement(By.xpath("//table/tbody/tr/td[2]"))
				.getText();
		System.out.println("branchName " + branch);

		branchIdCreated = driver.findElement(
				By.xpath("//table/tbody/tr[1]/td[1]/a[@class='ng-binding']"))
				.getText();
		System.out.println("branchIdCreated " + branchIdCreated);
		// Validate branch exists in the list
		assertEquals(branchName, branch);
	}

	@Test(priority = 5, description = "View branch  ")
	public void viewBranch() throws InterruptedException {
		String branchName1;
		// Navigate to Entities--> branch menu
		baseclass.CommonMethods.navEntitiesBranch();
		try {
			WebElement element = driver.findElement(By
					.xpath("//table/tbody/tr[1]/td[4]/button[1]"));
			baseclass.CommonMethods.jsScriptClick(element);
		} catch (StaleElementReferenceException e) {

			WebElement element = driver.findElement(By
					.xpath("//table/tbody/tr[1]/td[4]/button[1]"));
			baseclass.CommonMethods.jsScriptClick(element);

		}
		// Navigate back to validate

		WebElement element1 = driver.findElement(By
				.xpath("//button[@class='btn btn-info']/span[2]"));

		Actions action = new Actions(driver);
		WebElement btn = driver.findElement(By
				.xpath("//button[@class='btn btn-info']/span[2]"));
		action.moveToElement(btn).click().build().perform();

	}

	@Test(priority = 6)
	public void querybranch() {
		baseclass.CommonMethods.navEntitiesBranch();
		WebElement element = driver.findElement(By.id("searchQuery"));
		element.click();
		element.sendKeys(branchIdCreated);
		String branchId = driver.findElement(
				By.xpath("//table/tbody/tr[1]/td[1]/a[@class='ng-binding']"))
				.getText();
		System.out.println("branchId " + branchId);
		driver.findElement(
				By.cssSelector(".btn-info:nth-child(2) > span:nth-child(2)"))
				.click();
		// validate the id shows up in the list
		assertEquals(branchId, branchIdCreated);
	}

	@Test(priority = 7)
	public void deleteBranch() {
		// Navigate to Entities--> branch menu
		baseclass.CommonMethods.navEntitiesBranch();
		// get the first branch id
		String branchId = driver.findElement(
				By.xpath("//table/tbody/tr[1]/td[1]/a[@class='ng-binding']"))
				.getText();
		System.out.println("branch to be deleted " + branchId);
		WebElement element = driver.findElement(By
				.xpath("//table/tbody/tr[1]/td[4]/button[3]"));
		baseclass.CommonMethods.jsScriptClick(element);
		System.out.println("click on delete"); // Delete first branch
		// confirm dialog

		try {

			WebElement element2 = driver
					.findElement(By
							.xpath("//button/span[2][@class='ng-scope' and text()='Delete']"));
			baseclass.CommonMethods.jsScriptClick(element2);
			System.out.println("click on delete in confirm dialogue");
		} catch (StaleElementReferenceException e) {

			WebElement element2 = driver
					.findElement(By
							.xpath("//button/span[2][@class='ng-scope' and text()='Delete']"));
			baseclass.CommonMethods.jsScriptClick(element2);
			System.out.println("click on delete in confirm dialogue");
		}
	}

	@Test(priority = 8, description = "Create a new Staff  ")
	@Parameters({ "staffName", "newSTaffBrannch", "branchCode", "appUrl",
			"username", "password" })
	public void createStaff(String newSTaffBrannch, String staffName,
			String branchCode, String appUrl, String username, String password) {

		// Navigate to Entities--> branch menu
		baseclass.CommonMethods.navEntitiesBranch();
		// Create a new branch
		driver.findElement(
				By.xpath("//span[@class='ng-scope' and contains(.,'Create a new Branch')]"))
				.click();
		WebElement name = driver.findElement(By.name("name"));
		WebElement code = driver.findElement(By.name("code"));
		baseclass.CommonMethods.enterData(newSTaffBrannch, name);
		baseclass.CommonMethods.enterData(branchCode, code);
		driver.findElement(By.xpath("//span[text()='Save']")).click();
		driver.quit();
		loginToApp(appUrl, username, password);

		// Navigate to Entities--> branch menu
		baseclass.CommonMethods.navEntitiesStaff();

		// Create a new branch
		driver.findElement(
				By.xpath("//span[@class='ng-scope' and contains(.,'Create a new Staff')]"))
				.click();
		WebElement nameStaff = driver.findElement(By.name("name"));
		WebElement branch = driver.findElement(By.name("related_branch"));
		baseclass.CommonMethods.enterData(staffName, nameStaff);
		new Select(
				driver.findElement(By
						.xpath("//select[@class='form-control ng-pristine ng-untouched ng-valid']")))
				.selectByVisibleText(newSTaffBrannch);
		driver.findElement(By.xpath("//span[text()='Save']")).click();
		String staffCreated = driver.findElement(
				By.xpath("//table/tbody/tr/td[2]")).getText();
		System.out.println("staffName " + staffCreated);

		String staffNameCreated = driver.findElement(
				By.xpath("//table/tbody/tr[1]/td[2][@class='ng-binding']"))
				.getText();
		System.out.println("staffNameCreated " + staffNameCreated);
		// Validate Staff branch exists in the list
		assertEquals(staffNameCreated, staffName);

	}

	@Test(priority = 9, description = "View the new Staff  ")
	public void viewStaffCreated() throws InterruptedException {

		String branchName1;
		// Navigate to Entities--> branch menu
		baseclass.CommonMethods.navEntitiesStaff();
		try {
			WebElement element = driver.findElement(By
					.xpath("//table/tbody/tr[1]/td[4]/button[1]"));
			baseclass.CommonMethods.jsScriptClick(element);
		} catch (StaleElementReferenceException e) {

			WebElement element = driver.findElement(By
					.xpath("//table/tbody/tr[1]/td[4]/button[1]"));
			baseclass.CommonMethods.jsScriptClick(element);

		}
		// Navigate back

		WebElement element1 = driver.findElement(By
				.xpath("//button[@class='btn btn-info']/span[2]"));

		Actions action = new Actions(driver);
		WebElement btn = driver.findElement(By
				.xpath("//button[@class='btn btn-info']/span[2]"));
		action.moveToElement(btn).click().build().perform();

	}

	@Test(priority = 10, description = "Edit a  Staff  ")
	public void editStaff() {

		// Navigate to Entities--> branch menu
		baseclass.CommonMethods.navEntitiesStaff();
		WebElement element = driver.findElement(By
				.xpath("//table/tbody/tr[1]/td[4]/button[2]"));
		System.out.println("line1");

		JavascriptExecutor jse2 = (JavascriptExecutor) driver;
		baseclass.CommonMethods.jsScriptClick(element);
		// edit branch

		WebElement nameStaff = driver.findElement(By.name("name"));
		System.out.println("line7");
		jse2.executeScript("arguments[0].scrollIntoView()", nameStaff);
		baseclass.CommonMethods.enterData("myNewstaff", nameStaff);
		driver.findElement(By.cssSelector(".modal-footer > .btn-primary"))
				.click();

	}

	@Test(priority = 11, description = "Delete a  Staff  ")
	public void deleteStaff() {
		// Navigate to Entities--> branch menu
		baseclass.CommonMethods.navEntitiesBranch();
		// get the first staff id
		String staffId = driver.findElement(
				By.xpath("//table/tbody/tr[1]/td[1]/a[@class='ng-binding']"))
				.getText();
		System.out.println("Staff to be deleted " + staffId);
		WebElement element = driver.findElement(By
				.xpath("//table/tbody/tr[1]/td[4]/button[3]"));
		baseclass.CommonMethods.jsScriptClick(element);
		System.out.println("click on delete"); // Delete first branch
		// confirm dialog

		try {
			WebElement element2 = driver
					.findElement(By
							.xpath("//button/span[2][@class='ng-scope' and text()='Delete']"));
			baseclass.CommonMethods.jsScriptClick(element2);
			System.out.println("click on delete in confirm dialogue");
		} catch (StaleElementReferenceException e) {

			WebElement element2 = driver
					.findElement(By
							.xpath("//button/span[2][@class='ng-scope' and text()='Delete']"));
			baseclass.CommonMethods.jsScriptClick(element2);
			System.out.println("click on delete in confirm dialogue");
		}
	}

	@Test(priority = 12)
	@Parameters({ "newSTaffBrannch" })
	public void queryStaff(String newSTaffBrannch) {
		baseclass.CommonMethods.navEntitiesStaff();
		WebElement element = driver.findElement(By.id("searchQuery"));
		element.click();
		element.sendKeys(newSTaffBrannch);
		String staff = driver.findElement(
				By.xpath("//table/tbody/tr[1]/td[2][@class='ng-binding']"))
				.getText();
		System.out.println("staff " + staff);
		driver.findElement(By.xpath("//button[contains(.,' Search a Staff')]"))
				.click();
		// validate the id shows up in the list
		assertEquals(staff, newSTaffBrannch);

	}

	@DataProvider
	public Object[][] sendDataBranchName() {
		return new Object[][] { { "branchnameeeeeeeeeeee" }, { "smal" },
				{ "branch123" }, { "@branch" }, { " " } };
	}

	@Test(priority = 13, dataProvider = "sendDataBranchName", description = "Validate input data conditions")
	public void validateInputBranchName(String branchName) {
		Boolean Result = false;
		baseclass.CommonMethods.navEntitiesBranch();
		driver.findElement(
				By.xpath("//span[@class='ng-scope' and contains(.,'Create a new Branch')]"))
				.click();
		WebElement name = driver.findElement(By.name("name"));
		baseclass.CommonMethods.enterData(branchName, name);
		WebElement code = driver.findElement(By.name("code"));
		baseclass.CommonMethods.enterData("CODE123", code);
		try {
			driver.findElement(By.xpath("//span[text()='Save']")).click();
			;
		} catch (ElementClickInterceptedException e) {
			Result = true;
			Assert.assertTrue(Result);
		}

	}
	
	@DataProvider
	public Object[][] sendDataBranchCode() {
		return new Object[][] { { "nodecode1234" }, { "n123" }, { "@N" },
				{ " " } };
	}

	@Test(priority = 14, dataProvider = "sendDataBranchCode", description = "Validate input data conditions")
	public void validateInputBranchCode(String branchCode) {
		Boolean Result = false;
		baseclass.CommonMethods.navEntitiesBranch();
		driver.findElement(
				By.xpath("//span[@class='ng-scope' and contains(.,'Create a new Branch')]"))
				.click();
		WebElement code = driver.findElement(By.name("code"));
		baseclass.CommonMethods.enterData(branchCode, code);
		try {
			driver.findElement(By.xpath("//span[text()='Save']")).click();
			;
		} catch (ElementClickInterceptedException e) {
			Result = true;
			Assert.assertTrue(Result);
		}

	}

	@DataProvider
	public Object[][] sendDataNodeName() {
		return new Object[][] {
				{ "StaffNameaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" },
				{ "Staff123" }, { "@staffName" }, { " " } };
	}

	@Test(priority = 15, dataProvider = "sendDataNodeName", description = "Validate input data conditions")
	public void validateInputNodeName(String staffname) {
		Boolean Result = false;
		baseclass.CommonMethods.navEntitiesStaff();
		driver.findElement(
				By.xpath("//span[@class='ng-scope' and contains(.,'Create a new Staff')]"))
				.click();
		WebElement nameStaff = driver.findElement(By.name("name"));
		baseclass.CommonMethods.enterData(staffname, nameStaff);
		try {
			driver.findElement(By.xpath("//span[text()='Save']")).click();
			;
		} catch (ElementClickInterceptedException e) {
			Result = true;
			Assert.assertTrue(Result);
		}

	}

	@Test(priority = 16, description = "Validate forgot password works with valid email")
	@Parameters({ "validEmail" })
	public void forgetPswdValidMail(String validEmail) {
		baseclass.CommonMethods.logout();
		driver.findElement(By.linkText("login")).click();
		driver.findElement(By.xpath("//a[@class='ng-scope']")).click();
		driver.findElement(By.name("email")).sendKeys(validEmail);
		driver.findElement(
				By.xpath("//button[@class='btn btn-primary ng-scope']"))
				.click();
		String expectedMsg = "Enter the e-mail address you used to register";
		String actualMsg = driver.findElement(
				(By.xpath("//p[@class='ng-scope']"))).getText();
		System.out.println("actualMsg " + actualMsg);
		Assert.assertNotEquals(expectedMsg, actualMsg);
	}

	@Test(priority = 17, description = "Validate forgot password error with Invalid email")
	@Parameters({ "notvalidEmail" })
	public void forgetPswdNotValidMail(String notvalidEmail) {
		baseclass.CommonMethods.logout();
		driver.findElement(By.linkText("login")).click();
		driver.findElement(By.xpath("//a[@class='ng-scope']")).click();
		driver.findElement(By.name("email")).sendKeys(notvalidEmail);
		CommonMethods.save();
		String expectedMsg = "E-Mail address isn't registered! Please check and try again";
		CommonMethods.verifyError(expectedMsg);
	}

	@Test(priority = 18, description = "Validate account settings can be changed ")
	public static void changeAccountPaswd() {
		driver.findElement(
				By.xpath("//span[@class='hidden-tablet ng-scope' and contains(.,'Account')]"))
				.click();
		driver.findElement(By.linkText("Settings")).click();
		WebElement email = driver.findElement(By.name("email"));
		baseclass.CommonMethods.enterData("newadmin@localhost", email);
		CommonMethods.save();
		// "An error has occurred! Settings could not be saved.";
		String expectedMsg = "Settings saved.";
		CommonMethods.verifyError(expectedMsg);
	}

	@AfterMethod
	public void afterTest() {
		driver.quit();

	}
}
