package msl.qa.models.user;

import com.fasterxml.jackson.annotation.JsonInclude;
/*
@JsonInclude(JsonInclude.Include.NON_NULL) С этой аннотацией Jackson делает так,
что поля со значением null полностью пропускаются и не попадают в JSON
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // ← очень важно!
public record PatchUserReqModel(
        String username,
        String firstName,
        String lastName,
        String email) {
}
