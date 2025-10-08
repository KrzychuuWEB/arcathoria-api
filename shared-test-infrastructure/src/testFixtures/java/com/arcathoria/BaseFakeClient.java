package com.arcathoria;

import com.arcathoria.exception.DomainException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class BaseFakeClient<K, V> {

    protected final Map<K, V> data = new HashMap<>();
    protected boolean shouldThrowException = false;
    protected String exceptionType = null;

    protected V get(final K key, final BiFunction<K, String, DomainException> notFoundExceptionFactory) {
        if (shouldThrowException) {
            throwConfiguredException(key);
        }

        V value = data.get(key);
        if (value == null) {
            throw notFoundExceptionFactory.apply(key, getDefaultNotFoundErrorCode());
        }
        return value;
    }

    protected void put(final K key, final V value) {
        data.put(key, value);
    }

    protected void configureException(final String type) {
        this.shouldThrowException = true;
        this.exceptionType = type;
    }

    public void reset() {
        data.clear();
        shouldThrowException = false;
        exceptionType = null;
    }

    protected abstract void throwConfiguredException(final K key);

    protected abstract String getDefaultNotFoundErrorCode();
}
