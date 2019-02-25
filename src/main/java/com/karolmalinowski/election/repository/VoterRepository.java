package com.karolmalinowski.election.repository;

import com.karolmalinowski.election.model.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    Optional<Voter> findAllByPesel(String pesel);
}
