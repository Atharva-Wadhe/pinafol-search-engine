package com.java.pinafol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TrieNode{
	Map<Character, TrieNode> children = new HashMap<>();
	boolean isEndOfWord;
}

public class Trie {
	private final TrieNode root = new TrieNode();
	
	public void insert(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            curr = curr.children.computeIfAbsent(c, k -> new TrieNode());
        }
        curr.isEndOfWord = true;
    }
	
	public List<String> suggest(String prefix){
		List<String> results = new ArrayList<>();
		TrieNode node = root;
		for(char c : prefix.toCharArray()) {
			node = node.children.get(c);
			if(node == null) return results;
		}
		dfs(node, new StringBuilder(prefix), results);
		return results;
	}
	
	public void dfs(TrieNode node, StringBuilder sb, List<String> results) {
		if(node.isEndOfWord) results.add(sb.toString());
		for(char c : node.children.keySet()) {
			sb.append(c);
			dfs(node.children.get(c), sb, results);
			sb.deleteCharAt(sb.length()-1);
		}
	}
}
