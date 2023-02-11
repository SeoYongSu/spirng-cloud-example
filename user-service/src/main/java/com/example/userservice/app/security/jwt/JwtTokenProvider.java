package com.example.userservice.app.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.example.userservice.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String PERMISSTION_KEY = "roles";


    @Value("${app.jwt.secretKey}")
	private String key;
    
    @Value("${app.jwt.ACCESS_TOKEN_EXPIRE_TIME}")
	private long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${app.jwt.REFRESH_TOKEN_EXPIRE_TIME}")
	private long REFRESH_TOKEN_EXPIRE_TIME;
    
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
    	String secret = Base64.getEncoder().encodeToString(key.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT Validation 확인
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder().setSigningKey(this.secretKey).build()
                    .parseClaimsJws(token);
            
            log.info("expiration date: {}", claims.getBody().getExpiration());
            
//            Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * Security Authentication에서 인증 정보 가져오기
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).getBody();

        Object authoritiesClaim = claims.get(PERMISSTION_KEY);
        
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        
        // UserDetails 객체를 만들어서 Authentication 리턴
        UserPrincipal user = UserPrincipal.builder()
        							.userID(claims.getSubject())
        							.build();
//        User user = new User(claims.getSubject(),token, authorities);

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }
    
    
    /**
     * Create AccessToken
     * @return
     */
    public String createAccessToken(Authentication authentication) {

    	UserPrincipal user =  (UserPrincipal) authentication.getPrincipal();

    	return Jwts.builder()
    			.setSubject(String.valueOf( user.getUserId()) )
    			.claim("roles", "ROLE_USER")
    			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
    			.signWith(this.secretKey, SignatureAlgorithm.HS256)
    			.compact();
    }
    
    /**
     * Create RefreshToken
     * @param authentication
     * @return
     */
    public String createRefreshToken(Authentication authentication) {
    	Claims claims = Jwts.claims();
        claims.put("value", UUID.randomUUID());
        return Jwts.builder()
        		.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))     // payload "exp": 1516239022 (예시)
                .compact();
    }

}
