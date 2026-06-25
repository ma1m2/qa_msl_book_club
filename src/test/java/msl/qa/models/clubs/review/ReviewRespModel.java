package msl.qa.models.clubs.review;

public record ReviewRespModel(
        Integer id,
        Integer club,
        UserReviewRespModel user,
        String review,
        Integer assessment,
        Integer readPages,
        String created,
        String modified
) {
}

