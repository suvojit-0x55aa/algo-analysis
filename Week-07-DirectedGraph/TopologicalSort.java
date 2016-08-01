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

class DirectedCycles
{
    private boolean marked[];
    private boolean onStack[];
    private int edgeTo[];
    private Stack<Integer> cycle;

    DirectedCycles(DiGraphs G)
    {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new int[G.V()];

        for (int i = 0; i < G.V(); ++i)
            if (!marked[i] && cycle == null)  dfs(G, i);
    }

    private void dfs(DiGraphs G, int v)
    {
        marked[v]  = true;
        onStack[v] = true;

        for (int w : G.adj(v))
        {
            if (cycle != null)  return;

            else if (!marked[w])
            {
                edgeTo[w] = v;
                dfs(G, w);
            }

            else if (onStack[w])
            {
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x])
                    cycle.push(x);
                cycle.push(w);
                cycle.push(v);
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

    Topological(DiGraphs G)
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

    private void dfs(DiGraphs G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w])  dfs(G, w);
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
            G.add_edge(v,w);
        }

        //Print the Topological Order
        Topological top = new Topological(G);

        if (top.has_order())
        {
            for (int v : top.order())
                System.out.print(v + " ");
            System.out.println("\nOrder of Vertex");
            for (int i = 0; i < G.V(); ++i)
                System.out.println(i + " : " + top.order(i));
        }
        else  System.out.print("Graph is Cyclic");
    }
}