package org.example;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Result<T> implements Serializable {
    private final T value;
    private final Exception exception;

    private Result(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    private Result(String message) {
        this.value = null;
        this.exception = new Exception(message);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Result<T> success(T value) { return new Result<>(value,null); }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Result<T> failure(Exception exception) { return new Result<>(null, exception); }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Result<T> failure(String message) { return new Result<>(message); }

    public boolean isSuccess() { return exception == null; }

    public boolean isFailure() { return exception != null; }

    public T getOrNull() { return value; }

    public T getOrThrow() throws Exception {
        throwOnFailure();
        return value;
    }

    public Exception getExceptionOrNull() { return exception; }

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

    public <R> R fold(Function<T, R> onSuccess, Function<Exception, R> onFailure) {
        if(isSuccess()) {
            return onSuccess.apply(value);
        }

        return onFailure.apply(exception);
    }

    public <R> Result<R> map(Function<T, R> transform) {
        if (isSuccess()) {
            return Result.success(transform.apply(value));
        }

        return Result.failure(exception);
    }

    public <R> Result<R> mapCatching(Function<T, R> transform) {
        if (isSuccess()) {
            return runCatching(() -> transform.apply(value));
        }

        return Result.failure(exception);
    }

    public Result<T> onFailure(Consumer<Exception> action) {
        if (isFailure()) {
            action.accept(exception);
        }

        return this;
    }

    public Result<T> onSuccess(Consumer<T> action) {
        if (isSuccess()) {
            action.accept(value);
        }

        return this;
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

    public static <R> Result<R> runCatching(Callable<R> block) {
        try {
            return Result.success(block.call());
        } catch (Exception e) {
            return Result.failure(e);
        }
    }

    private void throwOnFailure() throws Exception { if(isFailure()) throw exception; }

    @Override
    public @NotNull String toString() {
        if(isFailure()) {
            return "Failure(" + exception.getMessage() + ")";
        }

        return "Success(" + value + ")";
    }
}

