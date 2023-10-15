package org.example;

import org.jetbrains.annotations.NotNull;
import java.util.function.Function;

public class ResultUtil {

    public static <R> R getOrElse(@NotNull Result<? extends R> result, Function<Exception, R> onFailure) {
        Exception exception = result.getExceptionOrNull();

        if(exception == null) {
            return result.getOrNull();
        }

        return onFailure.apply(exception);
    }

    public static <R> R getOrDefault(@NotNull Result<? extends R> result, R defaultValue) {
        if(result.isFailure()) {
            return defaultValue;
        }

        return result.getOrNull();
    }

    public static <R> @NotNull Result<R> recover(@NotNull Result<? extends R> result, Function<Exception, R> transform) {
        Exception exception = result.getExceptionOrNull();

        if(exception == null) {
            return Result.success(result.getOrNull());
        }

        return Result.success(transform.apply(exception));
    }

    public static <R> Result<R> recoverCatching(@NotNull Result<? extends R> result, Function<Exception, R> transform) {
        Exception exception = result.getExceptionOrNull();

        if(exception == null) {
            return Result.success(result.getOrNull());
        }

        return Result.runCatching(() -> transform.apply(exception));
    }
}
