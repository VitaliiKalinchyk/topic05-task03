package com.epam.rd.java.basic.topic05.task03;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {

    private final int numberOfThreads;

    private final int numberOfIterations;

    private final int pause;

    private int c1;

    private int c2;

    private AtomicInteger count;

    public Task(int numberOfThreads, int numberOfIterations, int pause) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfIterations = numberOfIterations;
        this.pause = pause;
    }

    public void compare() {
        count = new AtomicInteger(numberOfThreads);
        c1 = 0;
        c2 = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(this::counting).start();
        }
        while (count.intValue() > 0) {
            Thread.onSpinWait();
        }
    }

    public void compareSync() {
        count = new AtomicInteger(numberOfThreads);
        c1 = 0;
        c2 = 0;
        for (int i = 0; i < numberOfThreads; i++) {
			new Thread(() -> {
				synchronized (this) {
					counting();
				}
			}).start();
        }
        while (count.intValue() > 0) {
            Thread.onSpinWait();
        }
    }

    private void counting() {
        for (int j = 0; j < numberOfIterations; j++) {
            c1++;
            try {
                TimeUnit.MILLISECONDS.sleep(pause);
            } catch (InterruptedException ignored) {
            }
            c2++;
            System.out.println((c1 == c2) + " " + c1 + " " + c2);
        }
        count.decrementAndGet();
    }

    public static void main(String[] args) {
        Task t = new Task(2, 5, 10);
        t.compare();
        System.out.println("~~~");
        t.compareSync();
    }
}