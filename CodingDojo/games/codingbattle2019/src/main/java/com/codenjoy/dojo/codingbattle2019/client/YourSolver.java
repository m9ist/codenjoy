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

import com.codenjoy.dojo.codingbattle2019.model.Elements;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.List;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) {
            return "";
        }
        if (hasBoolets()) {
            // ищем цель
            return actionWithBoolets();
        }
        // ищем ближайшие пули
        return moveToBoolet().toString();
    }

    private Point boolet = null;

    private Point getNearestBoolet() {
        if (boolet != null) return boolet;
        List<Point> boolets = board.get(Elements.BULLET_PACK);
        return boolet = boolets.get((int) (boolets.size() * Math.random()));
    }

    private Direction moveToBoolet() {
        Point nearestBoolet = getNearestBoolet();
        Point me = board.getMe();
        if (me.getX() < nearestBoolet.getX()) return Direction.RIGHT;
        if (me.getX() > nearestBoolet.getX()) return Direction.LEFT;
        if (me.getY() > nearestBoolet.getY()) return Direction.DOWN;
        if (me.getY() < nearestBoolet.getY()) return Direction.UP;
        throw new IllegalStateException("At bullet pack");
    }

    private String actionWithBoolets() {
        if (hasSomethingOnTop(board)) {
            if (isLookTop(board)) return Direction.STOP.ACT(true);
            return Direction.UP.ACT(false);
        }

        return Math.random() < 0.5
                ? Direction.RIGHT.toString()
                : Direction.LEFT.toString();
    }

    private boolean hasBoolets() {
        return false;
    }

    private boolean isLookTop(Board board) {
        return board.isAt(board.getMe().getX(), board.getMe().getY(), Elements.HERO_UP);
    }

    private boolean hasSomethingOnTop(Board board) {
        Point me = board.getMe();
        for (int i = me.getY() + 1; i < board.size(); i++) {
            if (board.isAt(me.getX(), i, Elements.OTHER_HERO, Elements.STONE, Elements.GOLD)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://192.168.1.150:8080/codenjoy-contest/board/player/demo3@codenjoy.com?code=6904781535232807865",
                new YourSolver(new RandomDice()),
                new Board());
    }
}
