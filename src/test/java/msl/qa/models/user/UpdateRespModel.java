package msl.qa.models.user;

public record UpdateRespModel(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr) {
}
