package com.example.Receiver.Controller;

import com.example.Receiver.Model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ReceiverController {

    @GetMapping(path = "/receive/")
    public String getFirst() {
        return "First String!";
    }

    @GetMapping(path = "/receive/post")
    public Post getSecond() {
        return new Post(1, 11, "title", "body");
    }

    @GetMapping(path = "/receive/posts")
    public List<Post> getThird() {
        List<Post> posts = new ArrayList<>();
        for(int i = 0; i <= 1000; i++) {
            posts.add(new Post( i, i*i, "title " + i, "body " + i));
        }
        return posts;
    }

    @GetMapping(path = "/receive/post/{id}")
    public Post getFourth(@PathVariable int id) {
        return new Post( id, id,"cos", "cos");
    }

    @GetMapping(path = "/receive/post/{id}/{name}")
    public ResponseEntity<Post> getFifth(@PathVariable int id, @PathVariable String name) {
        if(id <= 0 || name == "") {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new Post( id, id, name, "cos"));
    }

    @GetMapping(path = "/receive/post/get-by-header/")
    public ResponseEntity<Post> getSixth(@RequestHeader Map<String, Object> stringObjectMap) {
        if(Integer.parseInt(stringObjectMap.get("id").toString()) <= 0 || stringObjectMap.get("name") == "") {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new Post(
                        Integer.parseInt(stringObjectMap.get("id").toString()),
                        Integer.parseInt(stringObjectMap.get("id").toString()),
                        stringObjectMap.get("name").toString(),
                        "cos"
                )
        );
    }
}
