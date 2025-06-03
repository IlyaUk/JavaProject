package sample;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SelenideWithRemoteGridTest {

  private WebDriver driver;

  @AfterEach
  void closeDriver() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void downloadPngSampleFileFromPublicResource() throws IOException {
    ChromeOptions options = new ChromeOptions();
    options.setEnableDownloads(true);
    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
    WebDriverRunner.setWebDriver(driver);
    Selenide.open("https://the-internet.herokuapp.com/download");
    new DownloadPage().clickDownloadXlsButton();
  }

  static class DownloadPage {

    public void clickDownloadXlsButton() {
      $(By.xpath("//*[@class='example']//a[text()='screenshot.png']")).click();
      $(By.xpath("//*[@class='example']//a[text()='sample_upload.txt']")).click();
      $(By.xpath("//*[@class='example']//a[text()='tk.txt']")).click();
      $(By.xpath("//*[@class='example']//a[text()='screenshot.png']")).click();
      $(By.xpath("//*[@class='example']//a[text()='sample_upload.txt']")).click();
      $(By.xpath("//*[@class='example']//a[text()='tk.txt']")).click();
    }
  }
}