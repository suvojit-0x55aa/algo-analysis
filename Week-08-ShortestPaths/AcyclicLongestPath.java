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

class DirectedCycles
{
    private boolean marked[];
    private boolean onStack[];
    private int edgeTo[];
    private LinkedList<Integer> cycle;

    DirectedCycles(WeightedDigraph G)
    {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new int[G.V()];

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
                edgeTo[w] = v;
                dfs(G, w);
            }

            else if (onStack[w])
            {
                cycle = new LinkedList<Integer>();
                for (int x = v; x != w; x = edgeTo[x])
                    cycle.addFirst(x);
                cycle.addFirst(w);
                cycle.addFirst(v);
            }
        }
        onStack[v] = false;
    }

    public boolean has_cycle() { return cycle != null; }

    public Iterable<Integer> cycle() { return cycle; }
}

class Topological
{
    private Deque<Integer> order;
    private int topologyOrder[];
    private boolean marked[];
    private int counter = 0;

    Topological(WeightedDigraph G)
    {
        DirectedCycles cycleFind = new DirectedCycles(G);
        if (!cycleFind.has_cycle())
        {
            order         = new LinkedList<Integer>();
            topologyOrder = new int[G.V()];
            marked        = new boolean[G.V()];

            for (int v = 0; v < G.V(); ++v)
                if (!marked[v])  dfs(G, v);
        }
    }

    private void dfs(WeightedDigraph G, int v)
    {
        marked[v] = true;
        for (WeightedEdge e : G.adj(v))
            if (!marked[e.to()])  dfs(G, e.to());
        order.addFirst(v);
        topologyOrder[v] = G.V() - 1 - counter++;
    }

    public boolean has_order()  { return order != null; }

    public Iterable<Integer> order()  { return order; }

    public int order(int v)
    {
        if (has_order())  return topologyOrder[v];
        else              return -1;
    }
}

class AcyclicLP
{
    private double distTo[];
    private WeightedEdge edgeTo[];

    AcyclicLP(WeightedDigraph G, int s)
    {
        distTo = new double[G.V()];
        edgeTo = new WeightedEdge[G.V()];
        for (int v = 0; v < G.V(); ++v)
            distTo[v] = Double.NEGATIVE_INFINITY;
        distTo[s] = 0.0;

        Topological topological = new Topological(G);
        if (!topological.has_order())  throw new IllegalArgumentException();

        for (int v : topological.order())
            for (WeightedEdge e: G.adj(v))
                relax(e);
    }

    private void relax(WeightedEdge e)
    {
        int v = e.from();
        int w = e.to();
        if (distTo[w] < distTo[v] + e.weight())
        {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }

    public double dist_to(int v)  { return distTo[v]; }

    public boolean has_path_to(int v)  { return distTo[v] > Double.NEGATIVE_INFINITY; }

    public Iterable<WeightedEdge> path_to(int v)
    {
        if (!has_path_to(v))  return null;
        Deque<WeightedEdge> path = new LinkedList<WeightedEdge>();

        for (WeightedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
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

        //Calculate Shortest path from vertex : s
        int s = readIn.nextInt();
        AcyclicLP lp = new AcyclicLP(G, s);

        //Print the paths
        for (int t = 0; t < G.V(); ++t)
        {
            if (lp.has_path_to(t))
            {
                System.out.println("\n" + s + " to " + t + " : " + lp.dist_to(t));
                for (WeightedEdge x : lp.path_to(t))
                    System.out.println(x);
            }
        }

    }
}