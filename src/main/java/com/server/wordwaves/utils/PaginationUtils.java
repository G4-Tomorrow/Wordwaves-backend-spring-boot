package com.server.wordwaves.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;

public class PaginationUtils {

    public static <T, R> PaginationInfo<List<R>> getAllEntities(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String searchQuery,
            List<String> validSortByFields,
            Function<Pageable, Page<T>> searchFunction,
            Function<T, R> mapFunction)
            throws AppException {
        validatePaginationParameters(pageNumber, pageSize, sortBy, sortDirection, validSortByFields);

        int pageIndex = pageNumber - 1;
        Sort sort = (sortBy == null || sortBy.isEmpty())
                ? Sort.unsorted()
                : Sort.by(Sort.Direction.fromString(sortDirection.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        Page<T> pageData = searchFunction.apply(pageable);

        List<R> content = pageData.getContent().stream().map(mapFunction).collect(Collectors.toList());

        return createPaginationInfo(pageData, content, pageNumber, pageSize, sortBy, sortDirection, searchQuery);
    }

    public static <R> PaginationInfo<List<R>> createPaginationInfo(
            Page<?> pageData,
            List<R> content,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String searchQuery) {
        Pagination pagination = Pagination.builder()
                .pageNumber(pageNumber)
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

    public static void validatePaginationParameters(
            int pageNumber, int pageSize, String sortBy, String sortDirection, List<String> validSortByFields) {
        if (pageNumber < 1) throw new AppException(ErrorCode.INVALID_PAGE_NUMBER);
        if (pageSize < 1) throw new AppException(ErrorCode.INVALID_PAGE_SIZE);
        if (sortDirection != null && !sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC"))
            throw new AppException(ErrorCode.INVALID_SORT_DIRECTION);
        if (sortBy != null && !validSortByFields.contains(sortBy)) throw new AppException(ErrorCode.INVALID_SORT_BY);
    }
}
