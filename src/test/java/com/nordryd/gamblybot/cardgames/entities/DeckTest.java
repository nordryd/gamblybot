package com.nordryd.gamblybot.cardgames.entities;

import static com.nordryd.gamblybot.cardgames.entities.Card.Rank;
import static com.nordryd.gamblybot.cardgames.entities.Card.Suit;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private static final int SOME_RANK_AND_SUIT_INDEX = 2;
    private static final Card TO_BE_DRAWN = Card.get(Rank.values()[SOME_RANK_AND_SUIT_INDEX])
            .of(Suit.values()[SOME_RANK_AND_SUIT_INDEX]);

    @Mock
    private Random rng;

    @InjectMocks
    private Deck deck;

    @Test
    public void testDraw() {
        setupDraws();
        assertEquals(TO_BE_DRAWN, deck.draw());
        verify(rng, times(2)).nextInt(anyInt());
    }

    @Test
    public void testDrawMultiple() {
        setupDraws();
        assertEquals(asList(TO_BE_DRAWN, TO_BE_DRAWN), deck.draw(2));
        verify(rng, times(4)).nextInt(anyInt());
    }

    @Test
    public void testDrawMultipleSingle() {
        setupDraws();
        assertEquals(singletonList(TO_BE_DRAWN), deck.draw(1));
        verify(rng, times(2)).nextInt(anyInt());
    }

    @Test
    public void testDrawMultipleZero() {
        assertEquals("Cannot draw zero cards!",
                assertThrows(IllegalArgumentException.class, () -> deck.draw(0)).getMessage());
    }

    @Test
    public void testDrawMultipleNegative() {
        assertEquals("Cannot draw a negative number of cards!",
                assertThrows(IllegalArgumentException.class, () -> deck.draw(-1)).getMessage());
    }

    private void setupDraws() {
        when(rng.nextInt(intThat(integer -> integer > 0))).thenReturn(SOME_RANK_AND_SUIT_INDEX);
    }
}
