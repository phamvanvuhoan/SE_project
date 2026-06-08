package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public final class PageResponseFactory {

    private PageResponseFactory() {}

    public static <S, T> PagedResponse<T> fromPage(Page<S> page, Function<S, T> mapper) {
        return new PagedResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    public static <T> PagedResponse<T> fromList(List<T> items, int pageNumber, int pageSize) {
        int safePage = Math.max(0, pageNumber);
        int safeSize = Math.max(1, pageSize);
        int from = Math.min(safePage * safeSize, items.size());
        int to = Math.min(from + safeSize, items.size());
        List<T> content = items.subList(from, to);
        int totalPages = items.isEmpty() ? 0 : (int) Math.ceil((double) items.size() / safeSize);
        return new PagedResponse<>(content, safePage, safeSize, items.size(), totalPages, safePage + 1 >= totalPages);
    }
}
