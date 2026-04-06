package vn.hunghaohan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.hunghaohan.controller.AuthenticationController;
import vn.hunghaohan.controller.EmailController;
import vn.hunghaohan.controller.UserController;

@SpringBootTest
class BackendServiceApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private EmailController emailController;

	@Autowired
	private AuthenticationController authenticationController;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(authenticationController);
		Assertions.assertNotNull(emailController);
		Assertions.assertNotNull(userController);
	}

}
