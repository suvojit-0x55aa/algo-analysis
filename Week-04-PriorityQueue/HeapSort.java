// Author : Shin

import java.lang.Integer;
import java.lang.System;
import java.lang.UnsupportedOperationException;
import java.util.Random;
import java.util.Scanner;

class HeapSort
{
    private static boolean less(Integer x, Integer y)
    {
        return x < y;
    }

    private static boolean more(Integer x, Integer y)
    {
        return x > y;
    }

    private static void swap(Integer a[], int i, int j)
    {
        Integer temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static void swim(Integer key[], int k, int n)
    {
        while (k > 0 && less(key[(k - 1) / 2], key[k]))
        {
            swap(key, (k - 1)/2, k);
            k = (k - 1) / 2;
        }
    }

    private static void sink (Integer key[], int k,int n)
    {
        while ( 2 * k + 1 < n )
        {
            int j = 2 * k + 1;
            if ( j < n && less(key[j], key[j + 1]))  ++j;
            if ( !less( key[k], key[j] ) ) break;
            swap(key, k , j);
            k = j;
        }
    }

    private static void max_heapify(Integer arr[])
    {
        for (int i = arr.length/2; i >= 0; --i) sink(arr, i, arr.length - 1);
    }

    static void sort(Integer arr[])
    {
        int n = arr.length - 1;
        max_heapify(arr);
        while(n > 0)
        {
            swap(arr, 0, n--);
            sink(arr, 0, n);
        }
    }
}

class RunApp
{

    public static void main(String args[])
    {
        //Take Input
        Scanner readIn = new Scanner(System.in);
        Integer n = readIn.nextInt();
        Integer min = readIn.nextInt();
        Integer max = readIn.nextInt();
        readIn.close();
        if (min >= max)
            throw new UnsupportedOperationException();

        //Intitalize Array with random Values
        Integer array[] = new Integer[n];
        Random rand = new Random();
        for (int i = 0; i < array.length; ++i)
            array[i] = rand.nextInt((max - min) + 1) + min;

        //Display Unsorted Array
        for (Integer x : array)
            System.out.print(x + " ");
        System.out.print("\n");

        //Sort array
        HeapSort.sort(array);

        //Display Sorted Array
        for (Integer x : array)
            System.out.print(x + " ");
        System.out.print("\n");
    }
}