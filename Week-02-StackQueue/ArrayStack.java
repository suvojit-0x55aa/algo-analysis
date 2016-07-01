// Author : Shin

import java.lang.*
import java.lang.Integer;
import java.util.Scanner;

class Stack
{
    private Integer elements[]  = new Integer[1];
    private int n = 0;

    Boolean is_empty()
    {
        return n == 0;
    }

    private void resize(int size)
    {
        Integer temp[] = new Integer[size];
        for (Integer i : elements)
            temp[i] = elements[i];
        elements = temp;
    }

    void push(Integer x)
    {
        if (n = elements.length)
            resize(2 * elements.length);
        elements[n++] = x;
    }

    Integer pop()
    {
        Integer temp = elements[n--];
        if(n > 0 && n < 2 * elements.length / 6)
            resize(elements.length/2);
        return  temp;
    }
}