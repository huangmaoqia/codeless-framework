package com.hmq.utis.framework.query;

import java.io.Serializable;

@FunctionalInterface
public interface IGetter<T> extends Serializable {
    Object apply(T source);
}
