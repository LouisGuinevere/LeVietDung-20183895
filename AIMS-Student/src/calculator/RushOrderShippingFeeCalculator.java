package calculator;

import java.util.Random;
import java.util.logging.Logger;

import controller.PlaceOrderController;

/**
 * Lop dung de tinh phi giao hang cho don giao hang nhanh
 * @author levietdung
 *
 */
public class RushOrderShippingFeeCalculator implements ShippingFeeCalculator{
	
    protected static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * Phuong thuc cap nhat phi giao hang cho hoa don giao hang nhanh
     * @param order: Don dat hang nhanh cua khach hang
     * @return shippingFee
     */
    public int calculateShippingFee(int amount){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * amount);
        LOGGER.info("Order Amount: " + amount + " -- Shipping Fees: " + fees);
        return fees;
    }
}
