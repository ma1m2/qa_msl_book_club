package msl.qa.models.logout;

public record WrongTokenLogoutRespModel(
        String detail,
        String code) {
}
