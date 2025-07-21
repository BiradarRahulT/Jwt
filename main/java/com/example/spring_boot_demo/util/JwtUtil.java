package com.example.spring_boot_demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import javax.crypto.SecretKey;
import java.util.Date;

@Component()
public class JwtUtil {
    private static final String secretKey="Hello";
    public String generateToken(String username){
        JwtBuilder jwt= Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() +1000*60*60*10))
                .signWith(generateJwtSecretKey(),SignatureAlgorithm.HS256);
        return jwt.compact();

    }
    public SecretKey generateJwtSecretKey(){
        //Convert the static word into a byte array
        byte[] keyBytes= secretKey.getBytes();

        //Ensure the key length is compatible with the algorithm
        byte[] keyBytesPadded= new byte[32];
        System.arraycopy(keyBytes,0,keyBytesPadded,0,Math.min(keyBytes.length,32));

        //Generate the Secretkey using static word
        return hmacShaKeyFor(keyBytesPadded);
    }
    public  boolean validateToken(String token, String username){
        return (username.equals(getUsername(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractUsername(String token){
        return getClaims(token).getSubject();

    }
    public Claims getClaims(String token){
        return Jwts.parser().verifyWith(generateJwtSecretKey()).build()
                .parseSignedClaims(token).getPayload();
    }
}
