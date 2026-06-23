package msl.qa.models.clubs.review;

public record ReviewReqModel(Integer club,
                             String review,
                             Integer assessment,
                             Integer readPages) {
}
