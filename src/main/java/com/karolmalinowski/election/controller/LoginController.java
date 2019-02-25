package com.karolmalinowski.election.controller;

import com.karolmalinowski.election.model.Person;
import com.karolmalinowski.election.service.interfaces.PersonService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField surnameInput;
    @FXML
    private TextField peselInput;
    @FXML
    private Button voteButton;

    @Autowired
    PersonService personService;

    public void initialize(){
        voteButton.setOnAction(event -> login());
    }

    private void login() {
        Person person = new Person();
        person.setName(nameInput.getText());person.setSurname(surnameInput.getText());person.setPesel(passwordEncoder.encode(peselInput.getText()));
        try {
            personService.valid(person);
        }catch (IllegalArgumentException e){
            new Alert(Alert.AlertType.INFORMATION, e.getMessage()).showAndWait();
        }
    }

}
