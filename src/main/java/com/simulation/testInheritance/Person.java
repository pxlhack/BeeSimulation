package com.simulation.testInheritance;

public abstract class Person {
    protected static int lifetime;
}

class Child extends Person {
    public static void main(String[] args) {
        Child.lifetime = 10; // обращение к унаследованному полю lifetime
        System.out.println(lifetime); // 10
    }
}