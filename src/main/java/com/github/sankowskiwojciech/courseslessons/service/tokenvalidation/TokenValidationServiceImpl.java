package com.github.sankowskiwojciech.courseslessons.service.tokenvalidation;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.TokenRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidTokenDetailedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {

    private final TokenRepository tokenRepository;

    @Override
    public TokenEntity validateToken(String tokenValue) {
        TokenEntity tokenEntity = readTokenByTokenValue(tokenValue);
        validateIfTokenIsNotRevoked(tokenEntity.getRevocationDateTime());
        validateIfTokenIsNotExpired(tokenEntity.getExpirationDateTime());
        return tokenEntity;
    }

    @Override
    public TokenEntity validateTokenAndUser(String tokenValue, String providedUserEmailAddress) {
        TokenEntity tokenEntity = readTokenByTokenValue(tokenValue);
        validateIfTokenIsNotRevoked(tokenEntity.getRevocationDateTime());
        validateIfTokenIsNotExpired(tokenEntity.getExpirationDateTime());
        validateIfTokenIsIssuedForProvidedUser(tokenEntity.getUserEmailAddress(), providedUserEmailAddress);
        return tokenEntity;
    }

    private TokenEntity readTokenByTokenValue(String tokenValue) {
        return tokenRepository.findByTokenValue(tokenValue).orElseThrow(InvalidTokenDetailedException::new);
    }

    private void validateIfTokenIsNotRevoked(LocalDateTime revocationDateTime) {
        if (revocationDateTime != null) {
            throw new InvalidTokenDetailedException();
        }
    }

    private void validateIfTokenIsNotExpired(LocalDateTime expirationDateTime) {
        if (expirationDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidTokenDetailedException();
        }
    }

    private void validateIfTokenIsIssuedForProvidedUser(String userEmailAddressFromToken, String providedUserEmailAddress) {
        if (!userEmailAddressFromToken.equals(providedUserEmailAddress)) {
            throw new InvalidTokenDetailedException();
        }
    }
}
