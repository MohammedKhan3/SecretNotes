package com.FK.Notes.Secrets.SecurityConfig.JWT;
/*

JWT component

Allows us to do different operations such as parsing,generating and validating.

Then we need instance of logger.


A JWT parser is a component (usually provided by a library)
that takes a JSON Web Token (JWT, which is just a Base64-encoded string) and
decodes + validates it so you can work with its data in your application.

 */


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

@Value("${spring.app.jwtSecret}")
private String jwtSecret;

@Value("${spring.app.jwtExpirationInMs}")
private  int jwtExpirationInMs;

/*
// This logger will be used to record messages (info, debug, error, etc.)
// and tag them with the class name. It is static and final so that
// only one instance is created and reused throughout the class.

 */
private static final Logger logger =  LoggerFactory.getLogger(JwtUtils.class);

    /**
     * write method returns string to getJwtFromHeader--> get Authorization header.
     * if bearerToken is not null and starts with "Bearer" then return after removing prefix of 7.
     * @return JwtToken
         */
    public  String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header : {}", bearerToken);
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);//remove first 7 character
        }
        return null;
    }

    /*
    Write method that generates JWTTokenFromUsername and takes userdetails in the param.

     */
    /*
    @param UserDetails
    @return String JwtToken
     */

    public  String generateTokenfromUserName(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date((new Date()).getTime() +jwtExpirationInMs))
        .signWith(key()).compact();
    }

    /*
    Write method to getUserNameFromJwtToken(String token):
return Jwt.parser().verifywith((SecretKey)(key)).build().pareseSignedClaims(token).getPayload().getSubject();

     */
// Parse the JWT token and extract the "subject" claim (usually the username).
// Steps:
// 1. Create a JWT parser with the secret key for signature verification.
// 2. Build the parser.
// 3. Parse the token and validate its signature (parseSignedClaims).
// 4. Extract the payload (claims).
// 5. Return the subject ("sub") field from the claims.
    public String getUserNameFromJwtToken(String token){
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    private  Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
    }

    /*
    Validate Jwt Token - we  take authToken
Jwts.parser().verifywith(secertKey)(key)).build.parseSignedClaims(authToken) return true else throw exceptions.

     */
    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token);
            return true;
        }catch (MalformedJwtException e){
        logger.error("Invalid JWT token: " , e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("Expired jwt error : " ,e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token: " , e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("Errror: ", e.getMessage());
        }
        return false;
    }


}
