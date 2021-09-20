package no.uis.dat240.assignment3;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // Create 2 users for demo
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.inMemoryAuthentication().withUser("user1").password("{noop}password").roles("USER").and().withUser("user2")
        .password("{noop}password").roles("USER");
    	//TODO add security config intialization code
    }

    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        // HTTP Basic authentication
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/").permitAll()
        .antMatchers(HttpMethod.GET, "/neighbors").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/neighbors").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/degree").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/degree").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/checkedge").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/checkedge").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/shortestpath/**").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/shortestpath/**").hasRole("USER")
        
        .and().csrf().disable().formLogin().disable();

      
    	//TODO specify the security config for different end points
    }
     
    
}