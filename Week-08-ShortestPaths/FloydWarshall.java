//Author : Shin

import java.lang.*;
import java.lang.Double;
import java.lang.Iterable;
import java.lang.UnsupportedOperationException;
import java.util.*;
import java.util.Deque;
import java.util.LinkedList;

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

class WeightedEdge implements Comparable<WeightedEdge>
{
    private final int v;
    private final int w;
    private final double weight;

    WeightedEdge(int v, int w, double weight)
    {
        if (v < 0 || w < 0)  throw new IllegalArgumentException();
        if (Double.isNaN(weight))  throw new IllegalArgumentException();

        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int from()  { return v; }

    public int to()    { return w; }

    public double weight()  { return weight; }

    public int compareTo(WeightedEdge that)  { return Double.compare(this.weight, that.weight); }

    public String toString()  { return new String(v + "->" + w + " " + weight); }
}

class WeightedDigraph
{
    private final int V;
    private int E;
    Bags<WeightedEdge> adj[];
    private int inDegree[];

    WeightedDigraph(int V)
    {
        this.V = V;
        this.E = 0;
        inDegree = new int[V];
        adj = (Bags<WeightedEdge>[]) new Bags[V];

        for (int i = 0; i < V; ++i)
            adj[i] = new Bags<WeightedEdge>();
    }

    WeightedDigraph(WeightedDigraph G)
    {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); ++v)
            inDegree[v] = G.in_degree(v);

        for (int v = 0; v < G.V(); ++v)
        {
            LinkedList<WeightedEdge> reverse = new LinkedList<WeightedEdge>();
            for (WeightedEdge e : G.adj(v))  reverse.addFirst(e);
            for (WeightedEdge e : reverse )  adj[v].add(e);
        }
    }

    private void valid_vertex(int v)
    {
        if (v < 0 || v >= V)  throw new IndexOutOfBoundsException();
    }

    public Iterable<WeightedEdge> adj(int v)
    {
        valid_vertex(v);
        return adj[v];
    }

    public int V()  { return V; }
    public int E()  { return E; }

    public void add_edge(WeightedEdge e)
    {
        int v = e.from();
        int w = e.to();

        valid_vertex(v);
        valid_vertex(w);

        adj[v].add(e);
        E++;
        inDegree[w]++;
    }

    public int in_degree(int v)
    {
        valid_vertex(v);
        return inDegree[v];
    }

    public int out_degree(int v)
    {
        valid_vertex(v);
        return adj[v].size();
    }

    public Iterable<WeightedEdge> edges()
    {
        Queue<WeightedEdge> list = new LinkedList<WeightedEdge>();
        for (int v = 0; v < V(); ++v)
            for (WeightedEdge e : adj(v))  list.add(e);
        return list;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append("V = " + V + " E = " + E + "\n");
        for (int v = 0; v < V(); ++v)
        {
            str.append(v + " : ");
            for (WeightedEdge x : adj(v))
                str.append(x + " ");
            str.append("\n");
        }
        return str.toString();
    }
}

class EdgeWeightedCycles
{
    private boolean marked[];
    private boolean onStack[];
    private WeightedEdge edgeTo[];
    private LinkedList<WeightedEdge> cycle;

    EdgeWeightedCycles(WeightedDigraph G)
    {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new WeightedEdge[G.V()];

        for (int i = 0; i < G.V(); ++i)
            if (!marked[i] && cycle == null)  dfs(G, i);
    }

    private void dfs(WeightedDigraph G, int v)
    {
        marked[v]  = true;
        onStack[v] = true;

        for (WeightedEdge e : G.adj(v))
        {
            int w = e.to();
            if (cycle != null)  return;

            else if (!marked[w])
            {
                edgeTo[w] = e;
                dfs(G, w);
            }

            else if (onStack[w])
            {
                cycle = new LinkedList<WeightedEdge>();
                WeightedEdge f = e;

                while (f.from() != w)
                {
                    cycle.addFirst(f);
                    f = edgeTo[f.from()];
                }
                cycle.addFirst(f);
                return;
            }
        }
        onStack[v] = false;
    }

    public boolean has_cycle() { return cycle != null; }

