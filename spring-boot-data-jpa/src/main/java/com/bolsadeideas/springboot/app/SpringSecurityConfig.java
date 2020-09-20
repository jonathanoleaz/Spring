package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JpaUserDetailService jpaUserDetailService;
    /* Implementacion para las rutas */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * Internamente se agregan interceptores para identificar las rutas con el
         * usuario loggeado
         */
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/locale").permitAll()
                /*
                 * .antMatchers("/ver/**").hasAnyRole("USER")
                 * .antMatchers("/uploads/**").hasAnyRole("USER")
                 * .antMatchers("/form/**").hasAnyRole("ADMIN")
                 * .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
                 * .antMatchers("/factura/**").hasAnyRole("ADMIN")
                 */
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(loginSuccesHandler).loginPage("/login").permitAll()
                .and()
                .logout().permitAll().and().exceptionHandling().accessDeniedPage("/error_403");
    }

    
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {

        builder.userDetailsService(jpaUserDetailService).passwordEncoder(passwordEncoder);
        /*Configuracion el builder usando autenticacion con consulta nativa SQL 
        builder.jdbcAuthentication().dataSource(dataSource)
        .passwordEncoder(passwordEncoder)
        .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
        .authoritiesByUsernameQuery("SELECT user.username, aut.authority FROM authorities aut INNER JOIN users user ON (aut.user_id = user.id) WHERE user.username=?");
        */


        /*Configuramos el builder creando usuarios en memoria */
        //PasswordEncoder encoder = this.passwordEncoder;
        /**
         * expresion lambda: por cada usuario registrado, se hashea la contrasenia.
         * Cada que se asigne una contrasenia a users, se hashea la contrasenia con
         * passwordEncoder
         
        UserBuilder users = User.builder().passwordEncoder(password -> {
            return encoder.encode(password);
        });*/

        /* Configuramos el builder creando usuarios en memoria
        builder.inMemoryAuthentication().withUser(users.username("admin").password("password").roles("ADMIN", "USER"))
                .withUser(users.username("jonathanoleaz").password("password").roles("USER")); */

    }

}
