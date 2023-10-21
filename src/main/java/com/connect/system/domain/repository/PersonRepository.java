package com.connect.system.domain.repository;

import com.connect.system.domain.model.EntityPerson.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
