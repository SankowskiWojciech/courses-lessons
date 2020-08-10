package com.github.sankowskiwojciech.courseslessons.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DateToLocalDateTimeTest {

    private final DateToLocalDateTime testee = DateToLocalDateTime.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        Date dateStub = new Date(1996, 01, 16, 10, 10, 10);

        //when
        LocalDateTime localDateTime = testee.apply(dateStub);

        //then
        assertNotNull(localDateTime);
        assertEquals(dateStub.getYear() + 1900, localDateTime.getYear());
        assertEquals(dateStub.getMonth() + 1, localDateTime.getMonthValue());
        assertEquals(dateStub.getDate(), localDateTime.getDayOfMonth());
        assertEquals(dateStub.getHours(), localDateTime.getHour());
        assertEquals(dateStub.getMinutes(), localDateTime.getMinute());
        assertEquals(dateStub.getSeconds(), localDateTime.getSecond());
    }
}