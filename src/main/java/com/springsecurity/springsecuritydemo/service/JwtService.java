package com.springsecurity.springsecuritydemo.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.*;

import com.springsecurity.springsecuritydemo.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.core.codec.Decoder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.function.Function;

// import com.springsecurity.springsecuritydemo.Model.Login;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {


    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        System.out.println("in generate");
        String token=createToken(claims, username);

        return token;

    }

    private String createToken(Map<String, Object> claims, String username) {

        System.out.println("in create" + username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getkey(), SignatureAlgorithm.HS256)
                .compact();
        // return Jwts.builder()
        // .setId("tk9931")
        // .setSubject(username)
        // .setIssuer("ABC_Ltd")
        // .setAudience("XYZ_Ltd")
        // .setIssuedAt(new Date(System.currentTimeMillis()))
        // .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
        // .signWith(SignatureAlgorithm.HS512,
        // Base64.getEncoder().encode("J@!gt*K".getBytes()))
        // .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Key getkey() {
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }
}
