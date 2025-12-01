package es.albavm.tfg.trifly.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import es.albavm.tfg.trifly.Security.jwt.JwtRequestFilter;
import es.albavm.tfg.trifly.Security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
	public RepositoryUserDetailsService userDetailService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            "/assets/**", "/css/**", "/js/**", "/img/**", "/images/**", 
            "/scss/**", "/cdn-cgi/**", "/cloudflare-static/**", "/plugins/**"
        );}

	@Bean
	@Order(1)
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {		
		http.authenticationProvider(authenticationProvider());
		http.authorizeHttpRequests(authorize -> authorize
				
				// PUBLIC PAGES
				.requestMatchers("/").permitAll()		
				.requestMatchers("/register").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/signup").permitAll()
				.requestMatchers("/itineraries/{id}/image").permitAll()
				// USER PAGES
				// ADMIN PAGES
				
				.anyRequest().authenticated())
		
				// LOGIN
				.formLogin(formLogin -> formLogin
					.loginPage("/login")
					.usernameParameter("email") 
					.failureUrl("/login?error=true")
					.defaultSuccessUrl("/",true)
					.permitAll())
				// LOGOUT
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll());
			
			 // Stateless session
			 http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

		return http.build();
	}
}
