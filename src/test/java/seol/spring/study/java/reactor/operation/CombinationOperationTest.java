package seol.spring.study.java.reactor.operation;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

@DisplayName("Flux 조합하기")
class CombinationOperationTest {

	@Nested
	class 결합 {

		@Test
		@DisplayName("Flux 결합")
		void mergeFluxes() {
			// given
			Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
					.delayElements(Duration.ofMillis(500)); // 500밀리초마다 방출.
			Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
					.delaySubscription(Duration.ofMillis(250)) // 250밀리초가 지난 후에 구독 및 데이터를 방출한다.
					.delayElements(Duration.ofMillis(500));

			// when
			// mergeWith는 Flux의 값들이 완벽하게 번갈아 방출되게 보장할 수 없음.
			// 한 항목씩 번갈아 가져와야 하는경우, zip() 오퍼레이션을 사용해야 한다.
			Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux); // 두 개의 소스 Flux 스트림을 번갈아 구독하게 됨.

			// then
			// 방출되는 항목의 순서는 Flux로부터 방출되는 시간에 맞춰 결정됨.
			StepVerifier.create(mergedFlux)
					.expectNext("Garfield")
					.expectNext("Lasagna")
					.expectNext("Kojak")
					.expectNext("Lollipops")
					.expectNext("Barbossa")
					.expectNext("Apples")
					.verifyComplete();
		}

		@Test
		@DisplayName("zip으로 데이터 조합")
		void zipFluxes() {
			// given
			Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
					.delayElements(Duration.ofMillis(500)); // 500밀리초마다 방출.
			Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
					.delaySubscription(Duration.ofMillis(250)) // 250밀리초가 지난 후에 구독 및 데이터를 방출한다.
					.delayElements(Duration.ofMillis(500));

			// when
			Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, foodFlux); // 두 개의 소스 Flux 스트림을 번갈아 구독하게 됨.

			// then
			StepVerifier.create(zippedFlux)
					.expectNextMatches(p ->
							p.getT1().equals("Garfield") && p.getT2().equals("Lasagna")
					)
					.expectNextMatches(p ->
							p.getT1().equals("Kojak") && p.getT2().equals("Lollipops")
					)
					.expectNextMatches(p ->
							p.getT1().equals("Barbossa") && p.getT2().equals("Apples")
					)
					.verifyComplete();
		}

		@Test
		@DisplayName("combinator로 데이터 가공")
		void zipFluxesToObject() {
			// given
			Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
					.delayElements(Duration.ofMillis(500)); // 500밀리초마다 방출.
			Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
					.delaySubscription(Duration.ofMillis(250)) // 250밀리초가 지난 후에 구독 및 데이터를 방출한다.
					.delayElements(Duration.ofMillis(500));

			// when
			Flux<String> zippedFlux = Flux.zip(characterFlux, foodFlux,
					(c, f) -> c + " eats " + f
			); // 두 개의 소스 Flux 스트림을 번갈아 구독하게 됨.

			// then
			StepVerifier.create(zippedFlux)
					.expectNext("Garfield eats Lasagna")
					.expectNext("Kojak eats Lollipops")
					.expectNext("Barbossa eats Apples")
					.verifyComplete();
		}
	}

	@Test
	@DisplayName("먼저 값을 방출하는 리액티브 타입 선택")
	void firstFlux() {
		// given
		Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
				.delaySubscription(Duration.ofMillis(100));
		Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

		// when
		Flux<String> firstFlux = Flux.firstWithSignal(slowFlux, fastFlux);

		// then
		StepVerifier.create(firstFlux)
				.expectNext("hare")
				.expectNext("cheetah")
				.expectNext("squirrel")
				.verifyComplete();
	}


}
