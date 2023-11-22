package com.connect.system.service;

import com.connect.system.domain.model.Account.DashboardStudies.AcademicEducation;
import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import org.springframework.stereotype.Service;

@Service
public interface DashboardStudiesService {
    DashboardStudies findDashboardById(Long dashboardId);

    Certificates shareMyCertificates(DashboardStudies dashboard, Long dashboardId, Certificates certificates);

    AcademicEducation shareMyStudies(DashboardStudies dashboard, Long dashboardId, AcademicEducation academic);

    Certificates toUpdateCertificate(Certificates certificates, Long idCertificate);

    AcademicEducation toUpdateAcademicStudies(AcademicEducation academic, Long idAcademicEducation);

    DashboardStudies getCertificatesById (Long dashboard_id);

    DashboardStudies getStudiesById(Long dashboard_id);
}
