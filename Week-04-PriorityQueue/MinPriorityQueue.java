// Author : Shin

import java.lang.*;
import java.lang.System;
import java.util.*;

class MinPQ<Item extends Comparable<Item>>
{
    private Item key[];
    private int n;

    MinPQ(int n)
    {
        key = (Item[]) new Comparable[n];
        n   = 0;
    }

    boolean is_empty()
    {
        return n == 0;
    }

    int size()  { return n; }

    boolean less(Item x, Item y)
    {
        return x.compareTo(y) < 0;
    }

    boolean more(Item x, Item y)
    {
        return x.compareTo(y) > 0;
    }

    void swap(Item a[], int i, int j)
    {
        Item temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private void resize(int size)
    {
        Item temp[] = (Item[])new Comparable[size];
        for (int i = 0 ;i < n; ++i )
            temp[i] = key[i];
        key = temp;
    }

    private void swim(int k)
    {
        while (k > 0 && more(key[(k - 1) / 2], key[k]))
        {
            swap(key, (k - 1)/2, k);
            k = (k - 1) / 2;
        }
    }

    private void sink (int k)
    {
        while ( 2 * k + 2 < n )
        {
            int j = 2 * k + 1;
            if ( j < n && more(key[j], key[j + 1]))  ++j;
            if ( !more(key[k], key[j]) ) break;
            swap(key, k , j);
            k = j;
        }
    }

    void insert (Item p)
    {
        if (n == key.length)
            resize(2 * key.length);
        key[n++] = p;
        swim(n - 1);
    }

    Item remove ()
    {
        Item min = key[0];
        swap(key, 0, --n);
        key[n] = null;
        sink(0);
        if ( n > 0 && n < key.length / 3 )
            resize(key.length / 2);
        return min;
    }
}

class RunApp
{
    public static void main (String arg[]) throws Exception
    {
        //Create Input Provision
        Scanner readIn = new Scanner(System.in);
        int n = readIn.nextInt();
        Integer temp;

        //Take Input
        MinPQ<Integer> pq = new MinPQ<Integer>(1);
        for ( int i = 0; i < n; ++i )
        {
            temp = readIn.nextInt();
            pq.insert(temp);
        }

        //Print Max M Keys
        while (!pq.is_empty())
            System.out.print(pq.remove() + " ");
        System.out.print("\n");
    }
}