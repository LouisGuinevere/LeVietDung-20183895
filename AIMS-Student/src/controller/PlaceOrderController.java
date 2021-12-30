package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calculator.CasualOrderShippingFeeCalculator;
import calculator.RushOrderShippingFeeCalculator;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    protected static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
            order.addOrderMedia(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
   * The method validates the info
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
    	if (!validateName(info.get("name"))) {
    		throw new InvalidDeliveryInfoException("Invalid Name");
    	}
    	if (!validatePhoneNumber(info.get("phone"))) {
    		throw new InvalidDeliveryInfoException("Invalid Phone Number");
    	}
    	if (!validateAddress(info.get("address"))) {
    		throw new InvalidDeliveryInfoException("Invalid Address");
    	}
    	if (!validateProvince(info.get("province"))) {
    		throw new InvalidDeliveryInfoException("Invalid Province");
    	}
    }
    
    /**
     * Phuong thuc xac nhan thong tin ve so dien thoai cua khach hang
     * @param phoneNumber So dien thoai cua khach hang
     * @return So dien thoai cua khach hang co hop le hay khong
     */
    public boolean validatePhoneNumber(String phoneNumber) {
    	if (phoneNumber == null) return false;
    	if (phoneNumber.length() != 10) return false;
    	if (!phoneNumber.startsWith("0")) return false;
    	try {
    		Integer.parseInt(phoneNumber);
    	} catch (NumberFormatException e) {
    		return false;
    	}
		return true;
    }
    
    /**
     * Phuong thuc xac nhan thong tin ve ten khach hang
     * @param name Ten khach hang
     * @return Ten khach hang co hop le hay khong
     */
    public boolean validateName(String name) {
    	if (name == null) return false;
    	if (Utils.letterCheck(name, "[^A-Za-z ]")) return false;
    	return true;
    }
    
    /**
     * Phuong thuc xac nhan dia chi giao hang
     * @param address Dia chi giao hang
     * @return Dia chi giao hang co hop le hay khong
     */
    public boolean validateAddress(String address) {
    	if (address == null) return false;
    	if (Utils.letterCheck(address, "[^A-Za-z0-9 ]")) return false;
    	return true;
    }
    
    /**
     * Phuong thuc xac nhan khu vuc tinh thanh
     * @param province Khu vuc tinh thanh
     * @return Khu vuc tinh thanh co hop le hay khong
     */
    public boolean validateProvince(String province) {
    	if (province == null) return false;
    	return true;
    }
    
    /**
     * Phuong thuc tinh phi giao hang
     * @param amount Tong gia tri hoa don
     * @return phi giao hang
     */
    public int calculateShippingFee(int amount) {
		CasualOrderShippingFeeCalculator cal = new CasualOrderShippingFeeCalculator();
		return cal.calculateShippingFee(amount);
	}
}
