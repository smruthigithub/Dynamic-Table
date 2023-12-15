package Testcases;

import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;

import org.testng.annotations.AfterClass;

import org.testng.annotations.BeforeClass;

import org.testng.annotations.Test;

import com.google.gson.JsonParser;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

public class TC001 {

private WebDriver driver;

private WebDriverWait wait;

private String url = "https://testpages.herokuapp.com/styled/tag/dynamic-table.html";

private By tableDataButton = By.xpath("//summary[normalize-space()='Table Data']");

private By refreshTableButton = By.id("refreshtable");

private By tableBody = By.id("dynamictable");

private List<HashMap<String, String>> expectedData = new ArrayList();

@BeforeClass

public void setUp() throws Exception {

WebDriverManager.chromedriver().setup();

driver = new ChromeDriver();

driver.manage().window().maximize();

wait = new WebDriverWait(driver, Duration.ofSeconds(15));

// Parse JSON file

JSONParser parser = new JSONParser();

try (FileReader reader = new FileReader(".\\src\\test\\resources\\configfiles\\Jsondata.json")) {;

JSONArray jsonArray =(JSONArray) parser.parse(reader);

for (int i = 0; i < jsonArray.size(); i++) {

JSONObject jsonObject = (JSONObject) jsonArray.get(i);

HashMap<String, String> dataMap = new HashMap<>();

dataMap.put("name", (String) jsonObject.get("name"));

dataMap.put("age", String.valueOf(jsonObject.get("age")));

dataMap.put("gender", (String) jsonObject.get("gender"));

expectedData.add(dataMap);

}

} catch (IOException e) {
    // Handle file reading error
    e.printStackTrace();
    System.err.println("Error reading JSON file: " + e.getMessage()); }

}

@Test

public void testDynamicTable() {

driver.get(url);

driver.findElement(tableDataButton).click();

wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jsondata")));

driver.findElement(By.id("jsondata")).sendKeys(expectedData.toString());

driver.findElement(By.id("refreshtable")).click();

System.out.println("Clicked on refresh button");

wait.until(ExpectedConditions.presenceOfElementLocated(tableBody));

List<WebElement> rows = driver.findElements(tableBody);

List<HashMap<String, String>> actualData = new ArrayList();

for (WebElement row : rows) {

List<WebElement> cells = row.findElements(By.cssSelector("td"));

HashMap<String, String> cellData = new HashMap();

cellData.put("name", cells.get(0).getText());

cellData.put("age", cells.get(1).getText());

cellData.put("gender", cells.get(2).getText());

actualData.add(cellData);

}

Assert.assertEquals(actualData, expectedData, "Data in table does not match JSON data!");

}

/*
 * @AfterClass
 * 
 * public void tearDown() {
 * 
 * driver.quit();
 * 
 * }
 */

}