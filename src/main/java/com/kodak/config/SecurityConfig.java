package com.kodak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.kodak.utility.SecurityUtility;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	private BCryptPasswordEncoder passwordEncoder() {
		return SecurityUtility.passwordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

//	List of urls to exempt from spring security
	private static final String[] PUBLIC_MATCHERS = { "/css/**", "/js/**", "/assert/**", "/images/**", "/login" };

	/**
	 * Overriding configure method PUBLIC_MATCHES - list of urls exempting from
	 * security configuring login and logout page's securities
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll()
		.antMatchers("/report/**").hasAnyAuthority("Admin", "Manager", "Accountant")
		.antMatchers("/employee").hasAnyAuthority("Admin", "Manager")
		.antMatchers("/employee/addEmployee").hasAnyAuthority("Admin", "Manager")
		.antMatchers("/employee/editEmployee").hasAnyAuthority("Admin", "Manager")
		.antMatchers("/employee/deleteEmployee").hasAnyAuthority("Admin", "Manager")
		.antMatchers("/branch/addBranch").hasAnyAuthority("Admin", "Manager")
		.antMatchers("/branch/editBranch").hasAnyAuthority("Admin", "Manager")
		.antMatchers("/branch/deleteBranch").hasAnyAuthority("Admin", "Manager")
		.anyRequest().authenticated().and()
		.exceptionHandling().accessDeniedPage("/accessDenied");

		http.csrf().disable().cors().disable().formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
				.loginPage("/login").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
				.deleteCookies("remember-me").permitAll().and().rememberMe();

	}

}
