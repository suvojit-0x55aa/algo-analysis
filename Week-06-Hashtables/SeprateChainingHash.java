//Author : Shin

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.System;
import java.util.Scanner;

class SCHashMap<Key, Value>
{
    private int m,n = 0;
    private Node table[];

    private static class Node
    {
        Object key;
        Object value;
        Node next = null;

        Node(Object key, Object value, Node next)
        {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize(int size)
    {
        Node temp[] = new Node[size];
        for (int i = 0 ;i < table.length; ++i )
            for (Node x = table[i]; x != null; x = x.next)
            {
                int h = size_hash((Key)x.key,size);
                Node p = new Node(x.key, x.value, temp[h]);
                temp[h] = p;
            }
        m = size;
        table = temp;
    }

    private SCHashMap(int m)
    {
        this.m = m;
        table = new Node[m];
    }

    public SCHashMap()
    {
        this(1);
    }

    public int size()
    {
        return n;
    }

    public boolean is_empty()
    {
        return n == 0;
    }

    private int hash(Key key)
    {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    private int size_hash(Key key, int size)
    {
        return (key.hashCode() & 0x7fffffff) % size;
    }

    public Value get(Key key)
    {
        int i = hash(key);
        for (Node x = table[i]; x != null; x = x.next)
            if (key.equals(x.key))  return (Value)x.value;
        return null;
    }

    public void put(Key key, Value value)
    {
        if (n/m > 3)
            resize(2*m);
        int i = hash(key);
        Node x = new Node(key, value, table[i]);
        table[i] = x;
        n++;
    }

    public boolean contains(Key key)
    {
        int i = hash(key);
        for (Node x = table[i]; x != null; x = x.next)
            if (key.equals(x.key))  return true;
        return false;
    }
}

class RunApp
{
    public static void main (String arg[])
    {
        SCHashMap<Integer,String> symTab = new SCHashMap<Integer, String>();

        //Take In-put
        Scanner readIn = new Scanner(System.in);
        Integer n = readIn.nextInt();
        Integer key;
        String val;
        for (int i = 0; i < n; ++i)
        {
            key = readIn.nextInt();
            val = readIn.next();
            symTab.put(key, val);
        }

        //Input Query
        n = readIn.nextInt();
        for (int i = 0; i < n; ++i)
        {
            key = readIn.nextInt();
            System.out.println(key + " -> " + symTab.get(key));
        }
    }
}
