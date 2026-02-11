package com.quangnv.service.utility_shared.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.quangnv.service.utility_shared.constant.SortDirection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class PageRequestCustom {
    Integer page;

    Integer size;

    String sortBy;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    SortDirection sortDirection = SortDirection.DESC;

    public Integer getOffset() {
        if (page == null || size == null) {
            return 0;
        }
        return (page - 1) * size;
    }
}
