package com.nordryd.gamblybot.cardgames.games.blackjack;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nordryd.gamblybot.cardgames.entities.Card;
import com.nordryd.gamblybot.cardgames.entities.Card.Rank;
import com.nordryd.gamblybot.cardgames.entities.Card.Suit;
import com.nordryd.gamblybot.cardgames.entities.Deck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <p>
 * Unit tests for {@link BlackjackGame}.
 * </p>
 *
 * @author Nordryd
 */
@ExtendWith(MockitoExtension.class)
public class BlackjackHandTest
{
    private static final Card QUEEN_OF_HEARTS = Card.get(Rank.QUEEN).of(Suit.HEARTS);
    private static final Card KING_OF_HEARTS = Card.get(Rank.KING).of(Suit.HEARTS);
    private static final Card THREE_OF_CLUBS = Card.get(Rank.THREE).of(Suit.CLUBS);
    private static final Card TWO_OF_CLUBS = Card.get(Rank.TWO).of(Suit.CLUBS);
    private static final Card ACE_OF_SPADES = Card.get(Rank.ACE).of(Suit.SPADES);
    private static final Card SEVEN_OF_DIAMONDS = Card.get(Rank.SEVEN).of(Suit.DIAMONDS);

    @Mock
    private Deck deck;

    @InjectMocks
    private BlackjackHand hand;

