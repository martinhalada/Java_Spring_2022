package com.example.weatherapp.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CityRepositoryImpl {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("City");
    }
    public List<City> findCitiesByStateCode(String stateCode){
        List<City> cities = jdbcTemplate.query("SELECT name, region, state FROM City WHERE region = ?",
                new Object[]{stateCode},
                new RowMapper<City>() {
                    @Override
                    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
                        City city = new City();
                        city.setName(rs.getString("name"));
                        city.setRegion(stateCode);
                        return city;
                    }
                });

        return cities;
    }

    public void deleteCityByName(String name){
        jdbcTemplate.update("DELETE FROM City WHERE name = ?", name);
    }
    public void save(City city){
        jdbcTemplate.update("INSERT INTO City VALUES (?, ?, ?)", city.getName(), city.getRegion(), city.getRegion());
    }
    public List<City> findAll(){
        return jdbcTemplate.query("SELECT name, region, state FROM City",
                new RowMapper<City>() {
            @Override
            public City mapRow(ResultSet rs, int rowNum) throws SQLException {
                City city = new City();
                city.setName(rs.getString("name"));
                city.setRegion(rs.getString("region"));

                State state = new State();
                state.setCode(rs.getString("state"));
                city.setState(state);
                return city;
            }
        });
    }
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM City");
    }

    public boolean existsCity(String name){
        String sql = "SELECT count(*) FROM City WHERE name = ?";
        boolean exists = false;
        int count = jdbcTemplate.queryForObject(sql, new Object[] { name }, Integer.class);
        exists = count > 0;
        return exists;
    }
}
