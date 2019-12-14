package com.codenjoy.dojo.codingbattle2019.launcher;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.codingbattle2019.client.Board;
import com.codenjoy.dojo.codingbattle2019.client.ai.AI3Solver;
import com.codenjoy.dojo.services.RandomDice;

/**
 * DryRunGame.
 */
public class DryRunGame {

    public static void main(String[] args) {
        LocalGameRunner.run(new SmallGameRunner(),
                //new KeyboardSolver(),
                new AI3Solver(new RandomDice()),
                new Board());
    }
}
