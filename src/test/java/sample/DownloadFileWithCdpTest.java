package sample;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.devtools.v136.browser.Browser.SetDownloadBehaviorBehavior.ALLOWANDNAME;
import static org.openqa.selenium.devtools.v136.browser.model.DownloadProgress.State.COMPLETED;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.HasDownloads;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v136.browser.Browser;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadFileWithCdpTest {
  private static final Logger log = LoggerFactory.getLogger(DownloadFileWithCdpTest.class);

  private WebDriver driver;
  private String filename;

  @BeforeEach
  void setUp() throws MalformedURLException {
    ChromeOptions options = new ChromeOptions();
    options.setEnableDownloads(true);
    options.enableBiDi();

    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
    driver.manage().timeouts().implicitlyWait(ofSeconds(5));
    driver.manage().window().maximize();
  }

  @AfterEach
  void closeDriver() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void downloadSampleFile() {
    driver.get("https://selenide.org/test-page/download.html");

    new DownloadPage().clickDownloadButton();
    List<String> files = ((HasDownloads) driver).getDownloadableFiles();
    assertThat(files).as("No files were downloaded").isNotEmpty();
  }

  class DownloadPage {

    private final WebDriverWait wait = new WebDriverWait(driver, ofSeconds(5));
    private final By downloadLink1 = By.linkText("hello-world.txt");
    private final By downloadLink2 = By.linkText("hello-world.pdf");

    public void clickDownloadButton() {
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
          Browser.downloadWillBegin(),
          e -> {
            filename = e.getSuggestedFilename();
            log.info("Download will begin: {}", filename);
          });
      devTools.addListener(
          Browser.downloadProgress(),
          e -> {
            log.info("Download progress: {} {}/{} bytes", e.getState(), e.getReceivedBytes(), e.getTotalBytes());
            completed.set(e.getState() == COMPLETED);
          });

      wait.until(elementToBeClickable(downloadLink1));
      driver.findElement(downloadLink1).click();
      driver.findElement(downloadLink2).click();

      wait.until(_d -> completed.get());
      wait.until(_d -> !((HasDownloads) driver).getDownloadableFiles().isEmpty());
    }
  }
}