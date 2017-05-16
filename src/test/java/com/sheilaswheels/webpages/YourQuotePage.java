package com.sheilaswheels.webpages;

import com.sheilaswheels.domain.InsuranceData;
import com.sheilaswheels.domain.enumType.BoostCover;
import com.sheilaswheels.utility.Driver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

/**
 * Created by bhawana on 09/05/2017.
 */
public class YourQuotePage {
    private final WebDriver aDriver;
    private JavascriptExecutor javascriptExecutor;

    @FindBy(how = How.XPATH , using = "//h1[contains(text(),'Thank you')]")
    private WebElement thankYouMessage;

    @FindBy(how = How.CSS , using = "#premiumAmountInstallments")
    private WebElement monthlyCost;

    @FindBy(how = How.CSS , using = "#premiumAmount")
    private WebElement annualPremium;

    @FindBy(how = How.CSS , using = "div#bundle1 h3")
    private WebElement fullProtectionOption;

    @FindBy(how = How.CSS , using = "div#bundle2 h3")
    private WebElement justInCaseOption;

    @FindBy(how = How.CSS , using = "div#bundle3 h3")
    private WebElement motorLegalOption;

    @FindBy(how = How.CSS , using = "div#bundle4 h3")
    private WebElement noExtraCoverOption;

    @FindBy(how = How.ID , using = "radio_bundleSelectRadio_1")
    private WebElement fullProtection;

    @FindBy(how = How.ID , using = "radio_bundleSelectRadio_2")
    private WebElement justInCase;

    @FindBy(how = How.ID , using = "radio_bundleSelectRadio_3")
    private WebElement motorLegalProtection;

    @FindBy(how = How.ID , using = "radio_bundleSelectRadio_4")
    private WebElement noExtraCover;

    @FindBy(how = How.CSS , using = "input#mobRecalculate")
    private WebElement recalculate;

    @FindBy(how = How.CSS , using = "div#recalcDialog p")
    private WebElement recalculateDialog;

    @FindBy(how = How.CSS , using = "input#ncdProtectionFlag_0")
    private WebElement NCDProtectionYes;

    @FindBy(how = How.CSS , using = "input#ncdProtectionFlag_1")
    private WebElement NCDProtectionNo;

    @FindBy(how = How.CSS , using = "select#voluntaryExcessLimit")
    private WebElement voluntaryExcess;

    @FindBy(how = How.CSS , using = "input#next")
    private WebElement next;

    public YourQuotePage(WebDriver aDriver) {
        this.aDriver = aDriver;
        PageFactory.initElements(aDriver,this);
        javascriptExecutor = (JavascriptExecutor)aDriver;
    }

    private void waitForInsuranceToBeRecalculated() {
        new WebDriverWait(aDriver,30).until(ExpectedConditions.invisibilityOf(recalculateDialog));
    }

    private void moveToNextPage() {
        next.click();
    }

    private void selectYourExcessProtection() {
       int bound =  new Select(voluntaryExcess).getOptions().size();
       new Random().nextInt(bound);
    }

    private void selectNCDProtection(boolean ncdProtection) {
         Driver.waitForPageElementToLoad(thankYouMessage);
        (ncdProtection?NCDProtectionYes:NCDProtectionNo).click();
    }

    private void clickOnRecalculate() {
        Driver.waitForPageElementToLoad(recalculate);
        recalculate.click();
    }

    private void boostYourCoverWithOptionalExtras(BoostCover boostCover) {
        if (fullProtectionOption.getText().equals(boostCover.getValue()))
        {
            javascriptExecutor.executeScript("arguments[0].click();", fullProtection);
        }
        else
            if (justInCaseOption.getText().equals(boostCover.getValue()))
            {
                javascriptExecutor.executeScript("arguments[0].click();", justInCase);
            }
            else
                if (motorLegalOption.getText().equals(boostCover.getValue()))
                {
                    javascriptExecutor.executeScript("arguments[0].click();", motorLegalProtection);
                }
                else
                    if (noExtraCoverOption.getText().equals(boostCover.getValue()))
                    {
                        javascriptExecutor.executeScript("arguments[0].click();", noExtraCover);
                    }
    }

    private void getAnnualPremiumFromQuote(InsuranceData.YourQuote yourQuote) {
        System.out.println(annualPremium.getText());
        yourQuote.getBoostInsuranceCover().setAnnualPremium(Double.parseDouble(annualPremium.getText()));
        System.out.println(yourQuote.getBoostInsuranceCover().getAnnualPremium());
    }

    public void selectExtraBoostOptionsAndRecalculateQuote(InsuranceData.YourQuote yourQuote) {
        Driver.waitForPageElementToLoad(thankYouMessage);
        // getAnnualPremiumFromQuote(yourQuote);
        boostYourCoverWithOptionalExtras(yourQuote.getBoostInsuranceCover().getBoostCover());
        clickOnRecalculate();
        waitForInsuranceToBeRecalculated();
        selectNCDProtection(yourQuote.getBoostInsuranceCover().isNcdProtection());
        selectYourExcessProtection();
        moveToNextPage();
    }
}
