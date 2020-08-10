package com.github.sankowskiwojciech.courseslessons.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateTimeToDate implements Function<LocalDateTime, Date> {

    private static final LocalDateTimeToDate INSTANCE = new LocalDateTimeToDate();

    @Override
    public Date apply(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTimeToDate getInstance() {
        return INSTANCE;
    }
}
