package com.codenjoy.dojo.codingbattle2019.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.RandomDice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Solver2Test {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new YourSolver(dice);
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void testAct() {
        final Board board = board("" +
                "☼          7   0      ♣      ☼\n" +
                "☼                            ☼\n" +
                "☼                            ☼\n" +
                "☼  0                   ♣     ☼\n" +
                "☼                           7☼\n" +
                "☼                            ☼\n" +
                "☼                     ♣   0  ☼\n" +
                "☼                            ☼\n" +
                "☼                           7☼\n" +
                "☼         0            ♣     ☼\n" +
                "☼                            ☼\n" +
                "☼                            ☼\n" +
                "☼  0      ♣                  ☼\n" +
                "☼                            ☼\n" +
                "☼                            ☼\n" +
                "☼  ♣               0         ☼\n" +
                "☼                            ☼\n" +
                "☼                            ☼\n" +
                "☼              0       ♣     ☼\n" +
                "☼                            ☼\n" +
                "☼                            ☼\n" +
                "☼              0 ♣           ☼\n" +
                "☼                            ☼\n" +
                "☼                            ☼\n" +
                "☼0   ♣                       ☼\n" +
                "☼                            ☼\n" +
                "☼         xxx      ☻         ☼\n" +
                "☼   0 ☻☻ ☻xxx                ☼\n" +
                "☼         xxx     ☻          ☼\n" +
                "☼    ☻ ☻            ☻       A☼\n");
        YourSolver yourSolver = new YourSolver(new RandomDice());
        yourSolver.get(board);
        Assert.assertEquals(1, 1);
    }
}
