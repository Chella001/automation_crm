package crm;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilsCrm.crmFunctions;
import utilsCrm.crmUtils;

public class createAccount extends crmDriver {

	crmUtils exUtil = new crmUtils();
	crmFunctions util = new crmFunctions();
	private boolean navigatedToCreateAccount = false;

	// ================= CELL VALUE =================
	private String getCellValue(Row row, int cellIndex) {
		if (row == null || row.getCell(cellIndex) == null) {
			return "";
		}
		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(row.getCell(cellIndex)).trim();
	}

	// ================= MAIN METHOD =================
	public boolean createAccount(Workbook workbook, int lastRow) throws InterruptedException {

		Sheet sheet = workbook.getSheet("createAccount");
		if (sheet == null) {
			sheet = workbook.getSheet("CreateAccount");
		}
		boolean allSuccess = true;
		int col = 0;

		for (int rw = 1; rw <= lastRow; rw++) {

			boolean finalStatus = false;

			Row row = sheet.getRow(rw);
			if (row == null)
				continue;

			String TC_ID = getCellValue(row, 1);
			String Branch = getCellValue(row, 2);
			String Mobile = getCellValue(row, 3);
			String Scheme = getCellValue(row, 4);
			String referredBy = getCellValue(row, 5);
			String referralCode = getCellValue(row, 6);
			String Employee = getCellValue(row, 7);
			String Gift = getCellValue(row, 8);
			String giftName = getCellValue(row, 9);
			String amount = getCellValue(row, 14);
			String panNo = getCellValue(row, 15);
			String panFront = getCellValue(row, 16);
			String panBack = getCellValue(row, 17);
			String aadharNo = getCellValue(row, 18);
			String aadharFront = getCellValue(row, 19);
			String aadharBack = getCellValue(row, 20);
			String imagePath = "C:\\Users\\Chella\\eclipse-workspace\\mavenCrm\\Test_Images";

			int quantity = 0;
			try {
				String qtyValue = getCellValue(row, 10);
				quantity = qtyValue.isEmpty() ? 0 : Integer.parseInt(qtyValue);
			} catch (Exception e) {
				quantity = 0;
			}

			String barCode = getCellValue(row, 11);
			String prizeDetails = getCellValue(row, 12);
			String Comments = getCellValue(row, 13);

			test = extent.createTest(TC_ID, "Create Account Test");
			test.info("Create Account Test Started for TC_ID : " + TC_ID);

			try {

				// ================= NAVIGATION (ONCE) =================
				if (!navigatedToCreateAccount) {

					util.clickByXpath("/html/body/div/header/nav/a");

					WebElement sidebar = driver.findElement(By.cssSelector("div.slimScrollDiv > section.sidebar"));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = 1500;", sidebar);

					util.clickByXpath("/html/body/div[1]/aside/div/section/ul/li[39]/a");
					util.clickByXpath("/html/body/div[1]/aside/div/section/ul/li[39]/ul/li[2]/a");

					navigatedToCreateAccount = true;
				}

				Thread.sleep(2000);

				// ================= FORM START =================
				util.clickByXpath("//*[@id=\"select2-branch_select-container\"]");
				util.enterTextByXpath("/html/body/span/span/span[1]/input", Branch);
				driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

				if (crmFunctions.isValidMobile(Mobile)) {
					util.enterTextById("mobile_number", Mobile);
				} else {
					System.out.println(Mobile + " Invalid number");
					test.warning("Invalid Customer Mobile Number : " + Mobile);
				}

				Thread.sleep(2000);
				try {
					WebElement mobileField = driver.findElement(By.id("mobile_number"));
					mobileField.sendKeys(Keys.ARROW_DOWN);
					mobileField.sendKeys(Keys.ARROW_DOWN);
					mobileField.sendKeys(Keys.ENTER);
				} catch (Exception e) {
					System.out.println("Enter the valid number");
				}

				Thread.sleep(2000);

				util.clickById("select2-scheme-container");
				util.enterTextByXpath("/html/body/span/span/span[1]/input", Scheme);
				driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

				util.clickByXpath("//*[@id=\"acc_join\"]/div[2]/div[1]/div[2]/div[2]/div/div[1]/div/button");
				Thread.sleep(2000);

				String message = util.getSuccessMessage("//*[@id=\"cus_mobile\"]");

				if (message.contains(Mobile)) {

					// ================= REFERRAL =================
					if (referredBy.equalsIgnoreCase("Employee")) {
						util.clickById("emp_referal_by");
						util.enterTextById("referal_code", referralCode);
						util.selectAutoSuggestionById("referal_code");

					} else if (referredBy.equalsIgnoreCase("Customer")) {
						util.clickById("cus_referal_by");
						Thread.sleep(3000);
						util.enterTextById("referal_code", referralCode);
						util.selectAutoSuggestionById("referal_code");

					} else if (referredBy.equalsIgnoreCase("Agent")) {
						util.enterTextByCss("input#agent_code", referralCode);
						WebElement Referral = driver.findElement(By.cssSelector("input#agent_code"));
						Referral.sendKeys(Keys.BACK_SPACE);
						Referral.sendKeys(Keys.ARROW_DOWN);
						Referral.sendKeys(Keys.ENTER);
					}

					util.clickByXpath("//*[@id=\"select2-employee_select-container\"]");
					util.enterTextByXpath("/html/body/span/span/span[1]/input", Employee);
					driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

					if (amount != null && !amount.equals("")) {
						util.enterTextByXpath("//*[@id=\"firstPayment_amt\"]", amount);
					}
					// ================= GIFT SECTION =================
					if (Gift.equalsIgnoreCase("Yes")) {

						util.clickById("add_gift_chart");
						WebElement giftDropdown = driver.findElement(By.id("gift_list_1"));
						Select selectGift = new Select(giftDropdown);
						selectGift.selectByVisibleText(giftName);

						String limitMessage = util.getSuccessMessage("//*[@id=\"limit_msg1\"]");
						int limitQty = Integer.parseInt(limitMessage.replaceAll("[^0-9]", ""));

						if (quantity <= limitQty) {
							util.enterTextById("gift_quantity1", String.valueOf(quantity));
						} else {
							System.out.println("The Quantity is more than the stock");
						}

						util.enterTextById("gift_barcode1", barCode.trim());
						util.enterTextById("prize_details", prizeDetails);

					}
					util.enterTextById("remark", Comments);

				} else {
					System.out.println("The phone number doesn't match the system number");

				}

			} catch (Exception e) {

				test.fail("Unexpected Exception in Create Account");
				// System.out.println("ROW FAILED – CONTINUING");

			}

			util.clickById("submit");
			Thread.sleep(2000);

			// ================= KYC FLOW (AFTER SAVE) =================
			try {
				boolean isModalPresent = false;
				try {
					WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
					shortWait.until(ExpectedConditions.or(
							ExpectedConditions.visibilityOfElementLocated(By.id("kyc_collection_modal")),
							ExpectedConditions.visibilityOfElementLocated(By.id("kycCollectionModalLabel"))));
					isModalPresent = true;
				} catch (Exception e) {
					isModalPresent = false;
				}

				if (isModalPresent) {
					boolean hasPan = (panNo != null && !panNo.trim().isEmpty());
					boolean hasAadhar = (aadharNo != null && !aadharNo.trim().isEmpty());

					boolean isPanTabDisplayed = driver.findElements(By.xpath("//a[contains(text(),'PAN')]")).size() > 0
							&&
							driver.findElement(By.xpath("//a[contains(text(),'PAN')]")).isDisplayed();

					boolean isAadharTabDisplayed = driver.findElements(By.xpath("//a[contains(text(),'AADHAR')]"))
							.size() > 0 &&
							driver.findElement(By.xpath("//a[contains(text(),'AADHAR')]")).isDisplayed();

					boolean filledAny = false;

					// 1. Check and fill PAN if it is displayed on the UI and present in Excel
					if (isPanTabDisplayed && hasPan) {
						test.info("KYC PAN Tab is displayed. Validating Excel PAN: " + panNo);
						if (crmFunctions.isValidPan(panNo)) {
							try {
								driver.findElement(By.xpath("//a[contains(text(),'PAN')]")).click();
								Thread.sleep(1000);
							} catch (Exception e) {
								System.out.println("PAN tab click failed");
							}
							util.enterTextByXpath("//input[@name='kyc_dynamic[1][pan_no]']", panNo);
							if (panFront != null && !panFront.isEmpty()) {
								driver.findElement(By.id("kyc_file_1_pan_front_img"))
										.sendKeys(imagePath + "\\" + panFront + ".jpg");
							}
							if (panBack != null && !panBack.isEmpty()) {
								driver.findElement(By.id("kyc_file_1_pan_back_img"))
										.sendKeys(imagePath + "\\" + panBack + ".jpg");
							}
							filledAny = true;
						} else {
							test.fail("Invalid PAN format: " + panNo);
						}
					}

					// 2. Check and fill Aadhaar if it is displayed on the UI and present in Excel
					if (isAadharTabDisplayed && hasAadhar) {
						test.info("KYC AADHAR Tab is displayed. Validating Excel Aadhaar: " + aadharNo);
						if (crmFunctions.isValidAadhar(aadharNo)) {
							try {
								driver.findElement(By.xpath("//a[contains(text(),'AADHAR')]")).click();
								Thread.sleep(1000);
							} catch (Exception e) {
								System.out.println("AADHAR tab click failed");
							}
							String formattedAadhar = aadharNo.trim();
							if (formattedAadhar.matches("^\\d{12}$")) {
								formattedAadhar = formattedAadhar.substring(0, 4) + " " +
										formattedAadhar.substring(4, 8) + " " +
										formattedAadhar.substring(8, 12);
							}
							util.enterTextByXpath("//input[@name='kyc_dynamic[2][aadhar_no]']",
									formattedAadhar);
							if (aadharFront != null && !aadharFront.isEmpty()) {
								driver.findElement(By.id("kyc_file_2_aadhar_front_img"))
										.sendKeys(imagePath + "\\" + aadharFront + ".jpg");
							}
							if (aadharBack != null && !aadharBack.isEmpty()) {
								driver.findElement(By.id("kyc_file_2_aadhar_back_img"))
										.sendKeys(imagePath + "\\" + aadharBack + ".jpg");
							}
							filledAny = true;
						} else {
							test.fail("Invalid Aadhaar format: " + aadharNo);
						}
					}

					// 3. Save if we filled any document
					if (filledAny) {
						util.clickById("btn_save_dynamic_kyc_modal");
						Thread.sleep(3000);
						test.pass("KYC Documents Submitted Successfully after click save button");
						
						// Click on the main submit/save button again to finalize
						util.clickById("submit");
						Thread.sleep(2000);
					} else {
						test.warning("KYC modal displayed after save, but no valid matching data was filled.");
					}
				}
			} catch (Exception kycEx) {
				test.info("KYC Modal not displayed or error occurred after save.");
			}

			String success = util.getSuccessMessage("/html/body/div/div[1]/section[1]/h1");

			System.out.println(success);

			if (success.contains("Payment")) {
				test.pass("Scheme Added Successfully : " + Scheme);

				((JavascriptExecutor) driver).executeScript(
						"window.scrollTo(0, document.body.scrollHeight);");

				util.clickByXpath("//div[@class='col-sm-offset-4']//button[1]");
				driver.navigate().refresh();
				Thread.sleep(2000);
				((JavascriptExecutor) driver).executeScript(
						"window.scrollTo(0, 0);");
				finalStatus = true;
			} else {
				test.fail("Enter the valid datas to complete Scheme" + Scheme);
				finalStatus = false;
				crmFunctions.captureScreenshot("scheme_fail" + TC_ID);
				driver.navigate().refresh();
			}

			// ================= WRITE RESULT =================
			exUtil.writeToExcel(workbook, "CreateAccount", rw, col, finalStatus);

			if (!finalStatus) {
				allSuccess = false;
			}
		}

		return allSuccess;
	}
}
