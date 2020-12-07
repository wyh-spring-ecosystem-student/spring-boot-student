package com.xiaolyuh;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MyString {

    private final char value[];

    private int hash; // Default to 0

    MyString(char[] value, boolean share) {
        // assert share : "unshared not supported";
        this.value = value;
    }

    public MyString() {
        this.value = new char[0];
    }

    public MyString replace(char oldChar, char newChar) {
        if (oldChar != newChar) {
            int len = value.length;
            int i = -1;
            char[] val = value; /* avoid getfield opcode */

            while (++i < len) {
                if (val[i] == oldChar) {
                    val[i] = newChar;
                }
            }
            return new MyString(val, false);
        }
        return this;
    }

    public MyString(String original) {
        this.value = original.toCharArray();
        this.hash = original.hashCode();
    }


    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }

    public static void main(String[] args) {
        String str = "11";
        System.out.println(str);
        str = "12323bcd";
        System.out.println(str);

        IntStream intStream = str.chars();
        Object[] sum = intStream.filter(c -> c > '1')
                .distinct()
                .sorted()
                .mapToObj(i ->(char)i)
                .toArray();

        System.out.println(Arrays.toString(sum));

        IntStream.range(1, 3).forEach(System.out::println);


        Random seed = new Random();
        Supplier<Integer> random = seed::nextInt;
        Stream.generate(random).limit(10).forEach(System.out::println);
        //Another way
        IntStream.generate(() -> (int) (System.nanoTime() % 10000)).
                limit(10).forEach(System.out::println);
    }
}
