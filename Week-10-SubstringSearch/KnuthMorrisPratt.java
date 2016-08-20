// Author : Shin

import java.lang.String;
import java.lang.System;
import java.util.Scanner;

class KMP
{
    private final int R = 256;
    private int dfa[][];
    private String pattern;

    public KMP(String pattern)
    {
        this.pattern = pattern;
        int m = pattern.length();
        dfa = new int[R][m];
        dfa[pattern.charAt(0)][0] = 1;
        for (int x = 0, j = 1; j < m; ++j)
        {
            for (int c = 0; c < R; ++c)
                dfa[c][j] = dfa[c][x];
            dfa[pattern.charAt(j)][j] = j + 1;
            x = dfa[pattern.charAt(j)][x];
        }
    }

    int search(String txt)
    {
        int m = pattern.length();
        int n = txt.length();
        int i, j;
        for (i = 0, j = 0; i < n && j < m; ++i)
            j = dfa[txt.charAt(i)][j];
        if (j == m)  return i - m;
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
        KMP matcher = new KMP(needle);
        int i = matcher.search(haystack);
        if (i == haystack.length())  System.out.println("Not Found");
        else System.out.println("Found At : " + i);
    }
}