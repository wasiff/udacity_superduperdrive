package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
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
public class NoteTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    TestService testService;

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
    public void doCRUDNoteAndVerifyTable() throws InterruptedException {
        String username = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        testService.doMockSignUp(driver, this.port,"Redirection","Test",username,"123");
        testService.doLogIn(driver, this.port, username, "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
        navNotesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("show-note-modal-button")));
        WebElement showNoteModalButton = driver.findElement(By.id("show-note-modal-button"));
        showNoteModalButton.click();

        String randomTitle = RandomStringUtils.randomAlphanumeric(8).toUpperCase();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.click();
        noteTitle.sendKeys(randomTitle);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.click();
        noteDescription.sendKeys("Lorem ipsum dolar sit");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSaveChangesBtn")));
        WebElement noteSaveChangesBtn = driver.findElement(By.id("noteSaveChangesBtn"));
        noteSaveChangesBtn.click();


        driver.get("http://localhost:" + this.port + "/");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));


        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        navNotesTab = driver.findElement(By.id("nav-notes-tab"));
        navNotesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));
        WebElement noteTable = driver.findElement(By.id("noteTable"));

        List<WebElement> rows = noteTable.findElements(By.tagName("tr"));

        Boolean foundValue = false;

        WebElement lastRow = rows.get(rows.size()-1);
        WebElement col = lastRow.findElement(By.tagName("th"));
        if(col.getText().equals(randomTitle) && !foundValue) {
            foundValue = true;
        }
        Assertions.assertTrue(foundValue);


        // checking edits
        WebElement firstColumn = lastRow.findElement(By.tagName("td"));
        WebElement editButton = firstColumn.findElement(By.className("edit-btn"));
        editButton.click();


        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.click();
        noteTitle.clear();
        noteTitle.sendKeys(randomTitle+"edited");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.click();
        noteDescription.sendKeys("Lorem ipsum dolar sit edited");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSaveChangesBtn")));
        noteSaveChangesBtn = driver.findElement(By.id("noteSaveChangesBtn"));
        noteSaveChangesBtn.click();

        // now checking if value is edited

        driver.get("http://localhost:" + this.port + "/");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        navNotesTab = driver.findElement(By.id("nav-notes-tab"));
        navNotesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));
        noteTable = driver.findElement(By.id("noteTable"));

        rows = noteTable.findElements(By.tagName("tr"));

        WebElement row = rows.get(rows.size()-1);

        WebElement titleCol = row.findElement(By.tagName("th"));
        Assertions.assertEquals(randomTitle+"edited", titleCol.getText());

        // checking delete
        WebElement buttonsCol = row.findElement(By.tagName("td"));
        WebElement deleteButton = firstColumn.findElement(By.className("delete-btn"));
        deleteButton.click();

        driver.get("http://localhost:" + this.port + "/");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        navNotesTab = driver.findElement(By.id("nav-notes-tab"));
        navNotesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));
        noteTable = driver.findElement(By.id("noteTable"));

        rows = noteTable.findElements(By.tagName("tr"));

        row = rows.get(rows.size()-1);

        titleCol = row.findElement(By.tagName("th"));

        Assertions.assertNotEquals(randomTitle+"edited",titleCol.getText());
    }

}
