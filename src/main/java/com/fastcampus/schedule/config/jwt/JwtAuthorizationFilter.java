package com.fastcampus.schedule.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.exception.constant.ErrorCode;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final UserRepository userRepository;

	public JwtAuthorizationFilter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain
	) throws IOException, ServletException {
		String token = null;
		try {
			// cookie 에서 JWT token을 가져옵니다.
			token = request.getHeader("Authorization").split(" ")[1].trim();
		} catch (Exception ignored) {
			log.info("JWT 토큰이 없습니다.");
			chain.doFilter(request, response);
			return;
		}
		if (token != null) {
			try {
				Authentication authentication = getUsernamePasswordAuthenticationToken(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * JWT 토큰으로 User를 찾아서 UsernamePasswordAuthenticationToken를 만들어서 반환한다.
	 * User가 없다면 null
	 */
	private Authentication getUsernamePasswordAuthenticationToken(String token) {
		String email = JwtUtils.getEmail(token);
		if (email != null) {
			User user = userRepository.findByEmail(email)
									  .orElseThrow(() -> new ScheduleException(ErrorCode.USER_NOT_FOUND,
																			   "")); // 유저를 유저명으로 찾습니다.
			return new UsernamePasswordAuthenticationToken(
				user, // principal
				null,
				user.getAuthorities()
			);
		}
		return null; // 유저가 없으면 NULL
	}
}