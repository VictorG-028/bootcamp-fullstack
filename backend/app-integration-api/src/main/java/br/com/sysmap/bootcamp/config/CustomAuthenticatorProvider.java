package br.com.sysmap.bootcamp.config;

import br.com.sysmap.bootcamp.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticatorProvider implements AuthenticationProvider {

    @Autowired
    private UserService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();

        try {
            UserDetails userDetails = this.userDetailsService
                                          .loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(
                                                userDetails.getUsername(),
                                                userDetails.getPassword(),
                                                userDetails.getAuthorities());
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid Credentials");
        } catch (Exception e) {
            String error = e.getClass().getSimpleName();
            throw new BadCredentialsException("Something went wrong: " + error);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken
                .class
                .isAssignableFrom(authentication);
    }
}
