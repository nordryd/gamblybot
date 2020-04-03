package com.nordryd.gamblybot.cardgames.games.blackjack;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

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
    private static final Card TEN_OF_DIAMONDS = Card.get(Rank.TEN).of(Suit.DIAMONDS);
    private static final Card KING_OF_HEARTS = Card.get(Rank.KING).of(Suit.HEARTS);
    private static final Card THREE_OF_CLUBS = Card.get(Rank.THREE).of(Suit.CLUBS);
    private static final Card TWO_OF_CLUBS = Card.get(Rank.TWO).of(Suit.CLUBS);
    private static final Card ACE_OF_SPADES = Card.get(Rank.ACE).of(Suit.SPADES);
    private static final Card SEVEN_OF_DIAMONDS = Card.get(Rank.SEVEN).of(Suit.DIAMONDS);

    @Mock
    private Deck deck;

    @Test
    public void testDefaultConstructor() {
        final int expectedValue = 13;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS);
        final BlackjackHand hand = new BlackjackHand(deck);
        assertHand(hand, BlackjackHand.State.PLAYING, expectedValue, QUEEN_OF_HEARTS, THREE_OF_CLUBS);
        verify(deck, times(2)).draw();
    }

    @Test
    public void testDefaultConstructorDealsSplit() {
        final int expectedValue = 20;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS);
        final BlackjackHand hand = new BlackjackHand(deck);
        assertHand(hand, BlackjackHand.State.PLAYING, expectedValue, QUEEN_OF_HEARTS, QUEEN_OF_HEARTS);
        verify(deck, times(2)).draw();
    }

    @Test
    public void testDefaultConstructorBlackjack() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(ACE_OF_SPADES);
        final BlackjackHand hand = new BlackjackHand(deck);
        assertHand(hand, BlackjackHand.State.BLACKJACK, expectedValue, QUEEN_OF_HEARTS, ACE_OF_SPADES);
        verify(deck, times(2)).draw();
    }

    @Test
    public void testHit() {
        final int expectedValue = 20;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS).thenReturn(SEVEN_OF_DIAMONDS);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.hit();
        assertHand(hand, BlackjackHand.State.PLAYING, expectedValue, QUEEN_OF_HEARTS, THREE_OF_CLUBS,
                SEVEN_OF_DIAMONDS);
        verify(deck, times(3)).draw();
    }

    @Test
    public void testHitDrawsAceAsHighThenToLowOnNextHit() {
        final int expectedValueOnDeal = 5, expectedValueAfter1stHit = 16, expectedValueAfter2ndHit = 13;
        when(deck.draw()).thenReturn(TWO_OF_CLUBS).thenReturn(THREE_OF_CLUBS).thenReturn(ACE_OF_SPADES)
                .thenReturn(SEVEN_OF_DIAMONDS);
        final BlackjackHand hand = new BlackjackHand(deck);
        assertHand(hand, BlackjackHand.State.PLAYING, expectedValueOnDeal, TWO_OF_CLUBS, THREE_OF_CLUBS);
        hand.hit();
        assertHand(hand, BlackjackHand.State.PLAYING, expectedValueAfter1stHit, TWO_OF_CLUBS, THREE_OF_CLUBS,
                ACE_OF_SPADES);
        hand.hit();
        assertHand(hand, BlackjackHand.State.PLAYING, expectedValueAfter2ndHit, TWO_OF_CLUBS, THREE_OF_CLUBS,
                ACE_OF_SPADES, SEVEN_OF_DIAMONDS);
        verify(deck, times(4)).draw();
    }

    @Test
    public void testHitReceives21() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(THREE_OF_CLUBS).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS)
                .thenReturn(THREE_OF_CLUBS).thenReturn(TWO_OF_CLUBS);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.hit();
        hand.hit();
        hand.hit();
        assertHand(hand, BlackjackHand.State.STAYING, expectedValue, THREE_OF_CLUBS, QUEEN_OF_HEARTS, THREE_OF_CLUBS,
                THREE_OF_CLUBS, TWO_OF_CLUBS);
        verify(deck, times(5)).draw();
    }

    @Test
    public void testHitReceives21WithHighAce() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(THREE_OF_CLUBS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(ACE_OF_SPADES);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.hit();
        assertHand(hand, BlackjackHand.State.STAYING, expectedValue, THREE_OF_CLUBS, SEVEN_OF_DIAMONDS, ACE_OF_SPADES);
        verify(deck, times(3)).draw();
    }

    @Test
    public void testHitReceives21WithLowAce() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(THREE_OF_CLUBS).thenReturn(KING_OF_HEARTS).thenReturn(THREE_OF_CLUBS)
                .thenReturn(TWO_OF_CLUBS).thenReturn(TWO_OF_CLUBS).thenReturn(ACE_OF_SPADES);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.hit();
        hand.hit();
        hand.hit();
        hand.hit();
        assertHand(hand, BlackjackHand.State.STAYING, expectedValue, THREE_OF_CLUBS, KING_OF_HEARTS, THREE_OF_CLUBS,
                TWO_OF_CLUBS, TWO_OF_CLUBS, ACE_OF_SPADES);
        verify(deck, times(6)).draw();
    }

    @Test
    public void testHitBusts() {
        final int expectedValue = 27;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(TEN_OF_DIAMONDS);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.hit();
        assertHand(hand, BlackjackHand.State.BUSTED, expectedValue, QUEEN_OF_HEARTS, SEVEN_OF_DIAMONDS,
                TEN_OF_DIAMONDS);
        verify(deck, times(3)).draw();
    }

    @Test
    public void testStay() {
        final int expectedValue = 13;
        when(deck.draw()).thenReturn(TEN_OF_DIAMONDS).thenReturn(THREE_OF_CLUBS);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.stay();
        assertHand(hand, BlackjackHand.State.STAYING, expectedValue, TEN_OF_DIAMONDS, THREE_OF_CLUBS);
        verify(deck, times(2)).draw();
    }

    @Test
    public void testSplit() {
        final int expectedSplits = 2, expectedValue1stSplit = 9, expectedValue2ndSplit = 17;
        when(deck.draw()).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(TWO_OF_CLUBS)
                .thenReturn(TEN_OF_DIAMONDS);
        final BlackjackHand hand = new BlackjackHand(deck);
        final List<BlackjackHand> splits = hand.split();
        assertEquals(expectedSplits, splits.size());
        assertHand(splits.get(0), BlackjackHand.State.PLAYING, expectedValue1stSplit, SEVEN_OF_DIAMONDS, TWO_OF_CLUBS);
        assertHand(splits.get(1), BlackjackHand.State.PLAYING, expectedValue2ndSplit, SEVEN_OF_DIAMONDS,
                TEN_OF_DIAMONDS);
        verify(deck, times(4)).draw();
    }

    @Test
    public void testSplitDifferentFaceCards() {
        final int expectedSplits = 2, expectedValue1stSplit = 12, expectedValue2ndSplit = 13;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(KING_OF_HEARTS).thenReturn(TWO_OF_CLUBS)
                .thenReturn(THREE_OF_CLUBS);
        final BlackjackHand hand = new BlackjackHand(deck);
        final List<BlackjackHand> splits = hand.split();
        assertEquals(expectedSplits, splits.size());
        assertHand(splits.get(0), BlackjackHand.State.PLAYING, expectedValue1stSplit, QUEEN_OF_HEARTS, TWO_OF_CLUBS);
        assertHand(splits.get(1), BlackjackHand.State.PLAYING, expectedValue2ndSplit, KING_OF_HEARTS, THREE_OF_CLUBS);
        verify(deck, times(4)).draw();
    }

    @Test
    public void testSplitFaceCardAndTen() {
        final int expectedSplits = 2, expectedValue1stSplit = 12, expectedValue2ndSplit = 13;
        when(deck.draw()).thenReturn(TEN_OF_DIAMONDS).thenReturn(KING_OF_HEARTS).thenReturn(TWO_OF_CLUBS)
                .thenReturn(THREE_OF_CLUBS);
        final BlackjackHand hand = new BlackjackHand(deck);
        final List<BlackjackHand> splits = hand.split();
        assertEquals(expectedSplits, splits.size());
        assertHand(splits.get(0), BlackjackHand.State.PLAYING, expectedValue1stSplit, TEN_OF_DIAMONDS, TWO_OF_CLUBS);
        assertHand(splits.get(1), BlackjackHand.State.PLAYING, expectedValue2ndSplit, KING_OF_HEARTS, THREE_OF_CLUBS);
        verify(deck, times(4)).draw();
    }

    @Test
    public void testSplit1stSplitIsBlackjack() {
        final int expectedSplits = 2, expectedValue1stSplit = 21, expectedValue2ndSplit = 13;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(KING_OF_HEARTS).thenReturn(ACE_OF_SPADES)
                .thenReturn(THREE_OF_CLUBS);
        final BlackjackHand hand = new BlackjackHand(deck);
        final List<BlackjackHand> splits = hand.split();
        assertEquals(expectedSplits, splits.size());
        assertHand(splits.get(0), BlackjackHand.State.BLACKJACK, expectedValue1stSplit, QUEEN_OF_HEARTS, ACE_OF_SPADES);
        assertHand(splits.get(1), BlackjackHand.State.PLAYING, expectedValue2ndSplit, KING_OF_HEARTS, THREE_OF_CLUBS);
        verify(deck, times(4)).draw();
    }

    @Test
    public void testSplitCannotBeSplit() {
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS);
        final BlackjackHand hand = new BlackjackHand(deck);
        assertEquals(
                "split() was called on a hand that cannot be split! Both initially dealt cards must be equal in value in order to split.",
                assertThrows(IllegalStateException.class, () -> hand.split()).getMessage());
        verify(deck, times(2)).draw();
    }

    @Test
    public void testHitOnBlackjackState() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(ACE_OF_SPADES);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.hit();
        assertHand(hand, BlackjackHand.State.BLACKJACK, expectedValue, QUEEN_OF_HEARTS, ACE_OF_SPADES);
        verify(deck, times(2)).draw();
    }

    @Test
    public void testStayOnBlackjackState() {
        final int expectedValue = 21;
        when(deck.draw()).thenReturn(TEN_OF_DIAMONDS).thenReturn(ACE_OF_SPADES);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.stay();
        assertHand(hand, BlackjackHand.State.BLACKJACK, expectedValue, TEN_OF_DIAMONDS, ACE_OF_SPADES);
        verify(deck, times(2)).draw();
    }

    @Test
    public void testHitOnStayedState() {
        final int expectedValue = 13;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(THREE_OF_CLUBS);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.stay();
        hand.hit();
        assertHand(hand, BlackjackHand.State.STAYING, expectedValue, QUEEN_OF_HEARTS, THREE_OF_CLUBS);
        verify(deck, times(2)).draw();
    }

    @Test
    public void testHitOnBustedState() {
        final int expectedValue = 27;
        when(deck.draw()).thenReturn(KING_OF_HEARTS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(QUEEN_OF_HEARTS);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.hit();
        hand.hit();
        assertHand(hand, BlackjackHand.State.BUSTED, expectedValue, KING_OF_HEARTS, SEVEN_OF_DIAMONDS, QUEEN_OF_HEARTS);
        verify(deck, times(3)).draw();
    }

    @Test
    public void testStayOnBustedState() {
        final int expectedValue = 27;
        when(deck.draw()).thenReturn(QUEEN_OF_HEARTS).thenReturn(SEVEN_OF_DIAMONDS).thenReturn(KING_OF_HEARTS);
        final BlackjackHand hand = new BlackjackHand(deck);
        hand.hit();
        hand.stay();
        assertHand(hand, BlackjackHand.State.BUSTED, expectedValue, QUEEN_OF_HEARTS, SEVEN_OF_DIAMONDS, KING_OF_HEARTS);
        verify(deck, times(3)).draw();
    }

    private void assertHand(final BlackjackHand hand, final BlackjackHand.State expectedState, final int expectedValue,
            final Card... expectedCards) {
        assertEquals(expectedState, hand.getState());
        assertEquals(expectedValue, hand.getValue());
        assertEquals(asList(expectedCards), hand.getCards());
    }
}
