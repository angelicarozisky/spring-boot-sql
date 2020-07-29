package hybris.lunchtalk.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

//Spring Boot Issue - https://github.com/spring-projects/spring-boot/issues/11911
//@Order(101)

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	private HttpFirewall allowUrlEncodedSlashHttpFirewall() {
//		StrictHttpFirewall firewall = new StrictHttpFirewall();
//		firewall.setAllowSemicolon(true);
//
//		return firewall;
//	}

//	@Override
//	public void configure(WebSecurity webSecurity) throws Exception {
//		System.out.println(":: SecurityConfig ::");
//		webSecurity.httpFirewall(allowUrlEncodedSlashHttpFirewall());
//	}

	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
        		authorizeRequests()
                    .antMatchers("/js/**",
                            "/css/**",
                            "/img/**",
                            "/webjars/**").permitAll()
                    .antMatchers("/dados-acesso").hasAnyRole("USER")
                    .antMatchers("/lista-usuarios").hasAnyRole("ADMIN")
                    .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .rememberMe();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder
                .inMemoryAuthentication()
                .withUser("marcelo").password("$2a$10$mLOJomifTnG4JwUmF43I6uHWvymu.roJvM75ImRKCUAnWYbAPeRWW").roles("USER", "ADMIN")
                .and()
                .withUser("ken").password("$2a$10$thB9l/Qw.IsYalfdxmkdPOvmCk89U4diWummB5YCAjCQQYqTCXhTm").roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}