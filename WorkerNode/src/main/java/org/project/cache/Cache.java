package org.project.cache;

public interface Cache<K, V> {
    public V get(K key);
    public void put(K key, V value);
    public V remove(K key);
}
