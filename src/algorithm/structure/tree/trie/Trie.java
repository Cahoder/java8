package algorithm.structure.tree.trie;

import java.util.HashMap;

/**
 * Trie 也叫做字典树 前缀树(Prefix Tree) 单词查找树
 * 特点: 1）根节点不包含字符,除根节点外的每一个子节点都包含一个字符
 *      2）从根节点到某一节点的路径上的字符连接起来,就是该节点对应的字符串
 *      3）每个节点的所有子节点包含的字符都不相同
 * 优点: 搜索字符串的效率主要跟字符串的长度有关
 * 缺点: 需要耗费大量的内存,因此还有待改进
 */
public class Trie {

    /** Root TrieNode of the trie. */
    private final TrieNode root = new TrieNode();

    /** Inserts a word into the trie. */
    public void insert(String word) {
        keyCheck(word);
        TrieNode node = this.root;

        int len = word.length();
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);

            TrieNode child = node.children.get(c);
            if (child == null) {
                child = new TrieNode();
                node.children.put(c,child);
            }
            node = child;
        }
        node.ending = true;
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        keyCheck(word);
        TrieNode node = this.root;

        int len = word.length();
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            node = node.children.get(c);
            if (node == null) return false;
        }
        return node.ending;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        keyCheck(prefix);
        TrieNode node = this.root;

        int len = prefix.length();
        for (int i = 0; i < len; i++) {
            char c = prefix.charAt(i);
            node = node.children.get(c);
            if (node == null) return false;
        }
        return true;
    }

    private void keyCheck(String word) {
        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException("word must not be null or empty !");
        }
    }

    private static class TrieNode {
        boolean ending; //是否为单词的结尾(识别是否完整的单词)
        HashMap<Character,TrieNode> children = new HashMap<>();
    }

}
