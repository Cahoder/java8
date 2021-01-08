package algorithm.structure.map;

import java.util.Comparator;
import java.util.Stack;

/**
 * 使用红黑树实现映射关系
 * 存储的key讲究顺序
 * 存储的key必需具备比较性
 * @param <K> key
 * @param <V> value
 */
public class TreeMap<K,V> implements Map<K,V> {
    /**
     * The Flag Represent the Red Black Tree Node's Color
     */
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    /**
     * A Comparator If you have new custom compare rule
     */
    private final Comparator<K> comparator;

    /**
     * Binary Search Tree Element's Size
     */
    private int size;

    /**
     * The Root Node of this RBT
     */
    private Entry<K,V> root;

    /**
     * 定义一个Entry类储存树节点元素
     * @param <K>
     * @param <V>
     */
    private static class Entry<K,V> {
        K key;
        V value;
        boolean color = RED;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;
        public Entry(K key, V value, Entry<K,V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
        public boolean isParentLeftChild() {
            return parent!=null && parent.left == this;
        }
        public boolean isParentRightChild() {
            return parent!=null && parent.right == this;
        }
        public Entry<K,V> sibling() {
            if (isParentLeftChild()) return parent.right;
            if (isParentRightChild()) return parent.left;
            return null;
        }
    }

    /**
     * @param comparator Use Comparator rule if it's not null
     */
    public TreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    /**
     * if you don't need a custom Comparator
     * K must have implemented the Comparable interface
     * @see java.lang.Comparable
     */
    public TreeMap() {
        this(null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return !(size > 0);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        final boolean[] rst = {false};
        traversal(new Visitor<K, V>() {
            @Override
            public boolean visit(K k, V v) {
                rst[0] = valueEqualCheck(v, value);
                return rst[0];
            }
        });
        return rst[0];
    }

    @Override
    public V put(K key, V value) {
        keyNotNullCheck(key);
        if (root == null) {
            root = new Entry<>(key,value,null);
            size++;
            afterPut(root);
            return null;
        }

        Entry<K,V> node = root;
        Entry<K,V> parentNode = null;
        int cmp = 0;
        while (node!=null) {
            cmp = compare(key, node.key);
            parentNode = node;
            //说明当前元素比较大应该往右边走
            if (cmp > 0) node = node.right;
            //说明当前元素比较小应该往左边走
            else if (cmp < 0) node = node.left;
            //元素一样大新覆盖旧并返回
            else {
                node.key = key;
                V old_value = node.value;
                node.value = value;
                return old_value;
            }
        }

        Entry<K,V> nNode = new Entry<>(key,value,parentNode);
        if (cmp > 0) parentNode.right = nNode;
        else parentNode.left = nNode;

        size++;
        afterPut(nNode);
        return null;
    }

    @Override
    public V get(K key) {
        Entry<K, V> node = node(key);
        return node!=null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) return;
        Entry<K, V> pointer = this.root;
        if (pointer == null) return;

        Stack<Entry<K, V>> stack = new Stack<>();
        while (!stack.empty() || pointer!=null) {
            if (pointer!=null) {
                stack.push(pointer);
                pointer = pointer.left;
            } else {
                pointer = stack.pop();
                visitor.stop = visitor.visit(pointer.key,pointer.value);
                if (visitor.stop) return;
                pointer = pointer.right;
            }
        }
    }

    /**
     * @param key1 the first object to be compared.
     * @param key2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the second.
     */
    @SuppressWarnings("unchecked")
    private int compare(K key1,K key2) {
        if (comparator!=null) return comparator.compare(key1,key2);
        //Use the K default comparable rule
        //We assume that the K have implements the java.lang.comparable interface
        //if K not it will throw a runtime exception
        return ((Comparable<K>)key1).compareTo(key2);
    }

    /**
     * @param key Get Node By Key
     * @return return <code>null</code> if element doesn't exist
     */
    private Entry<K,V> node(K key) {
        Entry<K,V> node = root;
        while (node!=null) {
            int cmp = compare(key,node.key);
            if (cmp==0) return node;
            if (cmp>0) node = node.right;
            else node = node.left;
        }
        return null;
    }

    /**
     * Dye the Red Black Node's Color
     * @param node The Red Black Node need to dye
     * @param color dying color
     * @return The Red Black Node have been dyed
     */
    private Entry<K,V> dye(Entry<K,V> node, boolean color) {
        if (node!=null) node.color = color;
        return node;
    }

    /**
     * Dye the Red Black Node to <code>RED</code> color
     * @param node The Red Black Node need to dye
     * @return The Red Black Node have been dyed
     */
    private Entry<K,V> dyeRed(Entry<K,V> node) {
        return dye(node,RED);
    }

    /**
     * Dye the Red Black Node to <code>BLACK</code> color
     * @param node The Red Black Node need to dye
     * @return The Red Black Node have been dyed
     */
    private Entry<K,V> dyeBlack(Entry<K,V> node) {
        return dye(node,BLACK);
    }

