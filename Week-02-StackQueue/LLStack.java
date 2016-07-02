// Author : Shin

import java.lang.*;
import java.lang.Integer;
import java.lang.System;
import java.util.*;
import java.util.NoSuchElementException;

class Stack implements Iterable<Integer>
{
    private class Node
    {
        Integer data;
        Node next;

        Node(Integer x)
        {
            data = x;
            next = null;
        }
    }

    Node top = null;

    boolean is_empty()
    {
        return top == null;
    }

    void push(Integer x)
    {
        Node temp = new Node(x);
        temp.next = top;
        top = temp;
    }

    Integer pop()
    {
        if (!is_empty())
        {
            Integer temp = top.data;
            top = top.next;
            return temp;
        }
        else
            throw new NoSuchElementException();
    }

    public Iterator<Integer> iterator()
    {
        Iterator<Integer> it = new Iterator<Integer>() {
            private Node ptr = top;

            @Override
            public boolean hasNext() {
                return ptr != null;
            }

            @Override
            public Integer next()
            {
                if( ptr != null )
                {
                    Integer temp = ptr.data;
                    ptr = ptr.next;
                    return temp;
                }
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
    public static void main(String arg[])
    {
        Scanner readIn = new Scanner(System.in);

        Stack test = new Stack();

        //Input no of query
        int query = readIn.nextInt();

        //Work on Query
        for (int i = 0; i < query; ++i )
        {
            char ops = readIn.next().charAt(0);

            if (ops == 'I')
                test.push(readIn.nextInt());
            else if (ops == 'D')
                test.pop();
            for( Integer x : test )
                System.out.print(x + " ");
            System.out.print("\n");
        }
    }
}