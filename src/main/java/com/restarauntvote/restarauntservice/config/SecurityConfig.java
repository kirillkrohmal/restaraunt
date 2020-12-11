package com.restarauntvote.restarauntservice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.
                inMemoryAuthentication()
                .withUser("login").password(passwordEncoder().encode("password")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/security")
                .permitAll()
                .antMatchers("/**")
                .hasAnyRole("USER")
                .and()
                .formLogin()
                .loginPage("/security")
                .loginProcessingUrl("/security")
                .defaultSuccessUrl("/list", true)
                .failureUrl("/security?error")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/security?logout=true")
                .invalidateHttpSession(true)
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ();
    }

}
