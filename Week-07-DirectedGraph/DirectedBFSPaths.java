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

class DiGraphs
{
    private final int V;
    private int E;

    Bags<Integer> adj[];

    DiGraphs(int n)
    {
        this.V = n;
        this.E = 0;
        adj = (Bags<Integer>[])new Bags[V];
        for (int i = 0; i < V; ++i)
            adj[i] = new Bags<Integer>();
    }

    DiGraphs(DiGraphs G)
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

    public DiGraphs reverse()
    {
        DiGraphs reverse = new DiGraphs(this.V);

        for (int v = 0; v < this.V; ++v)
            for (int w : this.adj(v))
                reverse.add_edge(w, v);

        return reverse;
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

class DirectedBFSPaths
{
    private boolean marked[];
    private int edgeTo[];
    private int distTo[];
    private final int V;
    private final int s;
    private static final int INFINITY = Integer.MAX_VALUE;

    DirectedBFSPaths(DiGraphs G, int s)
    {
        this.V = G.V();
        valid_vertex(s);
        this.s = s;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];

        bfs(G, s);
    }

    private void valid_vertex(int v)
    {
        if (v < 0 || v > (V-1))  throw new IndexOutOfBoundsException();
    }

    private void bfs(DiGraphs G, int s)
    {
        LinkedList<Integer> queue = new LinkedList<Integer>();
        for (int i = 0; i < G.V(); ++i)
            distTo[i] = INFINITY;
        distTo[s] = 0;
        marked[s] = true;
        queue.addLast(s);
        while (!queue.isEmpty())
        {
            int v = queue.removeFirst();
            for (int w : G.adj(v))
            {
                if (!marked[w])
                {
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    queue.addLast(w);
                }
            }
        }
    }

    public boolean has_path_to(int v)
    {
        valid_vertex(v);
        return marked[v];
    }

    public int dist_to(int v)
    {
        valid_vertex(v);
        return distTo[v];
    }

    public Iterable<Integer> path_to (int v)
    {
        valid_vertex(v);
        if (!has_path_to(v))  return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
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
        DiGraphs G = new DiGraphs(vert);
        for (int i = 0; i < edge; ++i)
        {
            int v = readIn.nextInt();
            int w = readIn.nextInt();
            G.add_edge(v, w);
        }

        //Take Path Query
        int s = readIn.nextInt();
        DirectedBFSPaths bfs = new DirectedBFSPaths(G, s);

        //Print paths
        for (int i = 0; i < G.V(); ++i)
        {
            if (bfs.has_path_to(i))
            {
                for (int x : bfs.path_to(i))
                {
                    if (x == s)  System.out.print(x);
                    else         System.out.print( x + " - ");
                }
                System.out.println(" : " + bfs.dist_to(i));
            }
        }
    }
}