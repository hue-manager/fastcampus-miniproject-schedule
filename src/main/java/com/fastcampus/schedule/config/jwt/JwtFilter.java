package com.fastcampus.schedule.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final UserService userService;

	private final String secretKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain)
		throws ServletException, IOException {
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String token;
		try {

			if (header == null) {
				log.error("there is no token in header : ", request.getRequestURI());
				chain.doFilter(request, response);
				return;
			} else {
				token = header.split(" ")[1].trim();
			}

			String email = JwtUtils.getEmail(token, secretKey);
			User userDetails = userService.getUserByEmail(email);

			if (!JwtUtils.validate(token, userDetails.getUsername(), secretKey)) {
				chain.doFilter(request, response);
				return;
			}
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, null,
				userDetails.getAuthorities()
			);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (RuntimeException e) {
			chain.doFilter(request, response);
			return;
		}
		chain.doFilter(request, response);
	}
}
