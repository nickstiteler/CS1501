// Based off of Robert Sedgewick's Algorithms 4th Edition: R-way trie
// Under Strings, Chpt 5.2
// Most changes are simply replacing the String objects with StringBuilders
// and removing un-important methods for simplicity 


import java.io.*;
import java.util.*;


public class TrieST<Value> {
    private static final int R = 256;     
    
    private Node root;
    
    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }
        
    public Value get(StringBuilder prefix){
        Node x = get(root, prefix, 0);
        if(x == null) return null;
        return (Value) x.val;
    }

    public boolean contains (StringBuilder prefix){
        return get(prefix) != null;
    }
    
    private Node get(Node x, StringBuilder prefix, int d) {
        if (x == null) return null;
        if (d == prefix.length()) return x;
        char c = prefix.charAt(d);
        return get(x.next[c], prefix, d+1);
    }
    
    public void put(StringBuilder prefix, Value val){
        root = put(root, prefix, val, 0);
    }
    
    private Node put(Node x, StringBuilder prefix, Value val, int d){
        if (x == null) 
            x = new Node();
        if (d == prefix.length()) {
            x.val = val;
            return x;
        }
        char c = prefix.charAt(d);
        x.next[c] = put(x.next[c], prefix, val, d+1);
        return x;
    }
    
    public StringBuilder longestPrefixOf(StringBuilder query) {
        int length = longestPrefixOf(root, query, 0, 0);
        return new StringBuilder(query.substring(0, length));
    }
    
    private int longestPrefixOf(Node x, StringBuilder query, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d+1, length);
    }
    
    public Iterable<StringBuilder> keys() {
        return keysWithPrefix(null);
    }
    
    public Iterable<StringBuilder> keysWithPrefix(StringBuilder pattern) {
        Queue<StringBuilder> queue = new Queue<StringBuilder>();
        Node x = get(root, pattern, 0);
        collect(x, pattern, queue);
        return queue;
    }
    
    private void collect(Node x, StringBuilder prefix, Queue<StringBuilder> results) {
        if (x == null) return;
        if (x.val != null) results.enqueue(prefix);
        for (int c = 0; c < R; c++)
            collect(x.next[c], prefix.append((char)c), results);
    }
    
    public Iterable<StringBuilder> keysThatMatch(StringBuilder pattern) {
        Queue<StringBuilder> results = new Queue<StringBuilder>();
        collect(root, null, pattern, results);
        return results;
    }
    
    public void collect(Node x, StringBuilder prefix, StringBuilder pattern, Queue<StringBuilder> results){
        if (x == null) return;
        if (prefix.length() == pattern.length() && x.val != null) results.enqueue(prefix);
        if (prefix.length() == pattern.length()) return;
        char next = pattern.charAt(prefix.length());
        for (int c = 0; c < R; c++)
            if (next == '.' || next == c)
                collect(x.next[c], prefix.append((char)c), pattern, results);
    }
    
    public void delete(StringBuilder prefix) {
        root = delete(root, prefix, 0);
    }

    private Node delete(Node x, StringBuilder prefix, int d){
        if (x == null) return null;
        if (d == prefix.length()) x.val = null;
        else {
            char c = prefix.charAt(d);
            x.next[c] = delete(x.next[c], prefix, d+1);
        }
        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if(x.next[c] != null)
                return x;
        return null;
    }  
}