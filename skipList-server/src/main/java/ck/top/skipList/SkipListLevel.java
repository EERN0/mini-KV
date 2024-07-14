package ck.top.skipList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储跳表每层对应的节点
 */
public class SkipListLevel<K, V> implements Serializable {
    private int level;
    private List<String> nodes;

    public SkipListLevel(int level) {
        this.level = level;
        this.nodes = new ArrayList<>();
    }

    public int getLevel() {
        return level;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void addNode(K key, V value) {
        this.nodes.add(key + ":" + value);
    }
}
