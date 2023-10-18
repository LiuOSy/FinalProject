package com.liuosy.kanbantests.ui;

import com.liuosy.kanbantests.common.BaseUITest;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class CreateTaskTest extends BaseUITest {

//    String newTaskName = String.format("LS New Task + %s", tmst);

    //Precondition - Creating User and Project. Connecting them.
    @BeforeClass(alwaysRun = true)
    public void dataSetUp() throws InterruptedException {
        createUserAndProject();
        loginToTheApplication(newUserName, newUserPassword);
    }

    @Feature("UI Testing")
    @Test(testName = "UI Task Creation - Happy Pass", groups = "UITests")
    public void createNewTask() {

        //UI Test - Creating Task - Happy pass

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement projectLink = driver.findElement(By.xpath("//a[contains(text(), 'LS Test Project')]"));
        wait.until(elementToBeClickable(projectLink));
        projectLink.click();

        WebElement selectButton = driver.findElement(By.xpath("//i[@class=\"fa fa-cog\"]"));
        wait.until(elementToBeClickable(selectButton));
        selectButton.click();


        WebElement addNewTaskButton = driver.findElement(By.xpath("//ul[@class=\"dropdown-submenu-open\"]//i[@class=\"fa fa-plus fa-fw js-modal-large\"]"));
        wait.until(elementToBeClickable(addNewTaskButton));
        addNewTaskButton.click();

        WebElement taskTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id=\"form-title\"]")));
        taskTitle.clear();
        taskTitle.sendKeys(newTaskName);
        WebElement saveTaskButton = driver.findElement(By.xpath("//button[@type=\"submit\"]"));
        wait.until(elementToBeClickable(saveTaskButton));
        saveTaskButton.click();


        WebElement taskCreated = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"task-board-title\"]//a[contains(text(), 'LS New Task')]")));
        String taskTitleActual = taskCreated.getText();
        Assert.assertEquals(taskTitleActual, newTaskName);

    }

        @AfterClass(alwaysRun = true)
        public void dataSetupRemoval(){
        deleteProject(projectId);
        deleteUser();
        driver.quit();

    }
}
