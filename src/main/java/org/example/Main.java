package org.example;

import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Result<String> something = Result.success("Some text");

        Function<String, Integer> getLength = String::length;
        Function<Exception, Integer> getMessageLength = a -> a.getMessage().length();

        Integer length = something.fold(getLength, getMessageLength);

        System.out.println(length);
    }
}