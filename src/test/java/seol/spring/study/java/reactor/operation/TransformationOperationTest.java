package seol.spring.study.java.reactor.operation;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@DisplayName("변환/필터링")
public class TransformationOperationTest {

	@Test
	@DisplayName("앞부터 원하는 개수 무시")
	void skipAFew() {
		// given when
		Flux<String> skipFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
				.skip(3);

		// then
		StepVerifier.create(skipFlux)
				.expectNext("ninety nine", "one hundred")
				.verifyComplete();
	}

	@Test
	@DisplayName("앞부터 시간 경과 후 방출")
	void skipAFewSeconds() {
		// given when
		Flux<String> skipFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
				.delayElements(Duration.ofSeconds(1))
				.skip(Duration.ofSeconds(4));

		// then
		StepVerifier.create(skipFlux)
				.expectNext("ninety nine", "one hundred")
				.verifyComplete();
	}

	@Test
	@DisplayName("앞부터 원하는 항목만 방출(skip 반대)")
	void take() {
		// given when
		Flux<String> takeFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
				.take(2);

		// then
		StepVerifier.create(takeFlux)
				.expectNext("one", "two")
				.verifyComplete();
	}

	@Test
	@DisplayName("앞부터 원하는 항목만 방출(skip 반대)")
	void takeSeconds() {
		// given when
		Flux<String> takeFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
				.delayElements(Duration.ofSeconds(1))
				.take(Duration.ofMillis(3500));

		// then
		StepVerifier.create(takeFlux)
				.expectNext("one", "two", "skip a few")
				.verifyComplete();
	}

	@Test
	@DisplayName("데이터 필터")
	void filter() {
		// given when
		Flux<String> filterFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
				.filter(n -> !n.contains(" "));

		// then
		StepVerifier.create(filterFlux)
				.expectNext("one", "two")
				.verifyComplete();
	}

	@Test
	@DisplayName("중복 제거")
	void distince() {
		// given when
		Flux<String> distinctFlux = Flux.just("dog", "cat", "bird", "dog", "bird", "anteater")
				.distinct();

		// then
		StepVerifier.create(distinctFlux)
				.expectNext("dog", "cat", "bird", "anteater")
				.verifyComplete();
	}

	@Test
	@DisplayName("데이터 매핑")
	void map() {
		// given when
		Flux<Player> mapFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
				.map(n -> {
					String[] split = n.split("\\s");
					return new Player(split[0], split[1]);
				});

		// then
		StepVerifier.create(mapFlux)
				.expectNext(new Player("Michael", "Jordan"))
				.expectNext(new Player("Scottie", "Pippen"))
				.expectNext(new Player("Steve", "Kerr"))
				.verifyComplete();
	}

	@Test
	@DisplayName("데이터 매핑")
	void flatMap() {
		// given when
		Flux<Player> mapFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
				.flatMap(n ->
						Mono.just(n)
								.map(p -> {
									String[] split = n.split("\\s");
									return new Player(split[0], split[1]);
								})
								.subscribeOn(Schedulers.parallel()) // 각 구독이 병렬 스레드로 수행되어야 한다는것을 나타냄. subscribeOn 없을경우 순차 생성.
				);

		List<Player> playerList = List.of(
				new Player("Michael", "Jordan")
				, new Player("Scottie", "Pippen")
				, new Player("Steve", "Kerr")
		);

		// then
		StepVerifier.create(mapFlux)
				.expectNextMatches(p -> playerList.contains(p))
				.expectNextMatches(p -> playerList.contains(p))
				.expectNextMatches(p -> playerList.contains(p))
				.verifyComplete();
	}

	@EqualsAndHashCode
	@AllArgsConstructor
	static class Player {

		private String firstName;
		private String lastName;
	}

	@Test
	@DisplayName("데이터 버퍼링")
	void buffer() {
		// given
		Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

		// when
		Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

		// then
		StepVerifier
				.create(bufferedFlux)
				.expectNext(Arrays.asList("apple", "orange", "banana"))
				.expectNext(Arrays.asList("kiwi", "strawberry"))
				.verifyComplete();
	}

	@Test
	@DisplayName("데이터 버퍼 병렬처리")
	void bufferFlatMap() {
		Flux.just("apple", "orange", "banana", "kiwi", "strawberry")
				.buffer(3)
				.flatMap(x ->
						Flux.fromIterable(x)
								.map(y -> y.toUpperCase())
								.subscribeOn(Schedulers.parallel())
								.log()
				).subscribe();
	}

	@Test
	@DisplayName("List로 모으기(buffer)")
	void collectListBuffer() {
		// given
		Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

		// when
		Flux<List<String>> fruitListFlux = fruitFlux.buffer();

		// then
		StepVerifier
				.create(fruitListFlux)
				.expectNext(Arrays.asList("apple", "orange", "banana", "kiwi", "strawberry"))
				.verifyComplete();
	}

	@Test
	@DisplayName("List로 모으기(collectList)")
	void collectList() {
		// given
		Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

		// when
		Mono<List<String>> fruitListMono = fruitFlux.collectList();

		// then
		StepVerifier
				.create(fruitListMono)
				.expectNext(Arrays.asList("apple", "orange", "banana", "kiwi", "strawberry"))
				.verifyComplete();
	}

	@Test
	@DisplayName("Map으로 모으기(collectMap)")
	void collectMap() {
		// given
		Flux<String> fruitFlux = Flux.just("apple", "orange", "banana");

		// when
		Mono<Map<Character, String>> fruitMapMono = fruitFlux.collectMap(f -> f.charAt(0));

		// then
		StepVerifier
				.create(fruitMapMono)
				.expectNextMatches(map ->
						map.size() == 3
								&& map.get('a').equals("apple")
								&& map.get('o').equals("orange")
								&& map.get('b').equals("banana")
				)
				.verifyComplete();
	}

}
