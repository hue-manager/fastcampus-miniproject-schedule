package com.fastcampus.schedule.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fastcampus.schedule.config.jwt.JwtFilter;
import com.fastcampus.schedule.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserService userService;

	private static final String[] PERMIT_URL_ARRAY = {
		/* swagger v2 */
		"/v2/api-docs",
		"/swagger-resources",
		"/swagger-resources/**",
		"/configuration/ui",
		"/configuration/security",
		"/swagger-ui.html",
		"/webjars/**",
		/* swagger v3 */
		"/v3/api-docs/**",
		"/swagger-ui/**"
	};

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs/**");
		web.ignoring().antMatchers("/swagger.json");
		web.ignoring().antMatchers("/swagger-ui.html");
		web.ignoring().antMatchers("/swagger-resources/**");
		web.ignoring().antMatchers("/webjars/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.authorizeRequests()
			.anyRequest().permitAll();
			// .authorizeRequests()
			// .antMatchers("/login", "/signup", "/", "/admins/login").permitAll()
			// .antMatchers(PERMIT_URL_ARRAY).permitAll()
			// .antMatchers("/admins/**").hasRole("ADMIN")
			// .anyRequest().authenticated()
			// .and()
			// .sessionManagement()
			// .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			// .and()
			// .exceptionHandling()
			// .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			// .and()
			// .addFilterBefore(new JwtFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}
}
