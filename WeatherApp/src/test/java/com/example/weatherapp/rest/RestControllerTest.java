package com.example.weatherapp.rest;

import com.example.weatherapp.controllers.LiveWeatherService;
import com.example.weatherapp.controllers.Service;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Before
    public void initBefore(){
        service.deleteStates();

        State state1 = new State("česká republika", "cz");
        State state2 = new State("německo", "de");

        City city1 = new City("liberec", "cz", state1);
        City city2 = new City("ústí", "cz", state1);
        City city3 = new City("berlin","de",state2);

        service.createNewState(state1);
        service.createNewState(state2);

        service.createNewCity(city1.getName(), city1.getRegion());
        service.createNewCity(city2.getName(), city2.getRegion());
        service.createNewCity(city3.getName(), city3.getRegion());
    }

    @org.junit.Test
    public void getAllStates() throws Exception {
        String uri = "/api/states";
        mockMvc.perform(get(uri)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[*].code", containsInAnyOrder("cz", "de")));
    }

    @org.junit.Test
    public void getAllCities() throws Exception {
        String uri = "/api/cities";
        mockMvc.perform(get(uri)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("liberec", "ústí", "berlin")));
    }

    @org.junit.Test
    public void createNewState() throws Exception {
        String uri = "/api/states";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "itálie");
        jsonObject.put("code","it");

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
        jsonObject.put("name", "jihlava");
        jsonObject.put("region","cz");
        JSONObject jsonObjectState = new JSONObject();
        jsonObjectState.put("name", "česká republika");
        jsonObjectState.put("code", "cz");
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
        String uri = "/api/states/cz";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(uri)
                .contentType(APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("State deleted", result.getResponse().getContentAsString()));
    }

    @org.junit.Test
    public void deleteCity() throws Exception {
        String uri = "/api/cities/berlin";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(uri)
                .contentType(APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("City deleted", result.getResponse().getContentAsString()));
    }

    @org.junit.Test
    public void searchForCityData() throws Exception {
        String uri = "/api/search/city/liberec/cz";
        mockMvc.perform(get(uri)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].locationName", is("liberec")))
                .andExpect(jsonPath("$[0].country", is("cz")));
    }

    @org.junit.Test
    public void searchForStateData() throws Exception {
        String uri_init = "/api/search/city/liberec/cz";
        mockMvc.perform(get(uri_init));
        uri_init = "/api/search/city/ústí/cz";
        mockMvc.perform(get(uri_init));

        String uri = "/api/search/state/cz";
        mockMvc.perform(get(uri)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*][0].locationName", containsInAnyOrder("liberec", "ústí")))
                .andExpect(jsonPath("$[*][0].country", containsInAnyOrder("cz", "cz")));
    }

    @org.junit.Test
    public void searchForAvg() throws Exception {
        String uri_init = "/api/search/city/liberec/cz";
        mockMvc.perform(get(uri_init));

        String uri = "/api/avg/liberec";

        mockMvc.perform(get(uri)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
    }

    @org.junit.Test
    public void updateState() throws Exception {
        String uri = "/api/updateState";

        JSONArray jsonArray = new JSONArray();
        jsonArray.put("česko");
        jsonArray.put("cz");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .accept(APPLICATION_JSON).content(String.valueOf(jsonArray))
                .contentType(APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("State updated", result.getResponse().getContentAsString()));
    }

    @org.junit.Test
    public void updateCity() throws Exception {
        String uri = "/api/updateCity";

        JSONArray jsonArray = new JSONArray();
        jsonArray.put("ústí");
        jsonArray.put("ústí nad labem");
        jsonArray.put("cz");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .accept(APPLICATION_JSON).content(String.valueOf(jsonArray))
                .contentType(APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("City updated", result.getResponse().getContentAsString()));
    }

    @org.junit.Test
    public void downloadCSV() throws Exception {
        String uri = "/api/downloadCSV";

        String fileName = "data.csv";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(uri)
                .accept(TEXT_PLAIN).content(fileName)
                .contentType(TEXT_PLAIN);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/csv")));
    }

    @org.junit.Test
    public void uploadCSV() throws Exception {
        String uri = "/api/uploadCSV";

        String fileName = "data.csv";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName, "multipart/form-data", "nahodnaHodnota".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(uri)
                        .file(mockMultipartFile))
                .andExpect(result -> assertEquals("File is not CSV, cannot upload.", result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest());

    }
}