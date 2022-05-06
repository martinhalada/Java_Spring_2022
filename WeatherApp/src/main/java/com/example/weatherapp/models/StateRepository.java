package com.example.weatherapp.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, String> {

    State findStateByCode(String code);
    void deleteStateByCode(String code);
}
