package com.codenjoy.dojo.codingbattle2019.client;

import com.codenjoy.dojo.services.Dice;

import java.security.SecureRandom;

public class SeedRandomDice implements Dice {
    private final SecureRandom random;

    public SeedRandomDice(final long seed) {
        random = new SecureRandom(String.valueOf(seed).getBytes());
    }

    /**
     * Simulates a dice throwing.
     *
     * @param n number of dice faces
     * @return integer between 0 (inclusively) and n (exclusively)
     */
    @Override
    public int next(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Dice should have a positive number of faces");
        }
        return random.nextInt(n);
    }
}
