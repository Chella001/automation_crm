package crm;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilsCrm.crmFunctions;
import utilsCrm.crmUtils;

public class Customer extends crmDriver {

	crmUtils exUtil = new crmUtils();
	crmFunctions util = new crmFunctions();
	private boolean navigatedToCustomer = false;

	private String getCellValue(Row row, int cellIndex) {
		if (row == null || row.getCell(cellIndex) == null) {
			return "";
		}

		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(row.getCell(cellIndex)).trim();
	}

	public boolean Customer(Workbook workbook, int lastRow) throws InterruptedException {

		Sheet sheet = workbook.getSheet("Customer");
		boolean allSuccess = true;
		int col = 0;

		for (int rw = 1; rw <= lastRow; rw++) {

			Row row = sheet.getRow(rw);
			if (row == null)
				continue;

			String TC_ID = getCellValue(row, 1);
			String customerType = getCellValue(row, 2);
			String Title = getCellValue(row, 3);
			String companyName = getCellValue(row, 4);
			String GSTnumber = getCellValue(row, 5);
			String firstName = getCellValue(row, 6);
			String lastName = getCellValue(row, 7);
			String Mobile = getCellValue(row, 8);
			String Profession = getCellValue(row, 9);
			String openingBalanceAmount = getCellValue(row, 10);
			String FY_Year = getCellValue(row, 11);
			String E_Mail = getCellValue(row, 12);
			String Phone = getCellValue(row, 13);
			String Address1 = getCellValue(row, 14);
			String Address2 = getCellValue(row, 15);
			String Address3 = getCellValue(row, 16);
			String Country = getCellValue(row, 17);
			String State = getCellValue(row, 18);
			String City = getCellValue(row, 19);
			String Pincode = getCellValue(row, 20);
			String Area = getCellValue(row, 21).trim();
			String selectReligion = getCellValue(row, 22);
			String maritalStatus = getCellValue(row, 26);
			String Nominiee = getCellValue(row, 27);
			String nominieeName = getCellValue(row, 28);
			String nominieeRelation = getCellValue(row, 29);
			String nomineeNumber = getCellValue(row, 30);
			String nomineeAdd1 = getCellValue(row, 31);
			String nomineeAdd2 = getCellValue(row, 32);

			// Document numbers - CORRECTED column indices
			String Password = getCellValue(row, 33);
			// String PanNo = getCellValue(row, 34);
			// String VoterId = getCellValue(row, 35);
			// String rationCard = getCellValue(row, 36);

			String edit = getCellValue(row, 34);
			String delete = getCellValue(row, 35);
			String editCustomer = getCellValue(row, 36);
			String editMobile = getCellValue(row, 37);
			String editMail = getCellValue(row, 38);
			String editAdd = getCellValue(row, 39);
			String PanNo = getCellValue(row, 40);
			String VoterId = getCellValue(row, 41);
			String rationCard = getCellValue(row, 42);
			String nominieeImage = getCellValue(row, 43);

			String path = "C:\\Users\\Chella\\eclipse-workspace\\mavenCrm\\Test_Images";

			boolean addSuccess = false;
			boolean editSuccess = false;
			boolean deleteSuccess = false;
			boolean finalStatus;

			// ================= EXTENT TEST =================
			test = extent.createTest(TC_ID, "Customer Add Test");
			test.info("Customer Add Test Started for TC_ID : " + TC_ID);

			try {

				// ================= NAVIGATION (ONCE) =================
				if (!navigatedToCustomer) {

					util.clickByXpath("/html/body/div/header/nav/a");

					WebElement sidebar = driver.findElement(
							By.cssSelector("div.slimScrollDiv > section.sidebar"));

					((JavascriptExecutor) driver)
							.executeScript("arguments[0].scrollTop = 1500;", sidebar);

					util.clickByXpath("/html/body/div[1]/aside[1]/div/section/ul/li[37]/a");
					util.clickByXpath("/html/body/div[1]/aside[1]/div/section/ul/li[37]/ul/li[3]/a");

					Thread.sleep(1500);
					navigatedToCustomer = true;
				}

				// ================= ADD =================
				util.clickById("add_customer");
				Thread.sleep(2000);
				if (customerType.equalsIgnoreCase("Individual")) {

					((JavascriptExecutor) driver).executeScript(
							"$(\"input[name='customer[cus_type]'][value='1']\").prop('checked', true).trigger('change');");

					util.clickByXpath("(//span[@role='presentation'])[1]");
					util.enterTextByXpath("/html/body/span/span/span[1]/input", Title);
					driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);
					util.enterTextById("firstname", firstName);
					util.enterTextById("lastname", lastName);
				}

				if (customerType.equalsIgnoreCase("Company")) {

					((JavascriptExecutor) driver).executeScript(
							"$(\"input[name='customer[cus_type]'][value='2']\").prop('checked', true).trigger('change');");

					util.clickByXpath("(//span[@role='presentation'])[1]");
					util.enterTextByXpath("/html/body/span/span/span[1]/input", Title);
					driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

					util.enterTextById("firstname", companyName);
					util.enterTextById("gst_number", GSTnumber);
				}

				util.enterTextById("mobile", Mobile);

				util.clickByXpath("//*[@id=\"profess\"]/div/span/span[1]/span/span[2]");
				util.enterTextByXpath("/html/body/span/span/span[1]/input", Profession);
				driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(
						Keys.ENTER);

				// System.out.println(Password);
				util.enterTextById("opening_bal_amt", openingBalanceAmount);
				util.enterTextById("email", E_Mail);
				util.enterTextByXpath("(//input[@id='phone'])[2]", Phone);
				if (Password != "") {
					util.enterTextById("passwd", Password);
				}
				util.enterTextById("address1", Address1);
				util.enterTextById("address2", Address2);
				util.enterTextById("address3", Address3);

				util.clickByXpath("//*[@id=\"select2-country-container\"]");
				util.enterTextByXpath("/html/body/span/span/span[1]/input", Country);

				driver.findElement(By.xpath("/html/body/span/span/span[1]/input"))
						.sendKeys(Keys.ARROW_DOWN);
				driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

				util.clickByXpath("//*[@id=\"tab_1\"]/div[1]/div[5]/div[2]/div/span/span[1]/span/span[2]");
				util.enterTextByXpath("/html/body/span/span/span[1]/input", State);
				driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

				util.clickByXpath("//*[@id=\"tab_1\"]/div[1]/div[5]/div[3]/div/span/span[1]/span/span[2]");
				util.enterTextByXpath("/html/body/span/span/span[1]/input", City);
				driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);
				Thread.sleep(1000);
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
				Thread.sleep(1000);

				util.enterTextById("pin_code_add", Pincode);
				driver.findElement(By.id("pin_code_add")).sendKeys(Keys.TAB);
				Thread.sleep(2000);
				WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
				WebElement villageContainer = shortWait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//span[@aria-labelledby='select2-Village-container']")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						villageContainer);
				Thread.sleep(1000);
				try {
					villageContainer.click();
				} catch (Exception e) {
					((JavascriptExecutor) driver).executeScript(
							"var evt = new MouseEvent('mousedown', { bubbles: true, cancelable: true, view: window }); arguments[0].dispatchEvent(evt);",
							villageContainer);
				}
				try {
					((JavascriptExecutor) driver).executeScript("$('#Village').select2('open');");
				} catch (Exception e2) {
				}
				WebElement searchInput = shortWait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("/html/body/span/span/span[1]/input | //input[@class='select2-search__field']")));
				searchInput.clear();
				searchInput.sendKeys(Area);
				Thread.sleep(1000);
				searchInput.sendKeys(Keys.ENTER);

				// Religion

				WebElement dropdown = driver.findElement(By.id("religion_select"));
				Select select = new Select(dropdown);

				if (selectReligion.equalsIgnoreCase("Hindu")) {
					select.selectByValue("1");
				} else if (selectReligion.equalsIgnoreCase("Muslim")) {
					select.selectByValue("2");
				} else if (selectReligion.equalsIgnoreCase("Christian")) {
					select.selectByValue("3");
				}

				// Gender

				if (Title.equalsIgnoreCase("Mr")) {
					util.clickByXpath("(//input[@name='customer[gender]'])[1]");

				} else if (Title.equalsIgnoreCase("Mrs")) {
					util.clickByXpath("(//input[@name='customer[gender]'])[2]");
				} else if (Title.equalsIgnoreCase("Mrs")) {
					util.clickByXpath("(//input[@name='customer[gender]'])[3]");
				}

				// JavascriptExecutor js = (JavascriptExecutor) driver; js.executeScript(
				// "document.getElementById('dateField').value='15/01/2003';" );
				String cleanedMarital = maritalStatus.replaceAll("\\u00A0", "").trim();
				System.out.println(
						"Marital Status from Excel: '" + maritalStatus + "' (Cleaned: '" + cleanedMarital + "')");

				if (cleanedMarital.equalsIgnoreCase("Single")) {
					try {
						util.clickByXpath("//input[@name='customer[marital_status]' and @value='1']");
					} catch (Exception e) {
					}
					((JavascriptExecutor) driver).executeScript(
							"$(\"input[name='customer[marital_status]'][value='1']\").prop('checked', true).trigger('change');");
				} else if (cleanedMarital.equalsIgnoreCase("Married")) {
					try {
						util.clickByXpath("//input[@name='customer[marital_status]' and @value='2']");
					} catch (Exception e) {
					}
					((JavascriptExecutor) driver).executeScript(
							"$(\"input[name='customer[marital_status]'][value='2']\").prop('checked', true).trigger('change');");
				}

				((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(1000);

				// Nominee Details

				if (Nominiee.equalsIgnoreCase("Yes")) {
					((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
					Thread.sleep(1000);

					util.clickById("others_tab");
					util.enterTextById("nominee_name", nominieeName);
					util.enterTextById("nominee_relationship", nominieeRelation);
					System.out.println(nominieeRelation);
					util.enterTextByXpath("(//label[normalize-space(text())='Mobile']/following::input)[1]",
							nomineeNumber);
					util.enterTextById("nominee_address1", nomineeAdd1);
					util.enterTextById("nominee_address2", nomineeAdd2);

					((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
					Thread.sleep(1000);

					if (PanNo != null && !PanNo.trim().isEmpty()) {
						util.enterTextById("panno", PanNo);
						driver.findElement(By.id("pan_proof"))
								.sendKeys(path + "\\" + nominieeImage + ".jpg");
					}

					if (VoterId != null && !VoterId.trim().isEmpty()) {
						util.enterTextById("voterid", VoterId);
						driver.findElement(By.id("voter_proof"))
								.sendKeys(path + "\\" + nominieeImage + ".jpg");
					}

					if (rationCard != null && !rationCard.trim().isEmpty()) {
						util.enterTextById("rationcard", rationCard);
						driver.findElement(By.id("ration_proof"))
								.sendKeys(path + "\\" + nominieeImage + ".jpg");
					}

					((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
					Thread.sleep(1000);
				}
				// ===================== SUCCESS MESSAGE =====================
				util.clickByXpath("//button[@type='submit']");
				Thread.sleep(1000);
				util.clickByXpath("//button[contains(@class,'btn-cancel')]");
				Thread.sleep(2000);
				util.clickByXpath("//*[@id=\"cus_create\"]/div[2]/div/div/button[2]");
				driver.navigate().refresh();
				Thread.sleep(2000);

				try {
					String searchBoxXpath = "//div[contains(@id,'customer_list_filter')]//input";
					util.enterTextByXpath(searchBoxXpath, Mobile);

					String addMessage = util.getSuccessMessage("//*[@id=\"customer_list\"]/tbody/tr/td[3]");

					if (addMessage != null && addMessage.contains(Mobile)) {
						addSuccess = true;
						test.pass("Customer Added Successfully : " + firstName);
					} else {
						addSuccess = false;
						test.fail("Customer Add Failed");
						try {
							util.clickByXpath("//button[contains(@class,'btn-cancel')]");
						} catch (Exception ex) {
						}
						crmFunctions.captureScreenshot("Add_Customer_Failed_" + TC_ID);
					}

				} catch (Exception e) {
					addSuccess = false;
					test.fail("Customer Add Verification Failed");
					try {
						util.clickByXpath("//button[contains(@class,'btn-cancel')]");
					} catch (Exception ex) {
					}
					crmFunctions.captureScreenshot("Add_Verify_Error_" + TC_ID);
					System.out.println("ADD VERIFY FAILED – CONTINUING");
				}

				// ================= EDIT =================
				if (edit.equalsIgnoreCase("Yes")) {
					editSuccess = CustomerEdit(Mobile, editCustomer, TC_ID, editMobile, editMail, editAdd);
				}

				// ================= DELETE =================
				if (delete.equalsIgnoreCase("Yes")) {
					String customerToDelete = edit.equalsIgnoreCase("Yes") ? editCustomer : Mobile;
					deleteSuccess = CustomerDelete(customerToDelete, TC_ID);
				}

				// ================= FINAL STATUS =================
				finalStatus = addSuccess
						&& (!edit.equalsIgnoreCase("Yes") || editSuccess)
						&& (!delete.equalsIgnoreCase("Yes") || deleteSuccess);

			} catch (Exception e) {
				finalStatus = false;
				try {
					util.clickByXpath("//button[contains(@class,'btn-cancel')]");
				} catch (Exception ex) {
				}
				crmFunctions.captureScreenshot("Customer_Error_" + TC_ID);
				test.fail("Unexpected Exception in Customer Add");
			}

			exUtil.writeToExcel(workbook, "Customer", rw, col, finalStatus);

			if (finalStatus) {
				test.pass("Customer Test Case PASSED");
			} else {
				test.fail("Customer Test Case FAILED");
				allSuccess = false;
			}
		}

		return allSuccess;
	}

	// ================= EDIT =================
	public boolean CustomerEdit(String Mobile, String editCustomer, String TC_ID,
			String editMobile, String editMail, String editAdd)
			throws InterruptedException {

		try {

			System.out.println("Edit Entered");

			// Search customer
			String searchBoxXpath = "//div[contains(@id,'customer_list_filter')]//input";
			util.enterTextByXpath(searchBoxXpath, Mobile);

			util.scrollIntoViewByXpath("//th[normalize-space()='Action']");
			util.clickByXpath("//*[@id=\"customer_list\"]/tbody/tr/td[26]/div/button");
			util.clickByXpath("//*[@id=\"customer_list\"]/tbody/tr/td[26]/div/ul/li[1]/a");

			Thread.sleep(1000);

			// ===== EDIT FIELDS =====
			WebElement editField = driver.findElement(By.id("firstname"));
			editField.clear();
			editField.sendKeys(editCustomer);

			WebElement clMail = driver.findElement(By.id("email"));
			clMail.clear();
			clMail.sendKeys(editMail);

			WebElement clmobNum = driver.findElement(By.xpath("(//input[@id='phone'])[2]"));
			clmobNum.clear();
			clmobNum.sendKeys(editMobile);

			WebElement clAdd = driver.findElement(By.id("address1"));
			clAdd.clear();
			clAdd.sendKeys(editAdd);

			// SAVE
			util.clickByXpath("//*[@id=\"save\"]");
			Thread.sleep(1500);

			// ===== VERIFY SUCCESS MESSAGE =====
			String message = util.getSuccessMessage(
					"/html/body/div[1]/div[1]/section[2]/div/div/div/div[2]/div[3]/div/div");

			if (message != null && message.toLowerCase().contains("success")) {
				test.pass("Customer Edited Successfully : " + TC_ID);
				return true;
			} else {
				crmFunctions.captureScreenshot("Customer_Edit_Failed_" + TC_ID);
				util.clickByXpath("//*[@id=\"cus_create\"]/div[2]/div/div/button[2]");
				test.fail("Customer Edit Failed");
				return false;
			}

		} catch (Exception e) {
			crmFunctions.captureScreenshot("Customer_Edit_Exception_" + TC_ID);
			try {
				util.clickByXpath("//button[contains(@class,'btn-cancel')]");
			} catch (Exception ex) {
			}
			test.fail("Exception during Customer Edit");
			return false;
		}
	}

	// ================= DELETE =================
	public boolean CustomerDelete(String Mobile, String TC_ID)
			throws InterruptedException {
		try {
			util.enterTextByXpath("//div[contains(@id,'customer_list_filter')]//input", Mobile);
			util.scrollIntoViewByXpath("//th[normalize-space()='Action']");
			util.clickByXpath("//*[@id=\"customer_list\"]/tbody/tr[1]/td[26]/div/button");
			util.clickByXpath("//*[@id=\"customer_list\"]/tbody/tr[1]/td[26]/div/ul/li[2]/a");
			util.clickByXpath("//*[@id=\"confirm-delete\"]/div/div/div[3]/a");
			Thread.sleep(1000);
			String message = util
					.getSuccessMessage("/html/body/div[1]/div[1]/section[2]/div/div/div/div[2]/div[3]/div/div");

			if (message != null && message.toLowerCase().contains("success")) {
				test.pass("Customer Deleted Successfully : " + TC_ID);
				return true;
			} else {
				crmFunctions.captureScreenshot("Customer_Edit_Failed_" + TC_ID);
				test.fail("Customer Delete Failed");
				return false;
			}

		} catch (Exception e) {
			crmFunctions.captureScreenshot("Customer_Delete_Error_" + TC_ID);
			return false;
		}
	}
}