    public Iterable<WeightedEdge> cycle() { return cycle; }
}

class FloydWarshall
{
    private double distTo[][];
    private WeightedEdge edgeTo[][];
    private boolean hasNegativeCycle;

    FloydWarshall(WeightedDigraph G)
    {
        int V = G.V();
        distTo = new double[V][V];
        edgeTo = new WeightedEdge[V][V];

        //|V| × |V| array of minimum distances initialized to Infinity
        for (int v = 0; v < V; v++)
            for (int w = 0; w < V; w++)
                distTo[v][w] = Double.POSITIVE_INFINITY;

        //dist[u][v] <- w(u,v)
        for (int v = 0; v < V; v++)
            for (WeightedEdge e : G.adj(v))
            {
                distTo[e.from()][e.to()] = e.weight();
                edgeTo[e.from()][e.to()] = e;
            }

        //dist[v][v] <- 0
        for (int v = 0; v < V; v++)
        {
            distTo[v][v] = 0;
            edgeTo[v][v] = null;
        }

        for (int k = 0; k < V; k++)
        {
            for (int v = 0; v < V; v++)
            {
                if (edgeTo[v][k] == null)  continue;
                for (int w = 0; w < V; w++)
                {
                    if (distTo[v][w] > distTo[v][k] + distTo[k][w]) {
                        distTo[v][w] = distTo[v][k] + distTo[k][w];
                        edgeTo[v][w] = edgeTo[k][w];
                    }
                }
                if (distTo[v][v] < 0.0)
                {
                    hasNegativeCycle = true;
                    return;
                }
            }
        }
    }

    public boolean has_neg_cycle()  { return hasNegativeCycle; }

    public Iterable<WeightedEdge> neg_cycle()
    {
        int V = edgeTo.length;
        for (int v = 0; v < V; v++)
        {
            if (distTo[v][v] < 0.0)
            {
                WeightedDigraph spt = new WeightedDigraph(V);
                for (int w = 0; w < V; w++)
                    if (edgeTo[v][w] != null)
                        spt.add_edge(edgeTo[v][w]);
                EdgeWeightedCycles cf = new EdgeWeightedCycles(spt);
                return cf.cycle();
            }
        }
        return null;
    }

    public boolean has_path(int s, int t)  { return distTo[s][t] < Double.POSITIVE_INFINITY; }

    public double  dist(int s, int t)
    {
        if (has_neg_cycle())  throw new UnsupportedOperationException();
        return distTo[s][t];
    }

    public Iterable<WeightedEdge> path(int s, int t)
    {
        if (has_neg_cycle())  throw new UnsupportedOperationException();
        if (!has_path(s, t))  return null;

        Deque<WeightedEdge> path = new LinkedList<WeightedEdge>();
        for (WeightedEdge e = edgeTo[s][t]; e != null; e = edgeTo[s][e.from()])
            path.addFirst(e);
        return path;
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take input
        Scanner readIn = new Scanner(System.in);
        int V = readIn.nextInt();
        int E = readIn.nextInt();
        WeightedDigraph G = new WeightedDigraph(V);
        WeightedEdge e;
        int v,w;
        double wt;
        for (int i = 0; i < E; ++i)
        {
            v  = readIn.nextInt();
            w  = readIn.nextInt();
            wt = readIn.nextDouble();
            e = new WeightedEdge(v,w,wt);
            G.add_edge(e);
        }

        //Calculate Shortest path from every pair of vertex
        FloydWarshall allsp = new FloydWarshall(G);

        //Print the paths
        if (allsp.has_neg_cycle())
        {
            System.out.println("Graph has Negative cycle");
            for (WeightedEdge x : allsp.neg_cycle())
                System.out.print(x + " -> ");
            System.out.println();
        }
        else
            for (int s = 0; s < G.V(); s++)
                for (int t = 0; t < G.V(); t++)
                    if (allsp.has_path(s, t))
                    {
                        System.out.println(s + " to " + t + " : " + allsp.dist(s, t));
                        for (WeightedEdge x : allsp.path(s,t))
                            System.out.println(x);
                        System.out.println();
                    }
    }
}