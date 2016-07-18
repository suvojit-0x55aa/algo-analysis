// Author : Shin

import java.lang.*;
import java.util.*;

class BST
{
    private Node root = null;

    private class Node
    {
        Integer key;
        int size;
        String data;
        Node left,right;
        Node (Integer key, String data)
        {
            this.key = key;
            this.data = data;
            left = right = null;
            size = 1;
        }
    }

    private String get (Node x, Integer key)
    {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.data;
    }

    public String get (Integer p)
    {
        return get(root, p);
    }

    public int size()
    {
        return size(root);
    }

    private int size(Node x)
    {
        if (x == null) return 0;
        else return x.size;
    }

    public boolean is_empty()
    {
        return size() == 0;
    }

    private Node put (Node x, Integer key, String val)
    {
        if (x == null)  return new Node(key, val);
        int cmp = key.compareTo(x.key);
        if     (cmp < 0)  x.left = put(x.left, key, val);
        else if(cmp > 0) x.right = put(x.right, key, val);
        else              x.data = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void put (Integer key, String data)
    {
        if (key == null || data == null)
            throw new NullPointerException();
        root = put(root, key, data);
    }

    private void in_order(Queue<Integer> keys, Node x)
    {
        if (x == null) return;
        in_order(keys,x.left);
        keys.add(x.key);
        in_order(keys, x.right);
    }

    public Iterable<Integer> keys()
    {
        Queue<Integer> keys = new ArrayDeque<Integer>();
        in_order(keys, root);
        return keys;
    }
}

class RunApp
{
    public static void main (String arg[])
    {
        BST symTab = new BST();

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

        //Print Symbol Table
        System.out.println(symTab.size());
        for (Integer x : symTab.keys())
            System.out.println(x + " : " + symTab.get(x));
    }
}