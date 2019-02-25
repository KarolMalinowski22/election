package com.karolmalinowski.election.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String party;
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private Set<Voter> voters;
    public Candidate(String name, String party){
        this.name = name;
        this.party = party;
    }
}
