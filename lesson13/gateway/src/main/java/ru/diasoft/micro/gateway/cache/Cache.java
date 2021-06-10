package ru.diasoft.micro.gateway.cache;

import java.util.function.Supplier;

public class Cache {
    static public <T> Supplier<T> cache(Supplier<T> load, Long timeLive) {
        ObjAndTime<T> obj = new ObjAndTime<>(timeLive);

        return (() -> {
            if (!obj.isExpired())
                return obj.obj;
            else {
                return (obj.update(load.get())).obj;
            }
        });
    }

    private static class ObjAndTime<T> {
        T obj;
        Long endTimeCache;
        Long timeLive = 5L;

        ObjAndTime(Long timeLive) {
            this.timeLive = timeLive;
            this.endTimeCache = System.currentTimeMillis() + timeLive * 60 * 1_000;
        }

        synchronized ObjAndTime<T> update(T obj) {
            this.obj = obj;
            this.endTimeCache = System.currentTimeMillis() + timeLive * 60 * 1_000;
            return this;
        }

        Boolean isExpired() {
            return obj == null || (System.currentTimeMillis() > endTimeCache);
        }
    }

}
