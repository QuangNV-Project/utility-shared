package com.quangnv.service.utility_shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class PageResponse<T> {
    int currentPage; // current page
    int totalPages; // total page
    int pageSize;   // current page size
    long totalElements; // total element

    @Builder.Default
    List<T> data = Collections.emptyList();

    public static <T, U> PageResponse<U> fromPage(Page<T> page, Function<T, U> mapperFunction) {
        return PageResponse.<U>builder()
                .currentPage(page.getNumber()+1)
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .data(page.getContent().stream().map(mapperFunction).collect(Collectors.toList()))
                .build();
    }
}
