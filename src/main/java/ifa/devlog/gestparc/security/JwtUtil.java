package ifa.devlog.gestparc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    public Claims getTokenBody(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    public String generateToken(UserDetailsCustom userDetails) {
        Map<String, Object> extra = new HashMap<>();
        List<String> listeRole = userDetails.getAuthorities().stream()
                .map(ga -> ga.toString())
                .collect(Collectors.toList()) ;
        extra.put("isAdmin",listeRole.contains("ROLE_ADMINISTRATEUR"));
        extra.put("id",userDetails.getId());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .addClaims(extra)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*12))
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }
    private boolean tokenDateNonExpire(String token) {
        return getTokenBody(token)
                .getExpiration()
                .after(new Date());

    }
    public boolean valideTokenDate(String token,UserDetails userDetails) {
        String userName = getTokenBody(token)
                .getSubject();
        return userName.equals(userDetails.getUsername()) && tokenDateNonExpire(token);

    }

    public int getUtilisateurIdFromAuthorization(String authorization) {
        String token = authorization.substring(7);
        return getTokenBody(token).get("id",Integer.class);
    }
    public boolean getIsAdminFromAuthorization(String authorization) {
        String token = authorization.substring(7);
        return getTokenBody(token).get("isAdmin",Boolean.class);
    }
}
