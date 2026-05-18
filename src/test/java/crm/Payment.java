package crm;

import java.text.DecimalFormat;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.Keys;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.Select;

import utilsCrm.crmFunctions;
import utilsCrm.crmUtils;

public class Payment extends crmDriver {

	crmUtils exUtil = new crmUtils();
	crmFunctions util = new crmFunctions();

	// ================= CELL VALUE =================
	private String getCellValue(Row row, int cellIndex) {
		if (row == null || row.getCell(cellIndex) == null) {
			return "";
		}
		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(row.getCell(cellIndex)).trim();
	}

	// Helper to correctly read Excel Date formats as dd-MM-yyyy
	private String getDateCellValue(Row row, int cellIndex) {
		if (row == null || row.getCell(cellIndex) == null) {
			return "";
		}
		Cell cell = row.getCell(cellIndex);

		if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			Date date = cell.getDateCellValue();
			return new SimpleDateFormat("dd-MM-yyyy").format(date);
		}

		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(cell).trim();
	}

	// Helper to safely parse strings to long, returning 0 if empty or invalid
	private long safeParseLong(String value) {
		if (value == null || value.trim().isEmpty()) {
			return 0;
		}
		try {
			// Remove any non-numeric characters except for the decimal point
			String cleanValue = value.replaceAll("[^0-9.]", "");
			if (cleanValue.isEmpty())
				return 0;

			// If it has a decimal, parse as double first then convert to long
			if (cleanValue.contains(".")) {
				return (long) Double.parseDouble(cleanValue);
			}
			return Long.parseLong(cleanValue);
		} catch (Exception e) {
			return 0;
		}
	}

	// Helper to safely parse strings to double, returning 0.0 if empty or invalid
	private double safeParseDouble(String value) {
		if (value == null || value.trim().isEmpty()) {
			return 0.0;
		}
		try {
			String cleanValue = value.replaceAll("[^0-9.]", "");
			if (cleanValue.isEmpty())
				return 0.0;
			return Double.parseDouble(cleanValue);
		} catch (Exception e) {
			return 0.0;
		}
	}

	// ================= MAIN METHOD =================
	public boolean Payment(Workbook workbook, int lastRow) throws InterruptedException {

		Sheet sheet = workbook.getSheet("Payment");
		boolean allSuccess = true;
		int col = 0;

		for (int rw = 1; rw <= lastRow; rw++) {

			Row row = sheet.getRow(rw);
			if (row == null)
				continue;

			// ===== Excel Data =====
			String TC_ID = getCellValue(row, 1);

			// Skip entirely blank rows
			if (TC_ID == null || TC_ID.trim().isEmpty()) {
				continue;
			}

			String customerMobile = getCellValue(row, 2);
			String schemeCode = getCellValue(row, 3);
			String Branch = getCellValue(row, 4);
			String Employee = getCellValue(row, 5);
			// String Amount = getCellValue(row, 6);
			String Cash = getCellValue(row, 7);
			String cardDetails = getCellValue(row, 8);
			String cardName = getCellValue(row, 9);
			String Type = getCellValue(row, 10);
			String Device = getCellValue(row, 11);
			String cardNo = getCellValue(row, 12);
			String cardAmount = getCellValue(row, 13);
			String approvalNo = getCellValue(row, 14);
			String netBanking = getCellValue(row, 15);
			String TypeBnk = getCellValue(row, 16);
			String BankDevice = getCellValue(row, 17);
			String paymentDate = getDateCellValue(row, 18);
			String refNo = getCellValue(row, 19);
			String BnkAmount = getCellValue(row, 20);

			String chequeDetails = getCellValue(row, 21);
			String chequeDate = getDateCellValue(row, 22);
			String cardBank = getCellValue(row, 23);
			String chequeNo = getCellValue(row, 24);
			String ifscCode = getCellValue(row, 25);
			String chequeAmt = getCellValue(row, 26);

			String voucher = getCellValue(row, 27);
			String voucherCode = getCellValue(row, 28);
			String voucherValue = getCellValue(row, 29);
			String weightGram = getCellValue(row, 30);
			String schemeType = getCellValue(row, 31);
			// String receivedAmt = getCellValue(row, 32);
			String flexible = getCellValue(row, 32);
			String panNo = getCellValue(row, 34);
			String panFront = getCellValue(row, 35);
			String panBack = getCellValue(row, 36);
			String aadharNo = getCellValue(row, 37);
			String aadharFront = getCellValue(row, 38);
			String aadharBack = getCellValue(row, 39);
			String imagePath = "C:\\Users\\Chella\\eclipse-workspace\\mavenCrm\\Test_Images";

			boolean addSuccess = false;
			boolean finalStatus;

			// ================= EXTENT =================
			test = extent.createTest(TC_ID, "Payment Test");
			test.info("Payment Test Started for TC_ID : " + TC_ID);

			try {

				WebElement payEma = driver.findElement(By.xpath("/html/body/div/header/nav/button/a"));
				payEma.click();
				// navigatedToPayment = true;

				// ================= ADD PAYMENT =================
				try {

					if (crmFunctions.isValidMobile(customerMobile)) {
						util.enterTextById("mobile_number", customerMobile);
					} else {
						System.out.println(customerMobile + " Invalid number");
						test.warning("Invalid Customer Mobile Number : " + customerMobile);
					}

					Thread.sleep(2000);
					try {
						WebElement mobileField = driver.findElement(By.id("mobile_number"));
						mobileField.sendKeys(Keys.ARROW_DOWN);
						mobileField.sendKeys(Keys.ENTER);
						Thread.sleep(2000);
					} catch (Exception e) {
						System.out.println("Enter the valid number");
					}

					LocalDate today = LocalDate.now();
					int year = today.getYear();
					int month = today.getMonthValue();

					if (month <= 3) {
						year = year - 1;
					}

					String financialYear = String.valueOf(year).substring(2);
					String financialCode = financialYear + "-" + schemeCode;
					System.out.println(financialCode);
					Thread.sleep(2000);

					util.clickById("select2-scheme_account-container");
					util.enterTextByXpath("/html/body/span/span/span[1]/input", financialCode);
					driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

					Thread.sleep(1000);
					util.clickById("select2-branch_select-container");
					util.enterTextByXpath("/html/body/span/span/span[1]/input", Branch);
					driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

					Thread.sleep(1000);
					util.clickById("select2-employee_select-container");
					util.enterTextByXpath("/html/body/span/span/span[1]/input", Employee);
					driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("window.scrollBy(0, 500);");

					String boardRateRaw = util.getSuccessMessage("/html/body/div/header/nav/div/ul/li[6]/a/span[2]/b");
					double boardRate = 0.0;
					if (boardRateRaw != null) {
						String boardRateClean = boardRateRaw.replaceAll("[^0-9.]", "");
						if (!boardRateClean.isEmpty()) {
							boardRate = Double.parseDouble(boardRateClean);
						}
					}
					System.out.println("Board Rate: " + boardRate);

					double truncatedWeight = 0;
					String paidWeight = "";
					double calculatedAmount = 0;
					double benefitPercentage = 0;
					double calculatedBenefitWeight = 0;
					String benfAmt = "";
					double benefitWeightValue = 0;
					double benefitAmount = 0;
					WebElement amtClear = driver.findElement(By.id("total_amt"));

					switch (schemeType) {

						case "Amount":
							break;

						case "Weight":
							String formattedValue = String.format("%.3f", safeParseDouble(weightGram));
							driver.findElement(
									By.xpath("//input[@name='weight_gold' and @value='" + formattedValue + "']"))
									.click();
							calculatedAmount = safeParseDouble(weightGram) * boardRate;
							paidWeight = driver.findElement(By.id("payment_weight")).getAttribute("value");
							System.out.println("Displayed Weight:" + paidWeight);
							System.out.println("Displayed Amount:" + calculatedAmount);
							break; // status check

						case "amountToWeight":
							double calculatedWeight1 = safeParseDouble(flexible) / boardRate;
							truncatedWeight = Math.floor(calculatedWeight1 * 1000) / 1000;
							paidWeight = driver.findElement(By.id("payment_weight")).getAttribute("value");
							break;

						case "FlexibleAmount":
							break;

						case "amountToWeightBOA":
						case "amountToWeightBOW":
							amtClear.clear();
							util.enterTextById("total_amt", flexible);
							util.clickById("proced");
							double calculatedWeight2 = safeParseDouble(flexible) / boardRate;
							truncatedWeight = Math.floor(calculatedWeight2 * 1000) / 1000;
							paidWeight = driver.findElement(By.id("payment_weight")).getAttribute("value");
							break;

						case "DigiGold":
							amtClear.clear();
							util.enterTextById("total_amt", flexible);
							util.clickById("proced");
							Thread.sleep(2000);

							double calculatedWeight3 = safeParseDouble(flexible) / boardRate;
							truncatedWeight = Math.floor(calculatedWeight3 * 1000) / 1000;
							System.out.println(truncatedWeight);

							paidWeight = driver.findElement(By.id("payment_weight")).getAttribute("value");

							// (remove % sign)
							WebElement benefitElement = driver.findElement(By.id("digi_benefit_content"));
							String fullText = benefitElement.getText();
							String benefitPercentageStr = fullText.split(" ")[0].replace("%", ""); // "5.5"
							benefitPercentage = safeParseDouble(benefitPercentageStr); // REMOVED 'double'
																						// declaration

							// Calculate Benefit Weight = truncatedWeight * (Benefit Percentage / 100)
							calculatedBenefitWeight = truncatedWeight * (benefitPercentage / 100); // REMOVED 'double'
																									// declaration
							calculatedBenefitWeight = Math.floor(calculatedBenefitWeight * 1000) / 1000;
							System.out.println("Calculated Benefit Weight: " + calculatedBenefitWeight);

							// (Remove INR)
							WebElement benefitAmt = driver.findElement(By.id("digi_benefit_amt"));
							String benefit_amt = benefitAmt.getText();
							benfAmt = benefit_amt.replace("INR", "").trim(); // REMOVED 'String' declaration

							// (remove 'gm' suffix)
							WebElement benefitWeight = driver.findElement(By.id("digi_benefit_wgt"));
							String benefit_weight = benefitWeight.getText();
							benefit_weight = benefit_weight.split(" ")[0]; // "0.047"
							benefitWeightValue = safeParseDouble(benefit_weight); // REMOVED 'double' declaration

							// Calculate benefit amount: benefit weight * board rate
							benefitAmount = benefitWeightValue * boardRate;

							System.out.println("Benefit Weight: " + benefitWeightValue);
							System.out.println("Benefit Percentage: " + benefitPercentage);
							System.out.println("Calculated Benefit Amount: " + benefitAmount);
							break;

						case "weightBOW":
						case "weightBOA":
						case "flexibleWeight":
							amtClear.clear();
							util.enterTextById("total_amt", flexible);
							break;
						default:
							System.out.println("No matching schemes are in Excel :" + schemeType);
							break;
					}

					String received = driver.findElement(By.id("total_amt")).getAttribute("value").trim();
					System.out.println("Received Amount : " + received);

					long total = safeParseLong(Cash) + safeParseLong(cardAmount) + safeParseLong(BnkAmount)
							+ safeParseLong(chequeAmt) + safeParseLong(voucherValue);
					String receivedAmount;
					String excelAmount;
					System.out.println(total);

					if (schemeType.equalsIgnoreCase("Weight")) {
						receivedAmount = String.valueOf(calculatedAmount) + "0";
						excelAmount = String.valueOf(total) + ".00";

					} else {
						receivedAmount = String.valueOf(received) + ".00";
						excelAmount = String.valueOf(total) + ".00";
						// System.out.println("Other Scheme");
					}

					System.out.println("Excel Amount:" + excelAmount);
					System.out.println("Received Amount:" + received);
					System.out.println("Total Amount" + total);
					System.out.println("Received Amount" + receivedAmount);

					// Payment details //
					if (excelAmount.equalsIgnoreCase(receivedAmount)) {

						if ((("amountToWeightBOA".equalsIgnoreCase(schemeType) ||
								"amountToWeightBOW".equalsIgnoreCase(schemeType) ||
								"amountToWeight".equalsIgnoreCase(schemeType))
								&& Double.parseDouble(paidWeight.trim()) == truncatedWeight)

								||

								("DigiGold".equalsIgnoreCase(schemeType)
										&& Double.parseDouble(paidWeight.trim()) == truncatedWeight
										&& Double.parseDouble(benfAmt) == benefitAmount
										&& benefitWeightValue == calculatedBenefitWeight)) {

							util.clickById("proced");

							// ================= KYC FLOW =================
							Thread.sleep(1000);
							try {
								if (driver.findElements(By.id("kyc_collection_modal")).size() > 0 &&
										driver.findElement(By.id("kyc_collection_modal")).isDisplayed()) {

									boolean hasPan = (panNo != null && !panNo.trim().isEmpty());
									boolean hasAadhar = (aadharNo != null && !aadharNo.trim().isEmpty());

									boolean isPanTabDisplayed = driver
											.findElements(By.xpath("//a[contains(text(),'PAN')]")).size() > 0 &&
											driver.findElement(By.xpath("//a[contains(text(),'PAN')]")).isDisplayed();

									boolean isAadharTabDisplayed = driver
											.findElements(By.xpath("//a[contains(text(),'AADHAR')]")).size() > 0 &&
											driver.findElement(By.xpath("//a[contains(text(),'AADHAR')]"))
													.isDisplayed();

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
											driver.navigate().refresh();
											continue;
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
											test.fail("Invalid Aadhaar format: " + aadharNo
													+ ". Refreshing and continuing.");
											driver.navigate().refresh();
											continue;
										}
									}

									// 3. Save if we filled any document
									if (filledAny) {
										util.clickById("btn_save_dynamic_kyc_modal");
										Thread.sleep(2000);
										test.pass("KYC Documents Submitted Successfully");
									} else {
										test.warning("KYC modal displayed, but no valid matching data was filled.");
									}
								}
							} catch (Exception kycEx) {
								test.info(
										"KYC Modal not displayed or error occurred. Proceeding with normal execution.");
							}

						}
						if (Cash != null && !Cash.trim().isEmpty()
								&& crmFunctions.isValidAmount(Cash)) {

							util.enterTextById("make_pay_cash", Cash);
						}

						// ==== Credit / DebitCard ==== //
						if (cardDetails.equalsIgnoreCase("Yes")) {
							util.addMultipleCards(cardName, Type, Device, cardNo, cardAmount, approvalNo);
						}

						// ==== // NetBanking === //
						if (netBanking.equalsIgnoreCase("Yes")) {
							util.addMultipleNetBanking(TypeBnk, BankDevice, paymentDate, refNo, BnkAmount);
						}

						// ==== Cheque ====//
						if (chequeDetails.equalsIgnoreCase("Yes")) {
							util.addMultipleCheques(chequeDate, cardBank, Branch, chequeNo, ifscCode, chequeAmt);
						}

						if (voucher.equalsIgnoreCase("Yes")) {

							util.clickByXpath("//*[@id=\"vch_modal\"]");

							if (!voucherCode.isEmpty()) {
								util.enterTextByXpath("//*[@id=\"vch_details\"]/tbody/tr/td[1]/input", voucherCode);
							}

							if (!voucherValue.isEmpty()) {
								util.enterTextByXpath("//*[@id=\"vch_details\"]/tbody/tr/td[2]/input", voucherValue);
							}

							util.clickByXpath("//*[@id=\"vch_newvch\"]");

							try {
								String voucherAmt = driver
										.findElement(By.xpath("//*[@id=\"payment_modes\"]/tbody/tr[7]/td[3]"))
										.getText();
								System.out.println(voucherAmt);
								voucherAmt = voucherAmt.replaceAll("[^0-9.]", "");
								double value = Double.parseDouble(voucherAmt);
								int finalValue = (int) value;
								System.out.println(finalValue);

							} catch (Exception e) {
								util.handleAlertIfPresent();
								System.out.println("Error getting Voucher: " + e.getMessage());
								util.clickByXpath("//*[@id=\"vch-detail-modal\"]/div/div/div[3]/button");
								test.fail("The voucher amount exceeed the maximum amount");
							}
						}

						String totalAmt = driver.findElement(By.xpath("//*[@id=\"payment_modes\"]/tfoot/tr[1]/th[3]"))
								.getText();
						System.out.println("Total Amount" + totalAmt);

						String totalString;

						if (schemeType.equalsIgnoreCase("Weight")) {
							totalString = excelAmount;
							System.out.println("Total String: " + totalString);
						} else { // Flexible Amount scheme no(.00)
							totalString = excelAmount;
							System.out.println("Total String: " + totalString);
						}

						if (totalAmt.equals(totalString) ||
								(("Weight".equalsIgnoreCase(schemeType)
										&& safeParseDouble(paidWeight) == safeParseDouble(weightGram)) ||
										(("amountToWeight".equalsIgnoreCase(schemeType)
												|| "amountToWeightBOA".equalsIgnoreCase(schemeType)
												|| "amountToWeightBOW".equalsIgnoreCase(schemeType))
												&& safeParseDouble(paidWeight) == truncatedWeight)
										||
										("DigiGold".equalsIgnoreCase(schemeType)
												&& safeParseDouble(paidWeight) == truncatedWeight
												&& safeParseDouble(benfAmt) == benefitAmount
												&& benefitWeightValue == calculatedBenefitWeight)
										||
										("Amount".equalsIgnoreCase(schemeType)
												|| "FlexibleAmount".equalsIgnoreCase(schemeType)
												|| "weightBOW".equalsIgnoreCase(schemeType)
												|| "weightBOA".equalsIgnoreCase(schemeType)
												|| "flexibleWeight".equalsIgnoreCase(schemeType)))) {

							System.out.println("Clicked save");
							((JavascriptExecutor) driver).executeScript(
									"window.scrollTo(0, document.body.scrollHeight);");
							util.clickByXpath("//*[@id=\"btn-submit\"]/label[2]");

						} else {
							util.clickByXpath("//*[@id=\"pay_form\"]/div[5]/div/div/div[2]/button");
							System.out.println("The given Weight doesn't match");
							test.fail("The payment amount exceeded the received amount");
						}

						String message = util
								.getSuccessMessage("/html/body/div[1]/div[1]/section[2]/div/div/div/div[2]/div[2]");

						String searchBoxXpath = "//div[@id='payment_list_filter']//input[1]";
						util.enterTextByXpath(searchBoxXpath, customerMobile);
						String accNo = util.getSuccessMessage("//*[@id=\"payment_list\"]/tbody/tr[1]/td[8]"); // excel
																												// update
						System.out.println(accNo);

						// Save Account Number to Column 33 in the Excel sheet
						if (accNo != null && !accNo.isEmpty()) {
							exUtil.writeToExcel(workbook, "Payment", rw, 33, accNo);
						}

						if (message != null && message.toLowerCase().contains("success")) {
							addSuccess = true;
							test.pass("Payment Added Successfully : " + TC_ID);
						} else {
							crmFunctions.captureScreenshot("Payment_Add_Failed_" + TC_ID);
							util.clickByXpath("//*[@id=\"pay_form\"]/div[5]/div/div/div[2]/button");
							addSuccess = false;
							test.fail("Payment Add Failed : " + TC_ID);
						}
					}

				} catch (Exception e) {
					addSuccess = false;
					crmFunctions.captureScreenshot("Payment_Add_Error_" + TC_ID);
					test.fail("Unexpected Exception in Payment Add");
				}

				finalStatus = addSuccess;
				exUtil.writeToExcel(workbook, "Payment", rw, col, finalStatus);

				if (finalStatus) {

					test.pass("Payment Test Case PASSED");
				} else {
					test.fail("Payment Test Case FAILED");
					allSuccess = false;
				}

			} catch (Exception e) {
				allSuccess = false;
				crmFunctions.captureScreenshot("Payment_Main_Error");
				test.fail("Unexpected Exception in Payment Main");
			}
		}

		return allSuccess;
	}
}
