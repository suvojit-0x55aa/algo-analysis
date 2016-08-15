// Author : Shin

import java.lang.IllegalArgumentException;
import java.lang.String;
import java.lang.System;
import java.util.Scanner;

class MSD
{
    private MSD()  {}

    private static int R     = 256;
    private static int CUTOFF = 15;

    private static int charAt(String s, int i)
    {
        if (i >= s.length())  return -1;
        else                  return s.charAt(i);
    }

    private static void sort(String a[], String aux[], int lo, int hi, int d)
    {
        if (hi <= lo + CUTOFF)
        {
            insertion(a, lo, hi, d);
            return;
        }

        int count[] = new int[R+2];
        for (int i = lo; i <= hi; ++i)
            count[charAt(a[i], d)+2]++;

        for (int r = 0; r < R + 1; ++r)
            count[r + 1] += count[r];

        for (int i = lo; i <= hi; ++i)
            aux[count[charAt(a[i], d) + 1]++] = a[i];

        for (int i = lo; i <= hi; ++i)
            a[i] = aux[i - lo];

        for (int r = 0; r < R; ++r)
            sort(a, aux, lo + count[r], lo + count[r + 1] - 1, d + 1);
    }

    public static void sort(String a[])
    {
        int n = a.length;
        String aux[] = new String[n];
        sort(a, aux, 0, n -1, 0);
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
        MSD.sort(arr);

        //Print Array
        for (int i = 0; i < n; ++i)
            System.out.println(arr[i]);
    }
}