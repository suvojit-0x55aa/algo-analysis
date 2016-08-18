// Author : Shin

import java.lang.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class TrieST<Value>
{
    private static final int R = 256;
    private Node root = new Node();
    private int n;

    private static class Node
    {
        private Object val;
        private Node next[] = new Node[R];
    }

    public TrieST() {}

    private Node get(Node x, String key, int d)
    {
        if (x == null)  return null;
        if (d == key.length())  return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    public Value get(String key)
    {
        Node x = get(root, key, 0);
        if (x == null)  return null;
        else            return (Value)x.val;
    }

    public boolean contains(String key)  { return get(key) != null; }

    private Node put(Node x, String key, Value val, int d)
    {
        if (x == null)  x = new Node();
        if (d == key.length())
        {
            if (x.val == null)  ++n;
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    public void put(String key, Value val)
    {
        root = put(root, key, val, 0);
    }

    public int size()  { return n; }

    public boolean is_empty()  { return n == 0; }

    private Node delete(Node x, String key, int d)
    {
        if (x == null)  return null;
        if (d == key.length())
        {
            if (x.val != null) n--;
            x.val = null;
        }
        else
        {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        if (x.val != null)  return x;
        for (char c = 0; c < R; ++c)
            if (x.next[c] != null)  return x;
        return null;
    }

    public void delete(String key)
    {
        root = delete(root, key, 0);
    }

    public Iterable<String> key_prefix(String prefix)
    {
        Queue<String> list = new LinkedList<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), list);
        return list;
    }

    private void collect(Node x, StringBuilder s, Queue<String> q)
    {
        if (x == null)  return;
        if (x.val != null)  q.add(s.toString());
        for (char c = 0; c < R; c++)
        {
            s.append(c);
            collect(x.next[c], s, q);
            s.deleteCharAt(s.length() - 1);
        }
    }

    public Iterable<String> keys()  { return key_prefix(""); }

    public Iterable<String> wildcard_match(String pattern)
    {
        Queue<String> list = new LinkedList<String>();
        collect(root, new StringBuilder(), pattern, list);
        return list;
    }

    private void collect(Node x, StringBuilder s, String pattern, Queue<String> q)
    {
        if (x == null)  return;
        int d = s.length();
        if (d == pattern.length() && x.val != null)  q.add(s.toString());
        if (d == pattern.length())  return;
        char c = pattern.charAt(d);
        if (c == '.')
        {
            for (char ch = 0; ch < R; ++ch)
            {
                s.append(ch);
                collect(x.next[ch], s, pattern, q);
                s.deleteCharAt(s.length() - 1);
            }
        }
        else
        {
            s.append(c);
            collect(x.next[c], s, pattern, q);
            s.deleteCharAt(s.length() - 1);
        }
    }

    public String longest_prefix(String query)
    {
        int length = longest_prefix(root, query, 0, -1);
        if (length == -1)  return null;
        else return query.substring(0, length);
    }

    private int longest_prefix(Node x, String query, int d, int length)
    {
        if (x == null)  return length;
        if (x.val != null)  length = d;
        if (d == query.length())  return length;
        char c = query.charAt(d);
        return longest_prefix(x.next[c], query, d + 1, length);
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take Input
        Scanner readIn = new Scanner(System.in);
        TrieST<Integer> trie = new TrieST<Integer>();
        for (int i = 0; readIn.hasNext(); ++i)
            trie.put(readIn.next(), i);

        //Print All String
        for (String s : trie.keys())
            System.out.println(s + " " + trie.get(s));
        System.out.println();

        //Print Wildcard Match
        for (String s : trie.wildcard_match("sh..l."))
            System.out.println(s);
        System.out.println();

        //Print Longest Prefix
        System.out.println(trie.longest_prefix("shellsort") + "\n");

        //Delete and Print
        trie.delete("shore");
        for (String s : trie.keys())
            System.out.println(s + " " + trie.get(s));
        System.out.println();
    }
}