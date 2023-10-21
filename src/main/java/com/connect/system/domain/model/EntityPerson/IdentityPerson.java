package com.connect.system.domain.model.EntityPerson;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_identity")
public class IdentityPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identity;
    protected String generateId() {

        String characters = "0123456789";
        StringBuilder identity = new StringBuilder();
        int length = 6;

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            identity.append(characters.charAt(index));
        }

        return identity.toString();
    }

}
