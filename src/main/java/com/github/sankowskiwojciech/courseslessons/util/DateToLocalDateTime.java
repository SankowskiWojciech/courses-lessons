package com.github.sankowskiwojciech.courseslessons.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateToLocalDateTime implements Function<Date, LocalDateTime> {

    private static final DateToLocalDateTime INSTANCE = new DateToLocalDateTime();

    @Override
    public LocalDateTime apply(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static DateToLocalDateTime getInstance() {
        return INSTANCE;
    }
}
