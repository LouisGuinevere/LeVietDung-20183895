package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidateAddressTest {

	private PlaceOrderController placeOrderController;

	@BeforeEach
	void setUp() throws Exception {
		placeOrderController = new PlaceOrderController();
	}
	
	@ParameterizedTest
	@CsvSource({
		"So 1 Dai Co Viet, true",
		", false",
		"So 1 @ Dai Co Viet, false"
	})
	
	public void test(String address, boolean expected) {
		//when
		boolean isValid = placeOrderController.validateAddress(address);
		//then
		assertEquals(expected, isValid);
	}
}
