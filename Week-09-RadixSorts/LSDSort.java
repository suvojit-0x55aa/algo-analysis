// Author : Shin

import java.lang.IllegalArgumentException;
import java.lang.String;
import java.lang.System;
import java.util.Scanner;

class LSD
{
    private LSD()  {}

    private static int R = 256;

    public static void sort(String a[], int w)
    {
        int n = a.length;
        String aux[] = new String[n];
        for (int d = w - 1; d >= 0; --d)
        {
            int count[] = new int[R+1];

            for (int i = 0; i < n; ++i)
                count[a[i].charAt(d) + 1]++;

            for (int i = 0; i < R; ++i)
                count[i + 1] += count[i];

            for (int i = 0; i < n; ++i)
                aux[count[a[i].charAt(d)]++] = a[i];

            for (int i = 0; i < n; ++i)
                a[i] = aux[i];
        }
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Read Input
        Scanner readIn = new Scanner(System.in);
        int n = readIn.nextInt();
        int w = readIn.nextInt();
        String arr[] = new String[n];
        for (int i = 0; i < n; ++i)
        {
            arr[i] = readIn.next();
            if (arr[i].length() != w)  throw new IllegalArgumentException();
        }

        //Sort Array
        LSD.sort(arr, w);

        //Print Array
        for (int i = 0; i < n; ++i)
            System.out.println(arr[i]);
    }
}