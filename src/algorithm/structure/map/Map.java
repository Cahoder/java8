package algorithm.structure.map;


/**
 * 映射
 * @param <K>
 * @param <V>
 */
public interface Map<K,V> {

    /**
     * @return return the size of the map
     */
    int size();

    /**
     * @return whether the map is empty
     */
    boolean isEmpty();

    /**
     * clear the elements in the map
     */
    void clear();

    /**
     * @param key the designed key
     * @return search to check whether contain
     * the specified key in this map
     * ps: each key is unique
     */
    boolean containsKey(K key);

    /**
     * @param value the designed map
     * @return search to check whether contain
     * the specified key in this map
     */
    boolean containsValue(V value);

    /**
     * add a new mapping
     * relation into this map
     * @param key the key of the mapping, the key must to be unique. if the key exist,
     *            we will replace the old value of the key with new value
     * @param value the value of the mapping, the value accept identical element
     * @return if put success, there are two kind of situation
     *         if put key doesn't exist, we will return null
     *         if put key exist, we will return the old value of the mapping
     */
    V put(K key,V value);

    /**
     * @param key the key of the mapping
     * @return the value of the specify key or
     *         <code>null</code> if the key doesn't exist
     */
    V get(K key);

    /**
     * @param key remove a designated key from this map
     * @return the value of the removed key
     */
    V remove(K key);

    /**
     * @see Visitor
     * @param visitor How to traversal this by special rule
     */
    void traversal(Visitor<K,V> visitor);

    /**
     * This Class use to provide a visit rule
     * @param <K>
     * @param <V>
     */
    public static abstract class Visitor<K,V> {
        boolean stop;
        public abstract boolean visit(K key, V value);
    }

}
