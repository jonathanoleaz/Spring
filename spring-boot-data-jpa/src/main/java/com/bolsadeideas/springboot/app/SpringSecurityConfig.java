package com.bolsadeideas.springboot.app;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * La anotacion EnableGlobalMethodSecurity habilita entre otras cosas, el uso de
 * anotaciones sobre autorizacion y autenticacion en los controladores, (se
 * comento la implementacion manual de restriccion de rutas por roles en esta
 * clase)
 * 
 */

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccesHandler loginSuccesHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* Implementacion para las rutas */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * Internamente se agregan interceptores para identificar las rutas con el
         * usuario loggeado
         */
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                /*
                 * .antMatchers("/ver/**").hasAnyRole("USER")
                 * .antMatchers("/uploads/**").hasAnyRole("USER")
                 * .antMatchers("/form/**").hasAnyRole("ADMIN")
                 * .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
                 * .antMatchers("/factura/**").hasAnyRole("ADMIN")
                 */
                .anyRequest().authenticated().and().formLogin().successHandler(loginSuccesHandler).loginPage("/login")
                .permitAll().and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/error_403");
    }

    /** Se guardan usuarios en memoria */
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {

        PasswordEncoder encoder = passwordEncoder();
        /**
         * expresion lambda: por cada usuario registrado, se hashea la contrasenia.
         * Cada que se asigne una contrasenia a users, se hashea la contrasenia con
         * passwordEncoder
         */
        UserBuilder users = User.builder().passwordEncoder(password -> {
            return encoder.encode(password);
        });

        /* Configuramos el Creamos usuarios en memoria */
        builder.inMemoryAuthentication().withUser(users.username("admin").password("password").roles("ADMIN", "USER"))
                .withUser(users.username("jonathanoleaz").password("password").roles("USER"));

    }

}
