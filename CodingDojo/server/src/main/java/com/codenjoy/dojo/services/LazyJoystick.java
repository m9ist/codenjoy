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


import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Когда пользователь зарегистрировался в игре создается новая игра в движке и джойстик игрока где-то там сохраняется во фреймворке.
 * Часто джойстик неразрывно связан с героем игрока, который бегает по полю. Так вот если этот герой помрет, и на его место появится новый
 * надо как-то вот тот изначально сохраненный в недрах фреймворка джойстик обновить. Приходится дергать постоянно game.
 * Кроме того из за ассинхронности ответов пришлось буфферизировать в этом джойстике ответ клиента, а по tick() передавать сохраненное
 * значение сервису.
 */
public class LazyJoystick implements Joystick, Tickable {

    private final Game game;

    private List<Consumer<Joystick>> commands = new CopyOnWriteArrayList<>();
    private List<String> list = new CopyOnWriteArrayList<>();

    public LazyJoystick(Game game) {
        this.game = game;
    }

    @Override
    public void down() {
        commands.add(Joystick::down);
        list.add("DOWN");
    }

    @Override
    public void up() {
        commands.add(Joystick::up);
        list.add("UP");
    }

    @Override
    public void left() {
        commands.add(Joystick::left);
        list.add("LEFT");
    }

    @Override
    public void right() {
        commands.add(Joystick::right);
        list.add("RIGHT");
    }

    @Override
    public void upLeft() {
        commands.add(Joystick::upLeft);
        list.add("UP_LEFT");
    }

    @Override
    public void upRight() {
        commands.add(Joystick::upRight);
        list.add("UP_RIGHT");
    }

    @Override
    public void downLeft() {
        commands.add(Joystick::downLeft);
        list.add("DOWN_LEFT");
    }

    @Override
    public void downRight() {
        commands.add(Joystick::downRight);
        list.add("DOWN_RIGHT");
    }

    @Override
    public void act(int... parameters) {
        if (parameters == null) {
            return;
        }
        commands.add(joystick -> joystick.act(parameters));
        list.add(String.format("ACT%s", Arrays.toString(parameters)));
    }

    @Override
    public void message(String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        commands.add(joystick -> joystick.message(message));
        list.add(String.format("MESSAGE('%s')", message));
    }

    @Override
    public synchronized void tick() {
        Joystick joystick = game.getJoystick();

        commands.forEach(command -> command.accept(joystick));

        commands.clear();
    }

    public synchronized String popLastCommands() {
        String result = list.toString();
        list.clear();
        return result;
    }
}
