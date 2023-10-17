package org.example;

import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        // Example usage of Result class
        Result<Integer> successResult = Result.success(42);
        Result<Integer> failureResult = Result.failure("Operation failed");

        // Example usage of map function in Result class
        Result<String> mappedResult = successResult.map(value -> "Result value: " + value);

        // Example usage of fold function in Result class
        String foldedSuccess = successResult.fold(
                value -> "Success: " + value,
                exception -> "Failure: " + exception.getMessage()
        );

        String foldedFailure = failureResult.fold(
                value -> "Success: " + value,
                exception -> "Failure: " + exception.getMessage()
        );

        System.out.println("Mapped result: " + mappedResult);
        System.out.println("Folded success: " + foldedSuccess);
        System.out.println("Folded failure: " + foldedFailure);

        // Example usage of ResultUtil class
        Function<Exception, Integer> onFailureFunction = exception -> {
            System.err.println("An error occurred: " + exception.getMessage());
            return -1;
        };

        int valueOrDefault = ResultUtil.getOrElse(successResult, onFailureFunction);
        System.out.println("Value or default: " + valueOrDefault);

        int valueOrDefault2 = ResultUtil.getOrDefault(failureResult, 100);
        System.out.println("Value or default 2: " + valueOrDefault2);

        // Example usage of remaining functions from ResultUtil
        Function<Integer, String> transformFunction = value -> "Transformed value: " + value;

        Result<String> mappedCatchingResult = ResultUtil.mapCatching(successResult, transformFunction);
        System.out.println("Mapped catching result: " + mappedCatchingResult);

        Result<Integer> recoveredResult = ResultUtil.recover(failureResult, exception -> -1);
        System.out.println("Recovered result: " + recoveredResult);

        Result<Integer> recoveredCatchingResult = ResultUtil.recoverCatching(failureResult, exception -> -2);
        System.out.println("Recovered catching result: " + recoveredCatchingResult);

        Result<Integer> runCatchingResult = ResultUtil.runCatching(() -> {
            throw new Exception("Simulated exception");
        });

        System.out.println("Run catching result: " + runCatchingResult);
    }
}