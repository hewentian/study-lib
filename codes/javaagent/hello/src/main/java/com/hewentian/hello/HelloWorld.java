package com.hewentian.hello;

import java.util.Date;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("---------- hello main start ----------");

        sayHello();

        for (int i = 0; i < 20; i++) {
            travelWorld("Tim");

            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("---------- hello main end ----------");
    }

    public static void sayHello() {
        System.out.println("sayHello() ----------");
    }

    public static void travelWorld(String name) {
        System.out.println(name + " travelWorld() ---------- " + new Date());
    }

}
