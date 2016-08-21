// Author : Shin

import java.lang.Math;
import java.lang.String;
import java.lang.System;
import java.util.Scanner;

class BoyerMoore
{
    private final int R = 256;
    private int right[];
    private String pattern;

    public BoyerMoore(String pattern)
    {
        this.pattern = pattern;
        right = new int[R];
        for (int c = 0; c < R; ++c)
            right[c] = -1;
        for (int c = 0; c < pattern.length(); ++c)
            right[pattern.charAt(c)] = c;
    }

    public int search(String txt)
    {
        int n = txt.length();
        int m = pattern.length();
        int skip;
        for (int i = 0; i <= n - m; i += skip)
        {
            skip = 0;
            for (int j = m - 1; j >= 0; --j)
                if (pattern.charAt(j) != txt.charAt(i + j))
                {
                    skip = Math.max(1, j - right[txt.charAt(i + j)]);
                    break;
                }
            if (skip == 0)  return i;
        }
        return n;
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take input
        Scanner readIn = new Scanner(System.in);
        String haystack = readIn.nextLine();
        String needle   = readIn.next();

        //Find Substring
        BoyerMoore matcher = new BoyerMoore(needle);
        int i = matcher.search(haystack);
        if (i == haystack.length())  System.out.println("Not Found");
        else System.out.println("Found At : " + i);
    }
}