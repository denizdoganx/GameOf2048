package com.denizdogan.gameof2048;

import java.util.Random;

public class RandomClass {

    private static RandomClass randomClass;

    private Random rnd;

    private final int num1 = 2;

    private final int num2 = 4;

    private final int percentage1 = 75;

    private final int percentage2 = 25;

    private int randomNumberHolder;

    private RandomClass(){
        rnd = new Random();
    }

    public static RandomClass getInstance(){
        if (randomClass == null){
            randomClass= new RandomClass();

        }
        return randomClass;
    }

    public int getRandomNumber(int percentage1, int percentage2, int num1, int num2){
        return 0;
    }

    public int getRandomNumber(){

        randomNumberHolder = rnd.nextInt(100) + 1;

        if(randomNumberHolder > 25){
            return num1;
        }
        else {
            return num2;
        }
    }


    public int[] getRandomCoordinate(int bound){
        return new int[] {rnd.nextInt(bound), rnd.nextInt(bound)};
    }
}
