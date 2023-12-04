package com.connect.system.domain.model.Account.ResponseDTO;

import com.connect.system.domain.model.Account.Jobs.Office;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonByOfficeHeadDTO {
    private String name;
    private String last_name;
    private Office office;


}
