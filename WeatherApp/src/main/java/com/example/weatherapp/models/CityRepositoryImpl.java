package com.example.weatherapp.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CityRepositoryImpl {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<City> findCitiesByStateCode(String stateCode){
        return jdbcTemplate.query("SELECT name, region, state FROM City WHERE region = ?",
                new Object[]{stateCode},
                (rs, rowNum) -> {
                    City city = new City();
                    city.setName(rs.getString("name"));
                    city.setRegion(stateCode);
                    return city;
                });
    }

    public void deleteCityByName(String name){
        jdbcTemplate.update("DELETE FROM City WHERE name = ?", name);
    }

    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM City");
    }

    public void save(City city){
        jdbcTemplate.update("INSERT INTO City VALUES (?, ?, ?)", city.getName(), city.getRegion(), city.getRegion());
    }
    public List<City> findAll(){
        return jdbcTemplate.query("SELECT name, region, state FROM City",
                (rs, rowNum) -> {
                    City city = new City();
                    city.setName(rs.getString("name"));
                    city.setRegion(rs.getString("region"));

                    State state = new State();
                    state.setCode(rs.getString("state"));
                    city.setState(state);
                    return city;
                });
    }

    public boolean existsCity(String name){
        String sql = "SELECT count(*) FROM City WHERE name = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[] { name }, Integer.class);
        return count > 0;
    }

    public boolean updateCity(String oldName, String newName, String code){
        if(existsCity(newName)) {
            return false;
        }
        jdbcTemplate.update("UPDATE City SET name = ? WHERE name = ? AND region = ?", newName, oldName, code);
        return true;
    }
}
