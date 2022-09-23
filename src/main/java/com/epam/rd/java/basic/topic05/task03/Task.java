package com.epam.rd.java.basic.topic05.task03;

public class Task {

	private final int numberOfThreads;

	private final int numberOfIterations;

	private final int pause;

	private int c1;

	private int c2;

	public Task(int numberOfThreads, int numberOfIterations, int pause) {
		this.numberOfThreads = numberOfThreads;
		this.numberOfIterations = numberOfIterations;
		this.pause = pause;
	}

	public void compare() {
		c1 = 0;
		c2 = 0;
		Thread[] threads = new Thread[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++) {
			threads[i] = new Thread(this::counting);
			threads[i].start();
		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException ignored) {}
		}
	}

	public void compareSync() {
		c1 = 0;
		c2 = 0;
		Thread[] threads = new Thread[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++) {
			threads[i] = new Thread(() -> {
				synchronized (this) {
					counting();
				}
			});
			threads[i].start();
		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException ignored) {}
		}
	 }

	private void counting() {
		for (int j = 0; j < numberOfIterations; j++) {
			c1++;
			try {
				Thread.sleep(pause);
			} catch (InterruptedException ignored) {}
			c2++;
			System.out.println((c1 == c2) + " " + c1 + " " + c2);
		}
	}

	public static void main(String[] args) {
		Task t = new Task(2, 5, 10);
		t.compare();
		System.out.println("~~~");
		t.compareSync();
	}
}