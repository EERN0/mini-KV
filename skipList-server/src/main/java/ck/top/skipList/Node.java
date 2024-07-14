package ck.top.skipList;

import java.util.ArrayList;
import java.util.Collections;

public class Node<K extends Comparable<K>, V> {
    private K key;
    private V value;
    private int level;    // 节点所在的层级
    private ArrayList<Node<K, V>> next;     // cur.next.get(i): 节点cur在i层的下一个节点的引用

    Node(K key, V value, int level) {
        this.key = key;
        this.value = value;
        this.level = level;
        this.next = new ArrayList<>(Collections.nCopies(level + 1, null));
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<Node<K, V>> getNext() {
        return next;
    }

    public void setNext(ArrayList<Node<K, V>> next) {
        this.next = next;
    }
}
