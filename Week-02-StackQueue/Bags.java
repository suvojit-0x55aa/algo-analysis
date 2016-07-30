//Author : Shin

import java.lang.*;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Bags<Item> implements Iterable<Item>
{
    private Node<Item> first;
    private int n;

    private static class Node<Item>
    {
        private Item item;
        private Node<Item> next;
    }

    public Bags()
    {
        first = null;
        n = 0;
    }

    public boolean is_empty()
    {
        return first == null;
    }

    public int size()
    {
        return n;
    }

    public void add(Item item)
    {
        Node<Item> temp = new Node<Item>();
        temp.item = item;
        temp.next = first;
        first = temp;
    }

    public Iterator<Item> iterator()
    {
        return new ListIterator<Item>(first);
    }

    private class ListIterator<Item> implements Iterator<Item>
    {
        private Node<Item> current;

        public ListIterator(Node<Item> first)
        {
            current = first;
        }

        public boolean hasNext()  { return current != null; }
        public void remove()  { throw new UnsupportedOperationException(); }

        public Item next()
        {
            if (!hasNext())  throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}

class RunApp
{
    public static void main(String args[])
    {
        Bags<String> bag = new Bags<String>();

        //Take Input
        Scanner readIn = new Scanner(System.in);
        String  input = new String();
        while (readIn.hasNext())
        {
            input = readIn.next();
            bag.add(input);
        }

        //Print items in Bag
        for (String x : bag)
            System.out.println(x);
    }
}