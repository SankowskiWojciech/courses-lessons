package com.github.sankowskiwojciech.courseslessons.backend.tokenvalidation;

import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidTokenException;
import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationInput;
import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationResponse;
import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationResult;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@AllArgsConstructor
public class TokenValidationBackendImpl implements TokenValidationBackend {

    private static final String TOKEN_VALIDATION_BACKEND_URL = "http://localhost:8090/token/validate";

    private final RestTemplate restTemplate;

    @Override
    public void validateToken(TokenValidationInput tokenValidationInput) {
        RequestEntity<TokenValidationInput> requestEntity = new RequestEntity<>(tokenValidationInput, HttpMethod.POST, URI.create(TOKEN_VALIDATION_BACKEND_URL));
        ResponseEntity<TokenValidationResponse> tokenValidationResponse = restTemplate.exchange(requestEntity, TokenValidationResponse.class);
        checkTokenValidationResult(tokenValidationResponse.getBody());
    }

    private void checkTokenValidationResult(TokenValidationResponse tokenValidationResponse) {
        if (tokenValidationResponse != null
                && TokenValidationResult.INVALID.equals(tokenValidationResponse.getValidationResult())) {
            throw new InvalidTokenException();
        }
    }
}
