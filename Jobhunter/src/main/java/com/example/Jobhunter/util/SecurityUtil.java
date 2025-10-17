package com.example.Jobhunter.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
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

    /**--------------------------------------------------------------
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */

// Mục đích: lấy tên đăng nhập (username) của người dùng hiện đang đăng nhập trong hệ thống.
// SecurityContextHolder.getContext() → lấy ra context bảo mật hiện tại, nơi Spring Security lưu thông tin người dùng đã xác thực.
// securityContext.getAuthentication() → trả về đối tượng Authentication, chứa principal (ai đăng nhập), credentials (thông tin xác thực), và authorities (quyền hạn).
// extractPrincipal(...) → dùng để bóc tách username hoặc ID người dùng từ các kiểu khác nhau của principal.

//Biết “ai đang đăng nhập”
// 🔹 Dùng để truy xuất thông tin người đang đăng nhập và token JWT gốc từ Spring Security,
// 🔹 Không tự xác thực, không giải mã token bằng tay, mà dựa vào thông tin Spring Security đã lưu trong SecurityContextHolder sau khi xác thực xong

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Get the JWT of the current user.
     *-
     * @return the JWT of the current user.
     */

    //Hàm này lấy ra token JWT của người dùng hiện tại (nếu có).
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    // public static boolean isAuthenticated() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    // }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    // public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     return (
    //         authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
    //     );
    // }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
    // public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
    //     return !hasCurrentUserAnyOfAuthorities(authorities);
    // }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    // public static boolean hasCurrentUserThisAuthority(String authority) {
    //     return hasCurrentUserAnyOfAuthorities(authority);
    // }

    // private static Stream<String> getAuthorities(Authentication authentication) {
    //     return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    // }
}
