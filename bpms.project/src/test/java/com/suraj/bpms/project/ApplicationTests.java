package com.suraj.bpms.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestMailConfiguration.class)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
