package com.java.pinafol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

//responsible for searching the indexed documents.

public class SearchService {
	private final Map<String, Map<String,Integer>> index;
	private final int totalDocs;
	
	public SearchService(Map<String, Map<String, Integer>> index, int totalDocs) {
	    this.index = index;
	    this.totalDocs = totalDocs;
	}
	
	// tf-idf approach --> automatically down-weights common words, because they don't carry meaningful info.
	public List<String> search(String query) {
	    query = query.toLowerCase();
	    String[] words = query.split("\\s+");
	    
	    List<String> correctedWords = new ArrayList<>();

	    for (String inputWord : words) {
	        if (index.containsKey(inputWord)) {
	            correctedWords.add(inputWord);
	            continue;
	        }

	        String bestMatch = null;
	        int minDistance = Integer.MAX_VALUE;

	        for (String indexedWord : index.keySet()) {
	            int dist = levenshtein(inputWord, indexedWord);
	            if (dist < minDistance && dist <= 2) { // threshold of 2 edits
	                minDistance = dist;
	                bestMatch = indexedWord;
	            }
	        }

	        if (bestMatch != null) {
	            System.out.println("🔍 Did you mean: '" + bestMatch + "' for '" + inputWord + "'?");
	            correctedWords.add(bestMatch);
	        } else {
	            correctedWords.add(inputWord); // fallback
	        }
	    }

	    // Replace original words with corrected ones
	    words = correctedWords.toArray(new String[0]);

	    Map<String, Double> docScores = new HashMap<>();

	    for (String word : words) {
	        if (!index.containsKey(word)) continue;

	        Map<String, Integer> docs = index.get(word);
	        int docFreq = docs.size(); // Number of docs with the word

	        double idf = Math.log((double) totalDocs / docFreq); // Inverse Document Frequency

	        for (Map.Entry<String, Integer> entry : docs.entrySet()) {
	            String doc = entry.getKey();
	            int tf = entry.getValue();

	            double tfidf = tf * idf;

	            docScores.put(doc, docScores.getOrDefault(doc, 0.0) + tfidf);
	        }
	    }

	    if (docScores.isEmpty()) return List.of();

	    PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(
	        (a, b) -> Double.compare(b.getValue(), a.getValue())
	    );
	    pq.addAll(docScores.entrySet());

	    List<String> result = new ArrayList<>();
	    while (!pq.isEmpty()) {
	        var entry = pq.poll();
	        result.add(entry.getKey() + " (TF-IDF: " + String.format("%.4f", entry.getValue()) + ")");
	    }

	    return result;
	}
	
	private int levenshtein(String a, String b) {
	    int[][] dp = new int[a.length() + 1][b.length() + 1];

	    for (int i = 0; i <= a.length(); i++) {
	        for (int j = 0; j <= b.length(); j++) {
	            if (i == 0) dp[i][j] = j;
	            else if (j == 0) dp[i][j] = i;
	            else if (a.charAt(i - 1) == b.charAt(j - 1)) {
	                dp[i][j] = dp[i - 1][j - 1];
	            } else {
	                dp[i][j] = 1 + Math.min(
	                    dp[i - 1][j - 1],
	                    Math.min(dp[i - 1][j], dp[i][j - 1])
	                );
	            }
	        }
	    }

	    return dp[a.length()][b.length()];
	}


}

// Single Word query --> auto-complete works
//public List<String> search(String word){
//	
//	word = word.toLowerCase();
//	if(!index.containsKey(word)) return List.of();
//	
//	PriorityQueue<Map.Entry<String,Integer>> pq = new PriorityQueue<>(
//		(a,b) -> b.getValue() - a.getValue()
//	);
//	pq.addAll(index.get(word).entrySet());
//	
//	List<String> result = new ArrayList<>();
//	while(!pq.isEmpty()) {
//		var entry = pq.poll();
//		result.add(entry.getKey() + " (frequency : " + entry.getValue() + ")");
//	}
//	return result;
//}

//--------------------------------------------------------------------------------------

//multi-word query
	// auto-complete doesn't work for multiple words.
//	public List<String> search(String query) {
//	    query = query.toLowerCase();
//	    String[] words = query.split("\\s+"); // Split into multiple words
//
//	    Map<String, Integer> docScore = new HashMap<>();
//
//	    for (String word : words) {
//	        if (!index.containsKey(word)) continue;
//
//	        for (Map.Entry<String, Integer> entry : index.get(word).entrySet()) {
//	            String doc = entry.getKey();
//	            int freq = entry.getValue();
//	            docScore.put(doc, docScore.getOrDefault(doc, 0) + freq);
//	        }
//	    }
//
//	    if (docScore.isEmpty()) return List.of();
//
//	    // Rank by score
//	    PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
//	        (a, b) -> b.getValue() - a.getValue()
//	    );
//	    pq.addAll(docScore.entrySet());
//
//	    List<String> result = new ArrayList<>();
//	    while (!pq.isEmpty()) {
//	        var entry = pq.poll();
//	        result.add(entry.getKey() + " (score: " + entry.getValue() + ")");
//	    }
//
//	    return result;
//	}

// ------------------------------------------------------------------------------------

//public List<String> search(String query) {
//    query = query.toLowerCase();
//    List<String> result = new ArrayList<>();
//
//    // For ranking
//    Map<String, Integer> docScore = new HashMap<>();
//
//    for (String word : index.keySet()) {
//        if (word.startsWith(query)) {
//            for (Map.Entry<String, Integer> entry : index.get(word).entrySet()) {
//                docScore.put(entry.getKey(),
//                    docScore.getOrDefault(entry.getKey(), 0) + entry.getValue());
//            }
//        }
//    }
//
//    if (docScore.isEmpty()) return List.of();
//
//    // Sort by score descending
//    PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
//        (a, b) -> b.getValue() - a.getValue()
//    );
//    pq.addAll(docScore.entrySet());
//
//    while (!pq.isEmpty()) {
//        var entry = pq.poll();
//        result.add(entry.getKey() + " (score: " + entry.getValue() + ")");
//    }
//
//    return result;
//}


