package com.github.sankowskiwojciech.courseslessons.service.tokenvalidation;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.TokenRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidTokenException;
import com.github.sankowskiwojciech.coursestestlib.stub.TokenEntityStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.TOKEN_VALUE_STUB;
import static org.junit.Assert.assertEquals;
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

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionWhenTokenIsNotFound() {
        //given
        String valueStub = TOKEN_VALUE_STUB;
        when(tokenRepositoryMock.findByTokenValue(valueStub)).thenReturn(Optional.empty());

        //when
        try {
            TokenEntity token = testee.validateToken(valueStub);
        } catch (InvalidTokenException e) {

            //then exception is thrown
            verify(tokenRepositoryMock).findByTokenValue(valueStub);
            throw e;
        }
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionWhenTokenIsRevoked() {
        //given
        String valueStub = TOKEN_VALUE_STUB;
        TokenEntity entityStub = TokenEntityStub.create(LocalDateTime.now().plusHours(1), LocalDateTime.now());
        when(tokenRepositoryMock.findByTokenValue(valueStub)).thenReturn(Optional.of(entityStub));

        //when
        try {
            TokenEntity token = testee.validateToken(valueStub);
        } catch (InvalidTokenException e) {

            //then exception is thrown
            verify(tokenRepositoryMock).findByTokenValue(valueStub);
            throw e;
        }
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionWhenTokenIsExpired() {
        //given
        String valueStub = TOKEN_VALUE_STUB;
        TokenEntity entityStub = TokenEntityStub.create(LocalDateTime.now().minusHours(1), null);
        when(tokenRepositoryMock.findByTokenValue(valueStub)).thenReturn(Optional.of(entityStub));

        //when
        try {
            testee.validateToken(valueStub);
        } catch (InvalidTokenException e) {

            //then exception is thrown
            verify(tokenRepositoryMock).findByTokenValue(valueStub);
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenTokenIsValid() {
        //given
        String valueStub = TOKEN_VALUE_STUB;
        TokenEntity entityStub = TokenEntityStub.create(LocalDateTime.now().plusHours(1), null);
        when(tokenRepositoryMock.findByTokenValue(valueStub)).thenReturn(Optional.of(entityStub));

        //when
        TokenEntity token = testee.validateToken(valueStub);

        //then nothing happens
        verify(tokenRepositoryMock).findByTokenValue(valueStub);
        assertEquals(entityStub, token);
    }
}