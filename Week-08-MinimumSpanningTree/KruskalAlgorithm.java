//Author : Shin

import java.lang.*;
import java.lang.Iterable;
import java.util.*;
import java.util.LinkedList;
import java.util.Queue;

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

class MinPQ<Item extends Comparable<Item>>
{
    private Item key[];
    private int n;

    MinPQ(int n)
    {
        key = (Item[]) new Comparable[n];
        n   = 0;
    }

    boolean is_empty()
    {
        return n == 0;
    }

    int size()  { return n; }

    boolean less(Item x, Item y)
    {
        return x.compareTo(y) < 0;
    }

    boolean more(Item x, Item y)
    {
        return x.compareTo(y) > 0;
    }

    void swap(Item a[], int i, int j)
    {
        Item temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private void resize(int size)
    {
        Item temp[] = (Item[])new Comparable[size];
        for (int i = 0 ;i < n; ++i )
            temp[i] = key[i];
        key = temp;
    }

    private void swim(int k)
    {
        while (k > 0 && more(key[(k - 1) / 2], key[k]))
        {
            swap(key, (k - 1)/2, k);
            k = (k - 1) / 2;
        }
    }

    private void sink (int k)
    {
        while ( 2 * k + 2 < n )
        {
            int j = 2 * k + 1;
            if ( j < n && more(key[j], key[j + 1]))  ++j;
            if ( !more(key[k], key[j]) ) break;
            swap(key, k , j);
            k = j;
        }
    }

    void insert (Item p)
    {
        if (n == key.length)
            resize(2 * key.length);
        key[n++] = p;
        swim(n - 1);
    }

    Item remove ()
    {
        Item min = key[0];
        swap(key, 0, --n);
        key[n] = null;
        sink(0);
        if ( n > 0 && n < key.length / 3 )
            resize(key.length / 2);
        return min;
    }
}

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

class Edge implements Comparable<Edge>
{
    private final int v,w;
    private final double weight;

    Edge(int v, int w, double weight)
    {
        this.v      = v;
        this.w      = w;
        this.weight = weight;
    }

    public double weight()  { return weight; }

    public int either()  { return v; }

    public int other(int vertex)
    {
        if      (vertex == v)  return w;
        else if (vertex == w)  return v;
        else throw new IllegalArgumentException();
    }

    public String toString()
    {
        return new String(v + "-" + w + " " + weight);
    }

    public int compareTo(Edge that)
    {
        return Double.compare(this.weight, that.weight);
    }
}

class EdgeWeightedGraph
{
    private final int V;
    private int E;
    Bags<Edge> adj[];

    EdgeWeightedGraph(int n)
    {
        this.V = n;
        this.E = 0;
        adj = (Bags<Edge>[])new Bags[V];
        for (int i = 0; i < V; ++i)  adj[i] = new Bags<Edge>();
    }

    EdgeWeightedGraph(EdgeWeightedGraph G)
    {
        this(G.V());
        this.E = G.E();
        for (int i = 0 ;i < V; ++i)
        {
            Bags<Edge> stack = new Bags<Edge>();

            for (Edge e : G.adj(i))
                stack.add(e);

            for (Edge e : stack)
                this.adj[i].add(e);
        }
    }

    public int V()  { return V; }

    public int E()  { return E; }

    private void validVertex(int v)  { if (v < 0 || v > (V -1)) throw new IndexOutOfBoundsException(); }

    public void add_edge(Edge e)
    {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Iterable<Edge> adj(int v)
    {
        validVertex(v);
        return adj[v];
    }

    public int degree(int v)
    {
        validVertex(v);
        return adj[v].size();
    }

    public Iterable<Edge> edges()
    {
        Bags<Edge> edge = new Bags<Edge>();
        for (int v = 0; v < V(); ++v)
        {
            int selfLoop = 0;
            for (Edge e : adj(v))
            {
                if (e.other(v) > v)
                    edge.add(e);
                else if (e.other(v) == v)
                    if (selfLoop % 2 == 0)
                    {
                        edge.add(e);
                        selfLoop++;
                    }
            }
        }
        return edge;
    }
}

class KruskalMST
{
    private Queue<Edge> mst;
    private double weight;

    KruskalMST(EdgeWeightedGraph G)
    {
        mst    = new LinkedList<Edge>();
        weight = 0.0;

        MinPQ<Edge> pq = new MinPQ<Edge>(G.E());
        for (Edge e : G.edges())
            pq.insert(e);

        UF forest = new UF(G.V());
        while (!pq.is_empty() && mst.size() < G.V() - 1)
        {
            Edge e = pq.remove();
            int v  = e.either();
            int w  = e.other(v);
            if (!forest.connected(v, w))
            {
                forest.union(v, w);
                mst.add(e);
                weight += e.weight();
            }
        }
    }

    public Iterable<Edge> edges()  { return mst; }

    public double weight()  { return weight; }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take Input
        Scanner readIn = new Scanner(System.in);
        int V = readIn.nextInt();
        int E = readIn.nextInt();
        EdgeWeightedGraph G = new EdgeWeightedGraph(V);
        Edge e;
        int v,w;
        double wt;
        for (int i = 0; i < E; ++i)
        {
            v  = readIn.nextInt();
            w  = readIn.nextInt();
            wt = readIn.nextDouble();
            e = new Edge(v,w,wt);
            G.add_edge(e);
        }

        //Calculate Minimum Spanning Tree
        KruskalMST mst = new KruskalMST(G);

        //Print all Weight and Edges
        System.out.println("Weight " + mst.weight());
        for (Edge x : mst.edges())
            System.out.println(x);
    }
}