package org.example;

import java.util.function.Function;

public class Main {

    static Integer counter = 2;

    private static void someFunction(Integer value) {
        counter += value;
    }

    public static void main(String[] args) {
        // Example usage of Result class
        Result<Integer> successResult = Result.success(42);
        Result<Integer> failureResult = Result.failure("Operation failed");

        Function<Exception, Integer> onFailureFunction = exception -> {
            System.err.println("An error occurred: " + exception.getMessage());
            return -1;
        };

        Function<Integer, String> transformFunction = value -> "Transformed value: " + value;
        Function<Integer, String> onSuccess = value -> "Success: " + value;
        Function<Exception, String> onFailure = exception -> "Failure: " + exception.getMessage();

        // Example usage of fold function in Result class
        String foldedSuccess = successResult.fold(onSuccess, onFailure);
        String foldedFailure = failureResult.fold(onSuccess, onFailure);

        System.out.println("Folded success: " + foldedSuccess);
        System.out.println("Folded failure: " + foldedFailure);

        int valueOrDefault = Result.getOrElse(successResult, onFailureFunction);
        System.out.println("Value or default: " + valueOrDefault);

        int valueOrDefault2 = Result.getOrDefault(failureResult, 100);
        System.out.println("Value or default 2: " + valueOrDefault2);

        // Example usage of map function in Result class
        Result<String> mappedResult = successResult.map(value -> "Result value: " + value);
        System.out.println("Mapped result: " + mappedResult);

        Result<String> mappedCatchingResult = successResult.mapCatching(transformFunction);
        System.out.println("Mapped catching result: " + mappedCatchingResult);

        System.out.println("Before update: " + counter);
        successResult.onSuccess(Main::someFunction);
        System.out.println("After successful computation: " + counter);

        failureResult.onFailure(exception -> System.out.println(exception.getMessage()));

        Result<Integer> recoveredResult = Result.recover(failureResult, exception -> -1);
        System.out.println("Recovered result: " + recoveredResult);

        Result<Integer> recoveredCatchingResult = Result.recoverCatching(failureResult, exception -> Integer.parseInt("2df"));
        System.out.println("Recovered catching result: " + recoveredCatchingResult);

        Result<Integer> runCatchingResult = Result.runCatching(() -> {
            throw new Exception("Simulated exception");
        });

        System.out.println("Run catching result: " + runCatchingResult);
    }
}