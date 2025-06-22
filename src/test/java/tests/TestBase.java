package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class TestBase {

    @BeforeAll
    static void installСonfiguration() {
        Configuration.reportsFolder = "build/reports";
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        Configuration.pageLoadStrategy = "eager";
        Configuration.remote = String.format(
                "https://%s:%s@%s/wd/hub",
                System.getProperty("selenoidUserLogin", "user1"),
                System.getProperty("selenoidUserPassword", "1234"),
                System.getProperty("selenoidUrl", "selenoid.autotests.cloud")
        );


        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;

    }

}

