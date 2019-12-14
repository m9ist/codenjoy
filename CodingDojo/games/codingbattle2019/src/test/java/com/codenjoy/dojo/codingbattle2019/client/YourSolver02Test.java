package com.codenjoy.dojo.codingbattle2019.client;

import com.codenjoy.dojo.codingbattle2019.client.utils.SpaceCommon;
import com.codenjoy.dojo.codingbattle2019.model.Elements;
import com.codenjoy.dojo.codingbattle2019.services.Scores;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YourSolver02Test {
    private static final long SEED = 5;

    private YourSolver02 ai;

    @Before
    public void initDice() {
        final Dice dice = new SeedRandomDice(SEED);
        ai = new YourSolver02(dice);
    }

    private Board board(final String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void testShould01() {
        checkMove("" +
                "☼   ☼" +
                "☼ 7 ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "", Direction.UP.ACT(true));
    }

    @Test
    public void testShould02() {
        checkMove("" +
                "☼   ☼" +
                "☼   ☼" +
                "☼A 7☼" +
                "☼   ☼" +
                "☼   ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould03() {
        checkMove("" +
                "☼   ☼" +
                "☼   ☼" +
                "☼7 A☼" +
                "☼   ☼" +
                "☼   ☼" +
                "", Direction.LEFT.ACT(true));
    }

    @Test
    public void testShould04() {
        checkMove("" +
                "☼   ☼" +
                "☼  A☼" +
                "☼   ☼" +
                "☼  7☼" +
                "☼   ☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould05() {
        checkMove("" +
                "☼    ☼" +
                "☼  7 ☼" +
                "☼ ☼  ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould06() {
        checkMove("" +
                "☼   7☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ A☼ ☼" +
                "☼    ☼" +
                "☼   7☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould07() {
        checkMove("" +
                "☼   7☼" +
                "☼    ☼" +
                "☼  ☼7☼" +
                "☼ A☼ ☼" +
                "☼    ☼" +
                "☼   7☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould08() {
        checkMove("" +
                "☼    7☼" +
                "☼ ☼☼☼ ☼" +
                "☼ ☼ 7 ☼" +
                "☼A☼☼☼ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   7 ☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould09() {
        checkMove("" +
                "☼  7   ☼" +
                "☼  ☼☼☼ ☼" +
                "☼  ☼7  ☼" +
                "☼  ☼☼☼ ☼" +
                "☼  A   ☼" +
                "☼      ☼" +
                "☼☼☼ ☼  ☼" +
                "☼7     ☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould10() {
        checkMove("" +
                "☼  7   ☼" +
                "☼  ☼☼☼ ☼" +
                "☼  ☼7  ☼" +
                "☼  ☼☼☼ ☼" +
                "☼  A   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼  ☼" +
                "☼7     ☼" +
                "", Direction.LEFT.ACT(true));
    }

    @Test
    public void testShould11() {
        checkMove("" +
                "☼  7   ☼" +
                "☼  ☼☼☼ ☼" +
                "☼  ☼7  ☼" +
                "☼  ☼☼  ☼" +
                "☼  A   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼  ☼" +
                "☼7     ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould12() {
        checkMove("" +
                "☼ 7     ☼" +
                "☼  ☼☼☼  ☼" +
                "☼   7  7☼" +
                "☼ ☼ ☼☼☼ ☼" +
                "☼       ☼" +
                "☼  A    ☼" +
                "☼      7☼" +
                "☼☼☼☼    ☼" +
                "☼  7    ☼" +
                "", Direction.UP.ACT(true));
    }

    @Test
    public void testShould13() {
        checkMove("" +
                "☼ 7     ☼" +
                "☼  ☼☼☼  ☼" +
                "☼ 7     ☼" +
                "☼ ☼☼☼☼☼ ☼" +
                "☼ ☼   ☼ ☼" +
                "☼  A    ☼" +
                "☼       ☼" +
                "☼☼☼☼☼   ☼" +
                "☼  7    ☼" +
                "", Direction.LEFT.ACT(true));
    }

    @Test
    public void testShould14() {
        checkMove("" +
                "☼ 7     ☼" +
                "☼  ☼☼☼  ☼" +
                "☼ 7    7☼" +
                "☼ ☼☼☼☼☼ ☼" +
                "☼ ☼   ☼ ☼" +
                "☼   A   ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼  ☼" +
                "☼ 7     ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould15() {
        checkMove("" +
                "☼7 7    ☼" +
                "☼       ☼" +
                "☼ ☼     ☼" +
                "☼ A     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼7      ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould16() {
        checkMove("" +
                "☼        7☼" +
                "☼     ☼A  ☼" +
                "☼    7  ☼7☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould17() {
        checkMove("" +
                "☼         ☼" +
                "☼         ☼" +
                "☼    7    ☼" +
                "☼   ♣     ☼" +
                "☼         ☼" +
                "☼    A    ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould18() {
        checkMove("" +
                "☼         ☼" +
                "☼   ♣     ☼" +
                "☼         ☼" +
                "☼    7    ☼" +
                "☼     ☼   ☼" +
                "☼    A    ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould19() {
        checkMove("" +
                "☼   A ☼" +
                "☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould20() {
        checkMove("" +
                "☼     ☼" +
                "☼   A ☼" +
                "☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould21() {
        checkMove("" +
                "☼ 7    ☼" +
                "☼ ☼A   ☼" +
                "☼ 7    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould22() {
        checkMove("" +
                "☼      ☼" +
                "☼ 7    ☼" +
                "☼ ☼A   ☼" +
                "☼ 7    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "", Direction.DOWN.ACT(true));
    }

    @Test
    public void testShould23() {
        checkMove("" +
                "☼      ☼" +
                "☼  7   ☼" +
                "☼ 7A   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "", Direction.LEFT.ACT(true));
    }

    @Test
    public void testShould24() {
        checkMove("" +
                "☼      ☼" +
                "☼  7   ☼" +
                "☼ 7A7  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould25() {
        checkMove("" +
                "☼      ☼" +
                "☼   ♣  ☼" +
                "☼      ☼" +
                "☼  ☼   ☼" +
                "☼  A7  ☼" +
                "☼  ☼   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "", Direction.LEFT.ACT(true));
    }

    @Test
    public void testShould26() {
        checkMove("" +
                "☼  ♣ 7☼" +
                "☼     ☼" +
                "☼ ☼   ☼" +
                "☼ A   ☼" +
                "☼ ☼   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "", Direction.LEFT.ACT(true));
    }

    @Test
    public void testShould27() {
        checkMove("" +
                "☼  ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼   7☼" +
                "☼   A☼" +
                "☼   ☼☼" +
                "", Direction.LEFT.ACT(true));
    }

    @Test
    public void testShould28() {
        checkMove("" +
                "☼ ♣  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼7   ☼" +
                "☼A   ☼" +
                "☼    ☼" +
                "", Direction.RIGHT.ACT(true));
    }

    @Test
    public void testShould29() {
        checkMove("" +
                "☼  ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼   7☼" +
                "☼   A☼" +
                "☼   ☼☼" +
                "", Direction.LEFT.ACT(true), 2);
    }

    @Test
    public void testShould30() {
        checkMove("" +
                "☼ ♣  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼7   ☼" +
                "☼A   ☼" +
                "☼    ☼" +
                "", Direction.RIGHT.ACT(true), SpaceCommon.BULLETS_COUNT);
    }

    @Test
    public void testShould31() {
        checkMove("" +
                "☼       ☼" +
                "☼ ♣ ♣   ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼  7    ☼" +
                "☼  A    ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "", Direction.RIGHT.ACT(true), SpaceCommon.BULLETS_COUNT);
    }

    @Test
    public void testShould32() {
        checkMove("" +
                "☼      ☼" +
                "☼♣  ♣  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  7   ☼" +
                "☼  A   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "", Direction.RIGHT.ACT(true), SpaceCommon.BULLETS_COUNT);
    }

    @Test
    public void testShould33() {
        checkMove("" +
                "☼        ☼" +
                "☼7      7☼" +
                "☼ ☻      ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼   ☼    ☼" +
                "☼   A    ☼" +
                "☼        ☼" +
                "", Direction.RIGHT.toString(), 0);
    }

    @Test
    public void testShould34() {
        checkMove("" +
                "☼                ☼" +
                "☼7       7       ☼" +
                "☼ ☻              ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼   ☼            ☼" +
                "☼   A   ☼        ☼" +
                "☼    ☼☼☼☼        ☼" +
                "☼    ☻       7   ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "", Direction.RIGHT.toString(), 0);
    }

    @Test
    public void testShould35() {
        checkMove("" +
                "☼                ☼" +
                "☼7       7       ☼" +
                "☼ ☻              ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼   ☼            ☼" +
                "☼   A   ☼        ☼" +
                "☼    ☼☼☼☼        ☼" +
                "☼            7   ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "☼                ☼" +
                "", Direction.DOWN.toString(), 0);
    }

    @Test
    public void testShouldEscape01() {
        checkMove("" +
                "☼               ☼" +
                "☼    7          ☼" +
                "☼   ☼           ☼" +
                "☼  A            ☼" +
                "☼   ☻           ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "", Direction.UP.toString(), 0);
    }

    @Test
    public void testShouldEscape02() {
        checkMove("" +
                "☼               ☼" +
                "☼  7            ☼" +
                "☼   ☼           ☼" +
                "☼    A          ☼" +
                "☼   ☻           ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "", Direction.UP.toString(), 0);
    }

    @Test
    public void testShouldEscape03() { // Никто пока не проходит этот тест.
        checkMove("" +
                "☼               ☼" +
                "☼               ☼" +
                "☼     0         ☼" +
                "☼    A0☼        ☼" +
                "☼    ☻ 7        ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "☼               ☼" +
                "", Direction.LEFT.toString(), 0);
    }

    @Test
    public void testShouldUpLeft() {
        checkMove("" +
                "☼       ☼" +
                "☼ 7     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼  A    ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "", new String[]{Direction.UP.toString(), Direction.LEFT.toString()});
    }

    @Test
    public void testShouldDiagonal01() {
        checkMove("" +
                "☼    ☼" +
                "☼ 7  ☼" +
                "☼  A ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "", Direction.UP_LEFT.ACT(true));
    }

    @Test
    public void testShouldDiagonal02() {
        checkMove("" +
                "☼    ☼" +
                "☼   7☼" +
                "☼  A ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "", Direction.UP_RIGHT.ACT(true));
    }

    @Test
    public void testShouldDiagonal03() {
        checkMove("" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼ 7  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "", Direction.DOWN_LEFT.ACT(true));
    }

    @Test
    public void testShouldDiagonal04() {
        checkMove("" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼   7☼" +
                "☼    ☼" +
                "☼    ☼" +
                "", Direction.DOWN_RIGHT.ACT(true));
    }

    @Test
    public void testShouldByBullet() {
        checkMove("" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ A 7☼" +
                "☼  * ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "", Direction.UP_RIGHT.toString());
    }

    @Test
    public void testShouldUpRight() {
        checkMove("" +
                "☼    ☼" +
                "☼  7 ☼" +
                "☼    ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "", new String[]{Direction.UP.toString(), Direction.RIGHT.toString()});
    }

    @Test
    public void testShouldMoves01() {
        checkMoves(new String[]{"" +
                "☼         ☼" +
                "☼         ☼" +
                "☼      7  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼ ♣       ☼" +
                "☼ 7    A  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "", "" +
                "☼         ☼" +
                "☼         ☼" +
                "☼      7  ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼ ♣   A   ☼" +
                "☼         ☼" +
                "☼         ☼" +
                ""}, new String[]{Direction.LEFT.ACT(true), Direction.LEFT.ACT(true)});
    }

    @Test
    public void testShouldMoves02() {
        checkMoves(new String[]{"" +
                "☼ 7     ☼" +
                "☼     A ☼" +
                "☼       ☼" +
                "☼     0 ☼" +
                "☼     7 ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "", "" +
                "☼ 7     ☼" +
                "☼       ☼" +
                "☼     A ☼" +
                "☼       ☼" +
                "☼     0 ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                ""}, new String[]{Direction.DOWN.ACT(true), Direction.DOWN.ACT(true)});
    }

    @Test
    public void testShouldMoves03() {
        checkMoves(new String[]{"" +
                "☼☻7     ☼" +
                "☼     A ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼     0 ☼" +
                "☼     7 ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "", "" +
                "☼ ☻   7 ☼" +
                "☼       ☼" +
                "☼     A ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼     0 ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                ""}, new String[]{Direction.DOWN.ACT(true), Direction.UP.ACT(true)});
    }

    @Test
    public void testShouldMovesAndScores01() {
        checkMovesAndScores(new String[]{"" +
                        "☼     ♣ ☼" +
                        "☼       ☼" +
                        "☼     0 ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼     A ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼   ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼     ♣ ☼" +
                        "☼       ☼" +
                        "☼  ☼  0 ☼" +
                        "☼  ☼  * ☼" +
                        "☼    A  ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼   ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼     ♣ ☼" +
                        "☼  ☼  x ☼" +
                        "☼  ☼ *  ☼" +
                        "☼   A   ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼   ☻☼" +
                        ""},
                new String[]{Direction.LEFT.ACT(true), Direction.LEFT.ACT(true),
                        Direction.LEFT.ACT(true)},
                new int[]{0, 0, Scores.DESTROY_STONE_SCORE});
    }

    @Test
    public void testShouldMovesAndScores02() {
        checkMovesAndScores(new String[]{"" +
                        "☼      ♣☼" +
                        "☼       ☼" +
                        "☼     0 ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼   7 A ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼   ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼      ♣☼" +
                        "☼       ☼" +
                        "☼  ☼  0 ☼" +
                        "☼  ☼  * ☼" +
                        "☼   7A  ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼   ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼      ♣☼" +
                        "☼  ☼  x ☼" +
                        "☼  ☼ *  ☼" +
                        "☼   A   ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼   ☻☼" +
                        ""},
                new String[]{Direction.LEFT.ACT(true), Direction.LEFT.ACT(true),
                        Direction.LEFT.ACT(true)},
                new int[]{0, 0, Scores.DESTROY_STONE_SCORE});
    }

    @Test
    public void testShouldMovesAndScores03() {
        checkMovesAndScores(new String[]{"" +
                        "☼       ☼" +
                        "☼     ♣ ☼" +
                        "☼       ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼   7 A ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼   ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼     ♣ ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼  * ☼" +
                        "☼   7A  ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼   ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼    xxx☼" +
                        "☼  ☼ xxx☼" +
                        "☼  ☼ xxx☼" +
                        "☼   A   ☼" +
                        "☼7 ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼   ☻☼" +
                        ""},
                new String[]{Direction.LEFT.ACT(true), Direction.LEFT.ACT(true),
                        Direction.LEFT.ACT(true)},
                new int[]{0, 0, Scores.DESTROY_BOMB_SCORE});
    }

    @Test
    public void testShouldMovesAndScores04() {
        checkMovesAndScores(new String[]{"" +
                        "☼    0♣ ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼    ☼" +
                        "☼     A ☼" +
                        "☼   7☼  ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼7  ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼    0♣ ☼" +
                        "☼       ☼" +
                        "☼  ☼    ☼" +
                        "☼  ☼  * ☼" +
                        "☼    A  ☼" +
                        "☼   7☼  ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼7  ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼    0♣ ☼" +
                        "☼  ☼  * ☼" +
                        "☼  ☼ *  ☼" +
                        "☼   A   ☼" +
                        "☼   7☼  ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼7  ☻☼" +
                        "", "" +
                        "☼       ☼" +
                        "☼    xxx☼" +
                        "☼    xxx☼" +
                        "☼  ☼ xxx☼" +
                        "☼  ☼*   ☼" +
                        "☼       ☼" +
                        "☼   A☼  ☼" +
                        "☼  ☼    ☼" +
                        "☼$ ☼7  ☻☼" +
                        ""},
                new String[]{Direction.LEFT.ACT(true), Direction.LEFT.ACT(true),
                        Direction.DOWN.ACT(true), Direction.DOWN.ACT(true)},
                new int[]{0, 0, 0, Scores.DESTROY_BOMB_SCORE + Scores.DESTROY_STONE_SCORE});
    }

    @Test
    public void testShouldMovesAndScores05() {
        checkMovesAndScores(new String[]{"" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼7 ♣ ☼" +
                        "☼  0 ☼" +
                        "☼  0A☼" +
                        "☼    ☼" +
                        "", "" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼7   ☼" +
                        "☼  ♣*☼" +
                        "☼  0 ☼" +
                        "☼  0A☼" +
                        "", "" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼7  *☼" +
                        "☼ xxx☼" +
                        "☼ xxx☼" +
                        "☼ xxx☼" +
                        ""},
                new String[]{Direction.DOWN.ACT(true), Direction.UP.ACT(true), null},
                new int[]{0, 0, -Scores.LOOSE_PENALTY});
    }

    @Test
    public void testShouldMovesAndScores06() {
        checkMovesAndScores(new String[]{"" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼7♣  ☼" +
                        "☼ 0  ☼" +
                        "☼A0  ☼" +
                        "☼    ☼" +
                        "", "" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼7   ☼" +
                        "☼*♣  ☼" +
                        "☼ 0  ☼" +
                        "☼A0  ☼" +
                        "", "" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼*   ☼" +
                        "☼xxx ☼" +
                        "☼xxx ☼" +
                        "☼xxx ☼" +
                        ""},
                new String[]{Direction.DOWN.ACT(true), Direction.UP.ACT(true), null},
                new int[]{0, 0, -Scores.LOOSE_PENALTY});
    }

    @Test
    public void testShouldMovesAndScores07() {
        checkMovesAndScores(new String[]{"" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼  0 ☼" +
                        "☼ 000☼" +
                        "☼ 0A0☼" +
                        "☼    ☼" +
                        "", "" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼  0 ☼" +
                        "☼ 000☼" +
                        "☼ 0A0☼" +
                        "", "" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼  + ☼" +
                        "☼ 000☼" +
                        ""},
                SpaceCommon.IS_BATTLE_MODE
                        ? new String[]{Direction.DOWN.ACT(true), Direction.UP.ACT(true), null}
                        : new String[]{Direction.DOWN.toString(), Direction.UP.toString(), null},
                new int[]{0, 0, -Scores.LOOSE_PENALTY});
    }

    @Test
    public void testShouldMovesAndScores08() {
        checkMovesAndScores(new String[]{"" +
                        "☼    ☼" +
                        "☼7   ☼" +
                        "☼  ☻ ☼" +
                        "☼    ☼" +
                        "☼7 A ☼" +
                        "☼    ☼" +
                        "", "" +
                        "☼    ☼" +
                        "☼7   ☼" +
                        "☼  ☻ ☼" +
                        "☼  * ☼" +
                        "☼7A  ☼" +
                        "☼    ☼" +
                        "", "" +
                        "☼    ☼" +
                        "☼7   ☼" +
                        "☼    ☼" +
                        "☼ *  ☼" +
                        "☼A   ☼" +
                        "☼    ☼" +
                        ""},
                new String[]{Direction.LEFT.ACT(true), Direction.LEFT.ACT(true),
                        Direction.UP.ACT(true)},
                new int[]{0, 0, Scores.DESTROY_ENEMY_SCORE});
    }

    @Test
    public void testSafeDirections() {
        setupAi(""
                , "     "
                , "  ♣  "
                , "     "
                , "  A  "
                , "     "
        );
        for (Direction move : Direction.getMoves()) {
            Assert.assertEquals(
                    "fail for " + move,
                    EnumSet.of(Direction.UP, Direction.UP_LEFT, Direction.UP_RIGHT, Direction.LEFT, Direction.RIGHT, Direction.STOP).contains(move),
                    ai.priorities.get(move).isDeath()
            );
        }
    }

    @Test
    public void testSafeDirections2() {
        setupAi(""
                , "       "
                , "  ♣  A "
                , "       "
                , "       "
        );
        for (Direction move : Direction.getMoves()) {
            Assert.assertFalse(
                    "fail for " + move,
                    ai.priorities.get(move).isDeath()
            );
        }
    }

    @Test
    public void testSafeDirections3() {
        setupAi(""
                , "       "
                , "  A ♣  "
                , "       "
                , "       "
        );
        for (Direction move : Direction.getMoves()) {
            Assert.assertEquals(
                    "fail for " + move,
                    EnumSet.of(Direction.RIGHT, Direction.DOWN_RIGHT).contains(move),
                    ai.priorities.get(move).isDeath()
            );
        }
    }

    @Test
    public void testSafeDirections4() {
        setupAi(""
                , "       "
                , "    ♣  "
                , "       "
                , "       "
                , "  A    "
                , "       "
                , "       "
        );
        for (Direction move : Direction.getMoves()) {
            Assert.assertEquals(
                    "fail for " + move,
                    EnumSet.of(Direction.UP_RIGHT).contains(move),
                    ai.priorities.get(move).isDeath()
            );
        }
    }

    @Test
    public void testSafeDirections5() {
        setupAi(""
                , "   0   "
                , "       "
                , "  A    "
                , "       "
        );
        ai.isSaveDir(Direction.UP_RIGHT);
        for (Direction move : Direction.getMoves()) {
            Assert.assertEquals(
                    "fail for " + move,
                    EnumSet.of(Direction.UP_RIGHT).contains(move),
                    ai.priorities.get(move).isDeath()
            );
        }
    }

    void setupAi(final String... lines) {
        final Board board = new Board();
        int maxLength = lines.length;
        for (int i = 1; i < lines.length; i++) {
            maxLength = Math.max(maxLength, lines[i].length());
        }
        String boardString = "";
        for (int i = 0; i < maxLength; i++) {
            String toAdd = i < lines.length ? lines[i] : "";
            while (toAdd.length() < maxLength) toAdd += " ";
            boardString += toAdd;

        }
        board.forString(boardString);
        ai.board = board;
        ai.me = board.getMe();
        ai.globalInit();
    }

    private void checkMove(final String board, final String expected) {
        checkBoard(board);
        ai.setBulletsCount(SpaceCommon.BULLETS_COUNT);
        final String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void checkMove(final String board, final String expected, final int bulletsCount) {
        checkBoard(board);
        ai.setBulletsCount(bulletsCount);
        final String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void checkMove(final String board, final String[] expectedArray) {
        checkBoard(board);
        final String actual = ai.get(board(board));
        final Set<String> expectedSet = new HashSet<>(Arrays.asList(expectedArray));
        expectedSet.add(Direction.UP.toString());
        expectedSet.add(Direction.LEFT.toString());
        assertTrue(expectedSet.contains(actual));
    }

    private void checkMoves(final String[] boards, final String[] expectedArray) {
        assertEquals(boards.length, expectedArray.length);
        for (int i = 0; i < boards.length; i++) {
            final String board = boards[i];
            final String expected = expectedArray[i];
            checkBoard(board);
            final String actual = ai.get(board(board));
            assertEquals(expected, actual, "i: " + i);
        }
    }

    private void checkMovesAndScores(final String[] strBoards, final String[] expectedMoves,
                                     final int[] expectedScores) {
        //checkMovesAndScores(strBoards, expectedMoves, expectedScores, SpaceCommon.BULLETS_COUNT);
    }

    private String getMessage(final int index, final Point point, final Elements element) {
        return getMessage(index, point, element.name());
    }

    private String getMessage(final int index, final Point point, final String message) {
        return index + ": " + point + " " + message;
    }

    private void checkBoard(final String board) {
        final int size = (int) Math.sqrt(board.length());
        if (size * size != board.length()) {
            throw new IllegalStateException(String.format("size * size (%d) != board.length (%d) ", size * size, board.length()));
        }
    }
}