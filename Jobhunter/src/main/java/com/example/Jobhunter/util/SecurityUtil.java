package com.example.Jobhunter.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jwt.JWT;

@Service

// Hàm tạo JWT sau khi user đăng nhập thành công.
public class SecurityUtil {

    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_Algorithm = MacAlgorithm.HS512;

    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtkey;

    @Value("${hoidanit.jwt.token-validity-in-seconds}")
    private long jwtExpiration;

    public String createToken(Authentication authentication) {

        Instant now = Instant.now(); // now: thời điểm phát hành token.
        Instant validity = now.plus(this.jwtExpiration, ChronoUnit.SECONDS); // validity: thời điểm hết hạn = now +
                                                                             // jwtExpiration (ví dụ 1h, 2h, …)

        // @formatter:off
        //Tạo payload (claims)
        JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuedAt(now) // iat: issued at
        .expiresAt(validity) // exp: expiration
        .subject (authentication.getName()) // sub: định danh (thường là username)
        .claim ("hoidanit", authentication) //custom claim (tự nhét thêm data)
        .build();

        //Tạo header (thuật toán ký)
        JwsHeader jwsHeader = JwsHeader.with(JWT_Algorithm).build();

        //Encode thành token
        // JwtEncoder (Spring Security cung cấp) sẽ:
        // Lấy header + payload.
        // Dùng jwtkey (secret key bạn config) + thuật toán HS512.
        // Sinh ra chữ ký.
        // Nối lại thành JWT chuẩn: header.payload.signature
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
        claims)).getTokenValue();
    }
}
