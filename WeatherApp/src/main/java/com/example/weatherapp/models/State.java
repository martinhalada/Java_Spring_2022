package com.example.weatherapp.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="State")
public class State{

    private String name;
    @Id
    private String code;

    public State(String code) {
        this.name = "Název neuveden";
        this.code = code;
    }

    public State(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public State() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
