package ssafy.study.ssafystudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.study.ssafystudy.service.HelloService;

@RestController
public class HelloController {
	private final HelloService helloService;

	@Autowired
	public HelloController(HelloService helloService) {
		this.helloService = helloService;
	}

	@GetMapping("/hello")
	String hello() {
		return helloService.hi();
	}
}
