package com.demoproject.SmartDesk.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	@Value("${jwt.secret}")
	private String SECRET;
	
	@Value("${jwt.expiration}")
	private Long expirationTime;

	public String generateToken(String username) {

		return Jwts.builder().subject(username).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(key()).compact();
	}

	public String getUsernameFromJwtToken(String token) {
		Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();

		return claims.getSubject();
	}

	public boolean validateJwtToken(String token) {
		try {

			Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);

			return true;

		} catch (Exception e) {

			return false;
		}
	}

	public SecretKey key() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}
}
