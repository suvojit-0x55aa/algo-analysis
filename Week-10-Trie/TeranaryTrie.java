// Author : Shin

import java.lang.*;
import java.lang.StringBuilder;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class TST<Value>
{
    private int n;
    private Node root;

    private static class Node<Value>
    {
        private char c;
        private Node<Value>  left, middle, right;
        private Value val;
    }

    public TST() {}

    public int size()  { return n; }
    public boolean is_empty()  { return n == 0; }

    private Node put(Node x, String key, Value value, int d)
    {
        char c = key.charAt(d);
        if (x == null)
        {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left   = put(x.left,   key, value, d);
        else if (c > x.c)               x.right  = put(x.right,  key, value, d);
        else if (d < key.length() - 1)  x.middle = put(x.middle, key, value, d + 1);
        else                            x.val = value;
        return x;
    }

    public void put(String key, Value value)
    {
        if (!contains(key))  ++n;
        root = put(root, key, value, 0);
    }

    private Node get(Node x, String key, int d)
    {
        if (x == null)  return null;
        char c = key.charAt(d);
        if      (c < x.c)               return get(x.left,   key, d);
        else if (c > x.c)               return get(x.right,  key, d);
        else if (d < key.length() - 1)  return get(x.middle, key, d + 1);
        else                            return x;
    }

    public Value get(String key)
    {
        if (key == null)  throw new NullPointerException();
        if (key.length() == 0)  throw new IllegalArgumentException();
        Node<Value> x = get(root, key, 0);
        if (x == null)  return null;
        return x.val;
    }

    public boolean contains(String key)  { return get(key) != null; }

    public void delete(String key)
    {
        if (!contains(key))  throw new IllegalArgumentException();
        n--;
        put(key, null);
    }

    public String longest_prefix(String query)
    {
        if (query == null || query.length() == 0)  return null;
        int length = 0, i = 0;
        Node<Value> x = root;
        while (x != null && i < query.length() - 1)
        {
            char c = query.charAt(i);
            if (c < x.c)  x = x.left;
            else if (c > x.c)  x = x.right;
            else
            {
                i++;
                if (x.val != null)  length = i;
                x = x.middle;
            }
        }
        return query.substring(0, length);
    }

    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> q)
    {
        if (x == null)  return;
        collect(x.left, prefix, q);
        if (x.val != null)  q.add(prefix.toString() + x.c);
        collect(x.middle, prefix.append(x.c), q);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, q);
    }

    public Iterable<String> keys()
    {
        Queue<String> list = new LinkedList<String>();
        collect(root, new StringBuilder(), list);
        return list;
    }

    public Iterable<String> prefix_keys(String prefix)
    {
        Queue<String> list = new LinkedList<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return list;
        if (x.val != null)  list.add(prefix);
        collect(x.middle, new StringBuilder(prefix), list);
        return list;
    }

    private void collect(Node<Value> x, StringBuilder prefix, int d, String pattern, Queue<String> q)
    {
        if (x == null)  return;
        char c = pattern.charAt(d);
        if (c == '.' || c < x.c)  collect(x.left,  prefix, d, pattern, q);
        if (c == '.' || c > x.c)  collect(x.right, prefix, d, pattern, q);
        if (c == '.' || c == x.c)
        {
            if (d == pattern.length() && x.val != null)  q.add(prefix.toString());
            if (d < pattern.length() - 1)
            {
                collect(x.middle, prefix.append(x.c), d + 1, pattern, q);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    public Iterable<String> wildcard_match(String pattern)
    {
        if (pattern == null || pattern.length() == 0)  throw new IllegalArgumentException();
        Queue<String> list = new LinkedList<String>();
        collect(root, new StringBuilder(), 0, pattern, list);
        return list;
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take Input
        Scanner readIn = new Scanner(System.in);
        TST<Integer> trie = new TST<Integer>();
        for (int i = 0; readIn.hasNext(); ++i)
            trie.put(readIn.next(), i);

        //Print All String
        for (String s : trie.keys())
            System.out.println(s + " " + trie.get(s));
        System.out.println();

        //Print Wildcard Match
        for (String s : trie.wildcard_match("s...."))
            System.out.println(s);
        System.out.println();

        //Print Longest Prefix
        System.out.println(trie.longest_prefix("shellsort") + "\n");

        //Print String With Prefix
        for (String s : trie.prefix_keys("sh"))
            System.out.println(s);
        System.out.println();

       //Delete and Print
        trie.delete("shore");
        for (String s : trie.keys())
            System.out.println(s + " " + trie.get(s));
        System.out.println();
    }
}