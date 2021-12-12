package controller.placeorder;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import controller.PlaceOrderController;
import controller.PlaceRushOrderController;

class ValidateDeliveryTimeTest {

	private PlaceRushOrderController placeRushOrderController;
	
	@BeforeEach
	void setUp() throws Exception {
		placeRushOrderController = new PlaceRushOrderController();
	}
	
	@ParameterizedTest
	@CsvSource({
		"2021-1-5, true",
		"2400-2-29, true",
		"2024-2-29, true",
		"2021-1-2, false",
		"2021-11-31, false",
		"2021-2-30, false",
		"2021-3-40, false",
		"2021-40-3, false",
		"2021a-12-10, false",
		"2021*-12-10, false",
		"2401-2-29, false",
		"2021-1-5-2, false",
		"2021-1, false",
		", false"
	})

	public void test(String time, boolean expected) {
		//when
		boolean isValid = placeRushOrderController.validateDeliveryTime("2021-1-3", time);
		//then
		assertEquals(expected, isValid);
	}
}
