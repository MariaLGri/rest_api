package tests.lesson_16;
import models.lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import tests.lesson_16.helpers.TestDataHelper;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static specs.SpecsList.*;


public class RestTestsObjectTest extends TestBase {
    private static String userId;
    private static final Logger logger = LogManager.getLogger(RestTestsObjectTest.class); // Настраивает вывод логов в соответствии с конфигурацией Log4j 2
    @BeforeEach
    void setUp() {
        // Если пользователь не создан, создаем тестового пользователя
        // со стандартными параметрами
        if (userId == null) {
            userId = TestDataHelper.createDefaultTestUser();
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
                        .spec(registrationResponse200Spec)
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
                        .queryParam("page", "2")
                        .get("/users")
                        .then()
                        .spec(registrationResponse200Spec)
                        .extract().as(ListUserResponseLombokTehModel.class)
        );

        step("Проверка в ответе per_page", () ->
                assertThat(response.getPerPage()).isEqualTo(6)
        );

        step("Проверка в ответе support.text содержания текста 'Content Caddy'" , () ->
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
                        .spec(registrationResponse200Spec)
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
    @DisplayName("Проверка запроса Patch на редактирование пользователя")
    void checkPatchUpdateUserTest() {
        СhangeUserRequestLombokTehModel upData = new СhangeUserRequestLombokTehModel();
        upData.setName("morpheusТЕСТ22изменен");
        upData.setJob("zion residentТЕСТ22изменен");
        ChangeUserResponseLombokTehModel response = step("Отправка Patch запроса", () ->
                given(registrationRequestSpec)
                        .body(upData)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(registrationResponse200Spec)
                        .extract()
                        .as(ChangeUserResponseLombokTehModel.class)
        );

        // Проверяем ответ
        step("Проверка имени в ответе", () ->
                assertThat(response.getName()).isEqualTo("morpheusТЕСТ22изменен")
        );

        step("Проверка должности в ответе", () ->
                assertThat(response.getJob()).isEqualTo("zion residentТЕСТ22изменен")
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
                        .spec(createUserResponse201Spec)
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
        logger.info("Created user ID: " + userId); // Вместо System.out.println используем Log4j для более гибкого и профессионального логирования.
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
                        .spec(deleteUserResponse204Spec)
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