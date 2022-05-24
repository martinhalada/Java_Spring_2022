package com.example.weatherapp.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StateRepositoryImpl {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(State state){
        jdbcTemplate.update("INSERT INTO State (name, code) VALUES (?, ?)", state.getName().toLowerCase(), state.getCode().toLowerCase());
    }

    public List<State> findAll(){
        return jdbcTemplate.query("SELECT name, code FROM State", (rs, rowNum) -> {
            State state = new State();
            state.setName(rs.getString("name"));
            state.setCode(rs.getString("code"));
            return state;
        });
    }

    public State findStateByCode(String code){
        try {
            return jdbcTemplate.queryForObject("SELECT name, code FROM State WHERE code = ?",
                    new Object[]{code},
                    (rs, rowNum) -> {
                        State state = new State();
                        state.setName(rs.getString("name"));
                        state.setCode(rs.getString("code"));
                        return state;
                    });
        }catch (Exception e){
            return null;
        }
    }

    public void deleteStateByCode(String code){
        jdbcTemplate.update("DELETE FROM State WHERE code = ?", code);
    }

    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM State");
    }

    public boolean existsState(String code){
        String sql = "SELECT count(*) FROM State WHERE code = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[] { code }, Integer.class);
        return count > 0;
    }

    public void updateState(String newValue, String code){
        jdbcTemplate.update("UPDATE State SET name = ? WHERE code = ?", newValue, code);
    }
}
