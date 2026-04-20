package msl.qa.models;

public record RegistrationRespModel(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr) {
}
