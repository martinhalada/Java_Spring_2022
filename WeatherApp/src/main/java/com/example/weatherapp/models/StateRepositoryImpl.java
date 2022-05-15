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
        jdbcTemplate.update("INSERT INTO State VALUES (?, ?)", state.getName(), state.getCode());
    }
    public List<State> findAll(){
        return jdbcTemplate.query("SELECT name, code FROM State", new RowMapper<State>() {
            @Override
            public State mapRow(ResultSet rs, int rowNum) throws SQLException {
                State state = new State();
                state.setName(rs.getString("name"));
                state.setCode(rs.getString("code"));
                return state;
            }
        });
    }
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM State");
    }
    public State findStateByCode(String code){
        try {
            State state = jdbcTemplate.queryForObject("SELECT name, code FROM State WHERE code = ?",
                    new Object[]{code},
                    new RowMapper<State>() {
                        @Override
                        public State mapRow(ResultSet rs, int rowNum) throws SQLException {
                            State state = new State();
                            state.setName(rs.getString("name"));
                            state.setCode(rs.getString("code"));
                            return state;
                        }
                    });
            return state;
        }catch (Exception e){
            return null;
        }
    }
    public void deleteStateByCode(String code){
        jdbcTemplate.update("DELETE FROM State WHERE code = ?", code);
    }

    public boolean existsState(String code){
        String sql = "SELECT count(*) FROM State WHERE code = ?";
        boolean exists = false;
        int count = jdbcTemplate.queryForObject(sql, new Object[] { code }, Integer.class);
        exists = count > 0;
        return exists;
    }
}
