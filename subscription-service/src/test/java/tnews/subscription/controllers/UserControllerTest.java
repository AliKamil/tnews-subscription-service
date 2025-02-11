package tnews.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mvc;
//  @Test
//  void createUserWithSubscription() {
//    mvc.perform(MockMvcRequestBuilders
//            .get("/employees")
//            .accept(MediaType.APPLICATION_JSON))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(MockMvcResultMatchers.jsonPath("$.employees").exists())
//        .andExpect(MockMvcResultMatchers.jsonPath("$.employees[*].employeeId").isNotEmpty
//
//  }
}