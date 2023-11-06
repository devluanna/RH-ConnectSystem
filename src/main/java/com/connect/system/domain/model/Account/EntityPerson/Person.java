package com.connect.system.domain.model.Account.EntityPerson;


import com.connect.system.domain.model.AccountInformation.PersonalData;
import com.connect.system.domain.model.Jobs.JobsDetails;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonPropertyOrder({ "id", "identityPerson", "name", "last_name", "email", "profileRole", "password", "status" })
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String last_name;
    private String email;
    private String identityPerson;
    private Status status;
    private String password;
    private ProfileRole profileRole;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "personal_data_id")
    private PersonalData personalData;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "jobs_details_id")
    private JobsDetails jobsDetails;

    public Person(String name, String last_name, String email, String identityPerson, String password, ProfileRole profileRole, PersonalData personalData, JobsDetails jobsDetails) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.identityPerson = identityPerson;
        this.password = password;
        this.profileRole = profileRole;
        this.personalData = personalData;
        this.jobsDetails = jobsDetails;
        this.status = Status.valueOf("AVAILABLE");

    }


}