    @Test
    public void testDefaultConstructor() {
        final int expectedValue = 13;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS);
        hand = new BlackjackHand();
        assertEquals(asList(QUEEN_OF_HEARTS, THREE_OF_CLUBS), hand.getCards());
        assertEquals(BlackjackHand.State.PLAYING, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(2)).draw();
    }

    @Test
    public void testDefaultConstructorDealsSplit() {
        final int expectedValue = 20;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS);
        hand = new BlackjackHand();
        assertEquals(asList(QUEEN_OF_HEARTS, QUEEN_OF_HEARTS), hand.getCards());
        assertEquals(BlackjackHand.State.PLAYING, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(2)).draw();
    }

    @Test
    public void testDefaultConstructorBlackjack() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(ACE_OF_SPADES);
        hand = new BlackjackHand();
        assertEquals(asList(QUEEN_OF_HEARTS, ACE_OF_SPADES), hand.getCards());
        assertEquals(BlackjackHand.State.BLACKJACK, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(2)).draw();
    }

    @Test
    public void testHit() {
        final int expectedValue = 20;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS).thenReturn(SEVEN_OF_DIAMONDS);
        hand = new BlackjackHand();
        hand.hit();
        assertEquals(asList(QUEEN_OF_HEARTS, THREE_OF_CLUBS, SEVEN_OF_DIAMONDS), hand.getCards());
        assertEquals(BlackjackHand.State.PLAYING, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(3)).draw();
    }

    @Test
    public void testHitDrawsAceAsHighThenToLowOnNextHit() {
        final int expectedValueOnDeal = 5, expectedValueAfter1stHit = 16, expectedValueAfter2ndHit = 13;
        when(deck.draw()).thenReturn(TWO_OF_CLUBS).thenReturn(THREE_OF_CLUBS).thenReturn(ACE_OF_SPADES)
                .thenReturn(SEVEN_OF_DIAMONDS);
        hand = new BlackjackHand();
        assertEquals(asList(TWO_OF_CLUBS, THREE_OF_CLUBS), hand.getCards());
        assertEquals(BlackjackHand.State.PLAYING, hand.getState());
        assertEquals(expectedValueOnDeal, hand.getValue());
        hand.hit();
        assertEquals(asList(TWO_OF_CLUBS, THREE_OF_CLUBS, ACE_OF_SPADES), hand.getCards());
        assertEquals(BlackjackHand.State.PLAYING, hand.getState());
        assertEquals(expectedValueAfter1stHit, hand.getValue());
        hand.hit();
        assertEquals(asList(TWO_OF_CLUBS, THREE_OF_CLUBS, ACE_OF_SPADES, SEVEN_OF_DIAMONDS), hand.getCards());
        assertEquals(BlackjackHand.State.PLAYING, hand.getState());
        assertEquals(expectedValueAfter2ndHit, hand.getValue());
        verify(deck, times(4)).draw();
    }

    @Test
    public void testHitReceives21() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(THREE_OF_CLUBS).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS)
                .thenReturn(THREE_OF_CLUBS).thenReturn(TWO_OF_CLUBS);
        hand = new BlackjackHand();
        hand.hit();
        hand.hit();
        hand.hit();
        assertEquals(asList(THREE_OF_CLUBS, QUEEN_OF_HEARTS, THREE_OF_CLUBS, THREE_OF_CLUBS, TWO_OF_CLUBS),
                hand.getCards());
        assertEquals(BlackjackHand.State.STAYING, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(5)).draw();
    }

    @Test
    public void testHitReceives21WithHighAce() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(THREE_OF_CLUBS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(ACE_OF_SPADES);
        hand = new BlackjackHand();
        hand.hit();
        assertEquals(asList(THREE_OF_CLUBS, SEVEN_OF_DIAMONDS, ACE_OF_SPADES), hand.getCards());
        assertEquals(BlackjackHand.State.STAYING, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(3)).draw();
    }

    @Test
    public void testHitReceives21WithLowAce() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(THREE_OF_CLUBS).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS)
                .thenReturn(TWO_OF_CLUBS).thenReturn(TWO_OF_CLUBS).thenReturn(ACE_OF_SPADES);
        hand = new BlackjackHand();
        hand.hit();
        hand.hit();
        hand.hit();
        hand.hit();
        assertEquals(asList(THREE_OF_CLUBS, QUEEN_OF_HEARTS, THREE_OF_CLUBS, TWO_OF_CLUBS, TWO_OF_CLUBS, ACE_OF_SPADES),
                hand.getCards());
        assertEquals(BlackjackHand.State.STAYING, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(6)).draw();
    }

    @Test
    public void testHitBusts() {
        final int expectedValue = 24;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(SEVEN_OF_DIAMONDS);
        hand = new BlackjackHand();
        hand.hit();
        assertEquals(emptyList(), hand.getCards());
        assertEquals(BlackjackHand.State.STAYING, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(3)).draw();
    }

    @Test
    public void testHitBustsWithLowAce() {
        final int expectedValue = 22;
        when(deck.draw()).thenReturn(THREE_OF_CLUBS).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS)
                .thenReturn(TWO_OF_CLUBS).thenReturn(TWO_OF_CLUBS).thenReturn(ACE_OF_SPADES).thenReturn(ACE_OF_SPADES);
        hand = new BlackjackHand();
        hand.hit();
        hand.hit();
        hand.hit();
        hand.hit();
        hand.hit();
        assertEquals(asList(THREE_OF_CLUBS, QUEEN_OF_HEARTS, THREE_OF_CLUBS, TWO_OF_CLUBS, TWO_OF_CLUBS, ACE_OF_SPADES,
                ACE_OF_SPADES), hand.getCards());
        assertEquals(BlackjackHand.State.BUSTED, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(7)).draw();
    }

    @Test
    public void testStay() {
        final int expectedValue = 13;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS);
        hand = new BlackjackHand();
        hand.stay();
        assertEquals(asList(QUEEN_OF_HEARTS, THREE_OF_CLUBS), hand.getCards());
        assertEquals(BlackjackHand.State.STAYING, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        verify(deck, times(2)).draw();
    }

    @Test
    public void testSplit() {
        when(deck.draw()).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(SEVEN_OF_DIAMONDS);

    }

    @Test
    public void testSplitDifferentFaceCards() {
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(KING_OF_HEARTS);

    }

    @Test
    public void testSplitCannotBeSplit() {
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS);
        hand = new BlackjackHand();
        assertEquals(
                "split() was called on a hand that cannot be split! Both initially dealt cards must be equal in value in order to split.",
                assertThrows(IllegalStateException.class, () -> hand.split()).getMessage());
        verify(deck, times(2)).draw();
    }

    @Test
    public void testHitOnBlackjackState() {
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(ACE_OF_SPADES);
        hand = new BlackjackHand();
        hand.hit();

        verify(deck, times(2)).draw();
    }

    @Test
    public void testHitOnStayedState() {
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS);
        hand = new BlackjackHand();
        hand.stay();
        hand.hit();
        assertEquals(BlackjackHand.State.STAYING, hand.getState());
        verify(deck, times(2)).draw();
    }

    @Test
    public void testHitOnBustedState() {
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(QUEEN_OF_HEARTS);
        hand = new BlackjackHand();
        hand.hit();
        hand.hit();
        assertEquals(BlackjackHand.State.BUSTED, hand.getState());
        verify(deck, times(3)).draw();
    }

    @Test
    public void testStayOnBustedState() {
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(QUEEN_OF_HEARTS);
        hand = new BlackjackHand();
        hand.hit();
        hand.stay();
        assertEquals(BlackjackHand.State.BUSTED, hand.getState());
        verify(deck, times(3)).draw();
    }
}
