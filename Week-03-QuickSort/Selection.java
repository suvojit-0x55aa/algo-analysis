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

class Selection
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

    private static int lomuto_partition(Integer arr[], int low, int high)
    {
        int x = arr[high];
        int i = low - 1;
        for ( int j = low; j <= high - 1; ++j )
        {
            if ( less(arr[j],x) )
            {
                i += 1;
                swap(arr, i, j);
            }
        }
        swap(arr, i+1, high);
        return i+1;
    }

    static Integer select(Integer arr[], int k)
    {
        KnuthShuffle.shuffle(arr);
        int low = 0, high = arr.length - 1;
        while(high > low)
        {
            int j = lomuto_partition(arr, low, high);
            if( j < k )        low  = j + 1;
            else  if ( j > k ) high = j - 1;
            else return arr[k];
        }
        return arr[k];
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

        //Get k
        int k = rand.nextInt(n);
        System.out.println("k = " + k);

        //Display Unsorted Array
        for (Integer x : array)
            System.out.print(x + " ");
        System.out.print("\n");

        //Sort array
        Integer kSmall = Selection.select(array,k);

        //Display Result
        System.out.println("kth Small = " + kSmall);
    }
}