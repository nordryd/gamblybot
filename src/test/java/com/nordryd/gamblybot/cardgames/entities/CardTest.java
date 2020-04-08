package com.nordryd.gamblybot.cardgames.entities;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThat(card.getRank()).isEqualByComparingTo(expectedRank);
        assertThat(card.getSuit()).isEqualByComparingTo(expectedSuit);
    }

    @Test
    public void testBuilderNullRank() {
        assertThat(assertThrows(IllegalArgumentException.class, () -> Card.get(null).of(Suit.SPADES)).getMessage())
                .isEqualTo("A card's rank cannot be null");
    }

    @Test
    public void testBuilderNullSuit() {
        assertThat(assertThrows(IllegalArgumentException.class, () -> Card.get(Rank.ACE).of(null)).getMessage())
                .isEqualTo("A card's suit cannot be null");
    }

    @Test
    public void testBuilderNullRankAndSuit() {
        assertThat(assertThrows(IllegalArgumentException.class, () -> Card.get(null).of(null)).getMessage())
                .isEqualTo("A card's rank cannot be null");
    }

    @Test
    public void testBattle() {
        final Card expectedHigherCard = Card.get(Rank.KING).of(Suit.CLUBS);
        final Card expectedLowerCard = Card.get(Rank.QUEEN).of(Suit.SPADES);
        assertThat(expectedHigherCard.battle(expectedLowerCard))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleSameRank() {
        final Card expectedHigherCard = Card.get(Rank.KING).of(Suit.HEARTS);
        final Card expectedLowerCard = Card.get(Rank.KING).of(Suit.DIAMONDS);
        assertThat(expectedHigherCard.battle(expectedLowerCard))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleSameRankAcesHigh() {
        final Card expectedHigherCard = Card.get(Rank.ACE).of(Suit.HEARTS);
        final Card expectedLowerCard = Card.get(Rank.ACE).of(Suit.DIAMONDS);
        assertThat(expectedHigherCard.battle(expectedLowerCard, true))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard, true))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleSameRankAcesLow() {
        final Card expectedHigherCard = Card.get(Rank.ACE).of(Suit.HEARTS);
        final Card expectedLowerCard = Card.get(Rank.ACE).of(Suit.DIAMONDS);
        assertThat(expectedHigherCard.battle(expectedLowerCard, false))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard, false))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleSameSuit() {
        final Card expectedHigherCard = Card.get(Rank.KING).of(Suit.SPADES);
        final Card expectedLowerCard = Card.get(Rank.QUEEN).of(Suit.SPADES);
        assertThat(expectedHigherCard.battle(expectedLowerCard))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleAcesHigh() {
        final Card expectedHigherCard = Card.get(Rank.ACE).of(Suit.CLUBS);
        final Card expectedLowerCard = Card.get(Rank.TWO).of(Suit.SPADES);
        assertThat(expectedHigherCard.battle(expectedLowerCard, true))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard, true))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleAcesLow() {
        final Card expectedHigherCard = Card.get(Rank.TWO).of(Suit.CLUBS);
        final Card expectedLowerCard = Card.get(Rank.ACE).of(Suit.SPADES);
        assertThat(expectedHigherCard.battle(expectedLowerCard, false))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard, false))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleAcesHighSameSuit() {
        final Card expectedHigherCard = Card.get(Rank.ACE).of(Suit.SPADES);
        final Card expectedLowerCard = Card.get(Rank.QUEEN).of(Suit.SPADES);
        assertThat(expectedHigherCard.battle(expectedLowerCard, true))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard, true))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleAcesLowSameSuit() {
        final Card expectedHigherCard = Card.get(Rank.TWO).of(Suit.CLUBS);
        final Card expectedLowerCard = Card.get(Rank.ACE).of(Suit.CLUBS);
        assertThat(expectedHigherCard.battle(expectedLowerCard, false))
                .withFailMessage(getBattleString(expectedHigherCard, expectedLowerCard))
                .isEqualByComparingTo(BattleResult.WIN);
        assertThat(expectedLowerCard.battle(expectedHigherCard, false))
                .withFailMessage(getBattleString(expectedLowerCard, expectedHigherCard))
                .isEqualByComparingTo(BattleResult.LOSE);
    }

    @Test
    public void testBattleTie() {
        final Card card = Card.get(Rank.EIGHT).of(Suit.CLUBS);
        final Card otherCard = Card.get(Rank.EIGHT).of(Suit.CLUBS);
        assertThat(card.battle(otherCard)).withFailMessage(getBattleString(card, otherCard))
                .isEqualByComparingTo(BattleResult.TIE);
        assertThat(otherCard.battle(card)).withFailMessage(getBattleString(otherCard, card))
                .isEqualByComparingTo(BattleResult.TIE);
    }

    @Test
    public void testBattleTieAcesHigh() {
        final Card card = Card.get(Rank.ACE).of(Suit.CLUBS);
        final Card otherCard = Card.get(Rank.ACE).of(Suit.CLUBS);
        assertThat(card.battle(otherCard, true)).withFailMessage(getBattleString(card, otherCard))
                .isEqualByComparingTo(BattleResult.TIE);
        assertThat(otherCard.battle(card, true)).withFailMessage(getBattleString(otherCard, card))
                .isEqualByComparingTo(BattleResult.TIE);
    }

    @Test
    public void testBattleTieAcesLow() {
        final Card card = Card.get(Rank.ACE).of(Suit.CLUBS);
        final Card otherCard = Card.get(Rank.ACE).of(Suit.CLUBS);
        assertThat(card.battle(otherCard, false)).withFailMessage(getBattleString(card, otherCard))
                .isEqualByComparingTo(BattleResult.TIE);
        assertThat(otherCard.battle(card, false)).withFailMessage(getBattleString(otherCard, card))
                .isEqualByComparingTo(BattleResult.TIE);
    }

    @Test
    public void testToString() {
        assertThat(Card.get(Rank.JACK).of(Suit.HEARTS).toString()).isEqualTo("JACK of HEARTS");
    }

    @Test
    public void testEqualsAndHashCode() {
        final Card baseCard = Card.get(Rank.TWO).of(Suit.CLUBS);
        final Card cardSame = Card.get(Rank.TWO).of(Suit.CLUBS);
        final Card cardDiffRank = Card.get(Rank.THREE).of(Suit.CLUBS);
        final Card cardDiffSuit = Card.get(Rank.TWO).of(Suit.SPADES);
        final Card cardCompletelyDiff = Card.get(Rank.THREE).of(Suit.SPADES);
        assertThat(baseCard).isEqualTo(cardSame).isNotEqualTo(cardDiffRank).isNotEqualTo(cardDiffSuit)
                .isNotEqualTo(cardCompletelyDiff);
        assertThat(cardSame).isEqualTo(baseCard);
        assertThat(baseCard.hashCode()).isEqualTo(cardSame.hashCode());
    }

    private static String getBattleString(final Card attacking, final Card defending) {
        return format("BATTLE[%s -> %s]", attacking, defending);
    }
}
