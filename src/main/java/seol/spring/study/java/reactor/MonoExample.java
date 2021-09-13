package seol.spring.study.java.reactor;

import reactor.core.publisher.Mono;

public class MonoExample {

	public static void main(String[] args) {
		명령형();
		리액티브();
	}

	private static void 리액티브() {
		// 데이터가 전달되는 파이프라인을 구성한다.
		Mono.just("Craig")
				.map(n -> n.toUpperCase())
				.map(cn -> "Hello, " + cn + "!")
				.subscribe(System.out::println);
	}

	private static void 명령형() {
		String name = "Craig";
		String capitalName = name.toUpperCase();
		String greeting = "Hello, " + capitalName + "!";
		System.out.println(greeting);
	}
}
