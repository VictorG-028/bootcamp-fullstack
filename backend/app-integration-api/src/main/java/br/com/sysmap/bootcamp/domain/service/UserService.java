package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entity.User;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // =-= Util section =-= //
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = this.findByEmail(email);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<GrantedAuthority>() // Lista vazia de autoridade
        );
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return this.userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User " +email+ " not found"));
    }
}
