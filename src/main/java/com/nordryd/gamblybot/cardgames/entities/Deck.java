package com.nordryd.gamblybot.cardgames.entities;

import static com.nordryd.gamblybot.cardgames.entities.Card.Rank;
import static com.nordryd.gamblybot.cardgames.entities.Card.Suit;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * Represents a standard deck from which to draw {@link Card Cards}. This will mimic a standard deck of 52 cards to
 * reduce the randomness of drawing cards, and make it feel more realistic.
 * </p>
 *
 * @author Nordryd
 */
public class Deck
{
    private static final List<Card> ALL_POSSIBLE_CARDS;

    private final List<Card> cards;
    private final Random rng;

    /**
     * Constructor.
     *
     * @param rng a {@link Random random number generator}.
     */
    @Autowired
    public Deck(final Random rng) {
        this.cards = new ArrayList<>();
        this.rng = rng;
        reset();
    }

    /**
     * @return a random {@link Card}.
     */
    public Card draw() {
        final int cardToDraw = rng.nextInt(cards.size());
        final Card drawn = cards.get(cardToDraw);
        cards.remove(cardToDraw);
        if (isEmpty(cards)) {
            reset();
        }
        return drawn;
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

    private void reset() {
        cards.clear();
        cards.addAll(ALL_POSSIBLE_CARDS);
    }

    static {
        //@formatter:off
        ALL_POSSIBLE_CARDS = asList(
                Card.get(Rank.TWO).of(Suit.CLUBS),
                Card.get(Rank.THREE).of(Suit.CLUBS),
                Card.get(Rank.FOUR).of(Suit.CLUBS),
                Card.get(Rank.FIVE).of(Suit.CLUBS),
                Card.get(Rank.SIX).of(Suit.CLUBS),
                Card.get(Rank.SEVEN).of(Suit.CLUBS),
                Card.get(Rank.EIGHT).of(Suit.CLUBS),
                Card.get(Rank.NINE).of(Suit.CLUBS),
                Card.get(Rank.TEN).of(Suit.CLUBS),
                Card.get(Rank.JACK).of(Suit.CLUBS),
                Card.get(Rank.QUEEN).of(Suit.CLUBS),
                Card.get(Rank.KING).of(Suit.CLUBS),
                Card.get(Rank.ACE).of(Suit.CLUBS),
                Card.get(Rank.TWO).of(Suit.HEARTS),
                Card.get(Rank.THREE).of(Suit.HEARTS),
                Card.get(Rank.FOUR).of(Suit.HEARTS),
                Card.get(Rank.FIVE).of(Suit.HEARTS),
                Card.get(Rank.SIX).of(Suit.HEARTS),
                Card.get(Rank.SEVEN).of(Suit.HEARTS),
                Card.get(Rank.EIGHT).of(Suit.HEARTS),
                Card.get(Rank.NINE).of(Suit.HEARTS),
                Card.get(Rank.TEN).of(Suit.HEARTS),
                Card.get(Rank.JACK).of(Suit.HEARTS),
                Card.get(Rank.QUEEN).of(Suit.HEARTS),
                Card.get(Rank.KING).of(Suit.HEARTS),
                Card.get(Rank.ACE).of(Suit.HEARTS),
                Card.get(Rank.TWO).of(Suit.DIAMONDS),
                Card.get(Rank.THREE).of(Suit.DIAMONDS),
                Card.get(Rank.FOUR).of(Suit.DIAMONDS),
                Card.get(Rank.FIVE).of(Suit.DIAMONDS),
                Card.get(Rank.SIX).of(Suit.DIAMONDS),
                Card.get(Rank.SEVEN).of(Suit.DIAMONDS),
                Card.get(Rank.EIGHT).of(Suit.DIAMONDS),
                Card.get(Rank.NINE).of(Suit.DIAMONDS),
                Card.get(Rank.TEN).of(Suit.DIAMONDS),
                Card.get(Rank.JACK).of(Suit.DIAMONDS),
                Card.get(Rank.QUEEN).of(Suit.DIAMONDS),
                Card.get(Rank.KING).of(Suit.DIAMONDS),
                Card.get(Rank.ACE).of(Suit.DIAMONDS),
                Card.get(Rank.TWO).of(Suit.SPADES),
                Card.get(Rank.THREE).of(Suit.SPADES),
                Card.get(Rank.FOUR).of(Suit.SPADES),
                Card.get(Rank.FIVE).of(Suit.SPADES),
                Card.get(Rank.SIX).of(Suit.SPADES),
                Card.get(Rank.SEVEN).of(Suit.SPADES),
                Card.get(Rank.EIGHT).of(Suit.SPADES),
                Card.get(Rank.NINE).of(Suit.SPADES),
                Card.get(Rank.TEN).of(Suit.SPADES),
                Card.get(Rank.JACK).of(Suit.SPADES),
                Card.get(Rank.QUEEN).of(Suit.SPADES),
                Card.get(Rank.KING).of(Suit.SPADES),
                Card.get(Rank.ACE).of(Suit.SPADES)
        );
        //@formatter:on
    }
}
