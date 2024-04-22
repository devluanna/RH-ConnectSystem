package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.TechnologyCommunity.ListAssociatesHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListAssociatesHierarchyRepository extends JpaRepository<ListAssociatesHierarchy, Integer> {

    @Query("SELECT h FROM ListAssociatesHierarchy h WHERE h.name_community = :name_community")
    ListAssociatesHierarchy findByCommunityName(@Param("name_community") String name_community);

    @Query("SELECT h FROM ListAssociatesHierarchy h WHERE h.name_community = :name_community")
    List<ListAssociatesHierarchy> findByNameCommunity(@Param("name_community") String name_community);

    @Query("SELECT h FROM ListAssociatesHierarchy h WHERE h.id_account = :id_account")
    Optional<ListAssociatesHierarchy> findByAccountId(Integer id_account);

    //@Query("SELECT h FROM ListAssociatesHierarchy h WHERE h.id_group = :id_group")
    //ListAssociatesHierarchy findByIdOfGroup(Integer id_group);

    @Query("SELECT h FROM ListAssociatesHierarchy h WHERE h.id_group = :id_group")
    List<ListAssociatesHierarchy> findByGroupId(Integer id_group);
}
