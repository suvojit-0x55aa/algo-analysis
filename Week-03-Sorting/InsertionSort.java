// Author : Shin

import java.lang.Integer;
import java.lang.System;
import java.lang.UnsupportedOperationException;
import java.util.Random;
import java.util.Scanner;

class SelectionSort
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

    static void sort(Integer arr[])
    {
        for (int i = 1; i < arr.length; ++i)
        {
            Integer key = arr[i];
            int j;
            for (j = i; j > 0; --j)
            {
                if( less (arr[j], arr[j - 1]))
                    swap(arr, j, j - 1);
                else
                    break;
            }
            for (Integer x : arr)
                System.out.print(x + " ");
            System.out.print("\n");
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
        SelectionSort.sort(array);

        //Display Sorted Array
        for (Integer x : array)
            System.out.print(x + " ");
        System.out.print("\n");
    }
}