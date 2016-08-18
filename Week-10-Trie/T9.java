// Author : Shin

import java.io.*;
import java.lang.*;
import java.lang.Runtime;
import java.lang.String;
import java.util.*;
import java.util.Arrays;

class T9
{
    private static Map<Integer, Set<Character>> keyMap;
    private static TrieDict26 dict;

    T9()
    {
        map_keys();
        dict = new TrieDict26();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("words.txt"));
            String word = br.readLine();
            while (word != null)
            {
                dict.put(word);
                word = br.readLine();
            }
        }
        catch (Exception e)  { throw new RuntimeException(); }
    }

    private static class Node
    {
        private boolean val = false;
        private Node next[] = new Node[TrieDict26.R];
    }

    private static class TrieDict26
    {
        private static final int R = 26;
        private Node root = new Node();
        private int n;

        TrieDict26()  {}

        private Node get(Node x, String key, int d)
        {
            if (x == null)  return null;
            if (d == key.length())  return x;
            char c = (char)(key.charAt(d) - 'a');
            return get(x.next[c], key, d + 1);
        }

        public boolean get(String key)
        {
            Node x = get(root, key, 0);
            if (x == null)  return false;
            else            return x.val;
        }

        public boolean contains(String key)  { return get(key) != false; }

        private Node put(Node x, String key, int d)
        {
            if (x == null)  x = new Node();
            if (d == key.length())
            {
                if (x.val == false)  ++n;
                x.val = true;
                return x;
            }
            char c = (char)(key.charAt(d) - 'a');
            x.next[c] = put(x.next[c], key, d + 1);
            return x;
        }

        public void put(String key)
        {
            root = put(root, key, 0);
        }

        public int size()  { return n; }

        public boolean is_empty()  { return n == 0; }
    }

    private static void map_keys()
    {
        keyMap = new HashMap<Integer, Set<Character>>();
        keyMap.put(2,new HashSet<Character>(Arrays.asList('a', 'b', 'c')));
        keyMap.put(3,new HashSet<Character>(Arrays.asList('d', 'e', 'f')));
        keyMap.put(4, new HashSet<Character>(Arrays.asList('g', 'h', 'i')));
        keyMap.put(5,new HashSet<Character>(Arrays.asList('j', 'k', 'l')));
        keyMap.put(6,new HashSet<Character>(Arrays.asList('m', 'n', 'o')));
        keyMap.put(7,new HashSet<Character>(Arrays.asList('p', 'q', 'r', 's')));
        keyMap.put(8,new HashSet<Character>(Arrays.asList('t', 'u', 'v')));
        keyMap.put(9,new HashSet<Character>(Arrays.asList('w', 'x', 'y', 'z')));
    }

    public Iterable<String> get_match(String keySequence)
    {
        int key[] = new int[keySequence.length()];
        for (int i = 0; i < keySequence.length(); ++i)
        {
            if (!Character.isDigit(keySequence.charAt(i)))
                throw new IllegalArgumentException();
            key[i] = Integer.parseInt(String.valueOf(keySequence.charAt(i)));
        }
        Queue<String> matches = new LinkedList<String>();
        suggest(dict.root, 0, new StringBuilder(), key, matches);
        return matches;
    }

    private void suggest(Node x, int i,StringBuilder store, int[] keyPresses, Queue<String> q)
    {
        if (x == null)  return;
        if (i == keyPresses.length)
        {
            if (x.val) q.add(store.toString());
        }
        else
        {
            for (Character ch : keyMap.get(keyPresses[i]))
            {
                suggest(x.next[ch.charValue() - 'a'], i + 1, store.append(ch), keyPresses, q);
                store.deleteCharAt(store.length() - 1);
            }
        }
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take Input A sequence of key presses
        Scanner readIn = new Scanner(System.in);
        T9 guesser = new T9();
        String keyPresses = readIn.next();
        readIn.close();

        //Print All Matches
        for (String guess : guesser.get_match(keyPresses))
            System.out.println(guess);
    }
}