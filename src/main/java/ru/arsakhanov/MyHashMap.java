package ru.arsakhanov;

import java.util.*;

/**
 * Класс для создания хэш мапы(hashmap)
 * для хранения пары ключ-значение используется класс Entry
 */
public class MyHashMap<K, V> implements Map<K, V> {
    private int capacity;
    public MyEntry<K, V>[] buckets;

    private int size = 0;

    public MyHashMap() {
        this(16);
    }

    public MyHashMap(int capacity) {
        this.capacity = capacity;
        this.buckets = new MyEntry[this.capacity];
    }

    /**
     * метод добавляет ключ-значение в мапу
     * ключ-значение может быть любого типа
     *
     * @param key   объект любого типа, которую можно указать как ключ
     * @param value объект любого типа, значение ключа
     * @return возвращает старое значение, если ключи одинаковые и новое значение заменяет старое
     */
    public V put(K key, V value) {
        V oldValue;
        double percent = 0.75;
        if (size == percent * capacity) {
            MyEntry<K, V>[] oldBuckets = buckets;
            capacity *= 2;
            size = 0;
            buckets = new MyEntry[capacity];
            for (MyEntry<K, V> e : oldBuckets) {
                while (e != null) {
                    put(e.key, e.value);
                    e = e.next;
                }
            }
        }
        MyEntry<K, V> entry = new MyEntry<>(key, value, null);
        int index = getIndex(key);

        MyEntry<K, V> current = buckets[index];
        if (current == null) {
            buckets[index] = entry;
            size++;
        } else {
            while (current.getNext() != null) {
                if (current.key.equals(key)) {
                    oldValue = current.value;
                    current.value = value;
                    return oldValue;
                }
                current = current.getNext();
            }
            if (current.key.equals(key)) {
                oldValue = current.value;
                current.value = value;
                return oldValue;
            } else {
                current.next = entry;
                size++;
                return current.getNext().getValue();
            }
        }
        return null;
    }


    /**
     * Добавляет в коллекцию все объекты из представления map
     * Он добавляет элементы нашей мапы в мапу из параметра
     * выбрасывает исключение nullpointerexception, если наша мапа пустая
     *
     * @param m новая мапа
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> bucket : m.entrySet()) {
            if (bucket != null) {
                put(bucket.getKey(), bucket.getValue());
            } else throw new NullPointerException("В параметре указан null");
        }
    }

    /**
     * Очищает всю коллекцию
     * Удаляет все элементы
     */
    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                buckets[i] = null;
                size--;
            }
        }
    }

    /**
     * Возвращает количество ключей в мапе
     *
     * @return возвращает коллекцию Set с параметризацией K(key)
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (MyEntry<K, V> current : buckets) {
            if (current != null) {
                while (current.next != null) {
                    keys.add(current.getKey());
                    current = current.next;
                }
                keys.add(current.key);
            }
        }
        return keys;
    }

    /**
     * @return Возвращает колеекцию всех значений из мапы
     */
    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<>();
        for (MyEntry<K, V> current : buckets) {
            if (current != null) {
                while (current.next != null) {
                    collection.add(current.getValue());
                    current = current.next;
                }
                collection.add(current.getValue());
            }
        }
        return collection;
    }

    /**
     * возвращает все пары ключ-значение из мапы
     *
     * @return возвращаемым объектом является Set с параметризацией <Entry<K,V>>
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<>();
        for (MyEntry<K, V> current : buckets) {
            if (current != null) {
                while (current.next != null) {
                    set.add(current);
                    current = current.next;
                }
                set.add(current);
            }
        }
        return set;
    }

    /**
     * Удаляет элемент из мапы, если он в нем существует
     *
     * @param key объект любого типа
     * @return возвращает true, если удление прошло успешно и false - в противном случае
     */
    @Override
    public V remove(Object key) {
        V currentValue = null;
        MyEntry<K, V> previous = null;
        MyEntry<K, V> current = buckets[getIndex(key)];
        if (current == null) {
            return null;
        }
        while (current != null) {
            if (current.key.equals(key)) {
                if (previous != null) {
                    previous.next = current.next;

                } else {
                    currentValue = current.getValue();
                    buckets[getIndex(key)] = null;
                }
                size--;
                return currentValue;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }


    /**
     * вычисляет хэш ключ, потому проделывает операцию
     * поразрядного сложения с размером buckets
     *
     * @param key объект любого типа
     * @return возвращает индекс по форм
     */
    public <K> int getIndex(K key) {
        return getHash(key) & getBucketsSize() - 1;
    }

    /**
     * возвращает размер массива buckets
     *
     * @return возвращает размер массива типа int
     */
    public int getBucketsSize() {
        return buckets.length;
    }


    /**
     * @return возвращает количество элементов мапы
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * проверяем пустая ли мапа или нет
     *
     * @return возвращает true, если в мапе нет элементов
     * и false в противном случае
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * проверяет есть ли данный key в мапе
     *
     * @param key объект любого типа
     * @return возвращает true, если ключ есть в мапе, в противном случае - false
     */
    @Override
    public boolean containsKey(Object key) {
        MyEntry<K, V> bucket = buckets[getIndex(key)];
        while (bucket != null) {
            if (getHash(key) == getHash(bucket.key)) {
                if (key.equals(bucket.key)) {
                    return true;
                } else bucket = bucket.next;
            }
        }
        return false;
    }

    /**
     * проверяет, если ли данное значение в мапе
     *
     * @param value объект value с таким же типом как и у value мапы
     * @return возвращает true, если в мапе есть такое значение
     * и false есть данное значение не удалось найти
     */
    @Override
    public boolean containsValue(Object value) {
        for (MyEntry<K, V> bucket : buckets) {
            if (bucket != null) {
                while (bucket.next != null) {
                    if (bucket.value.equals(value)) {
                        return true;
                    } else {
                        bucket = bucket.next;
                    }
                }
                if (bucket.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * переопределенный метод который получает объект из мапы по ключу
     * если такого объекта нет - возвращает null
     *
     * @param key объект любого типа
     * @return возвращает key, если удается его найти в мапе и null в противном случае
     */
    @Override
    public V get(Object key) {
        MyEntry<K, V> bucket = buckets[getIndex(key)];
        while (bucket != null) {
            if (getHash(key) == getHash(bucket.getKey())) {
                if (key.equals(bucket.key)) {
                    return bucket.getValue();
                }
            }
            bucket = bucket.next;
        }
        return null;
    }

    /**
     * возвращает хеш объекта
     *
     * @param key объект любого типа
     * @return возвращает null, если объект равен null, в противном случае хэшкод объекта
     */
    public <K> int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MyEntry<K, V> entry : buckets) {
            while (entry != null) {
                sb.append(entry);
                if (entry.next != null) {
                    sb.append(", ");
                }
                entry = entry.next;
            }
        }
        return sb.toString();
    }
}

