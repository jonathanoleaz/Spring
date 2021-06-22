package com.bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Rol;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jpaUserDetailService")
public class JpaUserDetailService implements UserDetailsService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);

        /*Lista con los roles del usuario */
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        if(usuario!=null){
            for(Rol role: usuario.getRoles()){
                authorities.add(new SimpleGrantedAuthority(role.getAuthority()) );
            }
        }
        
        return new User(usuario.getUsername(), usuario.getPassword(), authorities);
    }    
}
