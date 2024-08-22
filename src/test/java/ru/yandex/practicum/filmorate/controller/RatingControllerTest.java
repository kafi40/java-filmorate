package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(RatingController.class)
public class RatingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void forGetRatingByIdWhenIsExistsThenStatusOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/rating/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void forGetWhenFindAllRating() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/rating")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void forGetRatingByIdWhenIsNotExistsThenStatus404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/rating/10000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
