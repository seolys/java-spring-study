package seol.spring.study.java.reactor.operation;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("Flux 생성하기")
class CreationOperationTest {

	@Test
	@DisplayName("객체로부터 생성하기")
	void just() {
		// given
		Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");

		// when
		fruitFlux.subscribe(f -> System.out.println("Here's some fruit: " + f));

		// then
		fruitFluxVerify(fruitFlux);
	}

	@Nested
	class 컬렉션으로부터_생성하기 {

		@Test
		@DisplayName("배열로부터 생성하기")
		void fromArray() {
			// given
			String[] fruits = new String[]{"Apple", "Orange", "Grape", "Banana", "Strawberry"};
			Flux<String> fruitFlux = Flux.fromArray(fruits);

			// when
			fruitFlux.subscribe(f -> System.out.println("Here's some fruit: " + f));

			// then
			fruitFluxVerify(fruitFlux);
		}

		@Test
		@DisplayName("Iterable로부터 생성하기")
		void fromIterable() {
			// given
			List<String> fruits = List.of("Apple", "Orange", "Grape", "Banana", "Strawberry");
			Flux<String> fruitFlux = Flux.fromIterable(fruits);

			// when
			fruitFlux.subscribe(f -> System.out.println("Here's some fruit: " + f));

			// then
			fruitFluxVerify(fruitFlux);
		}

		@Test
		@DisplayName("Stream으로부터 생성하기")
		void fromStream() {
			// given
			Stream<String> fruits = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");
			Flux<String> fruitFlux = Flux.fromStream(fruits);

			// when
//		fruitFlux.subscribe(f -> System.out.println("Here's some fruit: " + f));

			// then
			fruitFluxVerify(fruitFlux);
		}
	}

	private void fruitFluxVerify(final Flux<String> fruitFlux) {
		StepVerifier.create(fruitFlux)
				.expectNext("Apple")
				.expectNext("Orange")
				.expectNext("Grape")
				.expectNext("Banana")
				.expectNext("Strawberry")
				.verifyComplete();
	}

	@Nested
	class Flux데이터생성하기 {

		@Test
		@DisplayName("일정 범위 값을 포함하는 Flux 생성")
		void range() {
			Flux<Integer> intervalFlux = Flux.range(1, 5);

			StepVerifier.create(intervalFlux)
					.expectNext(1)
					.expectNext(2)
					.expectNext(3)
					.expectNext(4)
					.expectNext(5)
					.verifyComplete();
		}

		@Test
		@DisplayName("매초마다 값을 방출")
		void interval() {
			Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1)).take(5);

			StepVerifier.create(intervalFlux)
					.expectNext(0L)
					.expectNext(1L)
					.expectNext(2L)
					.expectNext(3L)
					.expectNext(4L)
					.verifyComplete();
		}
	}

}
