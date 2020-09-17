package ru.arsakhanov;

import java.util.Map;

/**
 * Класс для создания ноды, чтобы добавлять новые элементы в map
 */
public class MyEntry<K,V> implements Map.Entry<K,V> {
    final K key;
    V value;
    MyEntry<K,V> next;

    //конструктор класса Entry
    public MyEntry(K key, V value, MyEntry<K,V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        return null;
    }

    public MyEntry<K,V> getNext() {
        return next;
    }

    /**
     * переопределеине equals
     * объекты равны только тогда, когда равны их ключи и значения
     *
     * @param obj объект с парой ключ-значение
     * @return возвращает true, если объекты равны и false в противном случае
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof MyEntry) {
            MyEntry myEntry = (MyEntry) obj;
            return key.equals(myEntry.getKey()) &&
                    value.equals(myEntry.getValue());
        }
        return false;
    }

    /**
     * переопределение хэш кода
     *
     * @return возвращает хэш код
     */
    @Override
    public int hashCode() {
        int hash = 13;
        hash = 17 * hash + ((key == null) ? 0 : key.hashCode());
        hash = 17 * hash + ((value == null) ? 0 : value.hashCode());
        return hash;
    }

    /**
     * переопределенный метод toString
     *
     * @return возвращает строку типа String
     */
    @Override
    public String toString() {
        return '{' + "key=" + getKey() +
                ", value=" + getValue() +
                '}' + '\n';
    }
}
