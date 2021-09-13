package seol.spring.study.java.reactor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("로직")
public class LogicOperationTest {

	@Test
	@DisplayName("모두 일치하는지 확인")
	void all() {
		// given
		Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

		// when
		Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a"));

		// then
		StepVerifier.create(hasAMono)
				.expectNext(true)
				.verifyComplete();

		// when
		Mono<Boolean> hasKMono = animalFlux.all(a -> a.contains("k"));

		// then
		StepVerifier.create(hasKMono)
				.expectNext(false)
				.verifyComplete();
	}

	@Test
	@DisplayName("일치하는항목이 하나라도 존재하는지 확인")
	void any() {
		// given
		Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

		// when
		Mono<Boolean> hasAMono = animalFlux.any(a -> a.contains("a"));

		// then
		StepVerifier.create(hasAMono)
				.expectNext(true)
				.verifyComplete();

		// when
		Mono<Boolean> hasZMono = animalFlux.all(a -> a.contains("z"));

		// then
		StepVerifier.create(hasZMono)
				.expectNext(false)
				.verifyComplete();
	}


}
