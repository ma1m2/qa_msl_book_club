package msl.qa.models.user;

public record UpdateUserReqModel(
        String username,
        String firstName,
        String lastName,
        String email) {
}
