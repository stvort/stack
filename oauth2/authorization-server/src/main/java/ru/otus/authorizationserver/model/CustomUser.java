package ru.otus.authorizationserver.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String fatherName;

    public CustomUser(String firstName, String fatherName,
                      String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.firstName = firstName;
        this.fatherName = fatherName;
    }
}
