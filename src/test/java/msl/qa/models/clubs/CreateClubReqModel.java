package msl.qa.models.clubs;

public record CreateClubReqModel(
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink) {
}
