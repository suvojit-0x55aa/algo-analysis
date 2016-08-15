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

class BellmanFord
{
    private double distTo[];
    private WeightedEdge edgeTo[];
    private boolean onQ[];
    private Queue<Integer> queue;
    private int cost;
    private Iterable<WeightedEdge> cycle;

    BellmanFord(WeightedDigraph G, int s)
    {
        distTo = new double[G.V()];
        edgeTo = new WeightedEdge[G.V()];
        onQ    = new boolean[G.V()];
        queue  = new LinkedList<Integer>();

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;
        queue.add(s);
        onQ[s] = true;

        while (!queue.isEmpty() && !this.has_neg_cycle())
        {
            int v  = queue.remove();
            onQ[v] = false;
            relax(G, v);
        }
    }

    private void relax(WeightedDigraph G, int v)
    {
        for (WeightedEdge e : G.adj(v))
        {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight())
            {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQ[w])
                {
                    queue.add(w);
                    onQ[w] = true;
                }
            }
            if (cost++ % G.V() == 0 )  find_neg_cycle();
            if (this.has_neg_cycle())  return;
        }
    }

    public double dist_to(int v) { return distTo[v]; }

    public boolean has_path_to(int v)  { return distTo[v] < Double.POSITIVE_INFINITY; }

    public Iterable<WeightedEdge> path(int v)
    {
        if (!has_path_to(v))  return null;

        LinkedList<WeightedEdge> path = new LinkedList<WeightedEdge>();
        for (WeightedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.addFirst(e);
        return path;
    }

    public boolean has_neg_cycle()  { return cycle != null; }

    private void find_neg_cycle()
    {
        int V = edgeTo.length;
        WeightedDigraph spt = new WeightedDigraph(V);
        for (int v = 0; v < V; v++)
            if (edgeTo[v] != null)  spt.add_edge(edgeTo[v]);

        EdgeWeightedCycles cf = new EdgeWeightedCycles(spt);
        cycle = cf.cycle();
    }

    public Iterable<WeightedEdge> neg_cycle()  { return cycle; }
}

class Arbitage
{
    private int n;
    private String names[];
    private WeightedDigraph G;
    private BellmanFord spt;

    Arbitage(int n)
    {
        this.n = n;
        G      = new WeightedDigraph(n);
        names   = new String[n];
    }

    public void add_rate(String name, int id, int ex, double rate)
    {
        if (names[id] != null && names[id] != name)
            throw new IllegalArgumentException();

        G.add_edge(new WeightedEdge(id, ex, -Math.log(rate)));
        names[id] = name;
    }

    public void calc()  { spt = new BellmanFord(G, 0); }

    public boolean can_loot() { return spt.has_neg_cycle(); }

    public String calc_loot(double stake)
    {
        StringBuilder str = new StringBuilder();
        if (!can_loot())  return null;
        for (WeightedEdge e : spt.neg_cycle())
        {
            str.append(stake + " " + names[e.from()]);
            stake *= Math.exp(-e.weight());
            str.append(" = " + stake + " " + names[e.to()]+ " ");
        }
        return str.toString();
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take Input
        Scanner readIn = new Scanner(System.in);
        int n = readIn.nextInt();
        Arbitage loot = new Arbitage(n);

        //Insert Rate
        String name;
        for (int i = 0; i < n; ++i)
        {
            name = readIn.next();
            for (int j = 0; j < n; ++j)
            {
                double rate = readIn.nextDouble();
                loot.add_rate(name, i, j, rate);
            }
        }
        loot.calc();

        //Find loot if any
        double stake = readIn.nextDouble();
        if (loot.can_loot())  System.out.println(loot.calc_loot(stake));
        else                  System.out.println("No Arbitage Oppertunity");
    }
}