package ru.arsakhanov;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;

class MyHashMapTest {
    private MyHashMap<String, String> map;

    //убедимся, действительно ли isEmpty возвращает true, если мапа пустая
    @Test
    void testIsEmpty() {
        this.map = new MyHashMap<>();
        Assert.assertTrue(map.isEmpty());
    }

    //убедимся, действительно ли isEmpty возвращает false, если мапа заполнена
    @Test
    void testIsEmptyFalse() {
        this.map = new MyHashMap<>();
        map.put("Japan", "Tokyo");
        Assert.assertFalse(map.isEmpty());
    }

    //убедимся, действительно ли метод put добавляет ключ-значение в мапу
    @Test
    void testPut() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        Object actual = map.get("Russia");
        Assert.assertEquals("Moscow", actual);
        actual = map.get("England");
        Assert.assertEquals("London", actual);
    }

    @Test
    void testIsPutReternOldValue() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        Assert.assertEquals("Moscow", map.put("Russia", "London"));
    }

    //Убедимся, не перепишет ли put ключи с одинаковым хешом
    @Test
    void testDoesNotOverwriteSeparateKeysWithSameHash() {
        this.map = new MyHashMap<>();
        map.put("Ea", "5");
        map.put("FB", "10");
        Assert.assertEquals("5", map.get("Ea"));
        Assert.assertEquals("10", map.get("FB"));
    }


    //убедимся возвращает ли ключ правильное значение
    @Test
    void testGetCorrectValue() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        Assert.assertEquals("Moscow", map.get("Russia"));
    }

    //убедимся, действительно ли contains возвращает true, если ключ существует в мапе
    @Test
    void testIsContainsKey() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        Assert.assertTrue(map.containsKey("Russia"));
    }

    //убедимся, действительно ли contains возвращает false, если ключа нет в мапе
    @Test
    void testIsContainsKeyFalse() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        Assert.assertFalse(map.containsKey("Japan"));
    }

    //Убедимся, что contains покажет false для новой мапы
    @Test
    void testIsContainsKeyForNewMap() {
        this.map = new MyHashMap<>();
        Assert.assertFalse(map.containsKey("Japan"));
    }

    //Убедимся, что contains покажет false для разных ключей с одинаковым хешом
    @Test
    void testIsContainsFalseForDifferentKeysWhithSameHash() {
        this.map = new MyHashMap<>();
        map.put("Ea", "10");
        Assert.assertFalse(map.containsKey("FB"));
    }

    @Test
    void isRemoveReternedValue() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("Japan", "Tokyo");
        map.put("Ukraine", "Kiev");
        Assert.assertEquals("Moscow", map.remove("Russia"));
    }

    //убедимся, действительно ли remove удаляет именно тот объект, который мы указали
    @Test
    void testRemoveIsTrue() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("Japan", "Tokyo");
        map.remove("Russia");
        Assert.assertFalse(map.containsKey("Russia"));
        Assert.assertTrue(map.containsKey("Japan"));
    }

    //Убедимся, что для новой мапы remove не покажет отрицательное число
    @Test
    void testRemoveDoesNotEffectNewMap() {
        this.map = new MyHashMap<>();
        map.remove("Russia");
        Assert.assertEquals(0, map.size());

    }

    //Убедимся, действительно ли remove уменьшает размер мапы при удалении элемента
    @Test
    void testRemove() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("Japan", "Tokyo");
        map.remove("Russia");
        Assert.assertEquals(1, map.size());
        map.remove("Japan");
        Assert.assertEquals(0, map.size());
    }

    //Убедимся, действительно ли getSize возвращает правильное значение
    @Test
    void testGetSize() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        Assert.assertEquals(1, map.size());
        map.put("England", "London");
        Assert.assertEquals(2, map.size());
    }

    //Убедимся, действительно ли getSize возвращает 0, если мапа пустая
    @Test
    void testGetSizeIfMapIsEmpty() {
        this.map = new MyHashMap<>();
        Assert.assertEquals(0, map.size());
    }

    //Убедимся, действительно ли entrySet возвращает все значения мапы
    //даже если будут присутствовать коллизии
    @Test
    void testEntrySet() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        map.put("Japan", "Tokyo");
        map.put("Japam", "Tokyo");
        map.put("Ukraine1", "Kiev1");
        map.put("Ukraine2", "Kiev2");
        map.put("Ukraine3", "Kiev3");
        map.put("Ukraine4", "Kiev4");
        map.put("Ukraine5", "Kiev5");
        map.put("Ukraine6", "Kiev6");
        map.put("Ukraine7", "Kiev7");
        Set<Map.Entry<String, String>> set;
        set = map.entrySet();
        for (MyEntry<String, String> entry : map.buckets) {
            if (entry != null) {
                while (entry.next != null) {
                    Assert.assertTrue(set.contains(entry));
                    entry = entry.next;
                }
                Assert.assertTrue(set.contains(entry));
            }
        }
    }

    //Убедимся, действительно ли values возвращает все значения из мапы
    //даже если у нас будут коллизии
    @Test
    void testViewAllValues() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        map.put("Japan", "Tokyo");
        map.put("Japam", "Tokyo");
        map.put("Ukraine1", "Kiev1");
        map.put("Ukraine2", "Kiev2");
        map.put("Ukraine3", "Kiev3");
        map.put("Ukraine4", "Kiev4");
        map.put("Ukraine5", "Kiev5");
        map.put("Ukraine6", "Kiev6");
        map.put("Ukraine7", "Kiev7");
        Collection<String> collection;
        Collection<String> testCollection;
        collection = map.values();
        testCollection = map.values();
        //чтобы убедиться, что в testCollection есть все значение - выведим его на экран
        System.out.println(testCollection);
        Assert.assertTrue(collection.containsAll(testCollection));
    }

    //убедимся, действительно ли keySet возвращает все ключи
    //даже те, где произошли коллизии
    @Test
    void testKeySet() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        map.put("Japan", "Tokyo");
        map.put("Japam", "Tokyo");
        map.put("Ukraine1", "Kiev1");
        map.put("Ukraine2", "Kiev2");
        map.put("Ukraine3", "Kiev3");
        map.put("Ukraine4", "Kiev4");
        map.put("Ukraine5", "Kiev5");
        map.put("Ukraine6", "Kiev6");
        map.put("Ukraine7", "Kiev7");
        Set<String> set;
        Set<String> set2;
        set = map.keySet();
        set2 = map.keySet();
        Assert.assertTrue(set.containsAll(set2));

    }

    //Убедимся, действительно ли clear очищает всю коллекцию
    @Test
    void testDoesClearReallyClearMap() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        map.put("Japan", "Tokyo");
        map.put("Ukraine", "Kiev");
        Assert.assertEquals(4, map.size());
        map.clear();
        Assert.assertEquals(0, map.size());
    }

    //Убедимся, действительно ли clear удаляет все элементы в мапе
    @Test
    void testDoesClearDeleteElements() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        map.put("Japan", "Tokyo");
        map.put("Ukraine", "Kiev");
        map.clear();
        Assert.assertFalse(map.containsKey("Russia"));
    }

    //Убедимся, действительно ли putAll добавляет все элементы из мапы
    @Test
    void testDoesPutAll() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        Map<String, String> realMap = new HashMap<>();
        realMap.put("Japan", "Tokyo");
        realMap.put("Ukraine", "Kiev");
        map.putAll(realMap);
        Assert.assertEquals("Tokyo", map.get("Japan"));
        Assert.assertEquals("Kiev", map.get("Ukraine"));
    }

    //Убедимся, действительно ли ContainsValue возвращает true, если находит значение
    @Test
    void testIsContainsValueReturnTrue() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        Assert.assertTrue(map.containsValue("London"));
    }

    //Убедимся, действительно ли ContainsValue возвращает false, если не находит значение
    @Test
    void testIsContainsValueReturnFalse() {
        this.map = new MyHashMap<>();
        map.put("Russia", "Moscow");
        map.put("England", "London");
        Assert.assertFalse(map.containsValue("Kiev"));
    }
}