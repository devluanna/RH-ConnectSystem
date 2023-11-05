package com.connect.system.domain.model.Account.Manager;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Informations.PersonalData;
import com.connect.system.domain.model.Jobs.JobsDetails;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("MANAGER")
@NoArgsConstructor
public class Manager extends Person {
    public Manager(String name, String last_name, String email, ProfileRole profileRole, PersonalData personalData, String identityPerson, String password, JobsDetails jobsDetails) {
        super(name, last_name, email, profileRole, personalData, identityPerson, password, jobsDetails);

        }

}
