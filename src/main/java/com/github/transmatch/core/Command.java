package com.github.transmatch.core;

import java.util.concurrent.Callable;

public class Command implements Comparable<Command>, Callable<Boolean> {

    private Category category;

    @Override
    public int compareTo(Command o) {
        return category.compareTo(o.category);
    }

    @Override
    public Boolean call() throws Exception {
        return null;
    }

    enum Category {
        CANCEL,MARKET,LIMIT,STOP;
    }
}
