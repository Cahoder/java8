package algorithm.structure.map;

/**
 * 使用哈希表实现映射
 * 存储的key不讲究顺序
 * 存储的key无需具备比较性
 * @param <K>
 * @param <V>
 */
public class HashMap<K,V> implements Map<K,V> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return false;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {

    }

}
