package controller.placeorder;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import controller.PlaceRushOrderController;

class ValidateAddressTest {
	
	PlaceRushOrderController placeRushOrderController;

	@BeforeEach
	void setUp() throws Exception {
		placeRushOrderController = new PlaceRushOrderController();
	}
	
	@ParameterizedTest
	@CsvSource({
		"Ngach 5/12 ngo 5 Le Duc Tho My Dinh Nam Tu Liem, true",
		"Ngach 5@12 ngo 5 Le Duc Tho, false",
		", false"
	})
	
	public void test(String address, boolean expected) {
		//when
		boolean isValid = placeRushOrderController.validateAddress(address);
		//then
		assertEquals(expected, isValid);
	}

}
