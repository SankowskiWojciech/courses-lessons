package com.github.sankowskiwojciech.courseslessons.stub;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseEntityStub {
    public static <T> ResponseEntity<T> create(T responseBody) {
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
