package msl.qa.models.login;

import java.util.List;

public record EmptyPasswordLoginRespModel(
        List<String> password) {
}
