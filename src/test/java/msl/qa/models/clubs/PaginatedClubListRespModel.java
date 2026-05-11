package msl.qa.models.clubs;

import java.util.List;

public record PaginatedClubListRespModel(
        Integer count,
        String next,
        String previous,
        List<ClubRespModel> results
) {
}

