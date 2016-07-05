// Author : Shin

import java.lang.Integer;
import java.lang.System;
import java.lang.UnsupportedOperationException;
import java.util.Random;
import java.util.Scanner;

class MergeSort
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

    private static void merge(Integer arr[], Integer aux[], int low, int mid, int high)
    {
        for (int i = low; i <= high; ++i)
            aux[i] = arr[i];

        int i = low, j = mid + 1, k = low;
        while( i <= mid && j <= high )
        {
            if (less(arr[i], arr[j]))
                arr[k++] = aux[i++];
            else
                arr[k++] = aux[j++];
        }

        while (i <= mid ) { arr[k++] = aux[i++]; }
        while (j <= high) { arr[k++] = aux[j++]; }
    }

    private static void sort(Integer arr[], Integer aux[], int low, int high)
    {
        if( low >= high ) return;

        int mid = low + (high - low) / 2;
        sort(arr, aux, low, mid);
        sort(arr, aux, mid+1, high);
        merge(arr, aux, low, mid, high);
    }

    static void sort(Integer arr[])
    {
        Integer aux[] = new Integer[arr.length];
        sort(arr, aux, 0, arr.length - 1);
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
        MergeSort.sort(array);

        //Display Sorted Array
        for (Integer x : array)
            System.out.print(x + " ");
        System.out.print("\n");
    }
}