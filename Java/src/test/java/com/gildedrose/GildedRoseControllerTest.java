package com.gildedrose;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GildedRoseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GildedRoseRepository repository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test_the_controller_returns_all_the_items() throws Exception {
        // given
        Item item1 = givenItem("+5 Dexterity Vest", 10, 20);
        Item item2 = givenItem("Conjured Mana Cake", 0, 20);
        given(repository.findAll()).willReturn(Arrays.asList(item1, item2));

        // then
        this.mvc.perform(get("/items"))
            .andExpect(status().isOk())
            .andExpect(content().json(asString(item1, item2)));
    }

    @Test
    public void test_the_controller_updates_the_items_successfully() throws Exception {
        // given
        Item item1 = givenItem("+5 Dexterity Vest", 10, 20);
        Item item2 = givenItem("Conjured Mana Cake", 0, 20);
        given(repository.findAll()).willReturn(Arrays.asList(item1, item2));


        // then
        this.mvc.perform(post("/items/update"))
            .andExpect(status().isOk());
        assertItem(item1, 9, 19);
        assertItem(item2, -1, 16);
    }

    private static Item givenItem(final String name, final int sellIn, final int quality) {
        return new Item(name, sellIn, quality);
    }

    private static String asString(final Item... items) throws JsonProcessingException {
        return objectMapper.writer().writeValueAsString(Arrays.asList(items));
    }

    private static void assertItem(final Item item, final int expectedSellIn, final int expectedQuality) {
        assertThat(item.sellIn).isEqualTo(expectedSellIn);
        assertThat(item.quality).isEqualTo(expectedQuality);
    }

}
