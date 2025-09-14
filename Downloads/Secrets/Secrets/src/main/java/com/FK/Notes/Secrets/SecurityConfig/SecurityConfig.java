package com.FK.Notes.Secrets.SecurityConfig;

import com.FK.Notes.Secrets.Models.AppRole;
import com.FK.Notes.Secrets.Models.Role;
import com.FK.Notes.Secrets.Repo.RoleRepository;
import com.FK.Notes.Secrets.Repo.UserRepo;
import com.FK.Notes.Secrets.SecurityConfig.JWT.AuthEntryPoint;
import com.FK.Notes.Secrets.SecurityConfig.JWT.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.FK.Notes.Secrets.Models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

import java.time.LocalDate;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration //@Configuration → Marks this class as a source of bean definitions.
@EnableWebSecurity //enables Spring Security’s web security support.
@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true) //Enables JSR-250 annotations for method-level security. such as
/*
@RolesAllowed("ADMIN") → Only users with ADMIN role can access the method

@PermitAll → Everyone can access

@DenyAll → No one can access

 */
public class SecurityConfig {

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringRequestMatchers("/api/auth/public/**"));
        http.authorizeHttpRequests((request)->request.requestMatchers("/api/csrf-token/**").permitAll().requestMatchers("/api/auth/public/**").permitAll().anyRequest().authenticated());
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
         http.httpBasic(withDefaults());
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
    }

    //Initializing random users when program starts
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepo userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            if (!userRepository.existsByUserName("user2")) {
                User user1 = new User("user2", "user2@example.com", passwordEncoder.encode("password1"));
                user1.setAccountNonLocked(true);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRole(userRole);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("password2"));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
            if(!userRepository.existsByUserName("developer")){
                User developer = new User("developer","developer@gmail.com",passwordEncoder.encode("dev"));
                developer.setAccountNonLocked(true);
                developer.setAccountNonExpired(true);
                developer.setCredentialsNonExpired(true);
                developer.setEnabled(true);
                developer.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                developer.setAccountExpiryDate(LocalDate.now().plusYears(1));
                developer.setTwoFactorEnabled(false);
                developer.setSignUpMethod("email");
                developer.setRole(userRole);
                userRepository.save(developer);

            }
        };

/*
A lambda expression is a shorthand way to implement a functional interface (an interface with a single abstract method) without writing a full class.

Syntax:

(parameters) -> { body }


parameters → the input(s) to the function

-> → “maps to”

{ body } → the code to execute


Without Lambda orignal function would look like this :
return new CommandLineRunner() {
        @Override
        public void run(String... args) throws Exception {
            // code to create roles and users
        }
    };


 */

    }
}
