package com.example.Sender.Controller;

import com.example.Sender.Model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SenderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/get/post/first")
    public Post getFirstPost() {
        return new RestTemplate().getForEntity("http://localhost:8081/get/post", Post.class).getBody();
    }

    @GetMapping(value = "/get/post/all")
    public Post[] getAllPost() {
        return new RestTemplate().getForEntity("http://localhost:8081/get/posts", Post[].class).getBody();
    }

    @GetMapping(value = "/get/post/{id}")
    public Post getPostById(@PathVariable int id) {
        return new RestTemplate().getForEntity("http://localhost:8081/get/post/{id}", Post.class, id).getBody();
    }

    @GetMapping(value = "/get/post/{id}/{title}")
    public Post getPostByIdWithCheckTitle(@PathVariable int id, @PathVariable String title) {

        ResponseEntity<Post> postResponseEntity = new RestTemplate().getForEntity(
                "http://localhost:8081/get/post/{id}/{name}",
                Post.class, id, title);

        if(postResponseEntity.getStatusCode() == HttpStatus.OK) {
            return postResponseEntity.getBody();
        } else {
            return null;
        }
    }

    @GetMapping(value = "/get/post/header/{id}/{title}")
    public Post getPostByIdByHeaderWithCheckTitle(@PathVariable int id, @PathVariable String title) {

        HttpHeaders headers = new HttpHeaders();

        headers.set("id" , String.valueOf(id));
        headers.set("title", title);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Post> postResponseEntity = new RestTemplate().exchange(
                "http://localhost:8081/get/post/get-by-header/", HttpMethod.GET, request, Post.class);

        if(postResponseEntity.getStatusCode() == HttpStatus.OK) {
            return postResponseEntity.getBody();
        } else {
            return null;
        }
    }

    @GetMapping(value = "/post/post/addbyheader/{userid}/{title}/{body}")
    public String addPostByHeader(
            @PathVariable int userid,
            @PathVariable String title,
            @PathVariable String body) {

        HttpHeaders headers = new HttpHeaders();

        headers.set("userid" , String.valueOf(userid));
        headers.set("title", title);
        headers.set("body", body);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Void> response = new RestTemplate().postForEntity(
                "http://localhost:8081/post/post/add-by-header", request, Void.class);

        if(response.getStatusCode() == HttpStatus.CREATED) {
            return response.getHeaders().getFirst("location");
        } else {
            return null;
        }
    }

    @GetMapping(path = "/put/post/data-send-by-headers/{id}/{userid}/{title}/{body}")
    public void updatePostByHeaders(
            @PathVariable String id,
            @PathVariable String userid,
            @PathVariable String title,
            @PathVariable String body
            ) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("userid", userid);
        headers.set("title", title);
        headers.set("body", body);

        HttpEntity httpEntity= new HttpEntity(headers);

        new RestTemplate().put("http://localhost:8081/put/post/update-by-headers/{id}", httpEntity, id);
    }

    @GetMapping(path = "/delete/post/{id}")
    public void deletePostById(@PathVariable String id) {
        new RestTemplate().delete("http://localhost:8081/delete/post/{id}", id);
    }
}
