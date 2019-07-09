package com.husen.ci.framework.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.husen.ci.framework.date.DateUtils;
import com.husen.ci.framework.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtUtils {

    private final static String JWT_KEY_ID = "JWT_KEY_ID";

    private final static String JWT_ISSUER = "JWT_HUSEN";

    private final static int DEFAULT_EXPIRE_SECOND = 10;


    public static String encode(String key) {
        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim(JWT_KEY_ID, CryptoUtils.encodeBase64String(key))
                .sign(Algorithm.HMAC256(UUID.randomUUID().toString()));
    }

    /**
     * Base64加密key，然后再生产token
     */
    public static String encodeWithExpire(String key, long second) {
        Date date = DateUtils.localDateTime2Date(LocalDateTime.now().plusSeconds(second));
        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim(JWT_KEY_ID, CryptoUtils.encodeBase64String(key))
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(UUID.randomUUID().toString()));
    }

    /**
     * Base64加密key，然后再生产token
     */
    public static String encodeWithExpire(String key) {
        return encodeWithExpire(key, DEFAULT_EXPIRE_SECOND);
    }

    /**
     * 解析token再Base64解密key
     * @param token
     * @return
     */
    public static String decode(String token) {
        Claim claim = JWT.decode(token).getClaim(JWT_KEY_ID);
        return CryptoUtils.decodeBASE64String(claim.asString());
    }

    public static boolean checkToken(String token) {
        try {
            if (!StringUtils.isEmpty(token)) {
                DecodedJWT decode = JWT.decode(token);
                return true;
            }
        } catch (Exception ex){
            log.error("JwtUtils checkToken fail", ex);
        }
        return false;
    }

    public static boolean checkTokenWithExpire(String token) {
        try {
            if (!StringUtils.isEmpty(token)) {
                DecodedJWT decode = JWT.decode(token);
                LocalDateTime expiresAt = DateUtils.date2LocalDateTime(decode.getExpiresAt());
                if (DateUtils.getMilllis(LocalDateTime.now()) <= DateUtils.getMilllis(expiresAt)) {
                    return true;
                }
            }
        } catch (Exception ex){
            log.error("JwtUtils checkTokenWithExpire fail", ex);
        }
        return false;
    }
}
