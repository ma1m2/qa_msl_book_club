package msl.qa.models.clubs.review;

public record BookReviewRespModel(
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

