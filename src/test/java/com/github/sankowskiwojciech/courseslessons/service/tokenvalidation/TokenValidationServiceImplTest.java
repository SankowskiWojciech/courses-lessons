package com.github.sankowskiwojciech.courseslessons.service.tokenvalidation;

import com.github.sankowskiwojciech.courseslessons.backend.repository.TokenRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidTokenDetailedException;
import com.github.sankowskiwojciech.courseslessons.stub.TokenEntityStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TOKEN_VALUE_STUB;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenValidationServiceImplTest {

    private final TokenRepository tokenRepositoryMock = mock(TokenRepository.class);
    private final TokenValidationService testee = new TokenValidationServiceImpl(tokenRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tokenRepositoryMock);
    }

    @Test(expected = InvalidTokenDetailedException.class)
    public void shouldThrowInvalidTokenExceptionWhenTokenIsNotFound() {
        //given
        String tokenValueStub = TOKEN_VALUE_STUB;
        when(tokenRepositoryMock.findByTokenValue(eq(tokenValueStub))).thenReturn(Optional.empty());

        //when
        try {
            TokenEntity tokenEntity = testee.validateToken(tokenValueStub);
        } catch (InvalidTokenDetailedException e) {

            //then exception is thrown
            verify(tokenRepositoryMock).findByTokenValue(eq(tokenValueStub));
            throw e;
        }
    }

    @Test(expected = InvalidTokenDetailedException.class)
    public void shouldThrowInvalidTokenExceptionWhenTokenIsRevoked() {
        //given
        String tokenValueStub = TOKEN_VALUE_STUB;
        TokenEntity tokenEntityStub = TokenEntityStub.create(LocalDateTime.now().plusHours(1), LocalDateTime.now());
        when(tokenRepositoryMock.findByTokenValue(eq(tokenValueStub))).thenReturn(Optional.of(tokenEntityStub));

        //when
        try {
            TokenEntity tokenEntity = testee.validateToken(tokenValueStub);
        } catch (InvalidTokenDetailedException e) {

            //then exception is thrown
            verify(tokenRepositoryMock).findByTokenValue(eq(tokenValueStub));
            throw e;
        }
    }

    @Test(expected = InvalidTokenDetailedException.class)
    public void shouldThrowInvalidTokenExceptionWhenTokenIsExpired() {
        //given
        String tokenValueStub = TOKEN_VALUE_STUB;
        TokenEntity tokenEntityStub = TokenEntityStub.create(LocalDateTime.now().minusHours(1), null);
        when(tokenRepositoryMock.findByTokenValue(eq(tokenValueStub))).thenReturn(Optional.of(tokenEntityStub));

        //when
        try {
            testee.validateToken(tokenValueStub);
        } catch (InvalidTokenDetailedException e) {

            //then exception is thrown
            verify(tokenRepositoryMock).findByTokenValue(eq(tokenValueStub));
            throw e;
        }
    }

    @Test(expected = InvalidTokenDetailedException.class)
    public void shouldThrowInvalidTokenExceptionWhenTokenIsNotIssuedForProvidedUser() {
        //given
        String tokenValueStub = TOKEN_VALUE_STUB;
        String userEmailAddressStub = STUDENT_EMAIL_ADDRESS_STUB;
        TokenEntity tokenEntityStub = TokenEntityStub.create(LocalDateTime.now().plusHours(1), null);
        when(tokenRepositoryMock.findByTokenValue(eq(tokenValueStub))).thenReturn(Optional.of(tokenEntityStub));

        //when
        try {
            TokenEntity tokenEntity = testee.validateTokenAndUser(tokenValueStub, userEmailAddressStub);
        } catch (InvalidTokenDetailedException e) {

            //then exception is thrown
            verify(tokenRepositoryMock).findByTokenValue(eq(tokenValueStub));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenTokenIsValid() {
        //given
        String tokenValueStub = TOKEN_VALUE_STUB;
        TokenEntity tokenEntityStub = TokenEntityStub.create(LocalDateTime.now().plusHours(1), null);
        when(tokenRepositoryMock.findByTokenValue(eq(tokenValueStub))).thenReturn(Optional.of(tokenEntityStub));

        //when
        TokenEntity tokenEntity = testee.validateToken(tokenValueStub);

        //then nothing happens
        verify(tokenRepositoryMock).findByTokenValue(eq(tokenValueStub));
        assertEquals(tokenEntityStub, tokenEntity);
    }
}