    /**
     * @param node Red Black Node
     * @return Get the color of the Red Black Node
     */
    private boolean colorOf(Entry<K,V> node) {
        return node == null ? BLACK : node.color;
    }

    /**
     * @param node Red Black Node
     * @return Whether the color of the Red Black Node is RED
     */
    private boolean isRed(Entry<K,V> node){
        return colorOf(node) == RED;
    }

    /**
     * @param node Red Black Node
     * @return Whether the color of the Red Black Node is BLACK
     */
    private boolean isBlack(Entry<K,V> node){
        return colorOf(node) == BLACK;
    }

    /**
     * @param key Key Argument Not Null Check
     */
    private void keyNotNullCheck(K key) {
        if (key==null)
            throw new IllegalArgumentException("key must not be null !");
    }

    /**
     * After add new Node into this RBT,
     * In some situation we need to restore.
     * @param node new Node
     */
    private void afterPut(Entry<K,V> node) {
        Entry<K,V> parent = node.parent;

        //如果当前是根节点,将根节点染成黑色
        if (parent == null) {
            dyeBlack(node);
            return;
        }

        //如果父节点是黑色,无需做额外处理的4种情况
        if (isBlack(parent)) return;

        //如果父节点是红色,需做额外处理的8种情况(Double-RED)
        Entry<K,V> uncle = node.parent.sibling();
        Entry<K,V> grand_parent = parent.parent;
        //如果叔父节点是红色,在B树的角度它上溢
        if (isRed(uncle)) {
            dyeBlack(parent);
            dyeBlack(uncle);
            //将祖父节点向上染成红色并且向上合并---可以看成是添加新的节点
            afterPut(dyeRed(grand_parent));
            return;
        }
        //如果叔父节点是黑色,需要分情况染色并旋转操作
        //L
        if (parent.isParentLeftChild()){
            //LL
            if (node.isParentLeftChild()) {
                dyeBlack(parent);
                dyeRed(grand_parent);
                zig(grand_parent);
            }
            //LR
            else if (node.isParentRightChild()) {
                dyeBlack(node);
                dyeRed(grand_parent);
                zag(parent);
                zig(grand_parent);
            }
        }
        //R
        else if (parent.isParentRightChild()) {
            //RL
            if (node.isParentLeftChild()) {
                dyeBlack(node);
                dyeRed(grand_parent);
                zig(parent);
                zag(grand_parent);
            }
            //RR
            else if (node.isParentRightChild()) {
                dyeBlack(parent);
                dyeRed(grand_parent);
                zag(grand_parent);
            }
        }
    }

    /**
     * @param node Remove the designed node
     */
    private V remove(Entry<K,V> node) {
        if (node==null) return null;
        V old_value = node.value;

        //节点度为2特殊处理,只有当度为2时前驱/后继都存在,只需找到它的前驱/后继代替它即可
        if (node.left!=null && node.right!=null) {
            //找到后继节点
            Entry<K,V> s = successor(node);
            //用后继节点元素覆盖
            node.key = s.key;
            node.value = s.value;
            //前驱或后继节点度为0|1,可用以下代码
            node = s;
        }

        //统一删除节点(度为0|1)
        Entry<K,V> replacement = (node.left!=null)?node.left:node.right;

        //当度为1的时候
        if (replacement!=null) {
            replacement.parent = node.parent;
            if (replacement.parent==null) {
                //说明当前节点是根
                root = replacement;
            } else if (node.parent.left == node) {
                //当删除节点是父节点的左子
                node.parent.left = replacement;
            } else {
                //当删除节点是父节点的右子
                node.parent.right = replacement;
            }
            //这里传替代节点不会影响AVL树,红黑树会用到
            afterRemove(replacement);
        }
        //当度为0的时候
        else {
            if (node.parent == null) {
                //说明当前节点是根
                root = null;
            } else if (node.parent.left == node) {
                //当删除节点是父节点的左子
                node.parent.left = null;
            } else {
                //当删除节点是父节点的右子
                node.parent.right = null;
            }
            afterRemove(node);
        }
        size--;
        return old_value;
    }

