package com.fastcampus.schedule.config.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {
	public static Boolean isTokenExpired(String token, String key) {
		Date expiration = extractAllClaims(token, key).getExpiration();
		return expiration.before(new Date());
	}

	public static Boolean validate(String token, String email, String key) {
		String emailByToken = getEmail(token, key);
		return emailByToken.equals(email) && !isTokenExpired(token, key);
	}

	public static String generateAccessToken(String email, String key, long expiredTimeMs) {
		return doGenerateToken(email, expiredTimeMs, key);
	}

	public static String getEmail(String token, String key) {
		return extractAllClaims(token, key).get("email", String.class);
	}

	public static Claims extractAllClaims(String token, String key) {
		return Jwts.parserBuilder()
				   .setSigningKey(getSigningKey(key))
				   .build()
				   .parseClaimsJws(token)
				   .getBody();
	}

	private static Key getSigningKey(String secretKey) {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private static String doGenerateToken(String email, long expireTime, String key) {
		Claims claims = Jwts.claims();
		claims.put("email", email);

		return Jwts.builder()
				   .setClaims(claims)
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .setExpiration(new Date(System.currentTimeMillis() + expireTime))
				   .signWith(getSigningKey(key), SignatureAlgorithm.HS256)
				   .compact();
	}
}
