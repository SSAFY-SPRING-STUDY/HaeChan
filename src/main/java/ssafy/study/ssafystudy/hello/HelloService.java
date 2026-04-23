package ssafy.study.ssafystudy.hello;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
	public String hi() {
		return "Hello Spring Boot!";
	}
}
