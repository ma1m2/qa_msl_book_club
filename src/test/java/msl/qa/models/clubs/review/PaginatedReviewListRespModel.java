package msl.qa.models.clubs.review;

import java.util.List;

public record PaginatedReviewListRespModel(
        Integer count,
        String next,
        String previous,
        List<ReviewRespModel> results
) {
}

