package com.connect.system.service;

import com.connect.system.domain.model.Account.DashboardStudies.AcademicEducation;
import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import org.springframework.stereotype.Service;

@Service
public interface DashboardStudiesService {
    DashboardStudies findDashboardById(Integer dashboardId);

    Certificates shareMyCertificates(DashboardStudies dashboard, Integer dashboardId, Certificates certificates);

    AcademicEducation shareMyStudies(DashboardStudies dashboard, Integer dashboardId, AcademicEducation academic);

    Certificates toUpdateCertificate(Certificates certificates, Integer idCertificate);

    AcademicEducation toUpdateAcademicStudies(AcademicEducation academic, Integer idAcademicEducation);

    DashboardStudies getCertificatesById (Integer dashboard_id);

    DashboardStudies getStudiesById(Integer dashboard_id);
}
