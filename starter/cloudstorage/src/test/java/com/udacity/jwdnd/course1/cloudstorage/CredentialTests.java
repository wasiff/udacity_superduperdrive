package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    TestService testService;

    @Autowired
    EncryptionService encryptionService;

    @Autowired
    CredentialService credentialService;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void doCRUDCredentialAndVerifyTable() throws InterruptedException {
        String username = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        testService.doMockSignUp(driver, this.port,"Redirection","Test",username,"123");
        testService.doLogIn(driver, this.port, username, "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        navCredentialsTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("show-credential-modal-button")));
        WebElement showCredentialModalButton = driver.findElement(By.id("show-credential-modal-button"));
        showCredentialModalButton.click();

        String randomUsername = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        String randomPassword = RandomStringUtils.randomAlphanumeric(8).toUpperCase();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credentialUrl = driver.findElement(By.id("credential-url"));
        credentialUrl.click();
        credentialUrl.sendKeys("https://www.google.com/");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credentialUsername = driver.findElement(By.id("credential-username"));
        credentialUsername.click();
        credentialUsername.sendKeys(randomUsername);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credentialPassword = driver.findElement(By.id("credential-password"));
        credentialPassword.click();
        credentialPassword.sendKeys(randomPassword);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSaveChangesBtn")));
        WebElement credentialSaveChangesBtn = driver.findElement(By.id("credentialSaveChangesBtn"));
        credentialSaveChangesBtn.click();


        driver.get("http://localhost:" + this.port + "/");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));


        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        navCredentialsTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement credentialTable = driver.findElement(By.id("credentialTable"));

        List<WebElement> rows = credentialTable.findElements(By.tagName("tr"));

        Boolean foundValue = false;

        WebElement lastRow = rows.get(rows.size()-1);
        List<WebElement> dataCols = lastRow.findElements(By.tagName("td"));

        WebElement usernameCol = dataCols.get(dataCols.size()-2);
        WebElement passwordCol = dataCols.get(dataCols.size()-1);

        if(usernameCol.getText().equals(randomUsername) && !foundValue) {
            foundValue = true;
        }
        Assertions.assertTrue(foundValue);

        Credential savedCredential = credentialService.getCredential(randomUsername);

        Assertions.assertEquals(savedCredential.getPassword(), passwordCol.getText());


        // checking edits
        WebElement buttonsCol = dataCols.get(dataCols.size()-3);
        WebElement editButton = buttonsCol.findElement(By.className("c-edit-btn"));
        editButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        credentialUsername = driver.findElement(By.id("credential-username"));
        credentialUsername.click();
        credentialUsername.clear();
        credentialUsername.sendKeys(randomUsername+"edited");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        credentialPassword = driver.findElement(By.id("credential-password"));

        String decryptedPassword = encryptionService.decryptValue(savedCredential.getPassword(), savedCredential.getKey());
        Thread.sleep(5000);
        Assertions.assertEquals(decryptedPassword,credentialPassword.getAttribute("value"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSaveChangesBtn")));
        credentialSaveChangesBtn = driver.findElement(By.id("credentialSaveChangesBtn"));
        credentialSaveChangesBtn.click();

        // now checking if value is edited

        driver.get("http://localhost:" + this.port + "/");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        navCredentialsTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        credentialTable = driver.findElement(By.id("credentialTable"));

        rows = credentialTable.findElements(By.tagName("tr"));

        WebElement row = rows.get(rows.size()-1); // last row

        dataCols = row.findElements(By.tagName("td"));
        usernameCol = dataCols.get(dataCols.size()-2);
        Assertions.assertEquals(randomUsername+"edited", usernameCol.getText());

        // checking delete
        buttonsCol = dataCols.get(dataCols.size()-3);
        WebElement deleteButton = buttonsCol.findElement(By.className("c-delete-btn"));
        deleteButton.click();

        driver.get("http://localhost:" + this.port + "/");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        navCredentialsTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        credentialTable = driver.findElement(By.id("credentialTable"));

        try {
            rows = credentialTable.findElements(By.tagName("td"));
            if(rows.size() > 1) {
                usernameCol = rows.get(rows.size()-2);
                Assertions.assertNotEquals(randomUsername+"edited",usernameCol.getText());
            }
        }catch (NoSuchElementException e) {}
    }
}
