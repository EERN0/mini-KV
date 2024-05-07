package ck.top.backend.SkipList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SkipList<K extends Comparable<K>, V> {

    private static final int MAX_LEVEL = 32;    // 跳表的最大层数

    private final Node<K, V> header;      // 每一层都有头节点，不存实际数据

    private int nodeNum;            // 跳表中的节点数量

    private int skipListLevel;      // 跳表当前的层级

    public SkipList() {
        // 初始化跳表头节点，其层级等于跳表的最大层级
        this.header = new Node<>(null, null, MAX_LEVEL);
        // 设置跳表当前层级为 0，节点计数为 0
        this.nodeNum = 0;
        this.skipListLevel = 0;
    }

    /**
     * 创建新的Node节点
     *
     * @param key   键
     * @param value 值
     * @param level 节点所在层级
     * @return 返回创建后的节点
     */
    private Node<K, V> createNode(K key, V value, int level) {
        return new Node<>(key, value, level);
    }

    /**
     * 按一定概率随机生成节点的最高层级（最高层及以下都包含这个节点）：
     * 第1层(最底层) 100%，第2层 50%，第3层 25%
     *
     * @return 返回节点层级
     */
    private static int generateRandomLevel() {
        int level = 1;
        Random random = new Random();
        while (random.nextInt(2) == 1) {    // [0,1]随机选一个的概率为0.5
            level++;
        }
        return Math.min(level, MAX_LEVEL);
    }

    /**
     * @return 返回跳表中节点的数量
     */
    public int size() {
        return this.nodeNum;
    }

    /**
     * 向跳表中插入一个键值对，如果跳表中已经存在相同 key 的节点，则更新这个节点的 value
     *
     * @param key   插入的 Node 的键
     * @param value 插入的 Node 的值
     * @return 插入成功返回 true，插入失败返回 false
     */
    public synchronized boolean insert(K key, V value) {
        // 从跳表最高层的头节点开始搜索 key 待插入的位置
        Node<K, V> cur = this.header;
        // 更新各层链表指向的数组，存待插入key节点的前驱
        ArrayList<Node<K, V>> prevNodes = new ArrayList<>(Collections.nCopies(MAX_LEVEL + 1, null));

        // 从最高层向下搜索插入位置
        for (int i = this.skipListLevel; i >= 0; i--) {
            // 寻找i层小于且最接近key的节点
            while (cur.getNext().get(i) != null && cur.getNext().get(i).getKey().compareTo(key) < 0) {
                cur = cur.getNext().get(i);
            }
            // i层cur的下一个节点 >= key，cur作为待插入节点的前驱
            prevNodes.set(i, cur);
        }
        // 移动到最底层的下一个节点，准备插入
        cur = cur.getNext().get(0);
        // 检查待插入的节点的key是否等于cur
        if (cur != null && cur.getKey().compareTo(key) == 0) {
            // key已存在，更新节点的value
            cur.setValue(value);
            return true;
        }

        // 随机生成节点的层数
        int randomLevel = generateRandomLevel();
        // 跳表中不存在这个key
        if (cur == null || cur.getKey().compareTo(key) != 0) {
            // 生成节点的层数 大于 跳表当前的层数
            if (randomLevel > this.skipListLevel) {
                for (int i = this.skipListLevel + 1; i <= randomLevel; i++) {
                    // 新建一层，开始为头节点
                    prevNodes.set(i, header);
                }
                // 更新跳表的最高层数
                this.skipListLevel = randomLevel;
            }
            // 待插入的节点
            Node<K, V> node = createNode(key, value, randomLevel);

            // 修改每一层里面链表节点的指向
            for (int i = 0; i <= randomLevel; i++) {
                // i层 当前节点的next指向 前驱节点的next
                node.getNext().set(i, prevNodes.get(i).getNext().get(i));
                prevNodes.get(i).getNext().set(i, node);
            }
            nodeNum++;
            return true;
        }
        return false;
    }

    /**
     * 判断跳表中是否存在键为 key 的键值对
     *
     * @param key 键
     * @return 跳表中存在键为 key 的键值对返回 true，不存在返回 false
     */
    public boolean isExist(K key) {
        Node<K, V> cur = this.header;

        for (int i = this.skipListLevel; i >= 0; i--) {
            while (cur.getNext().get(i) != null && cur.getNext().get(i).getKey().compareTo(key) < 0) {
                cur = cur.getNext().get(i);
            }
            // cur小于key，cur下一个节点为null 或者  >= key，直接去下一层搜
        }
        // cur仍然小于key，拿到最底层的 >= key的节点
        cur = cur.getNext().get(0);
        return cur != null && cur.getKey().compareTo(key) == 0;
    }

    /**
     * 查找节点key对应的value
     *
     * @param key 键
     * @return 返回键为 key 的节点，如果不存在则返回 null
     */
    public V get(K key) {
        Node<K, V> cur = this.header;
        for (int i = this.skipListLevel; i >= 0; i--) {
            while (cur.getNext().get(i) != null && cur.getNext().get(i).getKey().compareTo(key) < 0) {
                cur = cur.getNext().get(i);
            }
        }
        cur = cur.getNext().get(0);
        if (cur != null && cur.getKey().compareTo(key) == 0) {
            return cur.getValue();
        }
        return null;
    }

    /**
     * 根据key移除节点
     *
     * @param key 键
     * @return 成功返回true；失败返回false
     */
    public synchronized boolean remove(K key) {
        Node<K, V> cur = this.header;
        ArrayList<Node<K, V>> prevNodes = new ArrayList<>(Collections.nCopies(MAX_LEVEL + 1, null));

        // 从最高层开始向下搜索待删除的节点
        for (int i = this.skipListLevel; i >= 0; i--) {
            while (cur.getNext().get(i) != null && cur.getNext().get(i).getKey().compareTo(key) < 0) {
                cur = cur.getNext().get(i);
            }
            // cur小于key，是待删除节点的前驱，记录每一层的前驱
            prevNodes.set(i, cur);
        }
        // 移动到最底层 第一个为null 或者 大于等于 key 的节点
        cur = cur.getNext().get(0);

        // cur值为key，是待删除的节点
        if (cur != null && cur.getKey().compareTo(key) == 0) {
            // 删掉每一层为key的节点
            for (int i = 0; i <= this.skipListLevel; i++) {
                // i层cur节点的前驱 prevNodes.get(i)
                if (prevNodes.get(i).getNext().get(i) != cur) break;

                prevNodes.get(i).getNext().set(i, cur.getNext().get(i));
            }
            // 更新跳表的层数，删去只剩头节点的层，保存 level 0
            while (this.skipListLevel > 0 && this.header.getNext() == null) {
                this.skipListLevel--;
            }

            System.out.println("成功删除key: " + key);
            this.nodeNum--;
            return true;
        }
        System.out.println("key: " + key + "不存在");
        return false;
    }
}
