package controller.placeorder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ValidateAddressTest.class, ValidateDeliveryTimeTest.class, ValidateNameTest.class, 
	ValidatePhoneNumberTest.class, ValidateProvinceTest.class})
public class AllTests {

}