package msl.qa.models.review;

public record ReviewReqModel(Integer club,
                             String review,
                             Integer assessment,
                             Integer readPages) {
}
