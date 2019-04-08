package org.moduleap.api.models;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private Integer postalCode;
    @Column
    private String street;
    @Column
    private String Country;
    @Column
    private String email;
    @Column
    private int adminLevel;
    @Column
    private boolean active;
    @Column
    private Date date;
    //The verifykey which gets generated by registration
    @OneToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "verify_user_id")
    private VerifyUser verify;
}
