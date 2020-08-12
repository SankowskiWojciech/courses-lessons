package com.github.sankowskiwojciech.courseslessons.service.tokenvalidation;

import com.github.sankowskiwojciech.courseslessons.backend.repository.TokenRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidTokenException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {

    private final TokenRepository tokenRepository;

    @Override
    public void validateToken(String tokenValue) {
        TokenEntity tokenEntity = readTokenByTokenValue(tokenValue);
        validateIfTokenIsNotRevoked(tokenEntity.getRevocationDateTime());
        validateIfTokenIsNotExpired(tokenEntity.getExpirationDateTime());
    }

    @Override
    public void validateTokenAndUser(String tokenValue, String providedUserEmailAddress) {
        TokenEntity tokenEntity = readTokenByTokenValue(tokenValue);
        validateIfTokenIsNotRevoked(tokenEntity.getRevocationDateTime());
        validateIfTokenIsNotExpired(tokenEntity.getExpirationDateTime());
        validateIfTokenIsIssuedForProvidedUser(tokenEntity.getUserEmailAddress(), providedUserEmailAddress);
    }

    private TokenEntity readTokenByTokenValue(String tokenValue) {
        return tokenRepository.findByTokenValue(tokenValue).orElseThrow(InvalidTokenException::new);
    }

    private void validateIfTokenIsNotRevoked(LocalDateTime revocationDateTime) {
        if (revocationDateTime != null) {
            throw new InvalidTokenException();
        }
    }

    private void validateIfTokenIsNotExpired(LocalDateTime expirationDateTime) {
        if (expirationDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException();
        }
    }

    private void validateIfTokenIsIssuedForProvidedUser(String userEmailAddressFromToken, String providedUserEmailAddress) {
        if (!userEmailAddressFromToken.equals(providedUserEmailAddress)) {
            throw new InvalidTokenException();
        }
    }
}
