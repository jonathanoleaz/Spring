/**Primer filtro: autenticacion */
package com.bolsadeideas.springboot.app.auth.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bolsadeideas.springboot.app.auth.service.JWTService;
import com.bolsadeideas.springboot.app.auth.service.JWTServiceImpl;
import com.bolsadeideas.springboot.app.models.entity.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private AuthenticationManager authenticationManager;
	private JWTService jwtService;
	
	/**Notar que se recibe el JWTService en el constructor porque en los filtros no pueden inyectarse beans de spring */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

		this.jwtService = jwtService;
	}

	/**Metodo para intentar autenticar */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("Filtro");
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		if(username!=null && password!=null) {
			System.out.println("Username desde request parameter (form-data): "+username);
			System.out.println("Password desde request parameter (form-data): "+password);
		}else{
			Usuario user=null;
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

				username=user.getUsername();
				password=user.getPassword();

				System.out.println("Username desde request parameter (raw): "+username);
				System.out.println("Password desde request parameter (raw): "+password);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		username = username.trim();
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

		return authenticationManager.authenticate(authToken);
	}

	/**Metodo llamado cuando se autentica exitosamente */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String token = jwtService.create(authResult);
		
		response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX + token);
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", token);
		body.put("user", (User) authResult.getPrincipal());
		body.put("mensaje", "Sesion iniciada como:"+authResult.getName());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		
		response.setStatus(200);
		response.setContentType("application/json");
	}

	/**Metodo llamado cuando NO se autentica exitosamente */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("mensaje", "Error de autenticacion, datos incorrectos");
		body.put("error", failed.getMessage());

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		
		response.setStatus(401);
		response.setContentType("application/json");
	}
	

}
