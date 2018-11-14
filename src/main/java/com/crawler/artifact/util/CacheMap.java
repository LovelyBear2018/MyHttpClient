package com.crawler.artifact.util;

import com.crawler.artifact.http.httpclient.HttpCrawler;

import java.util.*;

/**
 * Created by liuzhixiong on 2018/9/29.
 * 增强版Map,定时清理过期数据
 */

public class CacheMap<K, V> extends AbstractMap<K, V> {
    private static final long DEFAULT_TIMEOUT = 60*1000*2;
    private static CacheMap<Object, Object> defaultInstance;

    public static synchronized final CacheMap<Object, Object> getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new CacheMap<Object, Object>(DEFAULT_TIMEOUT);
        }
        return defaultInstance;
    }
    public static synchronized final CacheMap<Object, Object> getDefault(long timeout) {
        if (defaultInstance == null) {
            defaultInstance = new CacheMap<Object, Object>(timeout);
        }
        return defaultInstance;
    }

    private class CacheEntry implements Entry<K, V> {
        long time;
        V value;
        K key;

        CacheEntry(K key, V value) {
            super();
            checkNotNull(key);
            checkNotNull(value);
            this.value = value;
            this.key = key;
            this.time = System.currentTimeMillis();
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            checkNotNull(value);
            return this.value = value;
        }
    }

    private class ClearThread extends Thread {
        ClearThread() {
            setName("clear cache thread");
        }

        public void run() {
            while (true) {
                try {
                    long now = System.currentTimeMillis();
                    Object[] keys = map.keySet().toArray();
                    for (Object key : keys) {
                        CacheEntry entry = map.get(key);
                        if ((now - entry.time)/1000 >= cacheTimeout) {
                            synchronized (map) {
                                Object obj = map.get(keys);
                                if(obj instanceof HttpCrawler){
                                    ((HttpCrawler) obj).close();
                                }
                                map.remove(key);
                            }
                        }
                    }
                    Thread.sleep(cacheTimeout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private long cacheTimeout;
    private Map<K, CacheEntry> map = new HashMap<K, CacheEntry>();

    public CacheMap(long millis) {
        this.cacheTimeout = millis;
        new ClearThread().start();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<Entry<K, V>>();
        Set<Entry<K, CacheEntry>> wrapEntrySet = map.entrySet();
        for (Entry<K, CacheEntry> entry : wrapEntrySet) {
            entrySet.add(entry.getValue());
        }
        return entrySet;
    }

    @Override
    public V get(Object key) {
        CacheEntry entry = map.get(key);
        return entry == null ? null : entry.value;
    }

    @Override
    public V put(K key, V value) {
        CacheEntry entry = new CacheEntry(key, value);
        synchronized (map) {
            map.put(key, entry);
        }
        return value;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
