package com.linkin;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import com.linkin.entity.User;
import com.linkin.service.impl.AuditorAwareImpl;
import com.linkin.utils.RoleEnum;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class LinkInApplication {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(LinkInApplication.class, args);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
		return bCryptPasswordEncoder;
	}

	@Configuration
	public class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/admin/**").hasAnyRole(RoleEnum.ADMIN.getRoleName())
					.antMatchers("/staff/**").hasAnyRole(RoleEnum.STAFF.getRoleName(), RoleEnum.ADMIN.getRoleName())
					.antMatchers("/member/**").authenticated().anyRequest().permitAll().and().formLogin()
					.loginPage("/dang-nhap").loginProcessingUrl("/dang-nhap").defaultSuccessUrl("/member/profile", true)
					.failureUrl("/dang-nhap?e").and().rememberMe().rememberMeCookieName("app-remember-me")
					.tokenValiditySeconds(24 * 60 * 60 * 30).tokenRepository(persistentTokenRepository()).and().logout()
					.logoutUrl("/dang-xuat").logoutSuccessUrl("/dang-nhap")
					.logoutRequestMatcher(new AntPathRequestMatcher("/dang-xuat")).clearAuthentication(true)
					.invalidateHttpSession(true).deleteCookies("JSESSIONID", "app-remember-me").permitAll().and()
					.exceptionHandling().accessDeniedPage("/access-deny").and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.ALWAYS).sessionFixation().migrateSession()
					.maximumSessions(-1).sessionRegistry(sessionRegistry());
		}
	}

	@Bean
	public AuditorAware<User> auditorAware() {
		return new AuditorAwareImpl();
	}

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		SpringSecurityDialect dialect = new SpringSecurityDialect();
		return dialect;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		// TODO: hardcode
		try {
			/// insert to db;
			String sql = "INSERT INTO role(id, name) VALUES(1, 'ROLE_ADMIN');INSERT INTO role(id, name) VALUES(2, 'ROLE_STAFF');"
					+ "INSERT INTO user(id, name,enabled,password,phone,role_id) VALUES(1, 'ADMIN',1,'$2a$12$6M88CIo.H9gsZhSmLJJWxe1AYPmhc.lEmYQi1E7QZgV2slVVD8YlW','admin',1);";
			dataSource.getConnection().prepareStatement(sql).executeUpdate();
		} catch (Exception ex) {
			System.out.println("DA CO DU LIEU");
		}
		return tokenRepository;
	}

}
