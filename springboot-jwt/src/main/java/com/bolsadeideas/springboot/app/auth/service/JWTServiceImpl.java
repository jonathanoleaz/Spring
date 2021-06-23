package com.bolsadeideas.springboot.app.auth.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.bolsadeideas.springboot.app.auth.SimpleGrantedAuthorityMixin;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTServiceImpl implements JWTService{

    public static final String SECRET = Base64Utils.encodeToString("algunaLlaveSecretaASCII03312_GAL22V10D.".getBytes());

    public static final long EXPIRATION_DATE = 14000000L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    /**Crea el token de JWT */
    @Override
    public String create(Authentication auth) throws JsonProcessingException {

        String username = ((User)auth.getPrincipal()).getUsername();
        
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		
		SecretKey secretKey = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
		
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(secretKey)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
				.compact();
        return token;
    }

    /**Obtiene los claims del JWT */
    @Override
    public Claims getClaims(String token) {
        SecretKey secretKey = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(resolve(token))
            .getBody();

        return claims;
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws JsonParseException, JsonMappingException, IOException  {
        Object roles = getClaims(token).get("authorities");

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
            .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

            return authorities;
    }

    @Override
    public String getUserName(String token) {
        return getClaims(token).getSubject();
    }

    /**Obtiene el token */
    @Override
    public String resolve(String token) {
        if(token!=null && token.startsWith(TOKEN_PREFIX))
        {
            return token.replace(TOKEN_PREFIX, "");
        }else{
            return null;
        }
        
    }

    @Override
    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
}