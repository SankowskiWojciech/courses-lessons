package com.github.sankowskiwojciech.courseslessons.backend.tokenvalidation;

import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidTokenException;
import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationInput;
import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationResponse;
import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationResult;
import com.github.sankowskiwojciech.courseslessons.stub.ResponseEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TokenValidationInputStub;
import com.github.sankowskiwojciech.courseslessons.stub.TokenValidationResponseStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenValidationBackendImplTest {

    private final RestTemplate restTemplateMock = mock(RestTemplate.class);
    private final TokenValidationBackend testee = new TokenValidationBackendImpl(restTemplateMock);

    @Before
    public void reset() {
        Mockito.reset(restTemplateMock);
    }

    @Test
    public void shouldDoNothingWhenTokenIsValid() {
        //given
        TokenValidationInput tokenValidationInputStub = TokenValidationInputStub.create();
        ResponseEntity<TokenValidationResponse> responseEntityStub = ResponseEntityStub.create(TokenValidationResponseStub.create(TokenValidationResult.VALID));

        when(restTemplateMock.exchange(any(RequestEntity.class), eq(TokenValidationResponse.class))).thenReturn(responseEntityStub);

        //when
        testee.validateToken(tokenValidationInputStub);

        //then nothing happens
        verify(restTemplateMock).exchange(any(RequestEntity.class), eq(TokenValidationResponse.class));
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionWhenTokenIsInvalid() {
        //given
        TokenValidationInput tokenValidationInputStub = TokenValidationInputStub.create();
        ResponseEntity<TokenValidationResponse> responseEntityStub = ResponseEntityStub.create(TokenValidationResponseStub.create(TokenValidationResult.INVALID));

        when(restTemplateMock.exchange(any(RequestEntity.class), eq(TokenValidationResponse.class))).thenReturn(responseEntityStub);

        //when
        try {
            testee.validateToken(tokenValidationInputStub);
        } catch (InvalidTokenException e) {

            //then exception is thrown
            verify(restTemplateMock).exchange(any(RequestEntity.class), eq(TokenValidationResponse.class));
            throw e;
        }
    }
}