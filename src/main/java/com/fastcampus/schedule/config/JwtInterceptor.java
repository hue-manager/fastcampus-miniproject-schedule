package com.fastcampus.schedule.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.exception.constant.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter {

	private static final String TOKEN = "jwt-token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		final String token = request.getHeader(TOKEN);

		if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
			return true;
		}

		if (token != null) {
			return true;
		} else {
			throw new ScheduleException(ErrorCode.INVALID_TOKEN, "토큰이 유효하지 않습니다.");
		}
	}
}