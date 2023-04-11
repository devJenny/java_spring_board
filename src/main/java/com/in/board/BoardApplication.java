package com.in.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 실행
// @SpringBootApplication은 기본적으로 메인 클래스에 적용, 스프링 부트 어플리케이션의 핵심 구성 요소들을 자동으로 설정하고 스프링 빈을 자동으로 스캔하고 등록함
@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
