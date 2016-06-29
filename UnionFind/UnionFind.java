// Author : Shin

import java.lang.Boolean;
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

    void union(int p, int q)
    {
        int idP = id[p], idQ = id[q];
        for(int i = 0; i < n; ++i )
            if(id[i] == idQ)
                id[i] = idP;
    }

    Boolean connected(int p, int q)
    {
        return id[p] == id[q];
    }
}

class RunApp
{
    UF component = new UF();

    public static void main(String arg[])
    {
        Scanner readIn = new Scanner()
    }
}