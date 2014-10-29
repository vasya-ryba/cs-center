package ru.compscicenter.java2014.collections;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Collection;
import java.util.Stack;


/**
 * Created by kano_vas on 25/10/14.
 */
public class MyMultiSet<T> implements MultiSet<T> {

    private TreeSet<T> set;
    private Map<T, Integer> map;
    private int size;

    private class MyIterator implements Iterator {
        private int nodePos, pos, amount;
        private T cur;
        private Iterator<T> it;

        MyIterator() {
            if (size == 0) {
                return;
            }
            it = set.iterator();
            cur = it.next();
            amount = map.get(cur);
            nodePos = 0;
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (nodePos < amount) {
                nodePos++;
            } else {
                cur = it.next();
                amount = map.get(cur);
                nodePos = 1;
            }
            pos++;
            return cur;
        }

        @Override
        public void remove() {
            int num = map.remove(cur);
            if (num == 1) {
                set.remove(cur);
            } else {
                map.put((T) cur, num - 1);
            }
            size--;
            pos--;
            nodePos--;
            amount--;
        }
    }

    MyMultiSet() {
        set = new TreeSet<>();
        map = new HashMap<>();
        size = 0;
    }

    MyMultiSet(final Collection<? extends T> c) {
        set = new TreeSet<>();
        map = new HashMap<>();
        size = 0;
        for (T o : c) {
            add(o);
        }
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final boolean isEmpty() {
        return size == 0;
    }

    @Override
    public final boolean contains(final Object o) {
        return set.contains(o);
    }

    @Override
    public final Iterator<T> iterator() {
        return new MyIterator();
    }

    @Override
    public final boolean add(final Object o) {
        if (set.contains(o)) {
            int num = map.remove(o);
            map.put((T) o, ++num);
        } else {
            set.add((T) o);
            map.put((T) o, 1);
        }
        size++;
        return true;
    }

    @Override
    public final int add(final Object o, final int occurrences) {
        int num;
        if (set.contains(o)) {
            num = map.remove(o);
            map.put((T) o, num + occurrences);
        } else {
            num = 0;
            set.add((T) o);
            map.put((T) o, occurrences);
        }
        size += occurrences;
        return num;
    }

    @Override
    public final boolean remove(final Object e) {
        if (!set.contains(e)) {
            return false;
        }
        int num = map.remove(e);
        if (num == 1) {
            set.remove(e);
        } else {
            map.put((T) e, num - 1);
        }
        size--;
        return true;
    }

    private boolean forceRemove(final Object e) {
        if (!set.contains(e)) {
            return false;
        }
        int num = map.remove(e);
        set.remove(e);
        size -= num;
        return true;
    }

    @Override
    public final boolean addAll(final Collection<? extends T> c) {
        boolean ans = false;
        for (Object o : c) {
            if (add(o)) {
                ans = true;
            }
        }
        return ans;
    }

    @Override
    public final int remove(final Object e, final int occurrences) {
        if (!set.contains(e)) {
            return 0;
        }
        int num = map.remove(e);
        if (num <= occurrences) {
            set.remove(e);
        } else {
            map.put((T) e, num - occurrences);
        }
        size -= occurrences;
        return num;
    }

    @Override
    public final int count(final Object e) {
        if (set.contains(e)) {
            return map.get(e);
        }
        return 0;
    }

    @Override
    public final Object[] toArray() {
        Object[] a = new Object[size];
        int pos = 0;
        for (Object o : set) {
            for (int i = 0; i < map.get(o); i++) {
                a[pos++] = o;
            }
        }
        return a;
    }

    @Override
    public final void clear() {
        set.clear();
        map.clear();
        size = 0;
    }

    @Override
    public final boolean retainAll(final Collection c) {     //yep i don't know how to copy interface arguments so
        boolean ans = false;                     //i made a copy using temporal multiset. hope somebody somewhere
        Stack<Object> trash = new Stack<>();     //will explain this thing to me

        MyMultiSet<T> tmp = new MyMultiSet<T>();
        for (Object o : c) {
            tmp.add(o);
        }

        for (Object o : this) {
            if (!tmp.contains(o)) {
                trash.push(o);
            }
        }
        while (!trash.isEmpty()) {
            if (forceRemove(trash.pop())) {
                ans = true;
            }
        }
        return ans;
    }

    @Override
    public final boolean removeAll(final Collection c) {
        boolean ans = false;
        for (Object o : c) {
            if (forceRemove(o)) {
                ans = true;
            }
        }
        return ans;
    }

    @Override
    public final T[] toArray(final Object[] a) {
        Object[] ans = a;
        if (a.length < size) {
            ans = new Object[size];
        }
        int pos = 0;
        for (Object o : set) {
            for (int i = 0; i < map.get(o); i++) {
                ans[pos++] = o;
            }
        }
        return (T[]) ans;
    }

    @Override
    public final boolean containsAll(final Collection c) {
        for (Object o : c) {
            if (!set.contains(o)) {
                return false;
            }
        }
        return true;
    }
    public final boolean equals(final Object o) {

        MyMultiSet<T> tmp = (MyMultiSet<T>) o;
        if (tmp.size() != size) {
            return false;
        }
        Iterator i = this.iterator();
        Iterator j = tmp.iterator();
        for ( ; i.hasNext() && j.hasNext();) {
            if (i.next().equals(j.next())) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        return map.hashCode();
    }

    public final void print() {
        for (Object o : this) {
            System.out.print(o + " ");
        }
        System.out.println();
    }
}
