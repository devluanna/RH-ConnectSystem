package com.connect.system.domain.model.AccountInformation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@JsonPropertyOrder({ "id_personalData", "id_account", "identity", "date_of_birth", "telephone", "cpf_person", "rg_person", "profile_image", "location"})
public class PersonalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_personalData;

    private Long id_account;
    private String identity;

    private String date_of_birth;
    private String telephone;
    private String cpf_person;
    private String rg_person;
    private String profile_image;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    public PersonalData() {
        this.date_of_birth = "Your date of birth";
        this.telephone = "Telephone";
        this.cpf_person = "CPF";
        this.rg_person = "RG";
        this.profile_image = profile_image;
        this.location = new Location();
    }



}
