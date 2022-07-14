package com.github.transmatch.engine;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.github.transmatch.core.PreMatchable;
import com.github.transmatch.entity.Order;
import com.github.transmatch.service.EventService;

public class PriorityEngine extends AbstractEngine implements PreMatchable {

    private ExecutorService executorService;

    public PriorityEngine(String currencyPair){
        super(currencyPair);
    }

    @Override
    @PostConstruct
    public void start() {
        if (executorService != null && !executorService.isShutdown())
            throw new IllegalStateException();
        executorService = new ThreadPoolExecutor(1, 1,
                        0L, TimeUnit.MILLISECONDS,
                        new PriorityBlockingQueue<Runnable>()){

            @Override
            public void finalize(){
                shutdown();
            }
        };
        super.start();
    }

    @Override
    @PreDestroy
    public void stop() {
        executorService.shutdown();
        super.stop();
    }


    @Override
    public boolean newOrder(Order order) {
        return false;
    }

    @Override
    public boolean cancelOrder(String orderId, Order.Side side) {
        return false;
    }

    @Override
    public boolean cancelOrder(Order order) {
        return false;
    }

    @Override
    public Collection<Order> getBidQueue() {
        return null;
    }

    @Override
    public Collection<Order> getAskQueue() {
        return null;
    }

    @Override
    public EventService getEventService() {
        return null;
    }

    @Override
    public String preMatch(Order order) {
        return null;
    }
}
