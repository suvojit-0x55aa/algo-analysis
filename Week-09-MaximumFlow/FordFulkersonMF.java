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

class FlowEdge
{
    private final int v,w;
    private final double capacity;
    private double flow;

    FlowEdge(int v, int w, double capacity)
    {
        this.v = v;
        this.w = w;
        this.capacity = capacity;
    }

    public int from()  { return v; }
    public int to()    { return w; }
    public int other(int v)
    {
        if      (v == this.v)  return this.w;
        else if (v == this.w)  return this.v;
        else    throw new IllegalArgumentException();
    }

    public double capacity()  { return capacity; }
    public double flow()      { return flow;     }

    public double residual_capacity_to(int v)
    {
        if      (v == this.v)  return flow;
        else if (v == this.w)  return capacity - flow;
        else    throw new IllegalArgumentException();
    }

    public void add_residual_flow_to(int v, double delta)
    {
        if      (v == this.v)  flow -= delta;    //backward edge
        else if (v == this.w)  flow += delta;   //forward edge
        else    throw new IllegalArgumentException();
    }

    public String toString()
    {
        return new String(v + "->" + w + " (" + flow + "/" + capacity + ")");
    }
}

class FlowNetwork
{
    private final int V;
    private int E;
    private Bags<FlowEdge> adj[];

    FlowNetwork(int V)
    {
        this.V = V;
        this.E = 0;

        adj = (Bags<FlowEdge>[]) new Bags[V];
        for (int v = 0; v < V; ++v)
            adj[v] = new Bags<FlowEdge>();
    }

    public void add_flow_edge(FlowEdge e)
    {
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
        ++E;
    }

    public int V()  { return V; }
    public int E()  { return E; }

    private void valid_vertex(int v)  { if (v < 0 || v >= V)  throw new IllegalArgumentException(); }

    public Iterable<FlowEdge> adj(int v)
    {
        valid_vertex(v);
        return adj[v];
    }

    Iterable<FlowEdge> edges()
    {
        Bags<FlowEdge> edges = new Bags<FlowEdge>();
        for (int v = 0; v < V; ++v)
            for (FlowEdge e : adj(v))
                edges.add(e);
        return edges;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append("Vertex : " + V + " Edges : " + E + "\n");
        for (int v = 0; v < V; ++v)
        {
            str.append(v + " : ");
            for (FlowEdge e : adj(v))
                str.append(e + " ");
            str.append("\n");
        }
        return str.toString();
    }
}

class FordFulkerson
{
    private boolean marked[];
    private FlowEdge edgeTo[];
    private double value;

    FordFulkerson(FlowNetwork G, int src, int tgt)
    {
        valid_vertex(src, G.V());
        valid_vertex(tgt, G.V());
        if (src == tgt)  throw new IllegalArgumentException();

        value = excess(G, tgt);
        while (has_agumented_path(G, src, tgt))
        {
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = tgt; v != src; v = edgeTo[v].other(v))
                bottle = Math.min(bottle, edgeTo[v].residual_capacity_to(v));

            for (int v = tgt; v != src; v = edgeTo[v].other(v))
                edgeTo[v].add_residual_flow_to(v, bottle);

            value += bottle;
        }
    }

    private double excess(FlowNetwork G, int v)
    {
        double excess = 0.0;
        for (FlowEdge e : G.adj(v))
        {
            if (v == e.from())  excess -= e.flow();
            else                excess += e.flow();
        }
        return excess;
    }

    private void valid_vertex(int v, int V)  { if (v < 0 || v >= V)  throw new IllegalArgumentException(); }

    public boolean inCut(int v)
    {
        valid_vertex(v, marked.length);
        return marked[v];
    }

    public double total_flow()  { return value; }

    private boolean has_agumented_path(FlowNetwork G, int s, int t)
    {
        marked = new boolean[G.V()];
        edgeTo = new FlowEdge[G.V()];
        Queue<Integer> queue = new LinkedList<Integer>();

        marked[s] = true;
        queue.add(s);
        while (!queue.isEmpty() && !marked[t])
        {
            int v = queue.remove();
            for (FlowEdge e : G.adj(v))
            {
                int w = e.other(v);
                if (e.residual_capacity_to(w) > 0 && !marked[w])
                {
                    marked[w] = true;
                    edgeTo[w] = e;
                    queue.add(w);
                }
            }
        }
        return marked[t];
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
        FlowNetwork G = new FlowNetwork(V);
        FlowEdge e;
        int v,w;
        double wt;
        for (int i = 0; i < E; ++i)
        {
            v  = readIn.nextInt();
            w  = readIn.nextInt();
            wt = readIn.nextDouble();
            e = new FlowEdge(v,w,wt);
            G.add_flow_edge(e);
        }

        //Calculate Shortest path from every pair of vertex
        FordFulkerson flowNet = new FordFulkerson(G, 0, G.V() - 1);

        //Print the MinCut
        for (int i = 0; i < G.V(); ++i)
            if (flowNet.inCut(i))  System.out.print(i + " ");
        System.out.println("are in cut");

        //Print Max Flow
        System.out.println("Max Flow form " + 0 + " to " + (G.V() - 1)
                + " : " + flowNet.total_flow());
        for (int t = 0; t < G.V(); ++t)
            for (FlowEdge x : G.adj(t))
                if (t == x.from() && x.flow() > 0)
                    System.out.println(x);

        //Print the Network
        System.out.println(G);
    }
}