// Author : Shin

import javax.naming.OperationNotSupportedException;
import java.lang.*;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Iterable;
import java.lang.Override;
import java.lang.String;
import java.lang.System;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Heap implements Iterable<Integer>
{
    private Integer key[]  = new Integer[1];
    private int n = 0;

    Heap(Integer a[])
    {
        key = a;
        n = a.length;
    }

    boolean is_empty()
    {
        return n == 0;
    }

    static boolean less(Integer x, Integer y)
    {
        return x < y;
    }

    static boolean more(Integer x, Integer y)
    {
        return x > y;
    }

    static void swap(Integer a[], int i, int j)
    {
        Integer temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private void resize(int size)
    {
        Integer temp[] = new Integer[size];
        for (int i = 0 ;i < key.length; ++i )
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
        while ( 2 * k + 1 < n  )
        {
            int j = 2 * k + 1;
            if ( j < n && more(key[j], key[j + 1]))  ++j;
            if ( !more( key[k], key[j] ) ) break;
            swap(key, k , j);
            k = j;
        }
    }

    void insert (Integer p)
    {
        if (n == key.length)
            resize(2 * key.length);
        key[n++] = p;
        swim(n - 1);
    }

    Integer remove ()
    {
        Integer temp = key[--n];
        swap(key, 0, n);
        if ( n > 0 && n < key.length / 3 )
            resize(key.length / 2);
        return temp;
    }

    int size()
    {
        return n;
    }

    public Iterator<Integer> iterator()
    {
        Iterator<Integer> it = new Iterator<Integer>()
        {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < n;
            }

            @Override
            public Integer next()
            {
                if( count < n )
                    return key[count++];
                else
                    throw new NoSuchElementException();
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
}

class RunApp
{
    public static void main (String arg[]) throws Exception
    {
        //Create Input Provision
        Scanner readIn = new Scanner(System.in);
        int n = readIn.nextInt();
        int m = readIn.nextInt();
        if (n < m)
            throw new OperationNotSupportedException();
        Integer temp;

        //Take Input
        Heap pq = new Heap();
        for ( int i = 0; i < n; ++i )
        {
            temp = readIn.nextInt();
            pq.insert(temp);
            if (pq.size() > m)
                pq.remove();
        }

        //Print Max M Keys
        for (Integer x : pq)
            System.out.print(x + " ");
        System.out.print("\n");
    }
}