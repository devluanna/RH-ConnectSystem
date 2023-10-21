package com.connect.system.domain.model.EntityPerson;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "tb_person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private IdentityPerson identity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PasswordPerson password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Status status;

    private ProfileRole role;
    
    private String name;
    private String last_name;
    private String email;

    public Person() {
        this.name = name;
        this.last_name = last_name;
        this.email = email;

        this.status = new Status();
        this.status.isStatus();


        this.identity = new IdentityPerson();
        this.identity.setIdentity(this.identity.generateId());

        this.password = new PasswordPerson();
        this.password.setPassword(this.password.generateRandomPassword());
    }

}
