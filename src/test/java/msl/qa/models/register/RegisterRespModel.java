package msl.qa.models.register;

public record RegisterRespModel(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr) {
}
