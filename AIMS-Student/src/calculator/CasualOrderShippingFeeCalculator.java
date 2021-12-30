package calculator;

import java.util.Random;
import java.util.logging.Logger;

import controller.PlaceOrderController;

/**
 * Lop dung de tinh phi giao hang cho don hang binh thuong
 * @author levietdung
 *
 */
public class CasualOrderShippingFeeCalculator implements ShippingFeeCalculator {
	
    protected static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method calculates the shipping fees of the order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(int amount){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * amount);
        LOGGER.info("Order Amount: " + amount + " -- Shipping Fees: " + fees);
        return fees;
    }
}
