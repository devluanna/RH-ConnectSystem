package com.connect.system.domain.model.Account.DashboardStudies;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@JsonPropertyOrder({ "id_dashboardStudies", "id_certificate", "name_certificate", "issuing_organization", "date_of_issue", "date_expiration" })
public class Certificates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_certificate;

    private Integer id_dashboardStudies;

    private String name_certificate;
    private String issuing_organization;
    private String date_of_issue;
    private String date_expiration;

}
