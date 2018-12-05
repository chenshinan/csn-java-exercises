package com.chenshinan.exercises.thread;

/**
 * join的作用，使线程从并行变为串行，join(10)的作用，使线程从并行变为串行10毫秒后又变为并行
 *
 * @author shinan.chen
 * @since 2018/12/5
 */
public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
//        Thread t1 = new Thread(new ThreadA("线程一"));
//        Thread t2 = new Thread(new ThreadA("线程二"));
//        t1.start();
//        t1.join();
//        t2.start();

        Thread t1 = new Thread(new ThreadA("线程一"));
        Thread t2 = new Thread(new ThreadA("线程二"));
        t1.start();
        t1.join(10);
        t2.start();
    }

    static class ThreadA implements Runnable {
        private String name;

        public ThreadA(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                System.out.println(name + ":" + i);
            }
        }
    }
}
