package com.quangnv.service.utility_shared.constant;

import lombok.Getter;

@Getter
public enum Duration {
    ONE_MONTH(1),
    THREE_MONTHs(3),
    SIX_MONTHS(6),
    ONE_YEAR(12);

    private final int months;

    Duration(int months) {
        this.months = months;
    }

}
