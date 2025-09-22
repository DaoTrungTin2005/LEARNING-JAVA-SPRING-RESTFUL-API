package com.example.Jobhunter.util;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jwt.JWT;

@Service

// Hàm tạo JWT sau khi user đăng nhập thành công.
public class SecurityUtil {

    public static final MacAlgorithm JWT_Algorithm = MacAlgorithm.HS512;

    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtkey;

    @Value("${hoidanit.jwt.token-validity-in-seconds}")
    private long jwtkeyExpiration;

    public void createToken(Authentication authentication) {
        // Lấy thông tin user từ Authentication (username, role).
        // Tạo JwtClaimsSet (payload chứa username, role, thời gian hết hạn).
        // Gọi jwtEncoder.encode(claims) → máy encoder sẽ trả về token JWT. (SecurityConfiguration)
        // Trả token này về cho Controller.
    }
}
