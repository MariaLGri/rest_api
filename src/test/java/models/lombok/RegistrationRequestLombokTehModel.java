package models.lombok;

import lombok.Data;

@Data //под капотом находится pojo (гетеры сетеры)
public class RegistrationRequestLombokTehModel {
    String email,password;

}
