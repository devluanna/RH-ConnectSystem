package com.connect.system.domain.model.Account.EntityPerson;


import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import com.connect.system.domain.model.Account.Jobs.*;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@JsonPropertyOrder({ "id", "identityPerson", "name", "last_name", "email", "role", "type_of_record", "office", "occupancy_area", "seniority", "community", "sub_position", "report_me", "password", "status" })
public class Person implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String last_name;
    private String email;
    private String identityPerson;
    private Status status;
    private String password;
    private ProfileRole role;

    private TypeOfRecord type_of_record;
    private Office office;
    private SubPosition sub_position;
    private OccupancyArea occupancy_area;
    private Seniority seniority;
    private String community;
    private String report_me;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "personal_data_id")
    private PersonalData personalData;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "jobs_details_id")
    private JobsDetails jobsDetails;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dashboardStudies_id")
    private DashboardStudies dashboardStudies;

    public Person(String name, String last_name, String email, String identityPerson, String password, ProfileRole role, TypeOfRecord type_of_record, Office office, OccupancyArea occupancy_area, Seniority seniority, String community, SubPosition sub_position, String report_me, PersonalData personalData, JobsDetails jobsDetails, DashboardStudies dashboardStudies) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.identityPerson = identityPerson;
        this.password = password;
        this.type_of_record = type_of_record;
        this.office = office;
        this.occupancy_area = occupancy_area;
        this.seniority = seniority;
        this.community = community;
        this.sub_position = sub_position;
        this.report_me = report_me;
        this.role = role;
        this.personalData = personalData;
        this.jobsDetails = jobsDetails;
        this.dashboardStudies = dashboardStudies;
        this.status = Status.valueOf("AVAILABLE");
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == ProfileRole.RH) return List.of(new SimpleGrantedAuthority("ROLE_RH"), new SimpleGrantedAuthority("ROLE_MANAGER"), new SimpleGrantedAuthority("ROLE_USER"));
        else if(this.role == ProfileRole.MANAGER) return List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return identityPerson;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }



    @Override
    public boolean isEnabled() {
        return true;
    }
}


