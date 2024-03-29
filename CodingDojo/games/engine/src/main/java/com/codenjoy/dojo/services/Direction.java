package com.codenjoy.dojo.services;

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


import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Имплементит возможные направления движения чего либо
 */
public enum Direction {
    LEFT(0, -1, 0),
    RIGHT(1, 1, 0),
    UP(2, 0, 1),
    DOWN(3, 0, -1),
    UP_LEFT(4, -1, 1),
    UP_RIGHT(5, 1, 1),
    DOWN_LEFT(6, -1, -1),
    DOWN_RIGHT(7, 1, -1),
    ACT(8, 0, 0),
    STOP(9, 0, 0);

    private final int value;
    private final int dx;
    private final int dy;

    Direction(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
    }

    public static List<Direction> getValues() {
        return Arrays.asList(LEFT, UP_LEFT, UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT);
    }

    public static List<Direction> getMoves() {
        return Arrays.asList(LEFT, UP_LEFT, UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, STOP);
    }

    public static Direction valueOf(int i) {
        for (Direction d : Direction.values()) {
            if (d.value == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Direction for " + i);
    }

    /**
     * @param x Given point.x.
     * @return New point.x that will be after move from current point.x in given direction.
     */
    public int changeX(int x) {
        return x + dx;
    }

    /**
     * @param y Given point.y
     * @return New point.y that will be after move from current point.y in given direction.
     */
    public int changeY(int y) {
        return y - dy;
    }

    /**
     * @param point Current point.
     * @return New point that will be after move from current point in given direction.
     */
    public Point change(Point point) {
        return pt(changeX(point.getX()),
                changeY(point.getY()));
    }

    /**
     * @return Value of this direction.
     */
    public int value() {
        return value;
    }

    public String toString() {
        return this.name();
    }

    /**
     * @return Inverted direction. Inverts UP to DOWN, RIGHT to LEFT, etc.
     */
    public Direction inverted() {
        switch (this) {
            case UP : return DOWN;
            case DOWN : return UP;
            case LEFT : return RIGHT;
            case RIGHT : return LEFT;
            case UP_LEFT: return DOWN_RIGHT;
            case DOWN_RIGHT: return UP_LEFT;
            case UP_RIGHT: return DOWN_LEFT;
            case DOWN_LEFT: return UP_RIGHT;
        }
        throw new IllegalArgumentException("Unsupported direction");
    }

    /**
     * @return Random direction.
     */
    public static Direction random() {
        return random(new RandomDice());
    }

    /**
     * @param dice Given dice.
     * @return Random direction for given dice.
     */
    public static Direction random(Dice dice) {
        return Direction.valueOf(dice.next(8));
    }

    /**
     * @return Next clockwise direction.
     * LEFT -> UP_LEFT -> UP -> UP_RIGHT -> RIGHT ->
     * DOWN_RIGHT -> DOWN -> DOWN_LEFT -> LEFT.
     */
    public Direction clockwise() {
        switch (this) {
            case LEFT: return UP_LEFT;
            case UP_LEFT: return UP;
            case UP: return UP_RIGHT;
            case UP_RIGHT: return RIGHT;
            case RIGHT: return DOWN_RIGHT;
            case DOWN_RIGHT: return DOWN;
            case DOWN: return DOWN_LEFT;
            case DOWN_LEFT: return LEFT;
        }
        throw new IllegalArgumentException("Cant clockwise for: " + this);
    }

    /**
     * @param parameters Given parameters.
     * @return ACT with parameters
     */
    public static String ACT(int... parameters) {
        String s = Arrays.toString(parameters).replaceAll("[\\[\\] ]", "");
        return ACT.toString() + "(" + s + ")";
    }

    /**
     * @param before true if direction should be before.
     * @return ACT with current Direction.
     */
    public String ACT(boolean before) {
        if (before) {
            return "ACT," + toString();
        } else {
            return toString() + ",ACT";
        }
    }

    public static List<Direction> onlyDirections() {
        return Arrays.asList(LEFT, RIGHT, UP, DOWN);
    }
}
