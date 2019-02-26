package com.karolmalinowski.election.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String pesel;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "vote",
            joinColumns = @JoinColumn(name = "voterId"),
            inverseJoinColumns = @JoinColumn(name = "candidateId"))
    private List<Candidate> candidate;
}
