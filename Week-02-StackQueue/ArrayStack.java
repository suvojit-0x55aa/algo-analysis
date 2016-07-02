// Author : Shin

import java.lang.*;
import java.lang.Integer;
import java.lang.Override;
import java.lang.System;
import java.lang.UnsupportedOperationException;
import java.util.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

class Stack implements Iterable<Integer>
{
    private Integer elements[]  = new Integer[1];
    private int n = 0;

    boolean is_empty()
    {
        return n == 0;
    }

    private void resize(int size)
    {
        Integer temp[] = new Integer[size];
        for (int i = 0 ;i < elements.length; ++i )
            temp[i] = elements[i];
        elements = temp;
    }

    void push(Integer x)
    {
        if (n == elements.length)
            resize(2 * elements.length);
        elements[n++] = x;
    }

    Integer pop()
    {
        Integer temp = elements[--n];
        if(n > 0 && n < 2 * elements.length / 6)
            resize(elements.length/2);
        return  temp;
    }

    public Iterator<Integer> iterator()
    {
        Iterator<Integer> it = new Iterator<Integer>() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < n;
            }

            @Override
            public Integer next()
            {
                if( count < n )
                    return elements[count++];
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