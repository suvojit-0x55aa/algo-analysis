// Author : Shin

import java.lang.*
import java.util.Scanner;

class UF
{
    int id[],size[];

    UF(int n)
    {
        id = new int[n];
        size = new int[n];
        for(int i = 0; i < n; ++i )
        {
            id[i] = i;
            size[i] = 0;
        }
    }

    int root(int i)
    {
        while (id[i] != i)
        {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }
    void union(int p, int q)
    {
        int prid = root(p);
        int qrid = root(q);
        if (prid == qrid)
            return;
        else if (size[prid] < size[qrid])
        {
            id[prid] = qrid;
            size[qrid] += size[prid];
        }
        else
        {
            id[qrid] = prid;
            size[prid] += size[qrid];
        }
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