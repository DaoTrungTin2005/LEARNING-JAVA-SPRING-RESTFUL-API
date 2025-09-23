package com.example.Jobhunter.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.Spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import com.example.Jobhunter.util.SecurityUtil;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jwt.JWT;

import io.micrometer.core.instrument.config.validate.Validated.Secret;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtkey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Nạp CustomAuthenticationEntryPoint là để custom lại thông báo lỗi 401
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        http
                .csrf(c -> c.disable())
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/", "login").permitAll()
                                .anyRequest().authenticated()
                // .anyRequest().permitAll()

                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())

                        .authenticationEntryPoint(customAuthenticationEntryPoint)

                )

                .exceptionHandling(
                        exceptions -> exceptions
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) // 401
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())) // 403

                .formLogin(f -> f.disable()) // Tắt form login
                // Bật session và nói với nó xài stateless trong khi đó mặc định nó là stateful
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //👉 Biến một JWT hợp lệ (sau khi decode) thành một Authentication object mà Spring Security hiểu.
    //JwtAuthenticationConverter = Cầu nối giữa JWT và Spring Security Authentication. 
    
    // Sau khi JwtDecoder xác thực token hợp lệ, Spring cần biết user này có quyền
    // gì để cho phép vào API.
    // Spring tạo ra một Authentication object → nó chứa:
    // principal (user id, username, email...)
    // authorities (các quyền: USER, ADMIN, READ, WRITE...)

    // 👉 Mà quyền này Spring phải lấy từ payload của JWT.
    // Nếu  dùng claim mặc định "scope" hoặc "scp", thì không cần config gì cả.
    // Nếu  lưu role ở claim "hoidanit" (theo code của ) → Spring không tự
    // biết → cần JwtAuthenticationConverter để chỉ cho nó.

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() { //Nó tạo ra một JwtAuthenticationConverter và đưa vào Spring Context để Spring Security dùng khi xử lý JWT.
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter(); //Thằng này chuyên dùng để đọc các claim trong JWT và chuyển thành danh sách quyền (GrantedAuthority).

        //👉 Mặc định Spring thêm tiền tố "SCOPE_" trước mỗi quyền.
        // Ví dụ: "scope": "read" → thành ["SCOPE_read"].
        // Ở đây bạn set thành chuỗi rỗng "" → nghĩa là không thêm prefix.
        // Ví dụ: "ADMIN" trong token → giữ nguyên "ADMIN", không bị thành "SCOPE_ADMIN".
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        // 👉 Mặc định Spring chỉ đọc quyền từ claim "scope" hoặc "scp".
        // Nhưng JWT của bạn chứa roles trong claim "hoidanit".

        // Ví dụ payload JWT của :
        // {
        //   "sub": "user123",
        //   "hoidanit": ["ADMIN", "USER"]
        // }
        // → Dòng này bảo Spring: hãy lấy quyền từ claim "hoidanit".
        grantedAuthoritiesConverter.setAuthoritiesClaimName("hoidanit");

        //👉 Tạo một converter cấp cao hơn (JwtAuthenticationConverter) → nó chịu trách nhiệm chuyển Jwt thành Authentication object.
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // @FunctionalInterface

    // Xác thực token hợp lệ
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_Algorithm).build(); // → chỉ định secret key dùng để kiểm
                                                                                  // tra chữ ký token.
        return token -> {
            try {
                return jwtDecoder.decode(token); // → giải mã token và kiểm tra chữ ký.
            } catch (Exception e) {
                System.out.println(">>> JWT error: " + e.getMessage());
                throw e; // Nếu token không hợp lệ / hết hạn / bị sửa → ném exception và in log.
            }
        };
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Mục đích: Cấu hình và cung cấp một “máy mã hóa” (encoder) để createToken sử
    // dụng.
    // NimbusJwtEncoder chính là cái thư viện thực hiện việc ký số JWT bằng secret
    // key.
    // Nó cần có SecretKey (tạo từ jwtkey trong file cấu hình).
    // Spring sẽ quản lý bean JwtEncoder, để khi gọi jwtEncoder.encode(claims) thì
    // nó biết cách mã hóa.
    @Bean
    public JwtEncoder jwtEncoder() {

        // NimbusJwtEncoder chính là “máy tạo JWT” (JWT Encoder) được Spring Security
        // xây dựng dựa trên thư viện mã hóa nổi tiếng Nimbus JOSE + JWT.
        // NimbusJwtEncoder cần biết Secret Key để ký JWT.
        // ImmutableSecret<>(getSecretKey()) chính là cách bạn đưa cái key bí mật đó
        // vào.

        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtkey); // chuyển secret từ dạng chuỗi Base64 thành mảng byte.
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_Algorithm.getName()); // gói mảng byte
                                                                                                      // thành một đối
                                                                                                      // tượng
                                                                                                      // SecretKey, chỉ
                                                                                                      // định thuật toán
                                                                                                      // (HS512).
    }

    // Đoạn code này để tạo và cấu hình một JwtEncoder (máy tạo token). Nó sẽ dùng
    // secret key bạn cấu hình để ký JWT.
    // Sau này trong hàm createToken(Authentication authentication), bạn gọi
    // jwtEncoder.encode(...) → token sẽ sinh ra.
}