package com.connect.system.domain.model.Account.DashboardStudies;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DashboardStudies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_dashboard;

    private Integer id_account;
    private String identity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AcademicEducation> academicEducation = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Certificates> myCertificates = new ArrayList<>();


    public void addCertificate(Certificates certificate) {
        myCertificates.add(certificate);
    }

    public void setMyCertificates(List<Certificates> myCertificates) {
        this.myCertificates = myCertificates;
    }

    public List<Certificates> getMyCertificates() {
        return myCertificates;
    }

    public void addStudies(AcademicEducation myStudies) {
        academicEducation.add(myStudies);
    }

    public void setAcademicEducation(List<AcademicEducation> academicEducation) {
        this.academicEducation = academicEducation;
    }

    public List<AcademicEducation> getAcademicEducation() {
        return academicEducation;
    }
}
