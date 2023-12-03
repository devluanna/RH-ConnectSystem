package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.DashboardStudies.AcademicEducation;
import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.repository.User.AcademicEducationRepository;
import com.connect.system.domain.repository.User.CertificatesRepository;
import com.connect.system.domain.repository.User.DashboardStudiesRepository;
import com.connect.system.service.DashboardStudiesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.connect.system.utils.Utils;

import java.util.List;
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
    public DashboardStudies findDashboardById(Integer dashboardId) {
        return dashboardStudiesRepository.findById(dashboardId).orElseThrow(NoSuchElementException::new);

    }


    @Override
    public Certificates shareMyCertificates(DashboardStudies dashboardStudies, Integer dashboardId, Certificates certificates) {

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
    public AcademicEducation shareMyStudies(DashboardStudies dashboard, Integer dashboardId, AcademicEducation academic) {
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
    public Certificates toUpdateCertificate(Certificates certificates, Integer idCertificate) {

         Certificates existingCertificate = certificatesRepository.findById(idCertificate).orElse(null);

         Integer id_DashboardStudies = existingCertificate.getId_dashboardStudies();

         Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

         if (!authenticatedUser.getDashboardStudies().getId_dashboard().equals(id_DashboardStudies)) {
         throw new IllegalArgumentException("Access denied!");
         }

        if (existingCertificate != null) {
            Utils.copyNonNullProperties(certificates, existingCertificate);
        }
            return certificatesRepository.save(existingCertificate);

    }

    @Override
    public AcademicEducation toUpdateAcademicStudies(AcademicEducation academic, Integer idAcademicEducation) {

        AcademicEducation existingAcademicStudies = academicEducationRepository.findById(idAcademicEducation).orElse(null);

        Integer id_DashboardStudies = existingAcademicStudies.getId_dashboardStudies();

        Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authenticatedUser.getDashboardStudies().getId_dashboard().equals(id_DashboardStudies)) {
        throw new IllegalArgumentException("Access denied!");
         }

        if (existingAcademicStudies != null) {
            Utils.copyNonNullProperties(academic, existingAcademicStudies);
        }

        return academicEducationRepository.save(existingAcademicStudies);
    }

    @Override
    public DashboardStudies getCertificatesById(Integer dashboard_id) {

        DashboardStudies dashboard = findDashboardById(dashboard_id);

        if (dashboard != null) {
            String user = dashboard.getIdentity();

            if (user != null) {

                String username = user.intern();

                List<Certificates> myCertificates = certificatesRepository.findDashboardById(dashboard_id);
                dashboard.setMyCertificates(myCertificates);
                dashboard.setIdentity(username);
            }
        }

        return dashboard;
    }

    @Override
    public DashboardStudies getStudiesById(Integer dashboard_id) {
        DashboardStudies dashboard = findDashboardById(dashboard_id);

        if(dashboard != null) {
            String user = dashboard.getIdentity();
            if(user != null) {
                String username = user.intern();
                List<AcademicEducation> myStudies = academicEducationRepository.findDashboardById(dashboard_id);
                dashboard.setAcademicEducation(myStudies);
                dashboard.setIdentity(username);
            }
        }
        return dashboard;
    }


}
