package com.hitori;

import com.hitori.entity.Account;
import com.hitori.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    BCryptPasswordEncoder pe;

    @Autowired
    AccountService accountService;

    @Autowired
    HttpSession session;

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(username -> {
        try {
            Account user = accountService.findById(username);
            if (!user.isAvailable()) { // Kiểm tra user available
                throw new UsernameNotFoundException(username + " is not available!");
            }

            String password = pe.encode(user.getPassword());
            String[] roles = user.getAuthorities().stream()
                    .map(er -> er.getRole().getId())
                    .collect(Collectors.toList())
                    .toArray(new String[0]);

            Map<String, Object> authentication = new HashMap<>();
            authentication.put("user", user);
            byte[] token = (username + ":" + user.getPassword()).getBytes();
            authentication.put("token", "Basic " + Base64.getEncoder().encodeToString(token));
            session.setAttribute("authentication", authentication);

            return User.withUsername(username).password(password).roles(roles).build();
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException(username + " not found!");
        }
    });
}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/create-booking").authenticated()
                .anyRequest().permitAll();


        http.formLogin()
                .loginPage("/security/login/form")
                .loginProcessingUrl("/security/login")
                .defaultSuccessUrl("/security/login/success", false)
                .failureUrl("/security/login/error");

//        http.exceptionHandling()
//                .accessDeniedPage("/user/unauthoried");

        http.logout()
                .logoutUrl("/security/logoff")
                .logoutSuccessUrl("/security/logoff/success");
    }
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
