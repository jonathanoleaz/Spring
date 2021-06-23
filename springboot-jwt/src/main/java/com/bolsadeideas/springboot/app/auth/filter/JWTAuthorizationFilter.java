/**Segundo filtro: autorizacion */
package com.bolsadeideas.springboot.app.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bolsadeideas.springboot.app.auth.service.JWTService;
import com.bolsadeideas.springboot.app.auth.service.JWTServiceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

    private JWTService jwtService;

    /**Notar que se recibe el JWTService en el constructor porque en los filtros no pueden inyectarse beans de spring */
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        super(authenticationManager);
        this.jwtService=jwtService;
    }

    /**Este metodo se ejecuta en todos los requests */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(JWTServiceImpl.HEADER_STRING);

        if(!requiresAuthentication(header)){
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken=null;

        if(jwtService.validate(header)){
            String username =jwtService.getUserName(header);

            authenticationToken = new UsernamePasswordAuthenticationToken(username, null, jwtService.getRoles(header));
        }

        /** Se autentica al usuario dentro de la peticion, pues no se usan sesiones */
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    /**Para saber si la peticion incluye el prefijo del token ('Bearer') */
    protected boolean requiresAuthentication(String header){
        if(header==null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)){
            return false;
        }
        return true;
    }
}
