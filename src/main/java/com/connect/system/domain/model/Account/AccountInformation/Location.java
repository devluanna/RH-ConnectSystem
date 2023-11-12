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
    private Long id_location;

    private String country;
    private String state;
    private String city;
    private String address;
    private int zip_code;
    private String complement;
    private int house_number;

    public Location() {
        this.country = "Country";
        this.state = "State";
        this.city = "City";
        this.address = "Your Address here";
        this.zip_code = 10000000;
        this.complement = "For example: BLOCK 12 and Apartment 12";
        this.house_number = 1;
    }

}
