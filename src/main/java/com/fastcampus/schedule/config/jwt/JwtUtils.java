package com.fastcampus.schedule.config.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.data.util.Pair;

import com.fastcampus.schedule.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;

public class JwtUtils {
	/**
	 * 토큰에서 email 찾기
	 *
	 * @param token 토큰
	 * @return email
	 */
	public static String getEmail(String token) {
		return Jwts.parserBuilder()
				   .setSigningKeyResolver(SigningKeyResolver.instance)
				   .build()
				   .parseClaimsJws(token)
				   .getBody()
				   .getSubject();
	}

	/**
	 * user로 토큰 생성
	 * HEADER : alg, kid
	 * PAYLOAD : sub, iat, exp
	 * SIGNATURE : JwtKey.getRandomKey로 구한 Secret Key로 HS512 해시
	 *
	 * @param user 유저
	 * @return jwt token
	 */
	public static String createToken(User user) {
		Claims claims = Jwts.claims().setSubject(user.getEmail());
		Date now = new Date();
		Pair<String, Key> key = JwtKey.getRandomKey();

		return Jwts.builder()
				   .setClaims(claims)
				   .setIssuedAt(now)
				   .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME))
				   .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
				   .signWith(key.getSecond())
				   .compact();
	}
}
