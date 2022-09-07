package com.kh0ma.spelannotation.controller;

import com.kh0ma.spelannotation.aspectlog.Log;
import com.kh0ma.spelannotation.aspectlog.LogParam;
import com.kh0ma.spelannotation.dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BookController {

    @PostMapping(
            value = "/books",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    @Log(label = "Log from aspect.", params = {
            @LogParam(name = "bookName", spel = "#createBook.getName()"),
            @LogParam(name = "author", spel = "#createBook.getAuthor()")
    })
    public ResponseEntity<BookDto> createArtifactTemplate(
            @RequestBody BookDto createBook
    ) {
        log.info("Log from method. bookName={}", createBook.getName());
        return ResponseEntity.ok(createBook);
    }
}
