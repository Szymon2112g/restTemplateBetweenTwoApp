package com.example.Sender.Controller;

import com.example.Sender.Model.Post;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class SenderController {

    @GetMapping(value = "/first")
    public String getFirst() {
        return new RestTemplate().getForObject("http://localhost:8081/receive/", String.class);
    }

    @GetMapping(value = "/second")
    public Post getSecond() {
        return new RestTemplate().getForEntity("http://localhost:8081/receive/post", Post.class).getBody();
    }

    @GetMapping(value = "/third")
    public Post[] getThird() {
        return new RestTemplate().getForEntity("http://localhost:8081/receive/posts", Post[].class).getBody();
    }

    @GetMapping(value = "/fourth/{id}")
    public Post getFourth(@PathVariable int id) {
        return new RestTemplate().getForEntity("http://localhost:8081/receive/post/{id}", Post.class, id).getBody();
    }

    @GetMapping(value = "/fifth/{id}/{name}")
    public Post getFifth(@PathVariable int id, @PathVariable String name) {

        ResponseEntity<Post> postResponseEntity = new RestTemplate().getForEntity(
                "http://localhost:8081/receive/post/{id}/{name}",
                Post.class, id, name);

        if(postResponseEntity.getStatusCode() == HttpStatus.OK) {
            return postResponseEntity.getBody();
        } else {
            return null;
        }
    }

    @GetMapping(value = "/sixth/{id}/{name}")
    public Post getSixth(@PathVariable int id, @PathVariable String name) {

        HttpHeaders headers = new HttpHeaders();

        headers.set("id" , String.valueOf(id));
        headers.set("name", name);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Post> postResponseEntity = new RestTemplate().exchange(
                "http://localhost:8081/receive/post/get-by-header/", HttpMethod.GET, request, Post.class);

        if(postResponseEntity.getStatusCode() == HttpStatus.OK) {
            return postResponseEntity.getBody();
        } else {
            return null;
        }


    }
}
