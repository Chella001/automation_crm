package crm;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilsCrm.crmFunctions;

public class KycHandler extends crmDriver {

	crmFunctions util = new crmFunctions();

	public boolean handleKycModal(String panNo, String panFront, String panBack, String aadharNo, String aadharFront, String aadharBack, String imagePath) {
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

				boolean isPanTabDisplayed = driver.findElements(By.xpath("//a[contains(text(),'PAN')]"))
						.size() > 0
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
						test.fail("Invalid PAN format: " + panNo + ". Refreshing and continuing.");
						return false;
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
						test.fail("Invalid Aadhaar format: " + aadharNo + ". Refreshing and continuing.");
						return false;
					}
				}

				// 3. Save if we filled any document
				if (filledAny) {
					util.clickById("btn_save_dynamic_kyc_modal");
					Thread.sleep(3000);
					test.pass("KYC Documents Submitted Successfully after click save button");
				} else {
					test.warning("KYC modal displayed, but no valid matching data was filled.");
				}
			}
		} catch (Exception kycEx) {
			test.info("KYC Modal not displayed or error occurred.");
		}
		return true;
	}
}
