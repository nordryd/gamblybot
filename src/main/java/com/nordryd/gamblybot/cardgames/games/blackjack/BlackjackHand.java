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
    private static final int INITIAL_HAND_SIZE = 2, BLACKJACK_VALUE = 21, STRENGTH_OF_10 = 10, ACE_HIGH = 11, ACE_LOW = 1;

    private final List<Card> cards;
    private final Deck deck;

    private State state;
    private int value;
    private int aces;
    private boolean acesAreHigh;

    /**
     * Constructor.
     *
     * @param deck an instance of {@link Deck}.
     */
    @Autowired
    public BlackjackHand(final Deck deck) {
        this(deck, deck.draw());
    }

    private BlackjackHand(final Deck deck, final Card initialCard) {
        this.cards = new ArrayList<>();
        this.deck = deck;
        this.state = State.PLAYING;
        this.value = 0;
        this.aces = 0;
        this.acesAreHigh = true;
        updateHandValue(initialCard);
        hit();
    }

    /**
     * Hits the hand.
     */
    public void hit() {
        if (State.PLAYING.equals(state)) {
            updateHandValue(deck.draw());
        }
    }

    /**
     * The hand will stay.
     */
    public void stay() {
        if (State.PLAYING.equals(state)) {
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
            throw new IllegalStateException("split() was called on a hand that cannot be split!\n" +
                    "Both initially dealt cards must be equal in value in order to split.\n" + "The cards were:\n" +
                    cards.get(0) + ", " + cards.get(1));
        }
        final Card initialCard = cards.get(0), otherInitialCard = cards.get(1);
        return asList(new BlackjackHand(deck, initialCard), new BlackjackHand(deck,
                !initialCard.equals(otherInitialCard) && HAS_STRENGTH_OF_10.contains(initialCard.getRank()) &&
                        HAS_STRENGTH_OF_10.contains(otherInitialCard.getRank()) ? otherInitialCard : initialCard));
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

    private void updateHandValue(final Card card) {
        cards.add(card);
        final Rank cardRank = card.getRank();
        if (Rank.ACE.equals(cardRank)) {
            aces++;
            value += acesAreHigh ? ACE_HIGH : ACE_LOW;
        }
        else {
            value += HAS_STRENGTH_OF_10.contains(cardRank) ? STRENGTH_OF_10 : cardRank.getStrength();
        }

        updateHandState();
    }

    private void updateHandState() {
        if (value == BLACKJACK_VALUE) {
            state = (cards.size() == INITIAL_HAND_SIZE) ? State.BLACKJACK : State.STAYING;
        }
        else if (value > BLACKJACK_VALUE) {
            if (acesAreHigh && (aces > 0)) {
                value = value - (aces * (ACE_HIGH - ACE_LOW));
                updateHandState();
            }
            else {
                state = State.BUSTED;
            }
        }
    }

    private boolean canSplit() {
        final Rank cardRank1st = cards.get(0).getRank(), cardRank2nd = cards.get(1).getRank();
        return cardRank1st.equals(cardRank2nd) ||
                (HAS_STRENGTH_OF_10.contains(cardRank1st) && HAS_STRENGTH_OF_10.contains(cardRank2nd));
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
