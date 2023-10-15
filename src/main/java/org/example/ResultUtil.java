package org.example;

import org.jetbrains.annotations.NotNull;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public <T, R> Result<R> mapCatching(Result<T> result, Function<T, R> transform) {
        if (result.isSuccess()) {
            return runCatching(() -> transform.apply(result.getOrNull()));
        }

        return Result.failure(result.getExceptionOrNull());
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

        return runCatching(() -> transform.apply(exception));
    }

    public static <R> Result<R> runCatching(Supplier<R> block) {
        try {
            return Result.success(block.get());
        } catch (Exception e) {
            return Result.failure(e);
        }
    }
}
