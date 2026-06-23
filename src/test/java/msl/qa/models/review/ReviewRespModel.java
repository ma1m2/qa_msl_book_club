package msl.qa.models.review;

import java.time.Instant;

public record ReviewRespModel(
        Integer id,
        Integer club,
        UserReviewRespModel user,
        String review,
        Integer assessment,
        Integer readPages,
        String created,
        String modified) {
}
