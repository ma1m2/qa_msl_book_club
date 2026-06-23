package msl.qa.models.clubs;

import msl.qa.models.clubs.review.BookReviewRespModel;

import java.util.List;

public record CreateClubRespModel(
        Integer id,
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink,
        Integer owner,
        List<Integer> members,
        List<BookReviewRespModel> reviews,
        String created,
        String modified) {
}
