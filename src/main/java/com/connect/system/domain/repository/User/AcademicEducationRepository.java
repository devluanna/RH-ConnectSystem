package com.connect.system.domain.repository.User;

import com.connect.system.domain.model.Account.DashboardStudies.AcademicEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicEducationRepository extends JpaRepository<AcademicEducation, Integer> {
    @Query("SELECT c FROM AcademicEducation c WHERE c.id_dashboardStudies = :id_dashboardStudies")
    List<AcademicEducation> findDashboardById(@Param("id_dashboardStudies") Integer dashboard_id);
}
