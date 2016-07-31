//Author : Shin

import java.lang.*;
import java.util.*;

class Bags<Item> implements Iterable<Item>
{
    private Node<Item> first;
    private int n;

    private static class Node<Item>
    {
        private Item item;
        private Node<Item> next;
    }

    public Bags()
    {
        first = null;
        n = 0;
    }

    public boolean is_empty()
    {
        return first == null;
    }

    public int size()
    {
        return n;
    }

    public void add(Item item)
    {
        Node<Item> temp = new Node<Item>();
        temp.item = item;
        temp.next = first;
        first = temp;
    }

    public Iterator<Item> iterator()
    {
        return new ListIterator<Item>(first);
    }

    private class ListIterator<Item> implements Iterator<Item>
    {
        private Node<Item> current;

        public ListIterator(Node<Item> first)
        {
            current = first;
        }

        public boolean hasNext()  { return current != null; }
        public void remove()  { throw new UnsupportedOperationException(); }

        public Item next()
        {
            if (!hasNext())  throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}


class Graphs
{
    private final int V;
    private int E;

    Bags<Integer> adj[];

    Graphs(int n)
    {
        this.V = n;
        this.E = 0;
        adj = (Bags<Integer>[])new Bags[V];
        for (int i = 0; i < V; ++i)
            adj[i] = new Bags<Integer>();
    }

    Graphs(Graphs G)
    {
        this(G.V());
        this.E = G.E();
        for (int i = 0; i < V; ++i)
        {
            Bags<Integer> stack = new Bags<Integer>();

            for (Integer x : G.adj[i])
                stack.add(x);

            for (Integer v  : stack)
                this.adj[i].add(v);
        }
    }

    public int V()  { return V; }
    public int E()  { return E; }

    private void valid_vertex(int v)
    {
        if (v < 0 || v > (V-1))  throw new IndexOutOfBoundsException();
    }

    public void add_edge(int v, int w)
    {
        valid_vertex(v);
        valid_vertex(w);
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    public Iterable<Integer> adj(int v)
    {
        valid_vertex(v);
        return adj[v];
    }

    public int degree(int v)
    {
        valid_vertex(v);
        return adj[v].size();
    }

    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(V + " Vertex " + E + " Edges\n");
        for (int i = 0; i < V; ++i)
        {
            s.append(i + " : ");
            for (Integer w : adj[i])
                s.append(w + " ");
            s.append("\n");
        }
        return s.toString();
    }
}

class ConnectComp
{
    private boolean marked[];
    private int id[];
    private int size[];
    private int count;

    ConnectComp(Graphs G)
    {
        marked = new boolean[G.V()];
        id   = new int[G.V()];
        size = new int[G.V()];
        count = 0;
        for (int i = 0; i < G.V(); ++i)
        {
            if (!marked[i])
            {
                dfs(G, i);
                count++;
            }
        }
    }

    private void dfs(Graphs G, int v)
    {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (int w : G.adj(v))
        {
            if (!marked[w])
                dfs(G, w);
        }
    }

    public int count()  { return count+1; }

    public int size(int v)  { return size[id[v]]; }

    public boolean connected(int v, int w)  { return id[v] == id[w]; }

    public int id(int v)  { return id[v]; }

    public Iterable<Integer> component(int c)
    {
        if (c < 0 || c > count) throw new IndexOutOfBoundsException();
        Stack<Integer> cc = new Stack<Integer>();
        for (int i = 0; i < id.length; ++i)
            if (id(i) == c)
                cc.push(i);
        return cc;
    }
}

class RunApp
{
    public static void main(String args[])
    {
        Scanner readIn = new Scanner(System.in);

        //Take Input
        int vert = readIn.nextInt();
        int edge = readIn.nextInt();
        Graphs G = new Graphs(vert);
        for (int i = 0; i < edge; ++i)
        {
            int v = readIn.nextInt();
            int w = readIn.nextInt();
            G.add_edge(v,w);
        }

        //Print all components
        ConnectComp cc = new ConnectComp(G);
        for (int i = 0; i < cc.count(); ++i)
        {
            for (int v : cc.component(i))
                System.out.print(v + ", ");
            System.out.println();
        }
    }
}

