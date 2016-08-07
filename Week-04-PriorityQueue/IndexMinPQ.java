//Author : Shin

import java.lang.Integer;
import java.lang.Iterable;
import java.util.*;
import java.lang.*;
import java.util.LinkedList;
import java.util.Queue;

class IndexMinPQ<Key extends Comparable<Key>>
{
    private int maxN;
    private int n;
    private int pq[];
    private int qp[];
    private Key keys[];

    IndexMinPQ(int maxN)
    {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        this.n    = 0;
        keys = (Key[])new Comparable[maxN];
        pq = new int[maxN];
        qp = new int[maxN];

        for (int i = 0; i < maxN; ++i)
            qp[i] = -1;
    }

    public boolean is_empty()  { return n == 0; }

    public boolean contains(int i)
    {
        if (i < 0 || i > maxN) throw new IndexOutOfBoundsException();
        return qp[i] != -1;
    }

    public int size()  { return n; }

    public void insert(int k, Key key)
    {
        if (k < 0 || k > maxN)  throw new IndexOutOfBoundsException();
        if (contains(k))  throw new IllegalArgumentException();
        qp[k] = n;
        pq[n] = k;
        keys[k] = key;
        swim(n);
        n++;
    }

    public int min_index()  { return pq[0]; }

    public Key min_key()  { return keys[pq[0]]; }

    public int del_min()
    {
        int min = pq[0];
        swap(0, --n);
        sink(0);
        qp[min] = -1;
        keys[min] = null;
        pq[n] = -1;
        return min;
    }

    public Key key_of(int i)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i))       throw new NoSuchElementException();
        else return keys[i];
    }

    public void change_key(int i)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i))       throw new NoSuchElementException();
        swim(qp[i]);
        sink(qp[i]);
    }

    public void decrease_key(int i, Key key)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i))       throw new NoSuchElementException();
        if (keys[i].compareTo(key) <= 0)  throw new IllegalArgumentException();
        keys[i] = key;
        swim(qp[i]);
    }

    public void increase_key(int i, Key key)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i))       throw new NoSuchElementException();
        if (keys[i].compareTo(key) >= 0)  throw new IllegalArgumentException();
        keys[i] = key;
        sink(qp[i]);
    }

    public void delete(int i)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException();
        int index = qp[i];
        swap(index, --n);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

    private boolean greater(int i, int j)
    {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    void swap(int i, int j)
    {
        int temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    private void swim(int k)
    {
        while (k > 0 && greater((k - 1) / 2, k))
        {
            swap(k, (k - 1) / 2);
            k = (k - 1) / 2;
        }
    }

    private void sink(int k)
    {
        while (2 * k + 2 < n)
        {
            int j = k * 2 + 1;
            if (j < n &&  greater(j, j+1)) ++j;
            if (!greater(k, j))  break;
            swap(k, j);
            k = j;
        }
    }

    public Iterable<Integer> index()
    {
        IndexMinPQ<Key> copy = new IndexMinPQ<Key>(pq.length);
        Queue<Integer> list  = new LinkedList<Integer>();

        for (int i = 0; i < n; ++i)
            copy.insert(pq[i], keys[pq[i]]);

        while (!copy.is_empty())  list.add(copy.del_min());

        return list;
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take Input
        Scanner readIn = new Scanner(System.in);
        int maxN = readIn.nextInt();
        int n    = readIn.nextInt();

        IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(maxN);
        for (int i = 0; i < n; ++i)
            pq.insert(i, readIn.nextInt());

        //Display Priority Queue
        for (Integer x : pq.index())
            System.out.println(x + " " + pq.key_of(x));
        System.out.println();

        //Take decrease query
        int m = readIn.nextInt();
        for (int i = 0; i < m; ++i)
        {
            int k      = readIn.nextInt();
            int newKey = readIn.nextInt();
            pq.decrease_key(k, newKey);
        }

        //Re-Display Priority Queue
        for (Integer x : pq.index())
            System.out.println(x + " " + pq.key_of(x));
        System.out.println();
    }
}