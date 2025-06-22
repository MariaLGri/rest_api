package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class RestTests {
    private String userId;
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Проверка запроса POST на регистрацию")
    void checkTokenTest() {
        String authdata = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .header("x-api-key", "reqres-free-v1")//тестовый ключ для доступа к API Reqres (фейковый сервис для практики тестирования)
                .body(authdata)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/register")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
                .body("token", not(emptyOrNullString()))
                .body("id", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("Проверка запроса GET на получение списка пользователей")
    void checkListUsersTest() {
        given()
                .header("x-api-key", "reqres-free-v1")//тестовый ключ для доступа к API Reqres (фейковый сервис для практики тестирования)
                .log().uri()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
                .body("per_page", equalTo(6))
                .body("support.text", equalTo("Tired of writing endless social media content? Let Content Caddy generate it for you."));
    }

    @Test
    @DisplayName("Проверка запроса PUT на редактирование пользователя")
    void checkUpdateUserTest() {
        String Update = "{\"name\": \"morpheusТЕСТ\", \"job\": \"zion resident\"}";
        given()
                .header("x-api-key", "reqres-free-v1")//тестовый ключ для доступа к API Reqres (фейковый сервис для практики тестирования)
                .body(Update)
                .contentType(JSON)
                .log().uri()
                .when()
                .put("/users/426")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
                .body("name", equalTo("morpheusТЕСТ"))
                .body("job", equalTo("zion resident"));
    }
    @Test
    @DisplayName("Проверка запроса PUT на редактирование пользователя")
    void checkPatchUpdateUserTest() {
        String Update = "{\"name\": \"morpheusТЕСТ22\", \"job\": \"zion resident\"}";
        given()
                .header("x-api-key", "reqres-free-v1")//тестовый ключ для доступа к API Reqres (фейковый сервис для практики тестирования)
                .body(Update)
                .contentType(JSON)
                .log().uri()
                .when()
                .put("/users/426")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
                .body("name", equalTo("morpheusТЕСТ22"))
                .body("job", equalTo("zion resident"));
    }
    @Test
    @DisplayName("Проверка запроса POST на создание пользователя")
    void checkCreateUserTest() {
        String сreateDate = "{\"name\": \"Mariay Grishina\", \"job\": \"engineer\"}";
        userId = given()
                .header("x-api-key", "reqres-free-v1")//тестовый ключ для доступа к API Reqres (фейковый сервис для практики тестирования)
                .body(сreateDate)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/users")
                .then()
                .log().status()
                .statusCode(201)
                .log().body()
                .body("name", equalTo("Mariay Grishina"))
                .body("job", equalTo("engineer"))
                .body("id", not(emptyOrNullString()))
                .body("createdAt", not(emptyOrNullString()))
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Проверка запроса Delete на удаление пользователя")
    void checkDeleteUserTest() {
        given()
                .header("x-api-key", "reqres-free-v1")//тестовый ключ для доступа к API Reqres (фейковый сервис для практики тестирования)
                .log().uri()
                .when()
                .delete("/users/" + userId)
                .then()
                .log().status()
                .statusCode(204)
                .body(equalTo(""));


    }

}