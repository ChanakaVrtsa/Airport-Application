package com.chanaka.project.oauthauthorizationserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {
    public User() {
    }

    public User(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.enabled = user.isEnabled();
        this.accountNonExpired = user.isAccountNonExpired();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.role = user.getRole();
        this.readAccess = user.isReadAccess();
        this.writeAccess = user.isWriteAccess();
        this.updateAccess = user.isUpdateAccess();
        this.deleteAccess = user.isDeleteAccess();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "accountNonExpired")
    private boolean accountNonExpired;
    @Column(name = "credentialsNonExpired")
    private boolean credentialsNonExpired;
    @Column(name = "accountNonLocked")
    private boolean accountNonLocked;
    @Column(name = "role")
    private String role;
    @Column(name = "readAccess")
    private boolean readAccess;
    @Column(name = "writeAccess")
    private boolean writeAccess;
    @Column(name = "updateAccess")
    private boolean updateAccess;
    @Column(name = "deleteAccess")
    private boolean deleteAccess;


}