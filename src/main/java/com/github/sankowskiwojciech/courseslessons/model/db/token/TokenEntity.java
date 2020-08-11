package com.github.sankowskiwojciech.courseslessons.model.db.token;

import com.github.sankowskiwojciech.courseslessons.model.account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TOKEN")
@EqualsAndHashCode
public class TokenEntity {

    @Id
    @Column(name = "TOKEN_ID", length = 36, nullable = false, unique = true, updatable = false)
    private String tokenId;

    @Setter
    @Column(name = "TOKEN_VALUE", length = 354, nullable = false, updatable = false)
    private String tokenValue;

    @Column(name = "USER_EMAIL_ADDRESS", length = 50, nullable = false, updatable = false)
    private String userEmailAddress;

    @Column(name = "ACCOUNT_TYPE", length = 12, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "RSA_PUBLIC_KEY", length = 212, nullable = false, updatable = false)
    private String rsaPublicKey;

    @Column(name = "CREATION_DATE_TIME", nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @Column(name = "EXPIRATION_DATE_TIME", nullable = false, updatable = false)
    private LocalDateTime expirationDateTime;

    @Column(name = "REVOCATION_DATE_TIME")
    private LocalDateTime revocationDateTime;
}
