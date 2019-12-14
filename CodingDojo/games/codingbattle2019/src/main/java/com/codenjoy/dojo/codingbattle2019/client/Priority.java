package com.codenjoy.dojo.codingbattle2019.client;

public class Priority {
    @Override
    public String toString() {
        if (isImpossible()) return "impossible";
        if (isDeath()) return "death";
        return "default";
    }

    boolean otherHero = false;
    boolean impossible = false;
    boolean isDeath = false;

    public boolean isDeath() {
        return isDeath;
    }

    public boolean isImpossible() {
        return impossible || otherHero;
    }
}
