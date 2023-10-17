package org.example;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
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

    private void throwOnFailure() throws Exception { if(isFailure()) throw exception; }

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

    @Override
    public @NotNull String toString() {
        if(isFailure()) {
            return "Failure(" + exception.getMessage() + ")";
        }

        return "Success(" + value + ")";
    }
}

