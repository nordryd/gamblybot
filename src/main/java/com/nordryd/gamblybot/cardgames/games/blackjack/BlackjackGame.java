package com.nordryd.gamblybot.cardgames.games.blackjack;

import java.util.ArrayList;
import java.util.List;

import com.nordryd.gamblybot.cardgames.entities.Deck;

/**
 * <p>
 * A game of Blackjack.
 * </p>
 *
 * @author Nordryd
 */
public class BlackjackGame
{
    private final Deck deck;
    private final BlackjackPlayer dealer;
    private final List<BlackjackPlayer> players;

    public BlackjackGame(final Deck deck) {
        this.deck = deck;
        this.dealer = new BlackjackPlayer(deck,0);
        this.players = new ArrayList<>();
    }

    public Deck getDeck() {
        return deck;
    }
    // dealer (BlackjackDealer extends BlackjackPlayer ?)
    // List<Player>
    // the hierarchy of hands should be calculated here, such as BLACKJACK beats 21 (a natural 21 beats a 21 from hits)
}
