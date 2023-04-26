package jpabook.jpabook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JpabookApplication {

	public static void main(String[] args) {

//		Logger logger = LoggerFactory.getLogger(JpabookApplication.class);
//		logger.info("hello");

//		Hello hello = new Hello();
//
//		hello.setData("wjk");
//		String data = hello.getData();
//		System.out.println("data = " + data);

		SpringApplication.run(JpabookApplication.class, args);
	}

}
