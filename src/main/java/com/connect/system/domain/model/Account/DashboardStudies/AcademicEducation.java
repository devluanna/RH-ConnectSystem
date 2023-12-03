package com.connect.system.domain.model.Account.DashboardStudies;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@JsonPropertyOrder({ "id_dashboardStudies", "id_academicEducation", "education_institution", "course_name", "study_area", "start_date", "end_date", "type_of_training" })
public class AcademicEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_academicEducation;

    private Integer id_dashboardStudies;

    private String education_institution;
    private String course_name;
    private String study_area;
    private String start_date;
    private String end_date;
    private String type_of_training;

}
