package com.karolmalinowski.election.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "disallowed_voting_try")
public class DisallowedVotingTry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
