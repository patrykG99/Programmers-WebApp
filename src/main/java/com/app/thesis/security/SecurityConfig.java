package com.app.thesis.security;


import com.app.thesis.security.jwt.AuthEntryPointJwt;
import com.app.thesis.security.jwt.AuthTokenFilter;
import com.app.thesis.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;





@Configuration
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig { // extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

//  @Override
//  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//  }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }




//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.cors().and().csrf().disable()
//      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//      .antMatchers("/api/test/**").permitAll()
//      .anyRequest().authenticated();
//
//    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//  }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/projects/save").hasAuthority("ROLE_USER")
                .antMatchers("/api/projects/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/invites/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/project/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/project/end/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/users/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/ratings/projectReviews/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/ratings/user/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/users/recommended/**").hasAuthority("ROLE_USER")
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/chatroom/public").permitAll()
                .antMatchers("/private-message").permitAll()
                .antMatchers("/user/**/private").permitAll()
                .antMatchers("/api/project/messages/**").hasAuthority("ROLE_USER")
                .antMatchers("/message/{id}").hasAuthority("ROLE_USER")
                .antMatchers("/api/users/description/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/projectsPage").permitAll()
                .antMatchers("/api/projectinvites/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/project/info/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/users").hasAuthority("ROLE_USER")
                .antMatchers("/api/rating/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/user/{id}/avatar").hasAuthority("ROLE_USER")
                .antMatchers("/api/myprojects").hasAuthority("ROLE_USER")
                .antMatchers("/api/report").hasAuthority("ROLE_USER")
                .antMatchers("/api/reports").hasAuthority("ROLE_MODERATOR")
                .antMatchers("/api/project/mod/{id}").hasAuthority("ROLE_MODERATOR")
                .antMatchers("/api/users/**").permitAll()
                .antMatchers("/api/test/**").permitAll()

                .anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}





