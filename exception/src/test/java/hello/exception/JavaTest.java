package hello.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JavaTest {

	@Test
	void assertSame() {
		String str1 = String.valueOf("야호");
		String str2 = String.valueOf("야호");
		System.out.println(str1);
		System.out.println(str2);
		System.out.println(str1 == str2);
		Integer i1 = 1;
		Integer i2 = 1;
		Integer i3 = 128;
		Integer i4 = 128;
		Assertions.assertSame(str1, str2);
		Assertions.assertSame(i1, i2);
		Assertions.assertSame(i3, i4);
	}

}
