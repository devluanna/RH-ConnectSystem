package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.DashboardStudies.AcademicEducation;
import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import com.connect.system.domain.repository.User.AcademicEducationRepository;
import com.connect.system.domain.repository.User.CertificatesRepository;
import com.connect.system.domain.repository.User.DashboardStudiesRepository;
import com.connect.system.service.DashboardStudiesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DashboardStudiesServiceImpl implements DashboardStudiesService {

    @Autowired
    DashboardStudiesRepository dashboardStudiesRepository;

    @Autowired
    CertificatesRepository certificatesRepository;

    @Autowired
    AcademicEducationRepository academicEducationRepository;


    @Override
    public DashboardStudies findDashboardById(Long dashboardId) {
        return dashboardStudiesRepository.findById(dashboardId).orElseThrow(NoSuchElementException::new);

    }

    @Override
    public Certificates shareMyCertificates(DashboardStudies dashboardStudies, Long dashboardId, Certificates certificates) {

        DashboardStudies serviceDashboard = findDashboardById(dashboardId);

        Certificates myCertificate = new Certificates();

        serviceDashboard.addCertificate(myCertificate);
        myCertificate.setId_dashboardStudies(serviceDashboard.getId_dashboard());
        myCertificate.setName_certificate(certificates.getName_certificate());
        myCertificate.setIssuing_organization(certificates.getIssuing_organization());
        myCertificate.setDate_of_issue(certificates.getDate_of_issue());
        myCertificate.setDate_expiration(certificates.getDate_expiration());

       Certificates certificatesSave = certificatesRepository.save(myCertificate);

       BeanUtils.copyProperties(certificatesSave, certificates);

       return (certificates);

    }

    @Override
    public AcademicEducation shareMyStudies(DashboardStudies dashboard, Long dashboardId, AcademicEducation academic) {
        DashboardStudies serviceDashboard = findDashboardById(dashboardId);

        AcademicEducation myStudies = new AcademicEducation();

        serviceDashboard.addStudies(myStudies);

        myStudies.setId_dashboardStudies(serviceDashboard.getId_dashboard());
        myStudies.setEducation_institution(academic.getEducation_institution());
        myStudies.setCourse_name(academic.getCourse_name());
        myStudies.setStudy_area(academic.getStudy_area());
        myStudies.setStart_date(academic.getStart_date());
        myStudies.setEnd_date(academic.getEnd_date());
        myStudies.setType_of_training(academic.getType_of_training());


        AcademicEducation studiesSave = academicEducationRepository.save(myStudies);

        BeanUtils.copyProperties(studiesSave, academic);

        return (academic);
    }

    @Override
    public Certificates toUpdateCertificate(Certificates certificates, Long idCertificate) {

        certificates.setId_certificate(idCertificate);

        Certificates existingCertificate = certificatesRepository.findById(idCertificate).orElse(null);

        if (existingCertificate != null) {
            certificates.setId_dashboardStudies(existingCertificate.getId_dashboardStudies());
        }

        return certificatesRepository.save(certificates);

    }

    @Override
    public AcademicEducation toUpdateAcademicStudies(AcademicEducation academic, Long idAcademicEducation) {

        academic.setId_academicEducation(idAcademicEducation);

        AcademicEducation existingAcademicStudies = academicEducationRepository.findById(idAcademicEducation).orElse(null);

        if (existingAcademicStudies != null) {
            academic.setId_dashboardStudies(existingAcademicStudies.getId_dashboardStudies());
        }

        return academicEducationRepository.save(academic);
    }


}
