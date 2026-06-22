package msl.qa.models.localstorage;

import com.fasterxml.jackson.annotation.JsonProperty;
import msl.qa.models.register.RegistrationRespModel;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public record LocalStorageAuthReqModel(
        @JsonProperty("user") RegistrationRespModel user,
        @JsonProperty("accessToken") String access,
        @JsonProperty("refreshToken") String refresh,
        @JsonProperty("isAuthenticated") boolean authenticated
) {

  private static final JsonMapper JSON = JsonMapper.builder().build();

  public String toJson() {
    try {
      return JSON.writeValueAsString(this);
    } catch (JacksonException e) {
      throw new RuntimeException(e);
    }
  }

}
