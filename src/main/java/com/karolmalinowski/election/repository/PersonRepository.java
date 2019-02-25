package com.karolmalinowski.election.repository;

import com.karolmalinowski.election.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findAllByPesel(String pesel);
}
