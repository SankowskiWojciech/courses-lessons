package com.github.sankowskiwojciech.courseslessons.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocalDateTimeToDateTest {

    private final LocalDateTimeToDate testee = LocalDateTimeToDate.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        //when
        Date dateNow = testee.apply(localDateTimeNow);

        //then
        assertNotNull(dateNow);
        assertEquals(localDateTimeNow.getYear() - 1900, dateNow.getYear());
        assertEquals(localDateTimeNow.getMonthValue() - 1, dateNow.getMonth());
        assertEquals(localDateTimeNow.getDayOfMonth(), dateNow.getDate());
        assertEquals(localDateTimeNow.getHour(), dateNow.getHours());
        assertEquals(localDateTimeNow.getMinute(), dateNow.getMinutes());
        assertEquals(localDateTimeNow.getSecond(), dateNow.getSeconds());
    }
}