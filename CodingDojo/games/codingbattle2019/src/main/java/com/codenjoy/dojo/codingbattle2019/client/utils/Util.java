package com.codenjoy.dojo.codingbattle2019.client.utils;

import com.codenjoy.dojo.codingbattle2019.client.Board;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public class Util {
    /**
     * Метод не должен быбрасывать исключений.
     */
    public static void processThrowable(final String label, final Board board, final Throwable throwable) {
        try {
            System.out.println(label);
            throwable.printStackTrace();
            printBoard(board);
        } catch (final Throwable throwable1) {
            System.out.println("processThrowable():");
            throwable1.printStackTrace();
        }
    }

    /**
     * Метод не должен быбрасывать исключений.
     */
    public static void printBoard(final Board board) {
        if (board != null) {
            try {
                System.out.println("[\n" + board.boardAsString() + "]");
            } catch (final Throwable throwable) {
                System.out.println("board has broken");
            }
        } else {
            System.out.println("board is null");
        }
    }

    /**
     * Метод не должен быбрасывать исключений.
     */
    public static void printTime(final String label, final long startTime) {
        System.out.println(String.format("Time, ms: %d (%s)", System.currentTimeMillis() - startTime, label));
    }

    public static boolean isGameOver(final Board board, final Point me) {
        return board.isGameOver(me);
    }

    public static int distance(final Point point1, final Point point2) {
        return Math.max(Math.abs(point1.getX() - point2.getX()), Math.abs(point1.getY() - point2.getY()));
    }

    static int getMinDistanceBySquare(final List<? extends Point> points, final Point from) {
        final Point nearest = getNearestBySquare(points, from);
        return distance(from, nearest);
    }

    public static Point getNearestBySquare(final List<? extends Point> points, final Point from) {
        if (points.isEmpty()) {
            throw new UnsupportedOperationException("getNearestBySquare(): points is empty");
        }
        Point nearest = points.get(0);
        for (final Point point : points) {
            if (distance(from, nearest) > distance(from, point)) {
                nearest = point;
            }
        }
        return nearest;
    }

    /**
     * Получить расстояние от заданной точки from до ближайшей точки из списка points, при условии, что в списке
     * otherPoints нет точки, которая ближе, чем from.
     * Если такой точки найти не удается, то возвращаем бесконечность.
     *
     * @return расстояние, или (если противники опережают нас для любого bullet pack) {@link SpaceCommon#INFINITY}.
     */
    static int getMinDistanceBySquareByOtherPoints(final List<Point> points, final Point from,
                                                   final List<? extends Point> otherPoints) {
        final Point nearest = getNearestBySquareByOtherPoints(points, from, otherPoints);
        if (nearest == null) {
            return SpaceCommon.INFINITY;
        }
        return distance(from, nearest);
    }

    /**
     * @return {code null}, если противники опережают нас для любого bullet pack.
     */
    private static Point getNearestBySquareByOtherPoints(final List<Point> points, final Point from,
                                                         final List<? extends Point> otherPoints) {
        if (otherPoints.isEmpty()) {
            return getNearestBySquare(points, from);
        }
        Point nearest = null;
        for (final Point point : points) {
            final int minDistanceToOtherPoint = getMinDistanceBySquare(otherPoints, point);
            if (minDistanceToOtherPoint < distance(from, point)) {
                continue;
            }
            if (nearest == null) {
                nearest = point;
                continue;
            }
            if (distance(from, nearest) > distance(from, point)) {
                nearest = point;
            }
        }
        return nearest;
    }

    public static Direction getDirectionWithoutAct(final String str) {
        for (final Direction direction : Direction.values()) {
            if (direction == Direction.ACT) {
                continue;
            }
            if (str.toUpperCase().contains(direction.toString().toUpperCase())) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Direction have not found, str: " + str);
    }

    public static boolean hasAct(final String str) {
        return str.toUpperCase().contains(Direction.ACT.toString().toUpperCase());
    }
}