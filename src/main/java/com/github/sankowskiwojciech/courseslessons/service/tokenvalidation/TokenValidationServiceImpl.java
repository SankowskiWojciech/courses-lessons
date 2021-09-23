package com.github.sankowskiwojciech.courseslessons.service.tokenvalidation;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.TokenRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidTokenException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {

    private final TokenRepository tokenRepository;

    @Override
    public TokenEntity validateToken(String tokenValue) {
        TokenEntity token = readTokenByTokenValue(tokenValue);
        validateIfTokenIsNotRevoked(token.getRevocationDateTime());
        validateIfTokenIsNotExpired(token.getExpirationDateTime());
        return token;
    }

    @Override
    public TokenEntity validateTokenAndUser(String tokenValue, String userEmailAddress) {
        TokenEntity token = readTokenByTokenValue(tokenValue);
        validateIfTokenIsNotRevoked(token.getRevocationDateTime());
        validateIfTokenIsNotExpired(token.getExpirationDateTime());
        validateIfTokenIsIssuedForProvidedUser(token.getUserEmailAddress(), userEmailAddress);
        return token;
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
