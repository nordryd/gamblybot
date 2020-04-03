package com.nordryd.gamblybot.cardgames.games.blackjack;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.nordryd.gamblybot.cardgames.entities.Card;
import com.nordryd.gamblybot.cardgames.entities.Card.Rank;
import com.nordryd.gamblybot.cardgames.entities.Deck;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * Represents a player or dealer's hand in a {@link BlackjackGame}.
 * </p>
 *
 * @author Nordryd
 */
public class BlackjackHand
{
    private static final List<Rank> HAS_STRENGTH_OF_10 = asList(Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING);
    private static final int BLACKJACK_VALUE = 21, STRENGTH_OF_10 = 10, ACE_HIGH = 11, ACE_LOW = 1;

    private final List<Card> cards;
    private final Deck deck;

    private State state;
    private int value;
    private int aces;
    private boolean acesAreHigh;

    /**
     * Constructor.
     */
    @Autowired
    public BlackjackHand(final Deck deck) {
        this.deck = deck;
        this.cards = new ArrayList<>();
        value = 0;
        aces = 0;
        acesAreHigh = true;
        hit();
        hit();
    }

    private BlackjackHand(final Deck deck, final Card initialCard) {
        this.deck = deck;
        this.cards = new ArrayList<>();
        value = 0;
        aces = 0;
        acesAreHigh = true;
        updateHandState(initialCard);
        hit();
    }

    /**
     * Hits the hand.
     */
    public void hit() {
        if (!State.PLAYING.equals(state)) {
            updateHandState(deck.draw());
        }
    }

    /**
     * The hand will stay.
     */
    public void stay() {
        if (!State.PLAYING.equals(state)) {
            state = State.STAYING;
        }
    }

    /**
     * Splits the current hand.
     *
     * @return two new hands with the duplicate card as the initial card of both.
     * @throws IllegalStateException if both initial cards are not equal in value.
     */
    public List<BlackjackHand> split() {
        if (!canSplit()) {
            throw new IllegalStateException(
                    "split() was called on a hand that cannot be split! Both cards must be equal in value in order to split");
        }
        final Card initialCard = cards.iterator().next();
        return asList(new BlackjackHand(deck, initialCard), new BlackjackHand(deck, initialCard));
    }

    /**
     * @return the {@link Card cards} currently in this hand.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * @return the value of the hand.
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the hand's current {@link BlackjackHand.State state}.
     */
    public State getState() {
        return state;
    }

    private void updateHandState(final Card card) {
        cards.add(card);
        final Rank cardRank = card.getRank();
        if (Rank.ACE.equals(cardRank)) {
            aces++;
            value = (acesAreHigh ? ACE_HIGH : ACE_LOW);
        }
        else {
            value += HAS_STRENGTH_OF_10.contains(cardRank) ? STRENGTH_OF_10 : cardRank.getStrength();
        }

        if ((value > BLACKJACK_VALUE) && acesAreHigh && (aces > 0)) {
            acesAreHigh = false;
            value = value - (aces * ACE_HIGH) + (aces * ACE_LOW);
        }

        state = (value == BLACKJACK_VALUE) ?
                ((cards.size() == 2) ? State.BLACKJACK : State.STAYING) :
                (value > BLACKJACK_VALUE) ? State.BUSTED : State.PLAYING;
    }

    private boolean canSplit() {
        final Rank cardRank1st = cards.get(0).getRank(), cardRank2nd = cards.get(1).getRank();
        return cardRank1st.equals(cardRank2nd) || HAS_STRENGTH_OF_10.containsAll(asList(cardRank1st, cardRank2nd));
    }

    /**
     * The current state of the hand for determining what actions may be taken with or as a result of this hand.
     */
    public enum State
    {
        PLAYING,
        STAYING,
        BUSTED,
        BLACKJACK
    }
}
