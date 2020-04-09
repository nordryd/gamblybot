package com.nordryd.gamblybot.cardgames.games.blackjack;

import java.util.ArrayList;
import java.util.List;

import com.nordryd.gamblybot.cardgames.entities.Deck;

/**
 * <p>
 * A player in a {@link BlackjackGame}.
 * </p>
 *
 * @author Nordryd
 */
public class BlackjackPlayer
{
    private final List<BlackjackHand> hands;
    private State state;
    private int wager;

    public BlackjackPlayer(final Deck deck, final int initialWager) {
        this.hands = new ArrayList<>();
        this.hands.add(new BlackjackHand(deck));
        this.state = State.WAITING;
        this.wager = initialWager;
    }

    public void play() {

    }

    public void doubleDown() {

    }

    public void surrender() {

    }

    public int getWager() {
        return wager;
    }

    public State getState() {
        return state;
    }

    public enum State
    {
        WAITING,
        PLAYING,
        FINISHED,
        SURRENDERED,
        DOUBLED_DOWN;
    }
    // wager
    // List<Hand> recursively go through each hands, split hands here (save the card type, remove the current hand, add two hands)
    // do insurance here
    // double down here. handle the wager, and then hit, then immediately stay if you dont bust
    // surrenders here. for each hand, return the correct wager, and discard the hand
}
