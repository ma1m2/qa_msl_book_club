package msl.qa.models.clubs.review;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReviewReqModel(Integer club,
                             String review,
                             Integer assessment,
                             Integer readPages) {
}
