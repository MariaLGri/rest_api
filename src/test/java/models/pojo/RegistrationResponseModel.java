package models.pojo;
public class RegistrationResponseModel {
    private String token;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    // Сеттер (не обязателен, но полезен)
    public void setToken(String token) {
        this.token = token;
    }
}
