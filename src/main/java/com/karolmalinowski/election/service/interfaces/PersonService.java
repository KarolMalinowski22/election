package com.karolmalinowski.election.service.interfaces;

import com.karolmalinowski.election.model.Person;

public interface PersonService {
    /**
     * Validate Person object passed.
     * Validate by name not empty, surname not empty, pesel of exact long, and did vote already.
     * @param person
     */
    void valid(Person person) throws IllegalArgumentException;
}
