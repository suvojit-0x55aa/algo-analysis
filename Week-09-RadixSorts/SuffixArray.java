// Author : Shin

import java.lang.Comparable;
import java.lang.Math;
import java.lang.String;
import java.lang.System;
import java.util.Arrays;
import java.util.Scanner;

class SuffixArray
{
    private Suffix suffixes[];

    SuffixArray(String text)
    {
        int n = text.length();
        suffixes = new Suffix[n];
        for (int i = 0; i < n; ++i)
            suffixes[i] = new Suffix(text, i);
        Arrays.sort(suffixes);
    }

    private static class Suffix implements Comparable<Suffix>
    {
        private final String text;
        private final int index;

        private Suffix(String text, int i)
        {
            this.text  = text;
            this.index = i;
        }

        private int length()  { return text.length() - index; }
        private char charAt(int i)  { return text.charAt(index + i); }

        public int compareTo(Suffix that)
        {
            if (this == that)  return 0;
            int n = Math.min(this.length(), that.length());
            for (int i = 0; i < n; ++i)
            {
                if (this.charAt(i) < that.charAt(i))  return -1;
                if (this.charAt(i) > that.charAt(i))  return +1;
            }
            return this.length() - that.length();
        }

        public String toString()  { return text.substring(index); }
    }

    public int length()  { return suffixes.length; }
    public int index(int i)  { return suffixes[i].index; }

    public int lcp(int i)  { return lcp(suffixes[i], suffixes[i + 1]); }

    private static int lcp(Suffix a, Suffix b)
    {
        int n = Math.min(a.length(), b.length());
        for (int i = 0; i < n; ++i)
            if (a.charAt(i) != b.charAt(i))  return i;
        return n;
    }

    public String select(int i)  { return suffixes[i].toString(); }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take Input
        Scanner readIn = new Scanner(System.in);
        String text = readIn.nextLine();
        text.replaceAll("//s+", " ").trim();

        //Create Suffix Array
        SuffixArray suf = new SuffixArray(text);

        //Print Longest Common Substring
        int m = -1, index = 0;
        for (int i = 0; i < suf.length() - 1; ++i)
        {
            int n = suf.lcp(i);
            if (n > m)
            {
                index = i;
                m = n;
            }
        }
        System.out.println("Index 1 : " + suf.index(index) + " Index 2 : " + suf.index(index + 1)
                + "\nLongest Common Substring : \n"
                + suf.select(index).substring(0, m));
    }
}