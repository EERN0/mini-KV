package ck.top.skipList;

import java.io.*;

/**
 * 数据持久化和加载磁盘文件到内存
 */
public class SkipListPersistence<K extends Comparable<K>, V> {
    private static final String STORE_FILE = "./skipList-KV/store";

    /**
     * 持久化跳表数据至磁盘文件
     *
     * @param skipList 跳表实例
     */
    public void flush(SkipList<K, V> skipList) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(STORE_FILE))) {
            // 只需要存最底层的全量数据
            Node<K, V> node = skipList.getHeader().getNext().get(0);
            while (node != null) {
                String data = node.getKey() + ":" + node.getValue() + ";";
                bufferedWriter.write(data);
                bufferedWriter.newLine();
                node = node.getNext().get(0);
            }
        } catch (IOException e) {
            throw new RuntimeException("写入文件失败", e);
        }
    }

    /**
     * 从磁盘文件读取数据加载到内存的跳表实例
     *
     * @return 加载后的跳表实例
     */
    public SkipList<K, V> load(SkipList<K, V> skipList) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(STORE_FILE))) {
            String data;
            // 读出每一行的key value
            while ((data = bufferedReader.readLine()) != null) {
                Node<K, V> node = parse(data);
                if (node != null) {
                    skipList.insert(node.getKey(), node.getValue());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return skipList;
    }

    /**
     * 解析字符串，获取 key value
     *
     * @param data 字符串 key:value;
     * @return 将 key 和 value 封装到 Node 对象中
     */
    private Node<K, V> parse(String data) {
        if (!isValid(data)) return null;
        // 截取key
        String k1 = data.substring(0, data.indexOf(":"));
        K key = (K) k1;
        // 截取value
        String v1 = data.substring(data.indexOf(":") + 1, data.length() - 1);
        V value = (V) v1;
        return new Node<K, V>(key, value, 0);
    }

    /**
     * 验证读取的字符串是否为 k1:v1; 形式
     *
     * @param data 字符串
     */
    private boolean isValid(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        // 检查是否包含分号
        return data.contains(":");
    }
}
