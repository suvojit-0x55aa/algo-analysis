//Author : Shin

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.System;
import java.util.Scanner;

class LPHashMap<Key, Value>
{
    private int m, n = 0;
    private Node table[];

    private static class Node
    {
        Object key;
        Object value;

        Node(Object key, Object value)
        {
            this.key = key;
            this.value = value;
        }
    }

    private void resize(int size)
    {
        Node temp[] = new Node[size];
        for (int i = 0 ;i < table.length; ++i )
        {
            if (table[i] == null)  continue;;
            int h = size_hash((Key)table[i].key, size);
            for (; temp[h] != null; h = (h+1) % m);
            temp[h] = table[i];
        }
        m = size;
        table = temp;
    }

    private LPHashMap(int m)
    {
        this.m = m;
        table = new Node[m];
    }

    public LPHashMap()
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
        for (int i = hash(key); table[i] != null; i = (i+1) % m)
            if (key.equals((Key)table[i].key))  return (Value)table[i].value;
        return null;
    }

    public void put(Key key, Value value)
    {
        if (size() == m)
            resize(2*m);
        int i;
        for (i = hash(key); table[i] != null; i = (i+1) % m)
            if (key.equals((Key)table[i].key))
                break;
        table[i] = new Node(key, value);
        n++;
    }

    public boolean contains(Key key)
    {
        for (int i = hash(key); table[i] != null; i = (i+1) % m)
            if (key.equals((Key)table[i].key))  return true;
        return false;
    }
}

class RunApp
{
    public static void main (String arg[])
    {
        LPHashMap<Integer,String> symTab = new LPHashMap<Integer, String>();

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
