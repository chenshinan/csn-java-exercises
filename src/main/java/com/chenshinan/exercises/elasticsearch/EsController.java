package com.chenshinan.exercises.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author shinan.chen
 * @since 2019/7/2
 */
@RestController
@RequestMapping(value = "/elasticsearch")
public class EsController {
    @Autowired
    private EsService esService;

    @GetMapping(value = "/search")
    public ResponseEntity<SearchResponse> search(@RequestParam String index,
                                                 @RequestParam String searchStr) {
        return Optional.ofNullable(esService.search(index, searchStr))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("error.elasticsearch.search"));
    }

    @GetMapping(value = "/searchTest")
    public ResponseEntity<SearchResponse> searchTest(@RequestParam String index,
                                                     @RequestParam String searchStr) {
        return Optional.ofNullable(esService.searchTest(index, searchStr))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("error.elasticsearch.search"));
    }

    @GetMapping(value = "/searchAll")
    public ResponseEntity<SearchResponse> searchAll(@RequestParam String index) {
        return Optional.ofNullable(esService.searchAll(index))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("error.elasticsearch.searchAll"));
    }

    @GetMapping(value = "/createPage")
    public ResponseEntity<IndexResponse> createPage(@RequestParam String index,
                                                    @RequestParam Long id,
                                                    @RequestParam String title,
                                                    @RequestParam String content) {
        return Optional.ofNullable(esService.createPage(index, id, title, content))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("error.elasticsearch.createPage"));
    }

    @GetMapping(value = "/createIndex")
    public ResponseEntity<CreateIndexResponse> createIndex(@RequestParam String index) {
        return Optional.ofNullable(esService.createIndex(index))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("error.elasticsearch.createIndex"));
    }

    @GetMapping(value = "/getPage")
    public ResponseEntity<GetResponse> getPage(@RequestParam String index,
                                               @RequestParam Long id) {
        return Optional.ofNullable(esService.getPage(index, id))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("error.elasticsearch.getPage"));
    }

    @DeleteMapping(value = "/deletePage")
    public ResponseEntity<DeleteResponse> deletePage(@RequestParam String index,
                                                     @RequestParam Long id) {
        return Optional.ofNullable(esService.deletePage(index, id))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("error.elasticsearch.deletePage"));
    }
}
