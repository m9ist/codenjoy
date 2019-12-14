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

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.codingbattle2019.model.Elements;
import com.codenjoy.dojo.services.Point;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isOutOfField(final Point point) {
        return isOutOfField(point.getX(), point.getY());
    }

    public boolean isBarrierAt(final Point point) {
        return isBarrierAt(point.getX(), point.getY());
    }

    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, Elements.WALL);
    }

    public Point getMe() {
        final List<Point> heroPoints = get(Elements.DEAD_HERO,
                Elements.HERO,
                Elements.HERO_UP,
                Elements.HERO_UP_RIGHT,
                Elements.HERO_RIGHT,
                Elements.HERO_DOWN_RIGHT,
                Elements.HERO_DOWN,
                Elements.HERO_DOWN_LEFT,
                Elements.HERO_LEFT,
                Elements.HERO_UP_LEFT
        );
        if (!heroPoints.isEmpty()) {
            final Point point = heroPoints.get(0);
            if (point != null) {
                return point;
            }
        }
        return pt(1, 1);
    }

    public boolean isGameOver() {
        return !get(Elements.DEAD_HERO).isEmpty();
    }

    public boolean isGameOver(final Point hero) {
        if (hero == null) {
            return isGameOver();
        }
        return get(Elements.HERO).isEmpty()
                && (!get(Elements.DEAD_HERO).isEmpty()
                || isAt(hero, Elements.EXPLOSION)
                || isAt(hero.getX() - 1, hero.getY(), Elements.EXPLOSION)
                || isAt(hero.getX() + 1, hero.getY(), Elements.EXPLOSION)
                || isAt(hero.getX(), hero.getY() - 1, Elements.EXPLOSION)
                || isAt(hero.getX(), hero.getY() + 1, Elements.EXPLOSION));
    }

    public boolean isBombAt(final Point point) {
        return isBombAt(point.getX(), point.getY());
    }

    public boolean isBombAt(int x, int y) {
        return isAt(x, y, Elements.BOMB);
    }

    public boolean isStoneAt(final Point point) {
        return isStoneAt(point.getX(), point.getY());
    }

    public boolean isStoneAt(int x, int y) {
        return isAt(x, y, Elements.STONE);
    }

    public boolean isBulletAt(final Point point) {
        return isBulletAt(point.getX(), point.getY());
    }

    public boolean isBulletAt(int x, int y) {
        return isAt(x, y, Elements.BULLET);
    }
}
