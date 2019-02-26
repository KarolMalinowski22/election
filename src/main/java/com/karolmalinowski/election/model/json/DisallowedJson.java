package com.karolmalinowski.election.model.json;

import lombok.Getter;

@Getter
public class DisallowedJson {
    private String publicationDate;
    private DisallowedPerson[] person;
}
