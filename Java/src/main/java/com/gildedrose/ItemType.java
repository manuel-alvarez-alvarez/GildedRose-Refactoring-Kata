package com.gildedrose;

import java.util.Optional;
import java.util.function.Predicate;

import static com.gildedrose.GildedRoseService.GildedRoseItemStrategy;

/**
 * The value in the enumeration is important due to the matching process defined by {@link GildedRoseItemStrategy#appliesTo(Item)},
 * the predicates will be evaluated in the order they are defined in the enumeration.
 */
public enum ItemType implements GildedRoseItemStrategy {

    AGED_BRIE("Aged Brie") {
        @Override
        protected int qualityDelta(final Item item) {
            return 1;
        }
    },

    SULFURAS("Sulfuras") {
        @Override
        public void updateSellIn(final Item item) {
            // do nothing
        }

        @Override
        public void updateQuality(final Item item) {
            // do nothing
        }

        @Override
        protected int qualityDelta(final Item item) {
            throw new UnsupportedOperationException();
        }
    },

    BACKSTAGE("Backstage passes") {
        protected int qualityDelta(final Item item) {
            int amount = 1;
            if (item.sellIn <= 0) {
                amount = (-1) * item.quality;
            } else if (item.sellIn <= 5) {
                amount = 3;
            } else if (item.sellIn <= 10) {
                amount = 2;
            }
            return amount;
        }
    },

    CONJURED("Conjured") {
        @Override
        protected int qualityDelta(final Item item) {
            return 2 * REGULAR.qualityDelta(item);
        }
    },

    REGULAR(item -> true) {
        @Override
        protected int qualityDelta(final Item item) {
            return item.sellIn <= 0 ? -2 : -1;
        }
    };


    private final Predicate<Item> predicate;

    ItemType(final String prefix) {
        // TODO evaluate usage of regexp if needed
        this(item -> Optional.ofNullable(item.name).map(name -> name.startsWith(prefix)).orElse(false));
    }

    ItemType(final Predicate<Item> matecher) {
        this.predicate = matecher;
    }


    @Override
    public boolean appliesTo(final Item item) {
        return predicate.test(item);
    }

    @Override
    public void updateQuality(final Item item) {
        item.quality = Math.max(0, Math.min(50, item.quality + qualityDelta(item)));
    }

    protected abstract int qualityDelta(final Item item);
}
