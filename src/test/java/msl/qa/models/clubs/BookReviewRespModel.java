package msl.qa.models.clubs;

public record BookReviewRespModel(
        Integer id,
        Integer club,
        ReviewUserRespModel user,
        String review,
        Integer assessment,
        Integer readPages,
        String created,
        String modified
) {
}

