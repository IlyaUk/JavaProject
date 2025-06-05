package sample;

import static com.codeborne.selenide.DownloadOptions.file;
import static com.codeborne.selenide.FileDownloadMode.CDP;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import com.codeborne.selenide.Configuration;

public class SelenideWithRemoteGridTest {
  @Test
  void downloadSampleFile() {
    Configuration.remote = "http://localhost:4444/wd/hub";
    Configuration.browserCapabilities = new ChromeOptions()
      .setEnableDownloads(true)
      .enableBiDi();

    open("https://selenide.org/test-page/download.html");
    File file = new DownloadPage().downloadFile("hello-world.txt");
    assertThat(file).hasName("hello-world.txt");
    assertThat(file).hasContent("Hello, world!");
  }

  static class DownloadPage {
    public File downloadFile(String fileName) {
      return $(byText(fileName)).download(file().withMethod(CDP).withExtension("txt"));
    }
  }
}