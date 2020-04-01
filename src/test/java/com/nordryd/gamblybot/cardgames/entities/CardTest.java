package com.nordryd.gamblybot.cardgames.entities;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.nordryd.gamblybot.cardgames.entities.Card.BattleResult;
import com.nordryd.gamblybot.cardgames.entities.Card.Rank;
import com.nordryd.gamblybot.cardgames.entities.Card.Suit;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * Unit tests for {@link Card}.
 * </p>
 *
 * @author Nordryd
 */
public class CardTest
{
    @Test
    public void testBuilder() {
        final Rank expectedRank = Rank.NINE;
        final Suit expectedSuit = Suit.SPADES;
        final Card card = Card.get(expectedRank).of(expectedSuit);
        assertEquals(expectedRank, card.getRank());
        assertEquals(expectedSuit, card.getSuit());
    }

    @Test
    public void testBuilderNullRank() {
        assertEquals("A card's rank cannot be null",
                assertThrows(IllegalArgumentException.class, () -> Card.get(null).of(Suit.SPADES)).getMessage());
    }

    @Test
    public void testBuilderNullSuit() {
        assertEquals("A card's suit cannot be null",
                assertThrows(IllegalArgumentException.class, () -> Card.get(Rank.ACE).of(null)).getMessage());
    }

    @Test
    public void testBuilderNullRankAndSuit() {
        assertEquals("A card's rank cannot be null",
                assertThrows(IllegalArgumentException.class, () -> Card.get(null).of(null)).getMessage());
    }

    @Test
    public void testDraw() {
        final int testingIterations = 100;
        final List<Rank> possibleRanks = asList(Rank.values());
        final List<Suit> possibleSuits = asList(Suit.values());
        for (int testingIteration = 0; testingIteration < testingIterations; testingIteration++) {
            final Card card = Card.draw();
            assertTrue(possibleRanks.contains(card.getRank()),
                    format("Card #%d had the impossible rank: %s", testingIteration, card.getRank()));
            assertTrue(possibleSuits.contains(card.getSuit()),
                    format("Card #%d had the impossible suit: %s", testingIteration, card.getRank()));
        }
    }

    @Test
    public void testBattle() {
        final Card expectedHigherCard = Card.get(Rank.KING).of(Suit.CLUBS);
        final Card expectedLowerCard = Card.get(Rank.QUEEN).of(Suit.SPADES);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleSameRank() {
        final Card expectedHigherCard = Card.get(Rank.KING).of(Suit.HEARTS);
        final Card expectedLowerCard = Card.get(Rank.KING).of(Suit.DIAMONDS);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleSameRankAcesHigh() {
        final Card expectedHigherCard = Card.get(Rank.ACE).of(Suit.HEARTS);
        final Card expectedLowerCard = Card.get(Rank.ACE).of(Suit.DIAMONDS);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard, true),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard, true),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleSameRankAcesLow() {
        final Card expectedHigherCard = Card.get(Rank.ACE).of(Suit.HEARTS);
        final Card expectedLowerCard = Card.get(Rank.ACE).of(Suit.DIAMONDS);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard, false),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard, false),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleSameSuit() {
        final Card expectedHigherCard = Card.get(Rank.KING).of(Suit.SPADES);
        final Card expectedLowerCard = Card.get(Rank.QUEEN).of(Suit.SPADES);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleAcesHigh() {
        final Card expectedHigherCard = Card.get(Rank.ACE).of(Suit.CLUBS);
        final Card expectedLowerCard = Card.get(Rank.TWO).of(Suit.SPADES);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard, true),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard, true),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleAcesLow() {
        final Card expectedHigherCard = Card.get(Rank.TWO).of(Suit.CLUBS);
        final Card expectedLowerCard = Card.get(Rank.ACE).of(Suit.SPADES);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard, false),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard, false),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleAcesHighSameSuit() {
        final Card expectedHigherCard = Card.get(Rank.ACE).of(Suit.SPADES);
        final Card expectedLowerCard = Card.get(Rank.QUEEN).of(Suit.SPADES);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard, true),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard, true),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleAcesLowSameSuit() {
        final Card expectedHigherCard = Card.get(Rank.TWO).of(Suit.CLUBS);
        final Card expectedLowerCard = Card.get(Rank.ACE).of(Suit.CLUBS);
        assertEquals(BattleResult.WIN, expectedHigherCard.battle(expectedLowerCard, false),
                getBattleString(expectedHigherCard, expectedLowerCard));
        assertEquals(BattleResult.LOSE, expectedLowerCard.battle(expectedHigherCard, false),
                getBattleString(expectedLowerCard, expectedHigherCard));
    }

    @Test
    public void testBattleTie() {
        final Card card = Card.get(Rank.EIGHT).of(Suit.CLUBS);
        final Card otherCard = Card.get(Rank.EIGHT).of(Suit.CLUBS);
        assertEquals(BattleResult.TIE, card.battle(otherCard), getBattleString(card, otherCard));
        assertEquals(BattleResult.TIE, otherCard.battle(card), getBattleString(otherCard, card));
    }

    @Test
    public void testBattleTieAcesHigh() {
        final Card card = Card.get(Rank.ACE).of(Suit.CLUBS);
        final Card otherCard = Card.get(Rank.ACE).of(Suit.CLUBS);
        assertEquals(BattleResult.TIE, card.battle(otherCard, true), getBattleString(card, otherCard));
        assertEquals(BattleResult.TIE, otherCard.battle(card, true), getBattleString(otherCard, card));
    }

    @Test
    public void testBattleTieAcesLow() {
        final Card card = Card.get(Rank.ACE).of(Suit.CLUBS);
        final Card otherCard = Card.get(Rank.ACE).of(Suit.CLUBS);
        assertEquals(BattleResult.TIE, card.battle(otherCard, false), getBattleString(card, otherCard));
        assertEquals(BattleResult.TIE, otherCard.battle(card, false), getBattleString(otherCard, card));
    }

    @Test
    public void testToString() {
        assertEquals("JACK of HEARTS", Card.get(Rank.JACK).of(Suit.HEARTS).toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        final Card baseCard = Card.get(Rank.TWO).of(Suit.CLUBS);
        final Card cardSame = Card.get(Rank.TWO).of(Suit.CLUBS);
        final Card cardDiffRank = Card.get(Rank.THREE).of(Suit.CLUBS);
        final Card cardDiffSuit = Card.get(Rank.TWO).of(Suit.SPADES);
        final Card cardCompletelyDiff = Card.get(Rank.THREE).of(Suit.SPADES);
        assertEquals(baseCard, cardSame);
        assertEquals(cardSame, baseCard);
        assertEquals(baseCard.hashCode(), cardSame.hashCode());
        assertNotEquals(baseCard, cardDiffRank);
        assertNotEquals(baseCard, cardDiffSuit);
        assertNotEquals(baseCard, cardCompletelyDiff);
    }

    private static String getBattleString(final Card attacking, final Card defending) {
        return format("BATTLE[%s -> %s]", attacking, defending);
    }
}
