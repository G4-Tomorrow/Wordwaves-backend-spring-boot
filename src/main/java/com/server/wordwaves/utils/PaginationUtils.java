package com.server.wordwaves.utils;

import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
import org.springframework.data.domain.Page;

import java.util.List;

public class PaginationUtils {

    public static <R> PaginationInfo<List<R>> createPaginationInfo(
            Page<?> pageData,
            List<R> content,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String searchQuery) {

        Pagination pagination = Pagination.builder()
                .pageNumber(pageNumber + 1)
                .pageSize(pageSize)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .build();

        QueryOptions queryOptions = QueryOptions.builder()
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .searchQuery(searchQuery)
                .build();

        return PaginationInfo.<List<R>>builder()
                .pagination(pagination)
                .queryOptions(queryOptions)
                .data(content)
                .build();
    }
}
