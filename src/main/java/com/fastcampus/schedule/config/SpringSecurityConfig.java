package com.fastcampus.schedule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fastcampus.schedule.config.jwt.JwtAuthenticationFilter;
import com.fastcampus.schedule.config.jwt.JwtAuthorizationFilter;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.repository.UserRepository;
import com.fastcampus.schedule.user.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Security 설정 Config
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserService userService;
	private final UserRepository userRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// csrf
		http.csrf().disable();

		// stateless
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// jwt filter
		http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()),
							 UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtAuthorizationFilter(userRepository), BasicAuthenticationFilter.class);

		// authorization
		http.authorizeRequests()
			.antMatchers("/", "/signup", "/login", "/h2-console/**", "/admins/login").permitAll()
			.antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs").permitAll()
			.antMatchers("/admins/**").hasRole("ADMIN")
			.anyRequest().authenticated();
	}

	/**
	 * UserDetailsService 구현
	 *
	 * @return UserDetailsService
	 */
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return username -> {
			User user = (User)userService.loadUserByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException(username);
			}
			return user;
		};
	}
}
