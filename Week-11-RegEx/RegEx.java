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

class DirectedDFSPaths
{
    private boolean marked[];
    private int edgeTo[];
    private final int s;
    private final int V;

    DirectedDFSPaths(DiGraphs G, int s)
    {
        this.V = G.V();
        valid_vertex(s);
        this.s = s;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        dfs(G, s);
    }

    DirectedDFSPaths(DiGraphs G, Iterable<Integer> s)
    {
        this.V = G.V();
        this.s = -1;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        for (Integer x : s)
        {
            valid_vertex(x);
            if (!marked[x]) dfs(G, x);
        }
    }

    private void valid_vertex(int v)
    {
        if (v < 0 || v > (V-1))  throw new IndexOutOfBoundsException();
    }

    private void dfs(DiGraphs G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w])
            {
                dfs(G, w);
                edgeTo[w] = v;
            }
    }

    public boolean has_path_to(int v)
    {
        valid_vertex(v);
        return marked[v];
    }

    public Iterable<Integer> path_to(int v)
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

class RegEx
{
    private char re[];
    private DiGraphs G;
    int m;

    RegEx(String regexp)
    {
        re = regexp.toCharArray();
        m  = regexp.length();
        G  = new DiGraphs(m + 1);
        Stack<Integer> op = new Stack<Integer>();

        for (int i = 0; i < m; ++i)
        {
            int lp = i;
            if (re[i] == '(' || re[i] == '|')
                op.push(i);
            else if (re[i] == ')')
            {
                int or = op.pop();
                if (re[or] == '|')
                {
                    lp = op.pop();
                    G.add_edge(lp, or + 1);
                    G.add_edge(or, i);
                }
                else lp = or;
            }
            if (i < m - 1 && re[i + 1] == '*')
            {
                G.add_edge(lp, i + 1);
                G.add_edge(i + 1, lp);
            }
            if (re[i] == '(' || re[i] == ')' || re[i] == '*')
                G.add_edge(i, i + 1);
        }
    }

    public boolean match(String txt)
    {
        if (txt.length() == 0)  throw new IllegalArgumentException();
        DirectedDFSPaths dfs = new DirectedDFSPaths(G, 0);
        Bags<Integer> pc = new Bags<Integer>();
        for (int i = 0; i < G.V(); i++)
            if (dfs.has_path_to(i))  pc.add(i);
        for (int i = 0; i < txt.length(); i++)
        {
            if (txt.charAt(i) == '*' || txt.charAt(i) == '(' || txt.charAt(i) == '|' || txt.charAt(i) == ')')
                throw new IllegalArgumentException();

            Bags<Integer> match = new Bags<Integer>();
            for (Integer v : pc)
            {
                if (v == m) continue;
                if (txt.charAt(i) == re[v] || re[v] == '.')
                    match.add(v + 1);
            }
            dfs = new DirectedDFSPaths(G, match);
            pc  = new Bags<Integer>();
            for (int j = 0; j < G.V(); j++)
                if (dfs.has_path_to(j))  pc.add(j);

            if (pc.is_empty())  return false;
        }

        for (Integer v : pc)
            if (v == m)  return true;
        return false;
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take input
        Scanner readIn = new Scanner(System.in);
        String text  = readIn.nextLine();
        String regex = readIn.next();

        //Find Substring
        RegEx matcher = new RegEx(regex);
        boolean x = matcher.match(text );
        if (x)  System.out.println("Found");
        else    System.out.println("Not Found");
    }
}