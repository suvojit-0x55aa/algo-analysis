// Author : Shin

import java.lang.String;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

class RabinKarp
{
    private String pattern;
    private long patHash;
    private int m;
    private long q;
    private int R;
    private long RM;

    public RabinKarp(String pattern)
    {
        this.R = 256;
        this.pattern = pattern;
        this.m = pattern.length();
        this.q = random_prime();

        RM = 1;
        for (int i = 1; i <= m - 1; ++i)
            RM = (R * RM) % q;
        patHash = hash(pattern, m);
    }

    private long hash(String str, int i)
    {
        long h = 0;
        for (int j = 0; j < i; ++j)
            h = (h * R + str.charAt(j)) % q;
        return h;
    }

    private long random_prime()
    {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    //Las Vegas Check
    private boolean check(String str, int i)
    {
        for (int j = 0; j < m; ++j)
            if (pattern.charAt(j) != str.charAt(i + j))
                return false;
        return true;
    }

    //Monte Carlo Check
    private boolean check(int i)  { return true; }

    public int search(String txt)
    {
        int n = txt.length();
        if (n < m)  return n;
        long txtHash = hash(txt, m);

        if ((patHash == txtHash) && check(txt, 0))
            return 0;

        for (int i = m; i < n; ++i)
        {
            txtHash = (txtHash + q - RM * txt.charAt(i - m) % q) % q;
            txtHash = (txtHash * R + txt.charAt(i)) % q;

            int offset = i - m + 1;
            if ((patHash == txtHash) && check(txt, offset))  return offset;
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
        RabinKarp matcher = new RabinKarp(needle);
        int i = matcher.search(haystack);
        if (i == haystack.length())  System.out.println("Not Found");
        else System.out.println("Found At : " + i);
    }
}