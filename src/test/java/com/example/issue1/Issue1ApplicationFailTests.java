package com.example.issue1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({ "foo", "baz_fail" })
public class Issue1ApplicationFailTests {

	@Test
	public void contextLoads() {
	}

}
