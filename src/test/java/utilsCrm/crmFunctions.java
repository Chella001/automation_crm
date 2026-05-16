package utilsCrm;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
//import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import crm.crmDriver;

public class crmFunctions extends crmDriver {

	private WebDriverWait wait;

	public crmFunctions() {
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	// ---------- CLICK ----------
	public void clickById(String id) {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
			try {
				element.click();
				System.out.println(id + " has been clicked");
			} catch (Exception normalClickFailed) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			}
		} catch (Exception e) {
			System.out.println(id + " is not visible");
		}
	}

	public void clickByXpath(String xpath) {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
			try {
				element.click();
				System.out.println("Xpath has been clicked");
			} catch (Exception normalClickFailed) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			}
		} catch (Exception e) {
			System.out.println("Xpath is not visible: " + xpath);
		}
	}

	// ---------- ENTER TEXT ----------
	public void enterTextById(String id, String text) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
			element.clear();
			element.sendKeys(text);
		} catch (Exception e) {
			System.out.println("Text entry failed for ID: " + id);
		}
	}

	public void enterTextByXpath(String xpath, String text) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			element.clear();
			element.sendKeys(text);
		} catch (Exception e) {
			System.out.println("Text entry failed for Xpath: " + xpath);
		}
	}

	public WebElement getElementById(String id) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
		} catch (Exception e) {
			System.out.println(id + " is not visible");
			return null;
		}
	}

	public String getSuccessMessage(String xpath) {
		try {
			WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			return message.getText();
		} catch (Exception e) {
			System.out.println("Success message path not found: " + xpath);
			return null;
		}
	}

	public void enterTextByCss(String cssSelector, String text) {
		try {
			WebElement element = getElementByCss(cssSelector);
			element.clear();
			element.sendKeys(text);
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);
			System.out.println(text + " has been entered");

		} catch (Exception e) {
			System.out.println("Text entry failed for CSS: " + cssSelector);
		}
	}

	private WebElement getElementByCss(String cssSelector) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
		} catch (Exception e) {
			System.out.println("Element not found for CSS: " + cssSelector);
			return null;
		}
	}

	// Scroll
	public void scrollIntoViewByXpath(String xpath) {
		WebElement element = driver.findElement(By.xpath(xpath));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"arguments[0].scrollIntoView({block:'nearest', inline:'center'});",
				element);
	}

	public static boolean isValidMobile(String mobile) {
		if (mobile == null)
			return false;
		return mobile.trim().matches("^[6-9][0-9]{9}$");
	}

	// Amount
	public static boolean isValidAmount(String amount) {
		if (amount == null)
			return false;

		amount = amount.trim(); // Fix: remove unwanted spaces

		return amount.matches("\\d+(\\.\\d{1,2})?");
	}

	// PAN Validation
	public static boolean isValidPan(String pan) {
		if (pan == null)
			return false;
		return pan.trim().matches("^[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}$");
	}


	// ================= EMPLOYEE CODE VALIDATION =================
	public static boolean isValidEmployeeCode(String empCode) {
		if (empCode == null)
			return false;
		return empCode.trim().length() >= 3;
	}

	public void clearAndType(By locator, String value) {
		WebElement element = driver.findElement(locator);
		element.clear();
		element.sendKeys(value);
	}

	// DownArrow and Select

	public void selectAutoSuggestionById(String id) {

		WebElement element = driver.findElement(By.id(id));

		element.sendKeys(Keys.BACK_SPACE);
		// Thread.sleep(1000);
		element.sendKeys(Keys.ARROW_DOWN);
		element.sendKeys(Keys.ARROW_DOWN);
		element.sendKeys(Keys.ENTER);
	}

	// DropDown functions

	public void selectByVisibleText(By locator, String value) {

		if (value == null || value.trim().isEmpty()) {
			System.out.println("Dropdown value is empty");
			return;
		}

		WebElement dropdown = driver.findElement(locator);
		Select select = new Select(dropdown);
		select.selectByVisibleText(value.trim());
	}

	// ---------- ALERT HANDLING ----------
	public String handleAlertIfPresent() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());

			String alertText = alert.getText();
			System.out.println("Alert Message: " + alertText);

			alert.accept(); // Click OK button

			return alertText;

		} catch (TimeoutException e) {
			// No alert appeared within 5 seconds
			return null;
		} catch (NoAlertPresentException e) {
			return null;
		}
	}

	// ---------- SCREENSHOT ----------
	public static void captureScreenshot(String testName) {
		try {
			File folder = new File("C:\\Users\\Chella\\OneDrive\\Pictures\\Screenshots");
			if (!folder.exists())
				folder.mkdirs();

			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			// Capture screenshot immediately to get current state
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String destPath = folder + "\\" + testName + "_" + timestamp + ".png";

			// Save to disk asynchronously so the test doesn't lag
			new Thread(() -> {
				try {
					FileHandler.copy(src, new File(destPath));
					System.out.println("Screenshot saved (background): " + destPath);
				} catch (Exception e) {
					System.out.println("Async save failed: " + e.getMessage());
				}
			}).start();

		} catch (Exception e) {
			System.out.println("Failed to capture screenshot: " + e.getMessage());
		}
	}
	// ================= MULTIPLE PAYMENTS =================

	public void addMultipleCards(String names, String types, String devices, String numbers, String amounts,
			String approvals) throws InterruptedException {
		String[] cardNames = names.split(",");
		String[] cardTypes = types.split(",");
		String[] cardDevices = devices.split(",");
		String[] cardNos = numbers.split(",");
		String[] cardAmts = amounts.split(",");
		String[] appNos = approvals.split(",");

		int count = cardAmts.length;

		for (int i = 0; i < count; i++) {
			clickById("card_detail_modal");
			Thread.sleep(1500);

			if (i < cardNames.length)
				selectByVisibleText(By.cssSelector("select.card_name"), cardNames[i]);
			if (i < cardTypes.length)
				selectByVisibleText(By.cssSelector("select.card_type"), cardTypes[i]);
			if (i < cardDevices.length)
				selectByVisibleText(By.cssSelector("select.form-control.id_device"), cardDevices[i]);
			if (i < cardNos.length)
				enterTextByXpath("//*[@id=\"card_details\"]/tbody/tr/td[4]/input", cardNos[i]);
			if (i < cardAmts.length)
				enterTextByXpath("//*[@id=\"card_details\"]/tbody/tr/td[5]/input", cardAmts[i]);
			if (i < appNos.length)
				enterTextByXpath("//*[@id=\"cardref_no_0\"]", appNos[i]);

			clickById("add_newcc");
			Thread.sleep(1000);
			handleAlertIfPresent();
		}
	}

	public void addMultipleNetBanking(String types, String devices, String dates, String refs, String amounts)
			throws InterruptedException {
		String[] bankTypes = types.split(",");
		String[] bankDevices = devices.split(",");
		String[] bankDates = dates.split(",");
		String[] bankRefs = refs.split(",");
		String[] bankAmts = amounts.split(",");

		int count = bankAmts.length;

		for (int i = 0; i < count; i++) {
			clickById("netbankmodal");
			Thread.sleep(1500);

			if (i < bankTypes.length)
				selectByVisibleText(By.cssSelector("select.nb_type"), bankTypes[i]);
			if (i < bankDevices.length)
				selectByVisibleText(By.cssSelector("select.id_device"), bankDevices[i]);
			if (i < bankDates.length)
				clearAndType(By.cssSelector("input[data-date-format='yyyy-mm-dd']"), bankDates[i]);
			if (i < bankRefs.length)
				enterTextById("nbref_no_0", bankRefs[i]);
			if (i < bankAmts.length)
				enterTextByXpath("//*[@id=\"net_bankdetails\"]/tr/td[6]/input", bankAmts[i]);

			clickByXpath("//*[@id=\"add_newnb\"]");
			Thread.sleep(1000);
			handleAlertIfPresent();
		}
	}

	public void addMultipleCheques(String dates, String banks, String branches, String numbers, String ifscs,
			String amounts) throws InterruptedException {
		String[] chqDates = dates.split(",");
		String[] chqBanks = banks.split(",");
		String[] chqBranches = branches.split(",");
		String[] chqNos = numbers.split(",");
		String[] chqIfscs = ifscs.split(",");
		String[] chqAmts = amounts.split(",");

		int count = chqAmts.length;

		for (int i = 0; i < count; i++) {
			clickById("cheque_modal");
			Thread.sleep(1500);

			if (i < chqDates.length)
				enterTextByXpath("//input[@class='cheque_date']", chqDates[i]);
			if (i < chqBanks.length)
				selectByVisibleText(By.cssSelector("select.bank_name"), chqBanks[i]);
			if (i < chqBranches.length)
				enterTextByXpath("(//table[@id='chq_details']//input)[2]", chqBranches[i]);
			if (i < chqNos.length)
				enterTextById("chq_no_0", chqNos[i]);
			if (i < chqIfscs.length)
				enterTextByXpath("//input[@name='cheque_details[bank_IFSC][]']", chqIfscs[i]);
			if (i < chqAmts.length)
				enterTextByXpath("//input[@name='cheque_details[payment_amount][]']", chqAmts[i]);

			clickById("add_newchq");
			Thread.sleep(1000);
			handleAlertIfPresent();
		}
	}
}
