package com.rishi.book.manager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final String SECRET_KEY = "0aa101dca9a8f371447ac4062d37d4835febd50a1d21ab9fed5a95ec23d8a0f1a4149dd52aad42db1c9fd92d9aa0a4e645be56ff211fa9bb69df01e6700132842e5cd6433a3d64f448cfbcbe49bfbc497b4f151e7467952297a35022e45d8b1c4ff7d310e5e7c39dd6a921964981fd244ac132e4a445630ea6c9d30ede783b354674b112d38b0b3ab8483e9ff0c17f0b43a8d9861107e683836675914eb0abc006f4359d768f8a0af27ff30c9eda35b9c13158c4e37dbe0dff4c8a74786264d1b624f8354a0208b89219f0d5deeeced0e89ea2c11bcdfb5c4cf6df78aff10601815649020bf63e640e8c5534af7c90e3b26253d5026f0b04da1eaa9886d3266d";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    // Core
    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // External APIs
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims, userDetails.getUsername());
    }

    // Extraction
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
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
