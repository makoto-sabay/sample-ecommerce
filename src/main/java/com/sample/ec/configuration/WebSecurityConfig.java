package com.sample.ec.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sample.ec.service.EcService;

/**
 * Configuration for Spring Security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private EcService service;


	/**
	 * Utilize the method to encode the data in the form as the password that an user input is encoded.
	 *
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}


	/**
	 * Ignore static resources
	 */
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                            "/img/**",
                            "/css/**",
                            "/js/**"
                            );
    }


	/**
	 * Setup urls and parameters
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    .authorizeRequests()
		    	.antMatchers("/").permitAll()
		    	.antMatchers("/index").permitAll()
		    	.antMatchers("/login").permitAll()
		    	.antMatchers("/product").permitAll()
		    	.antMatchers("/registration").permitAll()
		    	.antMatchers("/c_qty").authenticated()
		    	.antMatchers("/setting").authenticated()
		    	.antMatchers("/add-flist").authenticated()
		    	.antMatchers("/add-cart").authenticated()
		    	.antMatchers("/see-cart").authenticated()
		    	.antMatchers("/re-register").authenticated()
		    	.antMatchers("/move-next").authenticated()
		    	.antMatchers("/conversion").authenticated()
		    	.antMatchers("/delete-item").authenticated()
		    	.antMatchers("/see-favorites").authenticated()
		    	.antMatchers("/purchase-now").authenticated()
		    	.antMatchers("/purchase-items").authenticated()
		    	.antMatchers("/delete-cart-item").authenticated()
		    	.anyRequest().authenticated()
		        .and()
		    .formLogin()
		        .loginPage("/login")
		        .loginProcessingUrl("/log_in")
		        .usernameParameter("userId")
		        .passwordParameter("password")
		        .successForwardUrl("/index?login")
		        .failureUrl("/login?error")
		        .permitAll()
		        .and()
		    .logout()
		        .logoutUrl("/logout")
		        .logoutSuccessUrl("/index?logout")
		        .permitAll();
	}


	/**
	 * Set up Datasource when authrizing
	 * We set user information up to the UserDetailsService.
	 *
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .userDetailsService(service)
        .passwordEncoder(passwordEncoder());
	}
}