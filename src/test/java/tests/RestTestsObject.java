package tests;
import models.lombok.*;
import org.junit.jupiter.api.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static specs.SpecsList.*;

public class RestTestsObject {
    private static String userId;

    @BeforeEach
    void setUp() {
        if (userId == null) {
            checkCreateUserTest(); // Создаем пользователя, если его нет
        }
    }

    @Test
    @DisplayName("Проверка запроса POST на регистрацию")
    void checkTokenObjectTest() {


        RegistrationRequestLombokTehModel authdata = new RegistrationRequestLombokTehModel();
        authdata.setEmail("eve.holt@reqres.in");
        authdata.setPassword("cityslicka");

        RegistrationResponseLombokTehModel responseModel = step("Make response", () ->
                given(registrationRequestSpec)
                        .body(authdata)
                        .when()
                        .post("/register")
                        .then()
                        .spec(registrationResponseSpec)
                        .extract().as(RegistrationResponseLombokTehModel.class)
        );

        step("Verify response Token", () ->
                assertThat(responseModel.getToken())
                        .as("Token should not be null or empty")
                        .isNotNull()
                        .isNotEmpty());
        step("Verify response ID", () ->
                assertThat(responseModel.getId())
                        .as("ID should not be zero")
                        .isNotZero());

    }

    @Test
    @DisplayName("Проверка запроса GET на получение списка пользователей")
    void checkListUsersTest() {
        ListUserResponseLombokTehModel response = step("Make request and get response", () ->
                given(registrationRequestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .spec(registrationResponseSpec)
                        .extract().as(ListUserResponseLombokTehModel.class)
        );

        step("Verify response per_page", () ->
                assertThat(response.getPerPage()).isEqualTo(6)
        );

        step("Verify response support.text", () ->
                assertThat(response.getSupport().getText()).contains("Content Caddy")
        );
    }

    @Test
    @DisplayName("Проверка запроса PUT на редактирование пользователя")
    void checkUpdateUserTest() {
        СhangeUserRequestLombokTehModel upData = new СhangeUserRequestLombokTehModel();
        upData.setName("morpheusТЕСТ");
        upData.setJob("zion residentТЕСТ");
        ChangeUserResponseLombokTehModel response = step("Отправка PUT запроса", () ->
                given(registrationRequestSpec)
                        .body(upData)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(registrationResponseSpec)
                        .extract()
                        .as(ChangeUserResponseLombokTehModel.class)
        );

        // Проверяем ответ
        step("Проверка имени в ответе", () ->
                assertThat(response.getName()).isEqualTo("morpheusТЕСТ")
        );

        step("Проверка должности в ответе", () ->
                assertThat(response.getJob()).isEqualTo("zion residentТЕСТ")
        );
    }


    @Test
    @DisplayName("Проверка запроса POST на создание пользователя")
    void checkCreateUserTest() {
        // 1. Подготовка тестовых данных
        CreateUserRequestLombokTehModel requestData = new CreateUserRequestLombokTehModel();
        requestData.setName("Mariay Grishina");
        requestData.setJob("engineer");

        // 2. Отправка запроса и получение ответа
        CreateUserResponseLombokTehModel response = step("Создание пользователя", () ->
                given(registrationRequestSpec)
                        .body(requestData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createUserResponseSpec)
                        .extract()
                        .as(CreateUserResponseLombokTehModel.class)
        );

        // 3. Проверки ответа
        step("Проверка данных ответа", () -> {
            assertThat(response.getName()).isEqualTo("Mariay Grishina");
            assertThat(response.getJob()).isEqualTo("engineer");
            assertThat(response.getId()).isNotBlank();
            assertThat(response.getCreatedAt()).isNotBlank();
        });

        // Сохраняем ID для последующих тестов
        userId = response.getId();
        System.out.println("Created user ID: " + userId);
    }

    @Test
    @DisplayName("Проверка запроса DELETE на удаление пользователя")
    void checkDeleteUserTest() {
        step("Удаление пользователя", () ->
                given(registrationRequestSpec)
                        .pathParam("userId", userId)
                        .when()
                        .delete("/users/{userId}")
                        .then()
                        .spec(deleteUserResponseSpec)
                        .body(equalTo(""))
        );

        step("Проверка что пользователь удален", () -> {
            given(registrationRequestSpec)
                    .when()
                    .get("/users/" + userId)
                    .then()
                    .statusCode(404);
        });
    }
}