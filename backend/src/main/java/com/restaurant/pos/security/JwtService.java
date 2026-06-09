package com.restaurant.pos.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.pos.config.JwtProperties;
import com.restaurant.pos.entity.Employee;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String TOKEN_TYPE = "JWT";
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public JwtService(JwtProperties jwtProperties, ObjectMapper objectMapper) {
        this(jwtProperties, objectMapper, Clock.systemUTC());
    }

    JwtService(JwtProperties jwtProperties, ObjectMapper objectMapper, Clock clock) {
        this.jwtProperties = jwtProperties;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    public String generateToken(java.util.UUID employeeId, String username, String role) {
        Instant issuedAt = Instant.now(clock);
        Instant expiration = issuedAt.plus(jwtProperties.getExpiration());

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", TOKEN_TYPE);

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", username);
        claims.put("employeeId", employeeId.toString());
        claims.put("role", role);
        claims.put("iat", issuedAt.getEpochSecond());
        claims.put("exp", expiration.getEpochSecond());
        claims.put("issuedAt", issuedAt.getEpochSecond());
        claims.put("expiration", expiration.getEpochSecond());

        String unsignedToken = base64UrlJson(header) + "." + base64UrlJson(claims);
        return unsignedToken + "." + sign(unsignedToken);
    }

    public AuthenticatedEmployee parseAndValidate(String token) {
        String[] parts = token == null ? new String[0] : token.split("\\.");
        if (parts.length != 3) {
            throw new JwtAuthenticationException("Malformed JWT.");
        }

        String unsignedToken = parts[0] + "." + parts[1];
        String expectedSignature = sign(unsignedToken);
        if (!constantTimeEquals(expectedSignature, parts[2])) {
            throw new JwtAuthenticationException("Invalid JWT signature.");
        }

        Map<String, Object> claims = decodeJson(parts[1]);
        String username = requiredString(claims, "sub");
        UUID employeeId = UUID.fromString(requiredString(claims, "employeeId"));
        String role = requiredString(claims, "role");
        long expiration = requiredLong(claims, "exp");

        if (Instant.now(clock).getEpochSecond() >= expiration) {
            throw new JwtAuthenticationException("JWT is expired.");
        }

        return new AuthenticatedEmployee(employeeId, username, role);
    }

    public UUID extractEmployeeId(String token) {
        return parseAndValidate(token).employeeId();
    }

    public String extractUsername(String token) {
        return parseAndValidate(token).username();
    }

    public String extractRole(String token) {
        return parseAndValidate(token).role();
    }

    private String base64UrlJson(Map<String, Object> value) {
        try {
            byte[] json = objectMapper.writeValueAsBytes(value);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to encode JWT JSON.", ex);
        }
    }

    private Map<String, Object> decodeJson(String value) {
        try {
            byte[] json = Base64.getUrlDecoder().decode(value);
            return objectMapper.readValue(json, MAP_TYPE);
        } catch (Exception ex) {
            throw new JwtAuthenticationException("Invalid JWT payload.");
        }
    }

    private String sign(String unsignedToken) {
        String secret = jwtProperties.getSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret is not configured.");
        }
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 bytes for HS256.");
        }
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            byte[] signature = mac.doFinal(unsignedToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to sign JWT.", ex);
        }
    }

    private String requiredString(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (!(value instanceof String text) || text.isBlank()) {
            throw new JwtAuthenticationException("Missing JWT claim: " + name);
        }
        return text;
    }

    private long requiredLong(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (value instanceof Number number) {
            return number.longValue();
        }
        throw new JwtAuthenticationException("Missing JWT claim: " + name);
    }

    private boolean constantTimeEquals(String expected, String actual) {
        byte[] expectedBytes = expected.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = actual.getBytes(StandardCharsets.UTF_8);
        if (expectedBytes.length != actualBytes.length) {
            return false;
        }
        int diff = 0;
        for (int i = 0; i < expectedBytes.length; i++) {
            diff |= expectedBytes[i] ^ actualBytes[i];
        }
        return diff == 0;
    }
}
