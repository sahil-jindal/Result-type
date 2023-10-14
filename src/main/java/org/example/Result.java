package org.example;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class Result<T> {
    private T value;
    private Exception exception;

    public boolean isSuccess() { return exception == null; }

    public boolean isFailure() { return exception != null; }

    public T getOrNull() { return value; }

    public Exception getExceptionOrNull() { return exception; }

    @Override
    public String toString() {
        if(isFailure()) {
            return "Failure(" + value.toString() + ")";
        }

        return "Success(" + value.toString() + ")";
    }

    private Result(T value) { this.value = value; }

    private Result(Exception exception) { this.exception = exception; }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Result<T> success(T value) {
        return new Result<>(value);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Result<T> failure(Exception exception) { return new Result<>(exception); }

    public T getOrThrow() throws Exception {
        throwOnFailure();
        return value;
    }

    public <R> R fold(Function<T, R> onSuccess, Function<Exception, R> onFailure) {
        Exception exception = this.getExceptionOrNull();

        if(exception == null) {
            return onSuccess.apply(value);
        }

        return onFailure.apply(exception);
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

    private void throwOnFailure() throws Exception {
        if(isFailure()) throw exception;
    }
}

