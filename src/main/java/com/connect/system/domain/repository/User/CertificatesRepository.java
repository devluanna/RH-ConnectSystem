package com.connect.system.domain.repository.User;

import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificatesRepository extends JpaRepository<Certificates, Integer> {

    @Query("SELECT c FROM Certificates c WHERE c.id_dashboardStudies = :id_dashboardStudies")
    List<Certificates> findDashboardById(@Param("id_dashboardStudies") Integer id_dashboardStudies);
}
