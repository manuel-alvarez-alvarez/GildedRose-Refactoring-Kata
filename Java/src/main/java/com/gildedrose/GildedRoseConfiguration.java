package com.gildedrose;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

import static com.gildedrose.GildedRoseService.GildedRoseItemStrategy;


@Configuration
public class GildedRoseConfiguration {

    @Bean
    public List<GildedRoseItemStrategy> strategies() {
        return Arrays.asList(ItemType.values());
    }
}
