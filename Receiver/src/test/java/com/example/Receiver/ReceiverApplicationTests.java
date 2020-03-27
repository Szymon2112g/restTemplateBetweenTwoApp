package com.example.Receiver;

import com.example.Receiver.Dao.PostRepository;
import com.example.Receiver.Model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReceiverApplicationTests {

	@Autowired
	private PostRepository postRepository;

	@Test
	void contextLoads() {
	}

}
