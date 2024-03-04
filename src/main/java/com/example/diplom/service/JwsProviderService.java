package com.example.diplom.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@NoArgsConstructor
public class JwsProviderService {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String build(String username) {
        Date date = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(date)
                .signWith(key)
                .compact();
    }

    public boolean verify(String jws, String username) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws)
                .getBody()
                .getSubject()
                .equals(username);
    }

    public String getSubject(String jws) {
        return Jwts.parserBuilder()
                .setSigningKey(key.getEncoded())
                .build()
                .parseClaimsJws(jws)
                .getBody()
                .getSubject();
    }
}
