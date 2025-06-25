package tests.lesson_16;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeAll;


public class TestBase {

    @BeforeAll
    static void installConfiguration() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

}

