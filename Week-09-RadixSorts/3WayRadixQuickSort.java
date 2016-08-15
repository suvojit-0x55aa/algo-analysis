// Author : Shin

import java.lang.IllegalArgumentException;
import java.lang.String;
import java.lang.System;
import java.util.Random;
import java.util.Scanner;

class KnuthShuffle
{
    private static boolean less(String x, String y)
    {
        return x.compareTo(y) < 0;
    }

    private static boolean more(String x, String y)
    {
        return x.compareTo(y) > 0;
    }

    private static void swap(String a[], int i, int j)
    {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    static void shuffle(String arr[])
    {
        Random rand = new Random();
        for(int i = 0; i < arr.length; ++i)
        {
            int r = rand.nextInt(i + 1);
            swap(arr, i, r);
        }
    }
}


class RadixQuick
{
    private RadixQuick()  {}
    private static int CUTOFF = 15;

    private static int charAt(String s, int i)
    {
        if (i >= s.length())  return -1;
        else                  return s.charAt(i);
    }

    private static void sort(String a[], int lo, int hi, int d)
    {
        if (hi <= lo + CUTOFF)
        {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int i  = lo + 1;
        int v  = charAt(a[lo], d);

        while (i <= gt)
        {
            int t = charAt(a[i], d);
            if      (t < v)  exch(a, lt++, i++);
            else if (t > v)  exch(a, i,   gt--);
            else             i++;
        }

        sort(a, lo, lt - 1, d);
        if (v >= 0)  sort(a, lt, gt, d + 1);
        sort(a, gt, hi, d);
    }

    public static void sort(String a[])
    {
        KnuthShuffle.shuffle(a);
        sort(a, 0, a.length - 1, 0);
    }

    private static void exch(String[] a, int i, int j)
    {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(String v, String w, int d)
    {
        for (int i = d; i < Math.min(v.length(), w.length()); i++)
        {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
    }

    private static void insertion(String[] a, int lo, int hi, int d)
    {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Read Input
        Scanner readIn = new Scanner(System.in);
        int n = readIn.nextInt();
        String arr[] = new String[n];
        for (int i = 0; i < n; ++i)
            arr[i] = readIn.next();

        //Sort Array
        RadixQuick.sort(arr);

        //Print Array
        for (int i = 0; i < n; ++i)
            System.out.println(arr[i]);
    }
}