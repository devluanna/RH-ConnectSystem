package com.connect.system.rest.controller;

import com.connect.system.domain.model.Account.DashboardStudies.AcademicEducation;
import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import com.connect.system.domain.repository.User.CertificatesRepository;
import com.connect.system.service.DashboardStudiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardStudiesController {

    @Autowired
    DashboardStudiesService dashboardStudiesService;

    @Autowired
    CertificatesRepository certificatesRepository;

    @GetMapping("/find/certificates/{dashboard_id}")
    public ResponseEntity<DashboardStudies> findCertificates(@PathVariable Long dashboard_id) {
        DashboardStudies allMyCertificates  = dashboardStudiesService.getCertificatesById(dashboard_id);

        return ResponseEntity.ok(allMyCertificates);
    }

    @GetMapping("/find/academic/{dashboard_id}")
    public ResponseEntity<DashboardStudies> findAcademicEducation(@PathVariable Long dashboard_id) {
        DashboardStudies allMyStudies = dashboardStudiesService.getStudiesById(dashboard_id);

        return ResponseEntity.ok(allMyStudies);
    }

    @GetMapping("/findAll/{dashboard_id}")
    public ResponseEntity findMyAllStudies(@PathVariable Long dashboard_id) {
        DashboardStudies dashboard = dashboardStudiesService.findDashboardById(dashboard_id);

        if(dashboard == null) {
            ResponseEntity.notFound().build();}

        return ResponseEntity.ok(dashboard);
    }

    @PostMapping("/certificates/{dashboard_id}")
    public ResponseEntity shareCertificate(@PathVariable Long dashboard_id, @RequestBody Certificates certificates) {

        DashboardStudies dashboard = dashboardStudiesService.findDashboardById(dashboard_id);

        Certificates shareNewCertificate = dashboardStudiesService.shareMyCertificates(dashboard, dashboard_id, certificates);
        return ResponseEntity.ok(shareNewCertificate);
    }

    @PostMapping("/academic/{dashboard_id}")
    public ResponseEntity shareAcademicEducation(@PathVariable Long dashboard_id, @RequestBody AcademicEducation academic) {

        DashboardStudies dashboard = dashboardStudiesService.findDashboardById(dashboard_id);

        AcademicEducation shareNewStudies = dashboardStudiesService.shareMyStudies(dashboard, dashboard_id, academic);

        return ResponseEntity.ok(shareNewStudies);
    }

    @PutMapping("/update/certificate/{id_certificate}")
    public ResponseEntity updateCertificate(@PathVariable Long id_certificate, @RequestBody Certificates certificates) {

        Certificates updatedCertificate = dashboardStudiesService.toUpdateCertificate(certificates, id_certificate);

        return ResponseEntity.ok(updatedCertificate);
    }

    @PutMapping("/update/academic/{id_academicEducation}")
    public ResponseEntity updateStudies(@PathVariable Long id_academicEducation, @RequestBody AcademicEducation academic) {

        AcademicEducation updatedStudies = dashboardStudiesService.toUpdateAcademicStudies(academic, id_academicEducation);

        return ResponseEntity.ok(updatedStudies);
    }


}





