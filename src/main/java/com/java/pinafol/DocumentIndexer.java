package com.java.pinafol;

//Indexes documents --> maps term to web pages containing it.

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DocumentIndexer {
	private Map<String, Map<String,Integer>> index = new HashMap<>();
	private int totalDocs = 0;
	
	public Map<String, Map<String,Integer>> getIndex(){
		return index;
	}
	
	public int getTotalDocs() {
	    return totalDocs;
	}
	
	public void indexDocuments(String folderPath) throws IOException {
		File folder = new File(folderPath);
		for(File file : folder.listFiles()) {
			if(file.getName().endsWith(".txt")) {
				index(file);
				totalDocs++;
			}
		}
	}
	
	public void index(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = br.readLine()) != null) {
			for(String word : line.toLowerCase().split("\\W+")) {
				if(word.isEmpty()) continue;
				index.putIfAbsent(word, new HashMap<>());
				// reference
				Map<String, Integer> docMap = index.get(word);
				docMap.put(file.getName(), docMap.getOrDefault(file.getName(), 0)+1);
			}
		}
		br.close();
	}
}

