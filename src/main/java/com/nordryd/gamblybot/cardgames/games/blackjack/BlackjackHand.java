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
    private static final int BLACKJACK_VALUE = 21;

    private final List<Card> cards;

    @Autowired
    private Deck deck;

    private State state;
    private int value;
    private boolean areAcesHigh;

    public BlackjackHand() {
        this.cards = new ArrayList<>();
        hit();
        hit();
    }

    private BlackjackHand(final Card initialCard) {
        this.cards = new ArrayList<>();
        this.cards.add(initialCard);
        hit();
    }

    public void hit() {
        areAcesHigh = value <= 10;
    }

    public void stay() {

    }

    public List<BlackjackHand> split() {
        if (!cards.get(0).equals(cards.get(1))) {
            throw new IllegalStateException(
                    "split() was called on a hand that cannot be split! Both cards must be equal in value in order to split");
        }
        return asList(new BlackjackHand(cards.get(0)), new BlackjackHand(cards.get(0)));
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getValue() {
        return value;
    }

    public State getState() {
        return state;
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
