package com.connect.system.domain.model.Account.AccountInformation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_location")
@JsonPropertyOrder({ "id_location", "country", "state", "city", "address", "zip_code", "complement", "house_number" })
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_location;

    private String country;
    private String state;
    private String city;
    private String address;
    private int zip_code;
    private String complement;
    private int house_number;


}
