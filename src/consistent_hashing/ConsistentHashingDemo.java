package consistent_hashing;

import java.util.*;

public class ConsistentHashingDemo {

    // key的hash和物理节点的映射关系
    private SortedMap<Integer, String> nodeMap = new TreeMap<>();

    // 物理节点所对应的虚拟节点个数
    private int virtualNodeCount;

    public ConsistentHashingDemo(List<String> realNodes, int virtualNodeCount) {
        this.virtualNodeCount = virtualNodeCount;
        realNodes.forEach(this::addRealNode);
    }

    /**
     * 增加节点
     *
     * @param nodeIp nodeIp
     */
    public void addRealNode(String nodeIp) {
        for (int i = 0; i < virtualNodeCount; i++) {
            // 对每个物理节点，生成virtualNodeCount个虚拟节点
            int hash = hash(nodeIp + "&VN&" + i);
            // 存储hash和物理节点的映射关系
            nodeMap.put(hash, nodeIp);
        }
    }

    /**
     * 根据key获取对应的物理节点
     *
     * @param key key
     * @return 对应的物理节点
     */
    private String getRealNode(String key) {
        // 计算hash值
        int hash = hash(key);
        System.out.println(key + "的hash值为" + hash);

        // 得到大于该hash值的部分Map
        SortedMap<Integer, String> tailMap = nodeMap.tailMap(hash);

        if (tailMap.isEmpty()) {
            // 如果tailMap为空，说明没有比当前hash值大的，则返回第一个node
            return nodeMap.get(nodeMap.firstKey());
        } else {
            // 否则直接返回下一个node
            return tailMap.get(tailMap.firstKey());
        }
    }

    /**
     * 使用FNV1_32_HASH算法，效率更高
     *
     * @param key key
     * @return hash结果
     */
    private int hash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++)
            hash = (hash ^ key.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    public static void main(String[] args) {
        // 注册真实物理节点
        List<String> nodeIps = Arrays.asList("192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5");
        ConsistentHashingDemo demo = new ConsistentHashingDemo(nodeIps, 5);

        // 将不同的用户信息存储到不同的节点
        List<String> users = Arrays.asList("小明", "小红", "小亮", "小帅", "小黑", "小白");
        users.forEach(user -> {
            String realNode = demo.getRealNode(user);
            System.out.println(user + "对应的物理节点为" + realNode);
        });
    }
}
