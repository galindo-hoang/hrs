package org.example.hrs.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class Utils {
    public static final long JWT_ACCESS_TOKEN_VALIDITY = 5 * 60 * 60 * 1000L;
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 30 * 24 * 60 * 60 * 1000L;

    private static final String secret = "b65426b22643de0b6f28dcea96afe50efce7f818backend";

    //retrieve username from jwt token
    static String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public static String generateToken(Map<String, Object> claims, String subject, Long time) {
        return doGenerateToken(claims, subject, time);
    }

    private static String doGenerateToken(Map<String, Object> claims, String subject, Long time) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
