package org.example;

import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Result<Person> something = Result.success(new Person("Rahul", "Agarwal"));
        System.out.println(something);

        Function<Person, String> getLength = a -> a.firstname + " " + a.lastname;
        Function<Exception, String> getMessageLength = Throwable::getMessage;

        System.out.println(something.fold(getLength, getMessageLength));
    }
}