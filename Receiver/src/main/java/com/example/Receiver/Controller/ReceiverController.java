package com.example.Receiver.Controller;

import com.example.Receiver.Dao.PostRepository;
import com.example.Receiver.Model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ReceiverController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostRepository postRepository;

    @GetMapping(path = "/get/post")
    public Post getTestPost() {
        return postRepository.findById("1").get();
    }

    @GetMapping(path = "/get/posts")
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        postRepository.findAll().forEach(posts::add);

        return posts;
    }

    @GetMapping(path = "/get/post/{id}")
    public Post getPostById(@PathVariable String id) {
        return postRepository.findById(id).get();
    }

    @GetMapping(path = "/receive/post/{id}/{name}")
    public ResponseEntity<Post> getPostByIdWithCheckTitle(@PathVariable String id, @PathVariable String title) {

        if(id == "" || title == "") {
            return ResponseEntity.badRequest().build();
        }

        Post post = postRepository.findById(id).get();

        if(post.getTitle() != title) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @GetMapping(path = "/get/post/get-by-header/")
    public ResponseEntity<Post> getPostByIdByHeaderWithCheckTitle(@RequestHeader Map<String, Object> stringObjectMap) {
        if(stringObjectMap.get("id").toString() == "" || stringObjectMap.get("title").toString() == "") {
            return ResponseEntity.badRequest().build();
        }

        String id = stringObjectMap.get("id").toString();
        String title = stringObjectMap.get("title").toString();

        Post post = postRepository.findById(id).get();

        if(post.getTitle() != title) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping(path = "/post/post/add-by-header")
    public ResponseEntity<Void> addPostByHeader(@RequestHeader Map<String, Object> stringObjectMap) {

        int userId = Integer.parseInt(stringObjectMap.get("userid").toString());
        String title = stringObjectMap.get("title").toString();
        String body = stringObjectMap.get("body").toString();

        Post post = new Post(userId, title, body);

        postRepository.save(post);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/get/post/{id}").buildAndExpand((post.getId())).toUri();

        return  ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/put/post/update-by-headers/{id}")
    public void updatePostByHeaders(@RequestHeader Map<String, Object> map,
                                    @PathVariable String id) {

        map.forEach( (s, o) -> {
            logger.warn(id + " s = " + s + " o = " + o);
        });

        int userId = Integer.parseInt(map.get("userid").toString());
        String title = map.get("title").toString();
        String body = map.get("body").toString();

        if(id == null) { return;}

        Post postToChange = postRepository.findById(id).get();

        postToChange.setUserId(userId);
        postToChange.setTitle(title);
        postToChange.setBody(body);

        postRepository.save(postToChange);
    }

    @DeleteMapping(path = "/delete/post/{id}")
    public void deletePostById(@PathVariable String id) {
        postRepository.deleteById(id);
    }
}
