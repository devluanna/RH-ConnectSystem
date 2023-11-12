package com.connect.system.domain.model.Account.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocationDTO {
    private Long id_location;

    private String country;
    private String state;
    private String city;
    private String address;
    private int zip_code;
    private String complement;
    private int house_number;
}
