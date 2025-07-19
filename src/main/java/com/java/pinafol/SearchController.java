package com.java.pinafol;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SearchController {

    private final SearchService searchService;

    public SearchController() throws Exception {
        DocumentIndexer indexer = new DocumentIndexer();
        indexer.indexDocuments("D:\\Skills\\Pinafol Documents");
        this.searchService = new SearchService(indexer.getIndex(), indexer.getTotalDocs());
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        List<String> results = searchService.search(query);
        model.addAttribute("query", query);
        model.addAttribute("results", results);
        return "results";
    }
}

