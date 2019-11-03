package net.mooctest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeSet;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.PatternSyntaxException;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import org.junit.Test;

import net.mooctest.OST;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class OSTTest {
    @org.junit.Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @org.junit.Rule
    public final TextFromStandardInputStream systemInMock
            = emptyStandardInputStream();
    @org.junit.Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @org.junit.Rule
    public final ExpectedException thrown = ExpectedException.none();
    private boolean isExp = false;

    @After
    public void handleAfter() {
        if (isExp) {
            el("You should never see this message. If you do, please inform the author.");
            systemOutRule.clearLog();
            isExp = false;
        }
    }

    class CauseMatcher extends TypeSafeMatcher {
        private Class<?> expectedType;
        private String expectedCause;

        public CauseMatcher(Class<?> clazz, String str) {
            this.expectedType = clazz;
            this.expectedCause = str;
        }

        protected boolean matchesSafely(Object item) {
            Throwable throwable = (Throwable) item;
            return throwable.getClass().isAssignableFrom(this.expectedType) && throwable.getMessage().contains(this.expectedCause);
        }

        public void describeTo(Description description) {
            description.appendText("expects type ");
        }
    }

    private Method getMethod(Class clazz, String name, Class... parameters) throws Throwable {
        Method method = clazz.getDeclaredMethod(name, parameters);
        method.setAccessible(true);
        return method;
    }

    private Field getField(Class clazz, String fieldName) throws Throwable {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private Field getFinalField(Class clazz, String fieldName) throws Throwable {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        return field;
    }

    private void el(String out) {
        assertEquals(out, systemOutRule.getLog());
    }
    @Test(timeout = 4000)
    public void test() {
        OST<Integer> tree = new OST<>();
        TreeSet<Integer> set = new TreeSet<>();
        assertEquals(set.isEmpty(), tree.isEmpty());
    }

    private final OST<Integer> tree = new OST<>();
    private final TreeSet<Integer> set = new TreeSet<>();

    @Before
    public void before() {
        tree.clear();
        set.clear();
    }

    @Test
    public void testAdd() {
        assertEquals(set.isEmpty(), tree.isEmpty());

        for (int i = 10; i < 30; i += 2) {
            assertTrue(tree.isHealthy());
            assertEquals(set.contains(i), tree.contains(i));
            assertEquals(set.add(i), tree.add(i));
            assertEquals(set.add(i), tree.add(i));
            assertEquals(set.contains(i), tree.contains(i));
            assertTrue(tree.isHealthy());
        }

        assertEquals(set.isEmpty(), tree.isEmpty());
    }

    @Test
    public void testAddAll() {
        for (int i = 0; i < 10; ++i) {
            assertEquals(set.add(i), tree.add(i));
        }

        Collection<Integer> coll = Arrays.asList(10, 9, 7, 11, 12);

        assertEquals(set.addAll(coll), tree.addAll(coll));
        assertEquals(set.size(), tree.size());

        for (int i = -10; i < 20; ++i) {
            assertEquals(set.contains(i), tree.contains(i));
        }
    }

    @Test
    public void testClear() {
        for (int i = 0; i < 2000; ++i) {
            set.add(i);
            tree.add(i);
        }

        assertEquals(set.size(), tree.size());
        set.clear();
        tree.clear();
        assertEquals(set.size(), tree.size());
    }

    @Test
    public void testContains() {
        for (int i = 100; i < 200; i += 3) {
            assertTrue(tree.isHealthy());
            assertEquals(set.add(i), tree.add(i));
            assertTrue(tree.isHealthy());
        }

        assertEquals(set.size(), tree.size());

        for (int i = 0; i < 300; ++i) {
            assertEquals(set.contains(i), tree.contains(i));
        }
    }

    @Test
    public void testContainsAll() {
        for (int i = 0; i < 50; ++i) {
            set.add(i);
            tree.add(i);
        }

        Collection<Integer> coll = new HashSet<>();

        for (int i = 10; i < 20; ++i) {
            coll.add(i);
        }

        assertEquals(set.containsAll(coll), tree.containsAll(coll));
        coll.add(100);
        assertEquals(set.containsAll(coll), tree.containsAll(coll));
    }

    @Test
    public void testRemove() {
        for (int i = 0; i < 200; ++i) {
            assertEquals(set.add(i), tree.add(i));
        }

        for (int i = 50; i < 150; i += 2) {
            assertEquals(set.remove(i), tree.remove(i));
            assertTrue(tree.isHealthy());
        }

        for (int i = -100; i < 300; ++i) {
            assertEquals(set.contains(i), tree.contains(i));
        }
    }

    @Test
    public void testRemoveLast() {
        tree.add(1);
        tree.remove(1);
        assertEquals(0, tree.size());
    }

    @Test
    public void testRemoveAll() {
        for (int i = 0; i < 40; ++i) {
            set.add(i);
            tree.add(i);
        }

        Collection<Integer> coll = new HashSet<>();

        for (int i = 10; i < 20; ++i) {
            coll.add(i);
        }

        assertEquals(set.removeAll(coll), tree.removeAll(coll));

        for (int i = -10; i < 50; ++i) {
            assertEquals(set.contains(i), tree.contains(i));
        }

        assertEquals(set.removeAll(coll), tree.removeAll(coll));

        for (int i = -10; i < 50; ++i) {
            assertEquals(set.contains(i), tree.contains(i));
        }
    }

    @Test
    public void testSize() {
        for (int i = 0; i < 200; ++i) {
            assertEquals(set.size(), tree.size());
            assertEquals(set.add(i), tree.add(i));
            assertEquals(set.size(), tree.size());
        }
    }

    @Test
    public void testIndexOf() {
        for (int i = 0; i < 100; ++i) {
            assertTrue(tree.add(i * 2));
        }

        for (int i = 0; i < 100; ++i) {
            assertEquals(i, tree.indexOf(2 * i));
        }

        for (int i = 100; i < 150; ++i) {
            assertEquals(-1, tree.indexOf(2 * i));
        }
    }

    @Test
    public void testEmpty() {
        assertEquals(set.isEmpty(), tree.isEmpty());
        set.add(0);
        tree.add(0);
        assertEquals(set.isEmpty(), tree.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEmptyTreeGetThrowsOnNegativeIndex() {
        tree.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEmptyTreeSelectThrowsOnTooLargeIndex() {
        tree.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSelectThrowsOnNegativeIndex() {
        for (int i = 0; i < 5; ++i) {
            tree.add(i);
        }

        tree.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSelectThrowsOnTooLargeIndex() {
        for (int i = 0; i < 5; ++i) {
            tree.add(i);
        }

        tree.get(5);
    }

    @Test
    public void testGet() {
        for (int i = 0; i < 100; i += 3) {
            tree.add(i);
        }

        for (int i = 0; i < tree.size(); ++i) {
            assertEquals(Integer.valueOf(3 * i), tree.get(i));
        }
    }

    @Test
    public void findBug() {
        tree.add(0);
        assertTrue(tree.isHealthy());

        tree.add(-1);
        tree.remove(-1);
        assertTrue(tree.isHealthy());

        tree.add(1);
        tree.remove(1);
        assertTrue(tree.isHealthy());

        tree.add(-1);
        tree.add(1);
        tree.remove(0);
        assertTrue(tree.isHealthy());

        tree.clear();
        tree.add(0);
        tree.add(-1);
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(11);
        tree.add(30);
        tree.add(7);

        tree.remove(-1);

        assertTrue(tree.isHealthy());
    }

    @Test
    public void tryReproduceTheCounterBug() {
        long seed = System.nanoTime();
        Random random = new Random(seed);
        List<Integer> list = new ArrayList<>();

        System.out.println("tryReproduceTheCounterBug: seed = " + seed);

        for (int i = 0; i < 10; ++i) {
            int number = random.nextInt(1000);
            list.add(number);
            tree.add(number);
            assertTrue(tree.isHealthy());
        }

        for (Integer i : list) {
            tree.remove(i);
            boolean healthy = tree.isHealthy();
            assertTrue(healthy);
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyIterator() {
        tree.iterator().next();
    }

    @Test
    public void testIteratorThrowsOnDoubleRemove() {
        for (int i = 10; i < 20; ++i) {
            set.add(i);
            tree.add(i);
        }

        Iterator<Integer> iterator1 = set.iterator();
        Iterator<Integer> iterator2 = tree.iterator();

        for (int i = 0; i < 3; ++i) {
            assertEquals(iterator1.next(), iterator2.next());
        }

        iterator1.remove();
        iterator2.remove();

        try {
            iterator1.remove();
            fail("iterator1 should have thrown an exception.");
        } catch (IllegalStateException ex) {

        }

        try {
            iterator2.remove();
            fail("iterator2 should have thrown an exception.");
        } catch (IllegalStateException ex) {

        }
    }


    @Test
    public void testIterator() {
        for (int i = 0; i < 5; ++i) {
            tree.add(i);
            set.add(i);
        }

        Iterator<Integer> iterator1 = set.iterator();
        Iterator<Integer> iterator2 = tree.iterator();

        for (int i = 0; i < 5; ++i) {
            assertEquals(iterator1.hasNext(), iterator2.hasNext());
            assertEquals(iterator1.next(), iterator2.next());
        }

        assertEquals(iterator1.hasNext(), iterator2.hasNext());

        try {
            iterator1.next();
            fail("iterator1 should have thrown an exception.");
        } catch (NoSuchElementException ex) {

        }

        try {
            iterator2.next();
            fail("iterator1 should have thrown an exception.");
        } catch (NoSuchElementException ex) {

        }
    }

    @Test
    public void testRemoveBeforeNextThrowsEmpty() {
        try {
            set.iterator().remove();
            fail("The set iterator should have thrown an exception.");
        } catch (IllegalStateException ex) {

        }

        try {
            tree.iterator().remove();
            fail("The tree iterator should have thrown an exception.");
        } catch (IllegalStateException ex) {

        }
    }

    @Test
    public void testRemoveThrowsWithoutNext() {
        for (int i = 0; i < 10; ++i) {
            tree.add(i);
            set.add(i);
        }

        Iterator<Integer> iterator1 = set.iterator();
        Iterator<Integer> iterator2 = tree.iterator();

        for (int i = 0; i < 4; ++i) {
            assertEquals(iterator1.hasNext(), iterator2.hasNext());
            assertEquals(iterator1.next(), iterator2.next());
        }

        iterator1.remove();
        iterator2.remove();

        try {
            iterator1.remove();
            fail("Set iterator should have thrown an exception.");
        } catch (IllegalStateException ex) {

        }

        try {
            iterator2.remove();
            fail("Tree iterator should have thrown an exception.");
        } catch (IllegalStateException ex) {

        }
    }

    @Test
    public void testRetainAll() {
        for (int i = 0; i < 100; ++i) {
            set.add(i);
            tree.add(i);
        }

        Collection<Integer> coll = Arrays.asList(26, 29, 25);

        assertEquals(set.retainAll(coll), tree.retainAll(coll));
        assertEquals(set.size(), tree.size());

        assertTrue(set.containsAll(tree));
        assertTrue(tree.containsAll(set));
    }

    @Test
    public void testIteratorRemove() {
        for (int i = 10; i < 16; ++i) {
            assertEquals(set.add(i), tree.add(i));
        }

        Iterator<Integer> iterator1 = set.iterator();
        Iterator<Integer> iterator2 = tree.iterator();

        assertEquals(iterator1.hasNext(), iterator2.hasNext());
        assertEquals(iterator1.next(), iterator2.next());

        assertEquals(iterator1.hasNext(), iterator2.hasNext());
        assertEquals(iterator1.next(), iterator2.next());

        iterator1.remove(); // remove 11
        iterator2.remove();

        assertEquals(iterator1.hasNext(), iterator2.hasNext());
        assertEquals(iterator1.next(), iterator2.next());

        assertEquals(iterator1.hasNext(), iterator2.hasNext());
        assertEquals(iterator1.next(), iterator2.next());

        iterator1.remove(); // remove 13
        iterator2.remove();

        assertEquals(set.size(), tree.size());

        for (int i = 10; i < 16; ++i) {
            assertEquals(set.contains(i), tree.contains(i));
        }
    }

    @Test
    public void testIteratorBruteForce() {
        for (int i = 0; i < 10_000; ++i) {
            assertEquals(set.add(i), tree.add(i));
        }

        Iterator<Integer> iterator1 = set.iterator();
        Iterator<Integer> iterator2 = tree.iterator();

        long seed = System.nanoTime();
        Random random = new Random(seed);

        System.out.println("testIteratorBruteForce - seed: " + seed);

        while (true) {
            if (!iterator1.hasNext()) {
                assertFalse(iterator2.hasNext());
            }

            boolean toRemove = random.nextBoolean();

            if (toRemove) {
                try {
                    iterator1.remove();
                    iterator2.remove();
                } catch (IllegalStateException ex) {
                    try {
                        iterator2.remove();
                        fail("iterator2 should have thrown an exception.");
                    } catch (IllegalStateException ex2) {

                    }
                }
            } else {
                assertEquals(iterator1.hasNext(), iterator2.hasNext());

                if (iterator1.hasNext()) {
                    assertEquals(iterator1.next(), iterator2.next());
                } else {
                    break;
                }
            }
        }

        assertEquals(set.size(), tree.size());
        assertTrue(tree.isHealthy());
        assertTrue(set.containsAll(tree));
        assertTrue(tree.containsAll(set));
    }

    @Test
    public void testIteratorConcurrentModification() {
        for (int i = 0; i < 100; ++i) {
            set.add(i);
            tree.add(i);
        }

        Iterator<Integer> iterator1 = set.iterator();
        Iterator<Integer> iterator2 = tree.iterator();

        set.remove(10);
        tree.remove(10);

        assertEquals(iterator1.hasNext(), iterator2.hasNext());

        boolean thrown = false;

        try {
            iterator1.next();
        } catch (ConcurrentModificationException ex) {
            thrown = true;
        }

        if (thrown) {
            try {
                iterator2.next();
                fail("iterator2 should have thrown an exception.");
            } catch (ConcurrentModificationException ex) {

            }
        } else {
            try {
                iterator2.next();
            } catch (ConcurrentModificationException ex) {
                fail("iterator2. should not have thrown an exception.");
            }
        }
    }

    @Test
    public void testIteratorConcurrentRemove() {
        for (int i = 10; i < 20; ++i) {
            set.add(i);
            tree.add(i);
        }

        Iterator<Integer> iterator1 = set.iterator();
        Iterator<Integer> iterator2 = tree.iterator();

        for (int i = 0; i < 4; ++i) {
            iterator1.next();
            iterator2.next();
        }

        // None of them contains 2, should not change the modification count.
        set.remove(2);
        tree.remove(2);

        iterator1.remove();
        iterator2.remove();

        iterator1.next();
        iterator2.next();

        set.remove(12);
        tree.remove(12);

        // Both of them should throw.
        try {
            iterator1.remove();
            fail();
        } catch (ConcurrentModificationException ex) {

        }

        try {
            iterator2.remove();
            fail();
        } catch (ConcurrentModificationException ex) {

        }
    }

    @Test
    public void testConcurrentOrIllegalStateOnRemove() {
        for (int i = 0; i < 10; ++i) {
            set.add(i);
            tree.add(i);
        }

        Iterator<Integer> iterator1 = set.iterator();
        Iterator<Integer> iterator2 = tree.iterator();

        set.add(100);
        tree.add(100);

        try {
            set.iterator().remove();
            fail();
        } catch (IllegalStateException ex) {

        }

        try {
            tree.iterator().remove();
            fail();
        } catch (IllegalStateException ex) {

        }
    }

    @Test
    public void testConcurrentIterators() {
        for (int i = 0; i < 10; ++i) {
            set.add(i);
            tree.add(i);
        }

        Iterator<Integer> iterator1a = set.iterator();
        Iterator<Integer> iterator1b = set.iterator();
        Iterator<Integer> iterator2a = tree.iterator();
        Iterator<Integer> iterator2b = tree.iterator();

        for (int i = 0; i < 3; ++i) {
            iterator1a.next();
            iterator2a.next();
        }

        iterator1a.remove();
        iterator2a.remove();

        assertEquals(iterator1b.hasNext(), iterator2b.hasNext());

        try {
            iterator1b.next();
            fail();
        } catch (ConcurrentModificationException ex) {

        }

        try {
            iterator2b.next();
            fail();
        } catch (ConcurrentModificationException ex) {

        }
    }

    @Test
    public void testToArray() {
        Random r = new Random();

        for (int i = 0; i < 50; ++i) {
            int num = r.nextInt();
            set.add(num);
            tree.add(num);
        }

        assertTrue(Arrays.equals(set.toArray(), tree.toArray()));
    }

    @Test
    public void testToArrayGeneric() {
        for (int i = 0; i < 100; ++i) {
            set.add(i);
            tree.add(i);
        }

        Integer[] array1before = new Integer[99];
        Integer[] array2before = new Integer[99];

        Integer[] array1after = set.toArray(array1before);
        Integer[] array2after = tree.toArray(array2before);

        assertFalse(array1before == array1after);
        assertFalse(array2before == array2after);
        assertTrue(Arrays.equals(array1after, array2after));

        set.remove(1);
        tree.remove(1);

        array1after = set.toArray(array1before);
        array2after = tree.toArray(array2before);

        assertTrue(array1before == array1after);
        assertTrue(array2before == array2after);
        assertTrue(Arrays.equals(array1after, array2after));
    }

    @Test
    public void testToArrayGeneric2() {
        for (int i = 0; i < 100; ++i) {
            set.add(i);
            tree.add(i);
        }

        Integer[] array1before = new Integer[200];
        Integer[] array2before = new Integer[200];

        Integer[] array1after = set.toArray(array1before);
        Integer[] array2after = tree.toArray(array2before);

        assertTrue(array1before == array1after);
        assertTrue(array2before == array2after);
        assertTrue(Arrays.equals(array1after, array2after));

        set.remove(1);
        tree.remove(1);

        array1after = set.toArray(array1before);
        array2after = tree.toArray(array2before);

        assertTrue(array1before == array1after);
        assertTrue(array2before == array2after);
        assertTrue(Arrays.equals(array1after, array2after));
    }
}
