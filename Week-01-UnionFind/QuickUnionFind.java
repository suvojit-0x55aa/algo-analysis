// Author : Shin

import java.lang.Boolean;
import java.lang.System;
import java.util.Scanner;

class UF
{
    int id[];

    UF(int n)
    {
        id = new int[n];
        for(int i = 0; i < n; ++i )
            id[i] = i;
    }

    int root(int i)
    {
        while (id[i] != i)
            i = id[i];
        return i;
    }
    void union(int p, int q)
    {
        id[root(p)] = root(q);
    }

    Boolean connected(int p, int q)
    {
        return root(p) == root(q);
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        Scanner readIn = new Scanner(System.in);

        //Input no of components
        int n = readIn.nextInt();
        UF test = new UF(n);

        //Input no of query
        int query = readIn.nextInt();

        //Work on Query
        for (int i = 0; i < query; ++i )
        {
            char ops = readIn.next().charAt(0);
            int p = readIn.nextInt();
            int q = readIn.nextInt();

            if (ops == 'U')
                test.union(p, q);
            else if (ops == 'C')
                System.out.println(test.connected(p, q));
        }
    }
}