package com.travl.guide.ui;

import java.util.Scanner;

//Artificial intelligence by Pereved created on 01.03.2019.
public class Artificial_Intelligence {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Привет, как тебя зовут?");
        String name = scanner.nextLine();
        System.out.println("Как твои дела, " + name + "?");
        String job = scanner.nextLine();
        System.out.println(job + " когда дела " + job + ", " + name);
    }
}
