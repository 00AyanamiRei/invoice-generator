package com.makara.invoicegenerator;

import com.makara.invoicegenerator.handler.LoginSuccessHandler;
import com.makara.invoicegenerator.models.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableGlobalMethodSecurity(securedEnabled = true) //habilitar protección de rutas por anotacionea
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JpaUserDetailsService userDetailsServiceJpa;
	
//	@Bean //configuration global
//	public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//
//		List<UserDetails> userDetailsList = new ArrayList<>();
//		userDetailsList.add(User.withUsername("admin").password(this.passwordEncoder.encode("password"))
//				.roles("ADMIN", "USER").build());
//		userDetailsList.add(User.withUsername("andres").password(this.passwordEncoder.encode("password"))
//				.roles("USER").build());
//
//		return new InMemoryUserDetailsManager(userDetailsList);
//	}
	
	//con jpa y las clases entity
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception{
		build.userDetailsService(userDetailsServiceJpa).passwordEncoder(passwordEncoder);
	}
	
	/*
	@Autowired //con jdbc
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception{
		build.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
		.authoritiesByUsernameQuery("SELECT u.username, a.authority FROM authorities a INNER JOIN users u ON (a.user_id=u.id) WHERE u.username=?");
	}*/


	@Bean //autorización
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/list", "/locale", "/register")
				.permitAll()
				/*.antMatchers("/ver/**").hasAnyRole("USER")
                .antMatchers("/uploads/**").hasAnyRole("USER")
                .antMatchers("/form/**").hasAnyRole("ADMIN")
                .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
                .antMatchers("/factura/**").hasAnyRole("ADMIN")*/
				.antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.successHandler(successHandler)
				.loginPage("/login")
				.permitAll()
				.and()
				.logout().permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/error_403");

		return http.build();
	}
}
