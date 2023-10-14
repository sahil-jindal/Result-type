package org.example;

import java.util.function.Function;
import java.util.function.Supplier;

public class ResultUtil {
    public static <R> Result<R> runCatching(Supplier<R> block) {
        try {
            return Result.success(block.get());
        } catch (Exception e) {
            return Result.failure(e);
        }
    }
    public static <R> R getOrElse(Result<? extends R> result, Function<Exception, R> onFailure) {
        Exception exception = result.getExceptionOrNull();

        if(exception == null) {
            return result.getOrNull();
        }

        return onFailure.apply(exception);
    }

    public static <R> R getOrDefault(Result<? extends R> result, R defaultValue) {
        if(result.isFailure()) {
            return defaultValue;
        }

        return result.getOrNull();
    }

    public static <R> Result<R> recover( Result<? extends R> result, Function<Exception, R> transform) {
        Exception exception = result.getExceptionOrNull();

        if(exception == null) {
            return (Result<R>) result;
        }

        return Result.success(transform.apply(exception));
    }

    public static <R> Result<R> recoverCatching(Result<? extends R> result, Function<Exception, R> transform) {
        Exception exception = result.getExceptionOrNull();

        if(exception == null) {
            return (Result<R>) result;
        }

        return runCatching(() -> transform.apply(exception));
    }
}
