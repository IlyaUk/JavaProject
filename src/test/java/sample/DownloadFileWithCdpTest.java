package sample;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.devtools.v124.browser.Browser.SetDownloadBehaviorBehavior.ALLOWANDNAME;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.HasDownloads;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v124.browser.Browser;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DownloadFileWithCdpTest {

  private WebDriver driver;

  @AfterEach
  void closeDriver() {
    if (driver != null) {
      driver.quit();
    }
  }

  @RepeatedTest(25)
  void downloadPngSampleFileFromPublicResource() throws IOException {
    ChromeOptions options = new ChromeOptions();
    options.setEnableDownloads(true);
    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
    driver.manage().timeouts().implicitlyWait(ofSeconds(60));
    driver.manage().window().maximize();
    driver.get("https://the-internet.herokuapp.com/download");
    new DownloadPage().clickDownloadXlsButton();
    List<String> files = ((HasDownloads) driver).getDownloadableFiles();
    Assertions.assertFalse(files.isEmpty(), "No files were downloaded");
  }

  class DownloadPage {
    private final WebDriverWait wait = new WebDriverWait(driver, ofSeconds(60));
    private final By downloadXls = new ByXPath("//*[@class='example']//a[text()='webdriverIO.png']");

    public void clickDownloadXlsButton() throws IOException {
      driver = new Augmenter().augment(driver);
      DevTools devTools = ((HasDevTools) driver).getDevTools();
      devTools.createSession();

      devTools.send(
          Browser.setDownloadBehavior(
              ALLOWANDNAME,
              Optional.empty(),
              Optional.of(""),
              Optional.of(true)));

      AtomicBoolean completed = new AtomicBoolean(false);
      devTools.addListener(
          Browser.downloadProgress(),
          e -> completed.set(Objects.equals(e.getState().toString(), "completed")));

      wait.until(ExpectedConditions.elementToBeClickable(downloadXls));
      driver.findElement(downloadXls).click();

      Assertions.assertDoesNotThrow(() -> wait.until(_d -> completed));
    }
  }
}