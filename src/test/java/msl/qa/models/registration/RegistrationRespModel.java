package msl.qa.models.registration;

public record RegistrationRespModel(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr) {
}
