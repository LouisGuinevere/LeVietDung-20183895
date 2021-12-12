package controller.placeorder;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import controller.PlaceRushOrderController;

class ValidateNameTest {
	
	private PlaceRushOrderController placeRushOrderController;

	@BeforeEach
	void setUp() throws Exception {
		placeRushOrderController = new PlaceRushOrderController();
	}
	
	@ParameterizedTest
	@CsvSource({
		"Le Viet Dung, true",
		"Le V1et Dung, false",
		"Le V*et Dung, false",
		", false",
	})

	public void test(String name, boolean expected) {
		//when
		boolean isValid = placeRushOrderController.validateName(name);
		//then
		assertEquals(expected, isValid);
	}

}
