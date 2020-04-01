package com.nordryd.gamblybot.cardgames.entities;

import static com.nordryd.gamblybot.cardgames.entities.Card.Rank;
import static com.nordryd.gamblybot.cardgames.entities.Card.Suit;
import static com.nordryd.gamblybot.cardgames.entities.Card.get;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Represents a standard deck from which to draw {@link Card Cards}.
 * </p>
 *
 * @author Nordryd
 */
@Component
public class Deck
{
    private final Random rng;

    /**
     * Constructor.
     *
     * @param rng a {@link Random random number generator}.
     */
    @Autowired
    public Deck(final Random rng) {
        this.rng = rng;
    }

    /**
     * @return a random {@link Card}.
     */
    public Card draw() {
        return get(Rank.values()[rng.nextInt(Rank.values().length)])
                .of(Suit.values()[rng.nextInt(Suit.values().length)]);
    }

    /**
     * Draw a specific amount of random {@link Card Cards}.
     *
     * @param amount how many cards to draw.
     * @return the desired number of cards.
     * @throws IllegalArgumentException if amount &le; 0
     */
    public List<Card> draw(final int amount) {
        if (amount == 0) {
            throw new IllegalArgumentException("Cannot draw zero cards!");
        }

        if (amount < 0) {
            throw new IllegalArgumentException("Cannot draw a negative number of cards!");
        }

        return range(0, amount).mapToObj(drawCount -> draw()).collect(toList());
    }
}
