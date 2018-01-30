package com.gildedrose;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GildedRoseService {

    public interface GildedRoseItemStrategy {
        boolean appliesTo(final Item item);

        void updateQuality(final Item item);

        default void updateSellIn(final Item item) {
            item.sellIn--;
        }
    }

    private final GildedRoseRepository repository;
    private final List<GildedRoseItemStrategy> strategies;

    public GildedRoseService(final GildedRoseRepository repository, final List<GildedRoseItemStrategy> strategies) {
        this.repository = repository;
        this.strategies = strategies;
    }

    public List<Item> findAll() {
        return repository.findAll();
    }

    /**
     * TODO make this method atomic, a fail in one of the updates won't undo the already processed updates
     */
    public void updateQuality() {
        findAll().forEach(this::updateQuality);
    }

    private void updateQuality(final Item item) {
        GildedRoseItemStrategy strategy = strategies.stream()
            .filter(it -> it.appliesTo(item))
            .findFirst()
            .orElseThrow(() -> new UnknownItemTypeException(item));
        strategy.updateQuality(item);
        strategy.updateSellIn(item);
    }

    public static final class UnknownItemTypeException extends GildedRoseException {
        private final Item item;

        public UnknownItemTypeException(final Item item) {
            super(HttpStatus.NOT_IMPLEMENTED.value(), String.format("The item %s can't be updated", item));
            this.item = item;
        }

        public Item getItem() {
            return item;
        }
    }
}
