package com.nordryd.gamblybot.cardgames.entities;

import static com.nordryd.gamblybot.cardgames.entities.Card.Rank;
import static com.nordryd.gamblybot.cardgames.entities.Card.Suit;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <p>
 * Unit tests for {@link Deck}.
 * </p>
 *
 * @author Nordryd
 */
@ExtendWith(MockitoExtension.class)
public class DeckTest
{
    private static final int SOME_DECK_INDEX = 2;
    private static final List<Card> ALL_POSSIBLE_CARDS;
    private static final Card DRAWN_1ST, DRAWN_2ND;

    @Mock
    private Random rng;

    @InjectMocks
    private Deck deck;

    @Test
    public void testDraw() {
        when(rng.nextInt(intThat(integer -> integer > 0))).thenReturn(SOME_DECK_INDEX);
        assertThat(deck.draw()).isEqualTo(DRAWN_1ST);
        verify(rng, times(1)).nextInt(anyInt());
    }

    @Test
    public void testDrawMultiple() {
        when(rng.nextInt(intThat(integer -> integer > 0))).thenReturn(SOME_DECK_INDEX);
        assertThat(deck.draw(2)).isEqualTo(asList(DRAWN_1ST, DRAWN_2ND));
        verify(rng, times(2)).nextInt(anyInt());
    }

    @Test
    public void testDrawMultipleSingle() {
        when(rng.nextInt(intThat(integer -> integer > SOME_DECK_INDEX))).thenReturn(SOME_DECK_INDEX);
        assertThat(deck.draw(1)).isEqualTo(singletonList(DRAWN_1ST));
        verify(rng, times(1)).nextInt(anyInt());
    }

    @Test
    public void testDrawEntireDeckThenDrawOne() {
        when(rng.nextInt(intThat(integer -> integer > 0))).thenReturn(SOME_DECK_INDEX);
        when(rng.nextInt(intThat(integer -> integer <= SOME_DECK_INDEX))).thenCallRealMethod();
        assertThat(deck.draw(52)).containsAll(ALL_POSSIBLE_CARDS);
        assertThat(deck.draw()).isEqualTo(DRAWN_1ST);
        verify(rng, times(53)).nextInt(anyInt());
    }

    @Test
    public void testDrawMoreThanAvailable() {
        when(rng.nextInt(intThat(integer -> integer > 0))).thenReturn(SOME_DECK_INDEX);
        when(rng.nextInt(intThat(integer -> integer <= SOME_DECK_INDEX))).thenCallRealMethod();
        final List<Card> expectedCards = new ArrayList<>(ALL_POSSIBLE_CARDS);
        expectedCards.add(DRAWN_1ST);
        assertThat(deck.draw(53)).containsAll(expectedCards);
        verify(rng, times(53)).nextInt(anyInt());
    }

    @Test
    public void testDrawMultipleZero() {
        assertThat(assertThrows(IllegalArgumentException.class, () -> deck.draw(0)).getMessage())
                .isEqualTo("Cannot draw zero cards!");
    }

    @Test
    public void testDrawMultipleNegative() {
        assertThat(assertThrows(IllegalArgumentException.class, () -> deck.draw(-1)).getMessage())
                .isEqualTo("Cannot draw a negative number of cards!");
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
        DRAWN_1ST = ALL_POSSIBLE_CARDS.get(SOME_DECK_INDEX);
        DRAWN_2ND = ALL_POSSIBLE_CARDS.get(SOME_DECK_INDEX + 1);

    }
}
