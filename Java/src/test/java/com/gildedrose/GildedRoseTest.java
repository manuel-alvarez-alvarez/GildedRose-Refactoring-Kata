package com.gildedrose;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GildedRoseTest {

    @Autowired
    private GildedRoseService service;

    @MockBean
    private GildedRoseRepository repository;

    @Test
    public void test_the_system_lowers_quality_and_sell_date() {
        // given
        Item item = givenItem("+5 Dexterity Vest", 10, 20);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, 9, 19);
    }

    @Test
    public void test_the_system_lowers_quality_twice_as_fast_after_sell_date() {
        // given
        Item item = givenItem("+5 Dexterity Vest", 0, 20);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, -1, 18);
    }

    @Test
    public void test_the_system_never_lowers_quality_below_zero() {
        // given
        Item item = givenItem("+5 Dexterity Vest", 10, 0);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, 9, 0);
    }

    @Test
    public void test_the_system_increases_the_quality_for_aged_brie() {
        // given
        Item item = givenItem("Aged Brie", 10, 0);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, 9, 1);
    }

    @Test
    public void test_the_system_never_increases_the_quality_more_than_fifty_for_aged_brie() {
        // given
        Item item = givenItem("Aged Brie", 10, 50);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, 9, 50);
    }

    @Test
    public void test_the_system_never_changes_quality_or_sell_date_for_sulfuras() {
        // given
        Item item = givenItem("Sulfuras, Hand of Ragnaros", 10, 80);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, 10, 80);
    }

    @Test
    public void test_the_system_increases_the_quality_for_backstage() {
        // given
        Item item = givenItem("Backstage passes to a TAFKAL80ETC concert", 11, 0);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, 10, 1);
    }

    @Test
    public void test_the_system_increases_the_quality_for_backstage_when_there_are_ten_days_or_less() {
        // given
        Item item1 = givenItem("Backstage passes to a TAFKAL80ETC concert", 10, 0);
        Item item2 = givenItem("Backstage passes to a TAFKAL80ETC concert", 6, 0);
        given(repository.findAll()).willReturn(Arrays.asList(item1, item2));

        // when
        service.updateQuality();

        // then
        assertItem(item1, 9, 2);
        assertItem(item2, 5, 2);
    }

    @Test
    public void test_the_system_increases_the_quality_for_backstage_when_there_are_five_days_or_less() {
        // given
        Item item1 = givenItem("Backstage passes to a TAFKAL80ETC concert", 5, 0);
        Item item2 = givenItem("Backstage passes to a TAFKAL80ETC concert", 1, 0);
        given(repository.findAll()).willReturn(Arrays.asList(item1, item2));

        // when
        service.updateQuality();

        // then
        assertItem(item1, 4, 3);
        assertItem(item2, 0, 3);
    }

    @Test
    public void test_the_system_drops_the_quality_to_zero_for_backstage_after_the_concert() {
        // given
        Item item = givenItem("Backstage passes to a TAFKAL80ETC concert", 0, 250);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, -1, 0);
    }

    @Test
    public void test_the_system_never_increases_the_quality_over_fifty_for_backstage() {
        // given
        Item item1 = givenItem("Backstage passes to a TAFKAL80ETC concert", 11, 50);
        Item item2 = givenItem("Backstage passes to a TAFKAL80ETC concert", 10, 50);
        Item item3 = givenItem("Backstage passes to a TAFKAL80ETC concert", 5, 50);
        given(repository.findAll()).willReturn(Arrays.asList(item1, item2, item3));

        // when
        service.updateQuality();

        // then
        assertItem(item1, 10, 50);
        assertItem(item2, 9, 50);
        assertItem(item3, 4, 50);
    }

    @Test
    public void test_the_system_lowers_quality_and_sell_date_for_conjured() {
        // given
        Item item = givenItem("Conjured Mana Cake", 3, 6);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, 2, 4);
    }

    @Test
    public void test_the_system_lowers_quality_twice_as_fast_after_sell_date_for_conjured() {
        // given
        Item item = givenItem("Conjured Mana Cake", 0, 20);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, -1, 16);
    }

    @Test
    public void test_the_system_never_lowers_quality_below_zero_for_conjured() {
        // given
        Item item = givenItem("Conjured Mana Cake", 10, 0);
        given(repository.findAll()).willReturn(Collections.singletonList(item));

        // when
        service.updateQuality();

        // then
        assertItem(item, 9, 0);
    }

    private static Item givenItem(final String name, final int sellIn, final int quality) {
        return new Item(name, sellIn, quality);
    }

    private static void assertItem(final Item item, final int expectedSellIn, final int expectedQuality) {
        assertThat(item.sellIn).isEqualTo(expectedSellIn);
        assertThat(item.quality).isEqualTo(expectedQuality);
    }
}
