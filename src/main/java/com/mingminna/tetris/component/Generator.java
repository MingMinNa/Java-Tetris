package com.mingminna.tetris.component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;


public class Generator {

    private static final int QUEUE_SIZE = 5;

    private final List<TetrominoType> bag = new ArrayList<>();
    private final Deque<TetrominoType> nextQueue = new ArrayDeque<>();

    public Generator() 
    {
        ensureQueue();
    }

    public void reset() 
    {
        bag.clear();
        nextQueue.clear();
        ensureQueue();
    }

    private void refillBag() 
    {
        List<TetrominoType> list = new ArrayList<>(List.of(TetrominoType.values()));
        Collections.shuffle(list);
        bag.addAll(list);
    }

    private TetrominoType pollBag() 
    {
        if (bag.isEmpty()) refillBag();
        return bag.remove(0);
    }

    private void ensureQueue() 
    {
        while (nextQueue.size() < QUEUE_SIZE) {
            nextQueue.addLast(pollBag());
        }
    }

    public TetrominoType next() 
    {
        ensureQueue();
        TetrominoType type = nextQueue.pollFirst();
        ensureQueue();
        return type;
    }

    public Deque<TetrominoType> getNextQueue() 
    {
        return nextQueue;
    }
}