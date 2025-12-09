package com.sudarshandate.resumebuilderapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateJwtToken(String userId){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder().setSubject(userId).setIssuedAt(now).setExpiration(expiryDate).signWith(getSigningKey()).compact();
    }

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String getUserIdFromToken(String authToken){
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(authToken)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken){
        try{
            Jwts.parser().setSigningKey(getSigningKey())
                    .parseClaimsJws(authToken);

            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public boolean isTokenExpired(String authToken){
        try{
            Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(authToken).getBody();

            return claims.getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){
            return true;
        }
    }
}
