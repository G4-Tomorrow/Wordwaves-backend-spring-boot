package com.server.wordwaves.service;

import com.server.wordwaves.dto.response.common.PaginationInfo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public interface BaseService<T, R> {

    PaginationInfo<List<R>> getAll(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String searchQuery,
            Function<String, Page<T>> searchFunction,
            Function<Page<T>, List<R>> mapFunction
    );
}
