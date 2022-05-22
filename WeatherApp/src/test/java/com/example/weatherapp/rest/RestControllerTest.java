package com.example.weatherapp.rest;

import com.example.weatherapp.controllers.LiveWeatherService;
import com.example.weatherapp.controllers.Service;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import org.bson.json.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@ComponentScan({"com.example.weatherapp.config", "com.example.weatherapp.controllers","com.example.weatherapp.models", "com.example.weatherapp.rest"})
@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerTest {
    @Autowired
    Service service;
    @Autowired
    LiveWeatherService liveWeatherService;
    @Autowired
    private MockMvc mockMvc;

    @Value("${weather.csvFileName}")
    private String csvFileName;
    @Value("${weather.csvToUpload}")
    private String csvFileToUpload;


    State state1 = new State("Česká republika", "CZ");
    State state2 = new State("Německo", "DE");
    State state3 = new State("Anglie", "GB");

    City city1 = new City("Liberec", "CZ", state1);
    City city2 = new City("Praha", "CZ", state1);
    City city3 = new City("Berlin","DE",state2);
    City city4 = new City("London", "GB", state3);

    @Before
    public void initBefore(){
        service.createNewState(state1);
        service.createNewState(state2);
        service.createNewState(state3);

        service.createNewCity(city1);
        service.createNewCity(city2);
        service.createNewCity(city3);
        service.createNewCity(city4);
    }

    @org.junit.Test
    public void getAllStates() throws Exception {
        String uri = "/api/states";
        mockMvc.perform(get(uri)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].code", is("CZ")))
                .andExpect(jsonPath("$[1].code", is("DE")))
                .andExpect(jsonPath("$[2].code", is("GB")));
    }

    @org.junit.Test
    public void getAllCities() throws Exception {
        String uri = "/api/cities";
        mockMvc.perform(get(uri)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$[0].name", is("Liberec")))
                .andExpect(jsonPath("$[1].name", is("Praha")))
                .andExpect(jsonPath("$[2].name", is("Berlin")))
                .andExpect(jsonPath("$[3].name", is("London")));
    }

    @org.junit.Test
    public void createNewState() throws Exception {
        String uri = "/api/states";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Itálie");
        jsonObject.put("code","IT");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .accept(APPLICATION_JSON).content(String.valueOf(jsonObject))
                .contentType(APPLICATION_JSON);
       mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(result -> assertEquals("State saved", result.getResponse().getContentAsString()));
    }

    @org.junit.Test
    public void createNewCity() throws Exception {
        String uri = "/api/cities";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Jihlava");
        jsonObject.put("region","CZ");
        JSONObject jsonObjectState = new JSONObject();
        jsonObjectState.put("name", "Česká republika");
        jsonObjectState.put("code", "CZ");
        jsonObject.put("state", jsonObjectState);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .accept(APPLICATION_JSON).content(String.valueOf(jsonObject))
                .contentType(APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("City saved", result.getResponse().getContentAsString()));
    }

    @org.junit.Test
    public void deleteState() throws Exception {
        String uri = "/api/states/CZ";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(uri)
                .contentType(APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("State deleted", result.getResponse().getContentAsString()));
    }

    @org.junit.Test
    public void deleteCity() throws Exception {
        String uri = "/api/cities/London";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(uri)
                .contentType(APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("City deleted", result.getResponse().getContentAsString()));
    }

    @org.junit.Test
    public void searchForCityData() throws Exception {
        String uri = "/api/search/city/Liberec/CZ";
        mockMvc.perform(get(uri)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].locationName", is("Liberec")))
                .andExpect(jsonPath("$[0].country", is("CZ")));
    }

    @org.junit.Test
    public void searchForStateData() throws Exception {
        String uri = "/api/search/state/CZ";
        mockMvc.perform(get(uri)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0].locationName", is("Liberec")))
                .andExpect(jsonPath("$[1][0].locationName", is("Praha")))
                .andExpect(jsonPath("$[0][0].country", is("CZ")))
                .andExpect(jsonPath("$[1][0].country", is("CZ")));
    }

    @org.junit.Test
    public void downloadCSV() {
    }

    @org.junit.Test
    public void uploadCSV() {
    }
}