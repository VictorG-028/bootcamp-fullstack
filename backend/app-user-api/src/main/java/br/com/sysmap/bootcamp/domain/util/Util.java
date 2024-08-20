package br.com.sysmap.bootcamp.domain.util;

import br.com.sysmap.bootcamp.domain.entity.User;
import br.com.sysmap.bootcamp.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Slf4j
@Component
public class Util {

    private UserService userService;

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The logged-in user.
     */
    public User getLoggedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userService.findByEmail(email);
    }
}