    /**
     * After remove a Node from this RBT,
     * In some situation we need to restore.
     * @param node deleted Node
     */
    private void afterRemove(Entry<K,V> node) {
        //如果删除的节点是RED,无需额外操作,并可合并到BLACK节点有一个RED子节点情况
        //if (isRed(node)) return;
        //如果删除的节点是BLACK,需分情况额外操作
        //1.如果BLACK节点有2个RED子节点,无需额外操作
        //2.如果BLACK节点有1个RED子节点,需要将代替其的RED子节点染成BLACK
        if (isRed(node)){
            dyeBlack(node);
            return;
        }

        //3.如果BLACK节点是叶子节点
        Entry<K,V> parent = node.parent;
        //3.1如果BLACK节点是根节点,直接删除
        if (parent==null) return;

        //叶子节点被执行完remove方法后,父节点肯定有一边是null了,则它的兄弟节点就是另一边
        //但是afterRemove方法不一定是给remove调用,可能是递归调用,不能完全依靠null来判断
        boolean isParentLeftChild = (parent.left == null) || node.isParentLeftChild();
        Entry<K,V> sibling = isParentLeftChild ? parent.right : parent.left;

        //如果原本是父节点的左子,则兄弟节点在右
        if (isParentLeftChild) {
            //3.2如果BLACK节点的兄弟节点为RED
            if (isRed(sibling)) {
                dyeBlack(sibling);
                dyeRed(parent);
                zag(parent);
                sibling = parent.right;  //此时换新兄弟
            }
            //3.3如果BLACK节点的兄弟节点为BLACK
            //3.2.1如果兄弟节点没有RED子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                boolean oldParentColor = colorOf(parent);
                dyeBlack(parent);
                dyeRed(sibling);
                //如果父节点原本是黑色,删除后必定会导致父节点继续下溢,需将父节点当做被删除节点递归调用
                if (oldParentColor == BLACK) afterRemove(parent);
            }
            //3.2.2如果兄弟节点至少有1个RED子节点
            else {
                if (isBlack(sibling.right)) {
                    zig(sibling);
                    sibling = parent.right;
                }

                //先对新的parent继承旧parent的颜色,新parent的子节点都染成黑色
                dye(sibling,colorOf(parent));
                dyeBlack(sibling.right);
                dyeBlack(parent);

                zag(parent);
            }
        }
        //如果原本是父节点的右子,则兄弟节点在左
        else {
            //3.2如果BLACK节点的兄弟节点为RED
            if (isRed(sibling)) {
                dyeBlack(sibling);
                dyeRed(parent);
                zig(parent);
                sibling = parent.left;  //此时换新兄弟
            }
            //3.3如果BLACK节点的兄弟节点为BLACK
            //3.2.1如果兄弟节点没有RED子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                boolean oldParentColor = colorOf(parent);
                dyeBlack(parent);
                dyeRed(sibling);
                //如果父节点原本是黑色,删除后必定会导致父节点继续下溢,需将父节点当做被删除节点递归调用
                if (oldParentColor == BLACK) afterRemove(parent);
            }
            //3.2.2如果兄弟节点至少有1个RED子节点
            else {
                if (isBlack(sibling.left)) {
                    zag(sibling);
                    sibling = parent.left;
                }

                //先对新的parent继承旧parent的颜色,新parent的子节点都染成黑色
                dye(sibling,colorOf(parent));
                dyeBlack(sibling.left);
                dyeBlack(parent);

                zig(parent);
            }
        }
    }

    /**
     * how to get the successor of the design node
     * The successor is the node which order behind the design node in the inorder Traversal
     */
    private Entry<K,V> successor(Entry<K,V> node) {
        if (node == null) return null;

        //如果右子树不为空,则按照中序遍历规则,后继节点肯定在右子树中最左节点
        Entry<K,V> n = node.right;
        if (n!=null) {
            while (n.left!=null) n = n.left;
            return n;
        }
        //如果右子树为空,又分两种情况考虑
        while (node.parent!=null && node.parent.right == node) node = node.parent;

        //(1.parent==null说明无后继  2.node是parent的(左子它是后继) || (右子它不是后继))
        return node.parent;
    }

    /**
     * RR
     * @param grand_parent Rotate Left
     */
    private void zag(Entry<K,V> grand_parent) {
        Entry<K,V> parent = grand_parent.right;
        Entry<K,V> node = parent.left;

        grand_parent.right = node;
        parent.left = grand_parent;
        //更新节点属性和高度
        zagged_zigged(node,parent,grand_parent);
    }

    /**
     * LL
     * @param grand_parent Rotate Right
     */
    private void zig(Entry<K,V> grand_parent) {
        Entry<K,V> parent = grand_parent.left;
        Entry<K,V> node = parent.right;

        grand_parent.left = node;
        parent.right = grand_parent;
        //更新节点属性和高度
        zagged_zigged(node,parent,grand_parent);
    }

    /**
     * @param value1 Key's value_1
     * @param value2 Key's value_2
     * @return Use to check whether
     *         two object is equivalent
     */
    @SuppressWarnings("all")
    private boolean valueEqualCheck(V value1, V value2) {
        return value1 == null ? value2 == null : value1.equals(value2);
    }

    /**
     * After Rotate We need to update some property
     * @param node Unbalanced Node's old grandson
     * @param parent Unbalanced Node's old son
     * @param grand_parent Unbalanced Node
     */
    private void zagged_zigged(Entry<K,V> node, Entry<K,V> parent, Entry<K,V> grand_parent) {
        /*以下更新顺序不可随意改变*/
        //更新node的parent属性
        if (node!=null)
            node.parent = grand_parent;
        //更新parent的parent属性
        parent.parent = grand_parent.parent;
        if (grand_parent.isParentLeftChild()) grand_parent.parent.left = parent;
        else if (grand_parent.isParentRightChild()) grand_parent.parent.right = parent;
        else root = parent; //当grant_parent是根
        //更新grand_parent的parent属性
        grand_parent.parent = parent;
    }
}
