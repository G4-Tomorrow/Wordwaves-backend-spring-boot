package com.server.wordwaves.service.implement;

import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.service.BaseService;
import com.server.wordwaves.utils.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class BaseServiceImp<T, R> implements BaseService<T, R> {

    @Override
    public PaginationInfo<List<R>> getAll(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery,
                                          Function<String, Page<T>> searchFunction,
                                          Function<Page<T>, List<R>> mapFunction) {
        Page<T> pageData = searchFunction.apply(searchQuery);

        List<R> content = mapFunction.apply(pageData);

        return PaginationUtils.createPaginationInfo(pageData, content, pageNumber, pageSize, sortBy, sortDirection, searchQuery);
    }
}
