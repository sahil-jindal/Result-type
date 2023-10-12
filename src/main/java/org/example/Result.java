package org.example;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Result<T> {
    private T value;
    private Throwable exception;

    public boolean isSuccess() { return exception == null; }

    public boolean isFailure() { return exception != null; }

    public T getOrNull() { return value; }

    public Throwable getExceptionOrNull() { return exception; }

    @Override
    public String toString() {
        if(isFailure()) {
            return "Failure(" + value.toString() + ")";
        }

        return "Success(" + value.toString() + ")";
    }

    private Result(T value) { this.value = value; }

    private Result(Throwable exception) { this.exception = exception; }

    public static <T> Result<T> success(T value) {
        return new Result<>(value);
    }

    public static <T> Result<T> failure(Throwable exception) { return new Result<>(exception); }

    public T getOrThrow() throws Throwable {
        if (isFailure()) {
            throw exception;
        }

        return value;
    }

    public static <R> Result<R> runCatching(Supplier<R> block) {
        try {
            return Result.success(block.get());
        } catch (Throwable e) {
            return Result.failure(e);
        }
    }

    public <R> R fold(Function<T, R> onSuccess, Function<Throwable, R> onFailure) {
        Throwable exception = this.getExceptionOrNull();

        if(exception == null) {
            return onSuccess.apply(value);
        }

        return onFailure.apply(exception);
    }

    public Result<T> onFailure(Consumer<Throwable> action) {
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
}

