//package com.java.pinafol;
//
//import java.io.File;
//import java.util.*;
//
//public class SearchEngineService {
//    public static void main(String[] args) throws Exception {
//        Scanner scanner = new Scanner(System.in);
//        DocumentIndexer indexer = new DocumentIndexer();
//        Trie trie = new Trie();
//        
//        String folderPath = "D:\\Skills\\Pinafol Documents";
//        // Index sample documents
//        indexer.indexDocuments(folderPath);
//        Map<String, Map<String,Integer>> index = indexer.getIndex();
//
//        // Fill autocomplete trie
//        for (String word : index.keySet()) trie.insert(word);
//
//        // Search loop
//        SearchService searchService = new SearchService(index, indexer.getTotalDocs());
//        System.out.println("Search Engine Ready 🔍 (type 'exit' to quit)");
//
//        while (true) {
//            System.out.print("\nEnter word or prefix: ");
//            String query = scanner.nextLine().toLowerCase();
//            if (query.equalsIgnoreCase("exit")) break;
//
//            // Autocomplete suggestions
//            List<String> suggestions = trie.suggest(query);
//            System.out.println("Autocomplete suggestions: " + suggestions);
//
//            // Search results
//            List<String> results = searchService.search(query);
//            if (results.isEmpty()) {
//                System.out.println("No documents found.");
//            } else {
//                System.out.println("Documents containing '" + query + "':");
//                results.forEach(System.out::println);
//            }
//        }
//    }
//}
//
//
