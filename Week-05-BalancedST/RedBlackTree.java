// Author : Shin

import java.lang.*;
import java.util.*;

class RedBlackTree
{
    private final static boolean RED = true;
    private final static boolean BLACK = false;

    //define the root
    private Node root = null;

    //Node of the Binart tree
    private class Node
    {
        Integer key;
        int size;
        String data;
        Node left,right;
        boolean color = false;
        Node (Integer key, String data, boolean color)
        {
            this.key = key;
            this.data = data;
            this.color = color;
            left = right = null;
            size = 1;
        }
    }

    //check if a node link is red
    private boolean is_red(Node x)
    {
        if ( x == null )  return false;
        else  return x.color == RED;
    }

    //rotate a right leaning RED link to a left leaning RED link
    private Node rotate_left(Node h)
    {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        x.color = RED;
        return x;
    }

    //rotate a left leaning RED link to temporarily right leaning RED link
    private Node rotate_right(Node h)
    {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    //flip color of a temporary 4-node ie a node with two child RED links
    private void flip_colors(Node x)
    {
        x.color = RED;
        x.left.color = BLACK;
        x.right.color = BLACK;
    }

    //private function to search for a key in BST
    private String get (Node x, Integer key)
    {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.data;
    }

    // public accessor for get()
    public String get (Integer p)
    {
        return get(root, p);
    }

    //returns the size of the tree
    public int size()
    {
        return size(root);
    }

    //private function to check size of subtree
    private int size(Node x)
    {
        if (x == null) return 0;
        else return x.size;
    }

    //check if the tree is empty
    public boolean is_empty()
    {
        return size() == 0;
    }

    //Private function to insert node
    private Node put (Node x, Integer key, String val)
    {
        if (x == null)  return new Node(key, val, RED);
        int cmp = key.compareTo(x.key);
        if     (cmp < 0) x.left  = put(x.left, key, val);
        else if(cmp > 0) x.right = put(x.right, key, val);
        else             x.data  = val;

        //if right leaniing make left leaning
        if (is_red(x.right) && !is_red(x.left))      x = rotate_left(x);
        //if child and grand child are RED rotate right
        if (is_red(x.left)  && is_red(x.left.left))  x = rotate_right(x);
        //if both child are red flip colors
        if (is_red(x.left)  && is_red(x.right))      flip_colors(x);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    //public accessor to insert node
    public void put (Integer key, String data)
    {
        if (key == null || data == null)
            throw new NullPointerException();
        root = put(root, key, data);
    }

    //Delete the min node of the tree
    public void delete_min()
    {
        root = delete_min(root);
    }

    //Delete the min node of the subtree
    private Node delete_min(Node x)
    {
        if (x.left == null)  return x.right;
        x.left = delete_min(x.left);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    //Delete Max node of the tree
    public void delete_max()
    {
        root = delete_max(root);
    }

    //Delete max node of a sub tree
    private Node delete_max(Node x)
    {
        if (x.right == null)  return x.left;
        x.right = delete_max(x.right);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    //return the min of the tree
    public Integer min()
    {
        if (is_empty())  throw new NoSuchElementException();
        else return min(root).key;
    }

    //returns the min node of the sub tree
    private Node min(Node x)
    {
        if (x.left == null)  return x;
        else                 return min(x.left);
    }

    //return the max node of the tree
    public Integer max()
    {
        if (is_empty())  throw new NoSuchElementException();
        else return max(root).key;
    }

    //return max node of the sub tree
    private Node max(Node x)
    {
        if (x.right == null)  return x;
        else                  return max(x.right);
    }

    //Populates a Queue with in order traversal
    private void in_order(Queue<Integer> keys, Node x)
    {
        if (x == null) return;
        in_order(keys,x.left);
        keys.add(x.key);
        in_order(keys, x.right);
    }

    //Public fuction to iterate through all keys
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
        RedBlackTree symTab = new RedBlackTree();

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
        for (Integer x : symTab.keys())
            System.out.println(x + " : " + symTab.get(x));
    }
}