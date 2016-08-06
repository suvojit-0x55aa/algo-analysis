//Author : Shin

import java.lang.*;
import java.lang.Comparable;
import java.lang.Double;
import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;
import java.lang.Iterable;
import java.lang.String;
import java.lang.System;
import java.util.*;
import java.util.Scanner;

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

        //Print All Edges
        for (Edge x : G.edges())
            System.out.println(x);
    }
}
