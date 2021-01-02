package com.felipeduarte.agenda.security;

import java.util.Date;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generatedToken(User user) {
		
		JSONObject jo = new JSONObject();
		jo.put("id", user.getId());
		jo.put("nome", user.getNome());
		jo.put("email", user.getUsername());
		jo.put("perfil", user.getAuthorities().stream()
				.map(g -> g.getAuthority()).collect(Collectors.toList()));
		
		return Jwts.builder()
				.setSubject(jo.toString())
				.setExpiration(new Date(System.currentTimeMillis() + this.expiration))
				.signWith(SignatureAlgorithm.HS512 ,this.secret.getBytes())
				.compact();
		
	}
	
	public String getId(String token) {
		Claims claims = getClaims(token);
		if(claims != null) {
			String jsonUser = claims.getSubject();
			
			String[] parts = jsonUser.split(",");
			
			parts = parts[1].split(":");
			
			String id = parts[1];
			
			return id;
		}
		return null;
	}
	
	public boolean tokenValido(String token) {
		Claims claims = getClaims(token);
		if(claims != null) {
			String id = claims.getSubject();
			Date expiration = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if(id != null && expiration != null && now.before(expiration)) {
				return true;
			}
		}
		return false;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(this.secret.getBytes()).parseClaimsJws(token).getBody();
		}catch(Exception e) {
			return null;
		}
	}
	
}
