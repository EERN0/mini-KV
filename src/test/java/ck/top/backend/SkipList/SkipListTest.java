package ck.top.backend.SkipList;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SkipListTest {

    private SkipList<String, String> skipList;

    @Before
    public void setUp() {
        skipList = SkipList.getInstance();
    }

    @Test
    public void testInsertAndSize() {
        skipList.insert("1", "Value1");
        skipList.insert("2", "Value2");
        skipList.insert("3", "Value3");

        assertEquals(3, skipList.size());
    }

    @Test
    public void testInsertAndGet() {
        skipList.insert("1", "Value1");
        skipList.insert("2", "Value2");

        assertEquals("Value1", skipList.get("1"));
        assertEquals("Value2", skipList.get("2"));
        assertNull(skipList.get("3")); // 不存在的 key 返回 null
    }

    @Test
    public void testInsertAndRemove() {
        skipList.insert("1", "Value1");
        skipList.insert("2", "Value2");
        skipList.insert("3", "Value3");

        skipList.remove("2");

        assertNull(skipList.get("2")); // 已删除的 key 返回 null
        assertEquals(2, skipList.size()); // 删除一个节点后，跳表的大小应减小
    }

    @Test
    public void testIsExist() {
        skipList.insert("1", "Value1");
        skipList.insert("2", "Value2");

        assertTrue(skipList.isExist("1"));
        assertFalse(skipList.isExist("3")); // 不存在的 key 返回 false
    }
}