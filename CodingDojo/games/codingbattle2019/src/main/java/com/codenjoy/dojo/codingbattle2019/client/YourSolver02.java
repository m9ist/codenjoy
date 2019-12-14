package com.codenjoy.dojo.codingbattle2019.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.codingbattle2019.client.utils.SpaceCommon;
import com.codenjoy.dojo.codingbattle2019.client.utils.Util;
import com.codenjoy.dojo.codingbattle2019.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class YourSolver02 implements Solver<Board> {

    private final Dice dice;
    protected Board board;
    protected Point me;
    private int bulletsCount = 0;
    private Point bulletPack;
    private List<Point> bombs;
    private List<Point> stones;
    private List<Point> otherHeroes;
    private boolean needActBefore = false;
    private final boolean[] isDirEnabled = new boolean[8];
    private String lastComputedAnswer = null;
    protected final HashMap<Direction, Priority> priorities = new HashMap<>();

    protected static final int BULLETS_COUNT = 10;

    public YourSolver02(final Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(final Board board) {
        final long startTime = System.currentTimeMillis();
        try {
            if (board == null || ("nu\n" + "ll").equals(board.boardAsString().trim())) { // Это безобразие, конечно.
                Util.printBoard(board);
                if (lastComputedAnswer != null) {
                    // Кажется, что это лучше, чем кидать random, последовательности одинаковых ходов бывают часто.
                    // Но bullet зазря не будем тратить.
                    return Util.getDirectionWithoutAct(lastComputedAnswer).toString();
                } else {
                    return getRandom();
                }
            }
            this.board = board;
            me = board.getMe();
            if (me == null) return getRandom();
            if (Util.isGameOver(board, me)) {
                clear();
                return "";
            }
            globalInit();
            lastComputedAnswer = getMove01();
            return lastComputedAnswer;
        } catch (final Throwable throwable) {
            Util.processThrowable(getClass().getSimpleName(), board, throwable);
            if (SpaceCommon.IS_BATTLE_MODE) {
                return getRandom();
            } else {
                throw throwable;
            }
        } finally {
            Util.printTime(getClass().getSimpleName(), startTime);
        }
    }

    protected void globalInit() {
        initMove();
        findItems();
        checkBorders();
        computeFlags();
    }

    protected String getMove01() {
        final Direction dirToBulletPack = getDirToBulletPack();
        if (dirToBulletPack != null) {
            if (bulletsCount > 0) {
                if (needActBefore) {
                    bulletsCount--;
                    return dirToBulletPack.ACT(true);
                } else {
                    return dirToBulletPack.toString();
                }
            } else {
                return dirToBulletPack.ACT(true);
            }
        }

        return getRandomSave();
    }

    protected void checkBorders() {
        for (int i = 0; i < isDirEnabled.length; i++) {
            final Direction dir = Direction.values()[i];
            final Point nextPoint = dir.change(me);
            if (!isPointValid(nextPoint)
                    || otherHeroes.contains(nextPoint)) {
                isDirEnabled[dir.ordinal()] = false;
                priorities.get(dir).impossible = !isPointValid(nextPoint);
                priorities.get(dir).otherHero = otherHeroes.contains(nextPoint);
            }
        }
    }

    protected void computeFlags() {
        computeEnabledBySaveDir();
        computeEscapeBombDeltaX0and1();
        computeEscapeBombDiagDeltaX0and1();
        computeEscapeStone();
        computeEscapeBombDeltaX2();
        computePatterns();
        processOtherHeroes();
        processActForNearBulletPack();
        processBorders();
    }

    private void computeEnabledBySaveDir() {
        for (Direction direction : Direction.getMoves()) {
            Priority priority = priorities.get(direction);
            if (!priority.isImpossible()) {
                if (bombedIfMoveThisPoint(direction.change(me))) {
                    priority.isBombed = true;
                    continue;
                }
                if (!isSaveDir(direction)) {
                    priority.isDeath = true;
                }
            }
        }
        if (!isSaveDir(Direction.LEFT)) {
            disableLeft();
        }
        if (!isSaveDir(Direction.RIGHT)) {
            disableRight();
        }
        if (!isSaveDir(Direction.UP)) {
            disableUp();
        }
        if (!isSaveDir(Direction.DOWN)) {
            disableDown();
        }
        if (!isSaveDir(Direction.UP_LEFT)) {
            disableUpLeft();
        }
        if (!isSaveDir(Direction.UP_RIGHT)) {
            disableUpRight();
        }
        if (!isSaveDir(Direction.DOWN_LEFT)) {
            disableDownLeft();
        }
        if (!isSaveDir(Direction.DOWN_RIGHT)) {
            disableDownRight();
        }
    }

    private void computeEscapeBombDeltaX0and1() {
        for (final Point bomb : bombs) {
            if (me.getY() < bomb.getY() - 1) {
                continue;
            }
            if (me.getX() == bomb.getX()) {
                if (me.getY() - 2 >= bomb.getY()) {
                    if (bulletsCount > 0) {
                        needActBefore = true;
                    }
                } else if (me.getY() > bomb.getY()) {
                    disableUp();
                }
            } else if (Math.abs(me.getX() - bomb.getX()) == 1) {
                if (me.getY() >= bomb.getY() && me.getY() - 4 < bomb.getY()) {
                    disableUp();
                    if (me.getX() > bomb.getX()) {
                        disableLeft();
                    } else {
                        disableRight();
                    }
                }
            }
        }
    }

    private void computeEscapeBombDiagDeltaX0and1() {
        for (final Point bomb : bombs) {
            if (me.getY() < bomb.getY() - 1) {
                continue;
            }
            if (me.getX() == bomb.getX()) {
                if (me.getY() - 2 >= bomb.getY()) {
                    if (bulletsCount > 0) {
                        needActBefore = true;
                    }
                } else if (me.getY() > bomb.getY()) {
                    disableUp();
                }
            } else if (Math.abs(me.getX() - bomb.getX()) == 1) {
                if (me.getY() >= bomb.getY() && me.getY() - 4 < bomb.getY()) {
                    disableUp();
                    if (me.getX() > bomb.getX()) {
                        disableLeft();
                        disableUpLeft();
                    } else {
                        disableRight();
                        disableUpRight();
                    }
                }
            }
        }
    }

    private void computeEscapeBombDeltaX2() {
        for (final Point bomb : bombs) {
            if (me.getY() < bomb.getY() - 1) {
                continue;
            }
            if (Math.abs(me.getX() - bomb.getX()) == 2) {
                if (me.getY() >= bomb.getY() && me.getY() - 4 < bomb.getY()) {
                    if (me.getX() > bomb.getX()) {
                        if (!isRightEnabled() && !isUpEnabled()) {
                            disableLeft();
                        }
                    } else {
                        if (!isLeftEnabled() && !isUpEnabled()) {
                            disableRight();
                        }
                    }
                }
            }
        }
    }

    private void computePatterns() {
        computePattern01();
        computePattern02();
    }

    private void computePattern01() {
        // __b___
        // ______
        // ______
        // ___m__
        //disable left/right
        if (bulletsCount > 0) {
            return;
        }
        if (board.isAt(me.getX() - 1, me.getY() - 3, Elements.BOMB)) {
            disableLeft();
        }
        if (board.isAt(me.getX() + 1, me.getY() - 3, Elements.BOMB)) {
            disableRight();
        }
    }

    private void computePattern02() {
        // __b__
        // __b__
        // _____
        // _____
        // __m__
        //disable up
        if (bulletsCount > 0) {
            return;
        }
        if (board.isAt(me.getX(), me.getY() - 3, Elements.BOMB)
                || board.isAt(me.getX(), me.getY() - 4, Elements.BOMB)) {
            disableUp();
        }
    }

    private void computeEscapeStone() {
        for (final Point stone : stones) {
            if (me.getY() < stone.getY()) {
                continue;
            }
            if (me.getX() == stone.getX()) {
                if (bulletsCount > 1) {
                    needActBefore = true;
                } else if (Util.distance(me, bulletPack) < 3) {
                    final Point nearestBomb = getNearestBombX1Up();
                    if (nearestBomb != null && Util.distance(me, nearestBomb) > 5) {
                        needActBefore = true;
                    }
                }
            }
        }
    }

    private void processOtherHeroes() {
        for (final Point otherHero : otherHeroes) {
            if (me.getY() <= otherHero.getY()) {
                continue;
            }
            if (me.getX() == otherHero.getX() && Math.abs(me.getY() - otherHero.getY()) == 1 && bulletsCount > 0) {
                needActBefore = true;
            } else if (me.getX() == otherHero.getX() && Math.abs(me.getY() - otherHero.getY()) < 3
                    || Math.abs(me.getX() - otherHero.getX()) == 1 && Math.abs(me.getY() - otherHero.getY()) < 4) {
                if (bulletsCount > 1) {
                    needActBefore = true;
                } else if (bulletPack != null && Util.distance(me, bulletPack) < 3) {
                    final Point nearestBomb = getNearestBombX1Up();
                    if (nearestBomb != null && Util.distance(me, nearestBomb) > 5) {
                        needActBefore = true;
                    }
                }
            }
        }
    }

    private void processActForNearBulletPack() {
        if (needActBefore) {
            return;
        }
        if (bulletPack == null) {
            return;
        }
        if (me.getY() == 0) {
            return;
        }
        final int distanceToBulletPack = Util.distance(me, bulletPack);
        if (bulletsCount <= distanceToBulletPack) {
            return;
        }
        for (final Point otherHero : otherHeroes) {
            if (Util.distance(otherHero, bulletPack) <= distanceToBulletPack) {
                return;
            }
        }
        needActBefore = true;
    }

    private void processBorders() {
        if (bulletPack == null) {
            return;
        }
        final int distX = Math.abs(me.getX() - bulletPack.getX());
        if (me.getY() <= bulletPack.getY() + 1 && bulletPack.getY() <= 1 && distX > board.size() / 7
                && isDownEnabled() && (isLeftEnabled() || isRightEnabled())) {
            disableUp();
        }
    }

    private Point getNearestBombX1Up() {
        return getNearestX1Up(Elements.BOMB);
    }

    @SuppressWarnings("SameParameterValue")
    private Point getNearestX1Up(final Elements element) {
        final List<Point> elements = board.get(element);
        int minDistance = Integer.MAX_VALUE;
        Point nearest = null;
        for (final Point point : elements) {
            if (Math.abs(me.getX() - point.getX()) > 1) {
                continue;
            }
            if (me.getY() <= point.getY()) {
                continue;
            }
            final int distance = Util.distance(me, point);
            if (me.distance(point) < minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }
        return nearest;
    }

    private Direction getDirToBulletPack() {
        if (bulletPack == null) {
            if (isUpEnabled()) {
                return Direction.UP;
            }
            return null;
        }

        final int distX = Math.abs(me.getX() - bulletPack.getX());
        final int distY = Math.abs(me.getY() - bulletPack.getY());

        if (distX + distY == 0) {
            if (me.getY() < board.size() / 6) {
                return isDownEnabled() ? Direction.DOWN : null;
            } else {
                return isUpEnabled() ? Direction.UP : null;
            }
        }

        if (needStopByBulletPack(distX, distY)) {
            return Direction.STOP;
        }

        final Direction dirDiagonal = getDirToBulletPackDiagonal();
        if (dirDiagonal != null) {
            return dirDiagonal;
        }

        if (me.getY() <= bulletPack.getY() && bulletPack.getY() <= 1 && distX > board.size() / 5 && isDownEnabled()) {
            return Direction.DOWN;
        }

        final Direction dir;
        final int rand = dice.next(distX + distY);
        if (rand < distX) {
            dir = getDirToBulletPackDLRU();
        } else {
            dir = getDirToBulletPackDULR();
        }
        return dir;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean needStopByBulletPack(final int distX, final int distY) {
        if (distX + distY != 1) {
            return false;
        }
        if (distX == 0) { // distY == 1
            if (me.getY() > bulletPack.getY()) {
                if (!isUpEnabled()) {
                    if (!(board.isBulletAt(me.getX(), me.getY() - 1)
                            || board.isBombAt(me.getX() - 1, me.getY() - 2)
                            || board.isBombAt(me.getX(), me.getY() - 2)
                            || board.isBombAt(me.getX() + 1, me.getY() - 2))) {
                        return true;
                    }
                }
            } else {
                if (!isDownEnabled()) {
                    return true;
                }
            }
        } else { // distX == 1 && distY == 0
            if (me.getX() > bulletPack.getX()) {
                if (!isLeftEnabled()) {
                    return true;
                }
            } else {
                if (!isRightEnabled()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Direction getDirToBulletPackDiagonal() {
        if (isUpLeftEnabled() && me.getY() > bulletPack.getY() && me.getX() > bulletPack.getX()) {
            return Direction.UP_LEFT;
        }
        if (isUpRightEnabled() && me.getY() > bulletPack.getY() && me.getX() < bulletPack.getX()) {
            return Direction.UP_RIGHT;
        }
        if (isDownLeftEnabled() && me.getY() < bulletPack.getY() && me.getX() > bulletPack.getX()) {
            return Direction.DOWN_LEFT;
        }
        if (isDownRightEnabled() && me.getY() < bulletPack.getY() && me.getX() < bulletPack.getX()) {
            return Direction.DOWN_RIGHT;
        }
        return null;
    }

    private Direction getDirToBulletPackDLRU() {
        if (isDownEnabled() && me.getY() < bulletPack.getY()) {
            return Direction.DOWN;
        }
        final Direction dir = getDirToBulletPackLRWithRandom();
        if (dir != null) {
            return dir;
        }
        if (isUpEnabled() && me.getY() > bulletPack.getY()) {
            return Direction.UP;
        }
        return null;
    }

    private Direction getDirToBulletPackLRWithRandom() {
        final int safeDelimiter = 4 + dice.next(1);
        final double safePart = 1 / (double) safeDelimiter;
        if (me.getX() < board.size() * safePart) {
            return getDirToBulletPackRL();
        } else if (me.getX() > board.size() * (1 - safePart)) {
            return getDirToBulletPackLR();
        } else {
            return dice.next(2) == 0 ? getDirToBulletPackLR() : getDirToBulletPackRL();
        }
    }

    private Direction getDirToBulletPackDULR() {
        if (isDownEnabled() && me.getY() < bulletPack.getY()) {
            return Direction.DOWN;
        }
        if (isUpEnabled() && me.getY() > bulletPack.getY()) {
            return Direction.UP;
        }
        return getDirToBulletPackLRWithRandom();
    }

    private Direction getDirToBulletPackLR() {
        if (isLeftEnabled() && me.getX() > bulletPack.getX()) {
            return Direction.LEFT;
        }
        if (isRightEnabled() && me.getX() < bulletPack.getX()) {
            return Direction.RIGHT;
        }
        return null;
    }

    private Direction getDirToBulletPackRL() {
        if (isRightEnabled() && me.getX() < bulletPack.getX()) {
            return Direction.RIGHT;
        }
        if (isLeftEnabled() && me.getX() > bulletPack.getX()) {
            return Direction.LEFT;
        }
        return null;
    }

    protected boolean bombedIfMoveThisPoint(final Point nextPoint) {
        for (Point bomb : bombs) {
            Point nextBombPosition = Direction.DOWN.change(bomb);
            if (Math.abs(nextBombPosition.getX() - nextPoint.getX()) <= SpaceCommon.BOMB_RADIUS
                    && Math.abs(nextBombPosition.getY() - nextPoint.getY()) <= SpaceCommon.BOMB_RADIUS) {
                return true;
            }
        }
        List<Point> explosions = board.get(Elements.EXPLOSION);
        for (Point explosion : explosions) {
            if (explosion.equals(nextPoint)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isSaveDir(final Direction dirWithError) {
        final Point nextPoint = dirWithError.change(me);
        if (!isPointValid(nextPoint)) {
            return false;
        }
        final Point nextPointUp = Direction.UP.change(nextPoint);
        final Point nextPointDown = Direction.DOWN.change(nextPoint);
        if (bombedIfMoveThisPoint(nextPoint)) {
            return false;
        }

        if (board.isStoneAt(nextPointUp)
                || board.isBulletAt(nextPointDown)) {
            return false;
        }
        if (board.isAt(nextPoint, Elements.OTHER_HERO)) {
            return false;
        }
        return true;
    }

    private List<Point> getBombsWaves() {
        final List<Point> bombsWaves = new LinkedList<>();
        for (final Point bomb : bombs) {
            putBombWaves(bomb, bombsWaves);
        }
        return bombsWaves;
    }

    private void putBombWaves(final Point bomb, final List<Point> bombsWaves) {
        for (int x = bomb.getX() - SpaceCommon.BOMB_RADIUS; x <= bomb.getX() + SpaceCommon.BOMB_RADIUS; x++) {
            for (int y = bomb.getY() - SpaceCommon.BOMB_RADIUS; y <= bomb.getY() + SpaceCommon.BOMB_RADIUS; y++) {
                bombsWaves.add(pt(x, y));
            }
        }
    }

    private boolean isPointValid(final Point nextPoint) {
        return !board.isOutOfField(nextPoint)
                && !board.isBarrierAt(nextPoint);
    }

    private void findItems() {
        findBulletPack();
        bombs = board.get(Elements.BOMB);
        stones = board.get(Elements.STONE);
        otherHeroes = board.get(Elements.OTHER_HERO);
    }

    private void findBulletPack() {
        reload();
        final List<Point> bulletPacks = board.get(Elements.BULLET_PACK);
        if (bulletPacks.isEmpty()) {
            return;
        }
        if (bulletPacks.stream().anyMatch(e1 -> e1.getY() > 2)) {
            bulletPacks.removeIf(e -> e.getY() <= 2);
        }
        bulletPack = Util.getNearestBySquare(bulletPacks, me);
        reload();
    }

    private void reload() {
        if (me.equals(bulletPack)) {
            bulletsCount = BULLETS_COUNT;
        }
    }

    private String getRandomSave() {
        for (int i = 0; i < 20; i++) {
            final Direction dir = Direction.random(dice);
            if (!isDirEnabled[dir.ordinal()]) {
                continue;
            }
            if (dice.next(5) == 0) {
                if (bulletsCount > 0) {
                    bulletsCount--;
                }
                return dir.toString() + "," + Direction.ACT.toString();
            } else {
                return dir.toString();
            }
        }
        if (bulletsCount > 0) {
            bulletsCount--;
        }
        return Direction.ACT.toString();
    }

    private String getRandom() {
        if (dice.next(5) == 0) {
            if (bulletsCount > 0) {
                bulletsCount--;
            }
            return Direction.ACT.toString();
        }
        final Direction dir = Direction.random(dice);
        return dir.toString();
    }

    //    //- Блок методов BackupSolver --------------------------------------------------------------------------------------
//
//    @Override
    public void clear() {
        bulletsCount = 0;
        lastComputedAnswer = null;
    }
//
//    @Override
//    public void clearLastComputedAnswer() {
//        lastComputedAnswer = null;
//    }
//
//    @Override
//    public int getBulletsCount() {
//        return bulletsCount;
//    }
//
//    @Override
//    public void setBulletsCount(final int bulletsCount) {
//        this.bulletsCount = bulletsCount;
//    }
//
//    //------------------------------------------------------------------------------------------------------------------

    private void initMove() {
        needActBefore = false;
        Arrays.fill(isDirEnabled, true);
        priorities.clear();
        for (Direction direction : Direction.getMoves()) {
            priorities.put(direction, new Priority());
        }
    }

    private boolean isLeftEnabled() {
        return isDirEnabled[Direction.LEFT.ordinal()];
    }

    private boolean isRightEnabled() {
        return isDirEnabled[Direction.RIGHT.ordinal()];
    }

    private boolean isUpEnabled() {
        return isDirEnabled[Direction.UP.ordinal()];
    }

    private boolean isDownEnabled() {
        return isDirEnabled[Direction.DOWN.ordinal()];
    }

    private boolean isUpLeftEnabled() {
        return isDirEnabled[Direction.UP_LEFT.ordinal()];
    }

    private boolean isUpRightEnabled() {
        return isDirEnabled[Direction.UP_RIGHT.ordinal()];
    }

    private boolean isDownLeftEnabled() {
        return isDirEnabled[Direction.DOWN_LEFT.ordinal()];
    }

    private boolean isDownRightEnabled() {
        return isDirEnabled[Direction.DOWN_RIGHT.ordinal()];
    }

    private void disableLeft() {
        isDirEnabled[Direction.LEFT.ordinal()] = false;
    }

    private void disableRight() {
        isDirEnabled[Direction.RIGHT.ordinal()] = false;
    }

    private void disableUp() {
        isDirEnabled[Direction.UP.ordinal()] = false;
    }

    private void disableDown() {
        isDirEnabled[Direction.DOWN.ordinal()] = false;
    }

    private void disableUpLeft() {
        isDirEnabled[Direction.UP_LEFT.ordinal()] = false;
    }

    private void disableUpRight() {
        isDirEnabled[Direction.UP_RIGHT.ordinal()] = false;
    }

    private void disableDownLeft() {
        isDirEnabled[Direction.DOWN_LEFT.ordinal()] = false;
    }

    private void disableDownRight() {
        isDirEnabled[Direction.DOWN_RIGHT.ordinal()] = false;
    }

    public void setBulletsCount(final int bulletsCount) {
        this.bulletsCount = bulletsCount;
    }

    public static void main(final String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                //"http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789",
                "http://192.168.1.150:8080/codenjoy-contest/board/player/demo3@codenjoy.com?code=6904781535232807865",
                new YourSolver02(new RandomDice()),
                new Board());
    }
}
