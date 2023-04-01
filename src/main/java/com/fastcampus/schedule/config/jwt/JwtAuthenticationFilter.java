package com.fastcampus.schedule.config.jwt;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.authenticationManager = authenticationManager;
	}
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		// 로그인할 때 입력한 email과 password를 가지고 authenticationToken를 생성한다.
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			request.getParameter("email"), request.getParameter("password"), new ArrayList<>());
		return authenticationManager.authenticate(authenticationToken);
	}
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											  AuthenticationException failed) throws IOException {
		response.sendRedirect("/login");
	}
}