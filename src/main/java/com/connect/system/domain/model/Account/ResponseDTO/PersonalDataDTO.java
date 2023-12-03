package com.connect.system.domain.model.Account.ResponseDTO;

import com.connect.system.domain.model.Account.AccountInformation.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonalDataDTO  {
   private Integer id_personalData;
   private Integer id_account;
   private String identity;
   private String date_of_birth;
   private String telephone;
   private String cpf_person;
   private String rg_person;
   private String profile_image;
   private Location location;

}
