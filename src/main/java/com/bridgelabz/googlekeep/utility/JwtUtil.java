package com.bridgelabz.googlekeep.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bridgelabz.googlekeep.CustomException.CustomException;
import com.bridgelabz.googlekeep.model.Note;
import com.bridgelabz.googlekeep.model.User;
import com.bridgelabz.googlekeep.model.UserLabel;
import com.bridgelabz.googlekeep.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private String SECRET_KEY = "milind";
    
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractEpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractEpiration(token).before(new Date());
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}
	

	public Boolean checkUserById(User user) {
		if (user == null) 
			return false; 
		return true;
	}
	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public String validateToken(String token) {
		String username = null;
		try {
			username = extractUsername(token);	
		}catch (Exception e) {
			throw new CustomException.InvalidTokenException("Invalid Token");
			}
		
		if (isTokenExpired(token)) {
			throw new CustomException.TokenExpiredException("your token is expired");
		}
			
		

		return (username);
	}

	public void checkLabel(UserLabel userlabel) {
		if (userlabel == null) {
			throw new CustomException.InvalidLabelIdException("invalid label id");
		}
	}

	public void checkLabelList(List<UserLabel> userlabellist) {
		if (userlabellist == null) {
			throw new CustomException.InvalidLabelListException("invalid label list");
		}
	}

	public void checkNoteList(List<Note> notelist) {
		if (notelist == null) {
			throw new CustomException.InvalidLabelListException("invalid note list");
		}
	}

}