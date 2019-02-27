package com.karolmalinowski.election.repository;

import com.karolmalinowski.election.model.DisallowedVotingTry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisallowedVotingTriesRepository extends JpaRepository<DisallowedVotingTry, Long> {
}
