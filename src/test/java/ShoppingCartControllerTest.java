import dk.shoppingcart.controller.ShoppingCartController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ShoppingCartControllerTest {
    private MockMvc mockMvc;

    @Before
    public void setup() {
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        shoppingCartController.init();
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartController).build();
    }

    @Test
    public void getUsersTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":0,\"shoppingCarts\":[{\"id\":0,\"items\":[\"Apple\",\"Banana\",\"Orange\"]},{\"id\":1,\"items\":[\"Popcorn\",\"Ticket\"]}]},{\"id\":1,\"shoppingCarts\":[{\"id\":0,\"items\":[\"New record\",\"CD\",\"Poster\"]},{\"id\":1,\"items\":[\"Hard drive\"]}]}]"));
    }

    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(get("/users/0"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":0,\"shoppingCarts\":[{\"id\":0,\"items\":[\"Apple\",\"Banana\",\"Orange\"]},{\"id\":1,\"items\":[\"Popcorn\",\"Ticket\"]}]}"));
        mockMvc.perform(get("/users/50"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void removeUserTest() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/users/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getShoppingCartsTest() throws Exception {
        mockMvc.perform(get("/users/0/shoppingcarts"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":0,\"items\":[\"Apple\",\"Banana\",\"Orange\"]},{\"id\":1,\"items\":[\"Popcorn\",\"Ticket\"]}]"));
        mockMvc.perform(get("/users/30/shoppingcarts"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getShoppingCartTest() throws Exception {
        mockMvc.perform(get("/users/0/shoppingcarts/0"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":0,\"items\":[\"Apple\",\"Banana\",\"Orange\"]}"));
        mockMvc.perform(get("/users/0/shoppingcarts/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShoppingCartTest() throws Exception {
        mockMvc.perform(post("/users/0/shoppingcarts/0").contentType(MediaType.APPLICATION_JSON).content("{\"id\":0,\"items\":[\"Apple\",\"Banana\"]}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users/0/shoppingcarts/0").contentType(MediaType.APPLICATION_JSON).content("{x\"id\":0,\"items\":[\"Apple\",\"Banana\"}"))
                .andExpect(status().isNotAcceptable());
        mockMvc.perform(post("/users/0/shoppingcarts/2").contentType(MediaType.APPLICATION_JSON).content("{\"id\":0,\"items\":[\"Apple\",\"Banana\"]}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addShoppingCartTest() throws Exception {
        mockMvc.perform(put("/users/0/shoppingcarts/0").contentType(MediaType.APPLICATION_JSON).content("{\"id\":0,\"items\":[\"Apple\"]}"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/0/shoppingcarts/10").contentType(MediaType.APPLICATION_JSON).content("{\"id\":0,\"items\":[\"Apple\",\"Citrus\"]}"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/0/shoppingcarts/10").contentType(MediaType.APPLICATION_JSON).content("{\"id\":0,\"items\":\"Apple\",\"Citrus\"}"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void removeShoppingCartTest() throws Exception {
        mockMvc.perform(delete("/users/0/shoppingcarts/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/users/0/shoppingcarts/3"))
                .andExpect(status().isNotFound());
    }
}