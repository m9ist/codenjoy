package com.codenjoy.dojo.codingbattle2019.model;

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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodingBattle2019Test {

    private CodingBattle2019 game;
    private BulletCharger charger = getBulletCharger();
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void diceNew(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));

        if(ints.length == 0){ // we work just with nothing
            when = when.thenReturn(-1);
        }

        if(ints.length == 1){ // we work just with stones
            when = when.thenReturn(-1, -1, -1, -1, ints[0], -1);
        }

        if(ints.length == 2){ // we work with stones and bombs
            when = when.thenReturn(-1, -1, -1, -1, ints[0], ints[1], -1);
        }

        if(ints.length == 4){ // we work stones, bombs and bulletPacks
            when = when.thenReturn(ints[2], ints[3], ints[0], ints[1], -1);
        }
    }



    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero(charger).get(0);

        game = new CodingBattle2019(level, dice,
                charger.getTicksToRecharge(),
                charger.getBulletsCount());
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    private void newBulletPackForHeroWithGivenBullets(int i) {
        int ticksToRecharge = 1000;
        int bulletsCount = i;
        charger = new BulletCharger(ticksToRecharge, bulletsCount);
    }

    @Test
    public void shouldNoBulletsAfterFireWithEmptyBulletCharger() {

        newBulletPackForHeroWithGivenBullets(10);
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        diceNew(-1, -1, -1, -1);

        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        diceNew(-1, -1, 1, 0);
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ 7 ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        hero.up();
        game.tick();
        hero.recharge();

        //then
        assertE("☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.act();
        game.tick();

        //then
        assertE("☼ * ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.act();
        game.tick();

        //then
        assertE("☼ * ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        hero.act();
        game.tick();

        //then
        assertE("☼ * ☼" + //TODO не присваивается новый BulletCharger
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        for (int i = 0; i < 8; i++) {
            hero.act();
            game.tick();
        }

        //Given
        assertE("☼   ☼" +   // todo по подстетам пуля должна быть (последняя), говорит, что нету
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        hero.act();
        game.tick();

        //Given
        assertE("☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
    }



    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");

        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    // я могу двигаться
    @Test
    public void shouldFieldICanMove() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");
        //when
        diceNew(); // выключаем генерацию каменей и мин
        hero.up();
        game.tick();

        //then
        assertE("☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.right();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼  >☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.down();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼  V☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.left();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ < ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        hero.upRight();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼  ➚☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        hero.downLeft();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ ↙ ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        hero.upLeft();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼↖  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        hero.downRight();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ ➘ ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    // появляется новый комень
    @Test
    public void shouldNewStone() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼A  ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        diceNew(1);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼ 0 ☼" +
                "☼   ☼" +
                "☼A  ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        diceNew(2);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼  0☼" +
                "☼   ☼" +
                "☼A  ☼" +
                "☼ 0 ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldStoneMove() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");

        //When
        diceNew(0);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼0  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");
        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼0  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");
    }

    @Test
    public void shouldStoneAppearsEvery3seconds() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");

        //When
        diceNew(1); // камень в первой колонке, мины нет, камень во второй колонке
        game.tick();
        game.tick();
        game.tick();
        diceNew(2);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼  0☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ 0 ☼" +
                "☼ A ☼");
    }

    @Test
    public void shouldHeroShoot() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");

        //When
        diceNew();
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldBulletOutOfTheBoard() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        diceNew();
        hero.recharge();
        hero.act();
        game.tick();


        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");

        game.tick();
        game.tick();

           //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldStoneIsDestroyedByBullet() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");
        //When
        diceNew(1);
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼ 0 ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");

        //When
        game.tick();

        //Then
        assertE("☼ x ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");

    }

    @Test
    public void shouldStoneIsDestroyedByBullet2() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");

        //When
        diceNew(1);
        hero.recharge();
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ A ☼" +
                "☼   ☼");

        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");

        //When
        game.tick();

        //Then
        assertE("☼ x ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");

        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");

    }

    // проявляем новую мину
    @Test
    public void shouldNewBomb() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        diceNew(-1, 1); // камень не появляем, мину появляем, тоже на 3-м тике
        game.tick();
        game.tick();
        game.tick();


        //then
        assertE("☼ ♣ ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldNewBombAtRandomPlace() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼  A☼");

        //when
        diceNew(-1, 0);
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼♣  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼  A☼");

        //when
        diceNew(-1, 2);
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼  ♣☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼♣  ☼" +
                "☼  A☼");
    }

    @Test
    public void shouldNewBombAndNewStoneAtNewPlace() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");

        //when
        diceNew(0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼   ☼" +
                "☼0 ♣☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldBombRemovedWhenOutsideBorder() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼A  ☼");

        //When
        diceNew(-1, 2);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼  ♣☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼A  ☼");

        //When
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼A ♣☼");

        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼A  ☼");
    }

    @Test
    public void shouldBombDestroyedByBullet() {
        // given

        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");


        diceNew(-1, 1);
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();
        game.tick();


        assertE("☼ ♣ ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");

        game.tick();

        assertE("☼xxx☼" +
                "☼xxx☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼");
    }

    @Test
    public void shouldBombDestroyedByBullet2() {
        // given

        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");


        diceNew(-1, 1);
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();


        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");

        game.tick();

        assertE("☼xxx☼" +
                "☼xxx☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");

        game.tick();

        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ A ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldNewBombAndNewStoneAtNewPlace2() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        diceNew(1, 3);
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼ 0 ♣☼" +
                "☼    ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldBombDestroyedByBulletNew() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼");

        diceNew(0, 2);
        hero.recharge();
        game.tick();
        game.tick();
        hero.act();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼  * ☼" +
                "☼    ☼" +
                "☼  A ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼" +
                "☼  A ☼");

    }

    @Test
    public void shouldBombDestroyHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        diceNew(0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldBombDestroyHeroAndResurrectionHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        diceNew(0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼0   ☼");
    }

    @Test
    public void shouldBombDestroyHeroRightAndResurrectHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        diceNew(0, 3);

        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼0  ♣☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼  xxx" +
                "☼0 xxx" +
                "☼  xxx" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼0   ☼");
    }

    @Test
    public void shouldStoneDestroyHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        diceNew(2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  0 ☼" +
                "☼  A ☼" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  + ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldBulletChargerOnField() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

        diceNew(-1, -1, 1, 1);
        game.tick();
        game.tick();

        // then
        assertE("☼ 7  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  A ☼" +
                "☼    ☼");

    }

    @Test
    public void shouldHeroPickUpBulletPack() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        diceNew(-1, -1, 1, 1);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼ 7  ☼" +
                "☼    ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        hero.up();
        game.tick();

        assertE("☼ 7  ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        hero.up();
        game.tick();

        assertE("☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldNewBulletPackAfterHeroGetOldBulletPack() {
        // given
        givenFl("☼    ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        diceNew(-1, -1, 0, 1);
        game.tick();

        // then
        assertE("☼7   ☼" +
                "☼ A  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        hero.left();
        game.tick();

        // then
        assertE("☼7   ☼" +
                "☼<   ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        hero.up();
        game.tick();
        diceNew(-1, -1, 3, 0);
        game.tick();

        // then
        assertE("☼A   ☼" +
                "☼   7☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldHeroShootAfterRecharge() {
        //Given
        givenFl("☼   ☼" +
                "☼ A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        diceNew(-1, -1, 0, 0);
        game.tick();

        //Given
        assertE("☼   ☼" +
                "☼7A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        hero.act();
        game.tick();

        //Given
        assertE("☼   ☼" +
                "☼7A ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        hero.left();
        game.tick();

        //Given
        assertE("☼   ☼" +
                "☼<  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

      //When
        hero.act();
        game.tick();

        //Then
        assertE("☼*  ☼" +
                "☼<  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    public static BulletCharger getBulletCharger() {
        return new BulletCharger(100, 1);
    }
}



    // появление на поле магазина патронов
    // итераторы
    // инструкция
    // золото/здоровье
    // лазер
    // выстрелы не каждый тик (?)
    // плюшка, которая позволяет стрелать каждый тик, пока она действует (время или количество снарядов)
    // узнать причину загадочного ексепшна при вылете игры и исправить
    // написать нормального бота

/*
    некорорые тесты сломались из-за акт(когда есть возможность выстрела - сказать, что нет патронов)
    если есть пули - более агрессивная политика
    если падает сверху мина - считать, куда уклоняться (сейчас вправо)
    как можно меньше времени проводить в самых верхних рядах
    уклонения от пуль
    стрелять в других игроков
    потестить все позиции мин рядом
    если меня убивают - вылетает эксепшин (вроде пофиксил)
    проверить баг полета навстречу камню
    вынести в настройки кол-во пэков
    переделать дайс и все тесты
    рефакторинг!!!!

    */

