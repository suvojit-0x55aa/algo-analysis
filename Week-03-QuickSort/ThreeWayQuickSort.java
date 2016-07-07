// Author : Shin

import java.lang.Integer;
import java.lang.System;
import java.lang.UnsupportedOperationException;
import java.util.Random;
import java.util.Scanner;

class KnuthShuffle
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

    static void shuffle(Integer arr[])
    {
        Random rand = new Random();
        for(int i = 0; i < arr.length; ++i)
        {
            int r = rand.nextInt(i + 1);
            swap(arr, i, r);
        }
    }
}

class QuickSort
{
    static boolean less(Integer x, Integer y)
    {
        return x < y;
    }

    static boolean more(Integer x, Integer y)
    {
        return x > y;
    }

    static void swap(Integer a[], int i, int j)
    {
        Integer temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static void sort(Integer arr[], int low, int high)
    {
        if (high <= low )
            return;
        int lt = low, gt = high, i = low;
        Integer v = arr[low];
        while( i <= gt )
        {
            if ( less(arr[i], v) )  swap(arr, lt++, i++);
            else if ( more(arr[i], v) )  swap(arr, gt--, i);
            else ++i;
        }
        sort(arr, low, lt - 1);
        sort(arr, gt + 1, high);
    }

    static void sort(Integer arr[])
    {
        KnuthShuffle.shuffle(arr);;
        sort(arr, 0, arr.length - 1);
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
        QuickSort.sort(array);

        //Display Sorted Array
        for (Integer x : array)
            System.out.print(x + " ");
        System.out.print("\n");
    }
}