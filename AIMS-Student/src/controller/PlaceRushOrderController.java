package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calculator.CasualOrderShippingFeeCalculator;
import calculator.RushOrderShippingFeeCalculator;
import common.exception.InvalidDeliveryInfoException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import utils.Configs;
import utils.Utils;

/**
 * Class controller phuc vu cho use case Place Rush Order
 * Date 11/12/2021
 * @author levietdung
 *
 */
public class PlaceRushOrderController extends PlaceOrderController {
	 /**
     * Thuoc tinh giup log ra thong tin ra console
     */
    private static Logger LOGGER = Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * Phuong thuc kiem tra trang thai ton kho cua san pham khi nguoi dung tao Rush Order
     * @throws SQLException
     */
    public void placeRushOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * Phuong thuc tao ra doi tuong Order moi dua vao Cart
     * @return Order
     * @throws SQLException
     */
    public Order createRushOrder() throws SQLException{
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
     * Phuong thuc tao ra doi tuon Invoice moi dua vao Order
     * @param order: Don dat hang cua khach hang
     * @return Invoice: Hoa don cua don hang
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * Phuong thuc xu ly thong tin giao hang nhap vao tu khach hangs
     * @param info: Thong tin giao hang khach hang nhap vao
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
     * Phuong thuc xac nhan thong tin giao hang
     * @param info: Thong tin giao hang khach hang nhap vaos
     * @throws InterruptedException
     * @throws IOException
     */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
    	String currentTime = Utils.getToday();
    	currentTime = currentTime.substring(0, currentTime.lastIndexOf(" "));
    	if (!validateName(info.get("name"))) {
    		throw new InvalidDeliveryInfoException("Invalid Name");
    	}
    	if (!validatePhoneNumber(info.get("phone"))) {
    		throw new InvalidDeliveryInfoException("Invalid Phone Number");
    	}
    	if (!validateProvince(info.get("province"))) {
    		throw new InvalidDeliveryInfoException("Invalid Province");
    	}
    	if (!validateAddress(info.get("address"))) {
    		throw new InvalidDeliveryInfoException("Invalid Address");
    	}
    	if (!validateDeliveryTime(currentTime, info.get("deliveryTime"))) {
    		throw new InvalidDeliveryInfoException("Invalid Delivery Time");
    	}
    }
    
    /**
     * Phuong thuc xac nhan thong tin ve so dien thoai
     * @param phoneNumber: Thong tin ve so dien thoai
     * @return: Thong tin ve so dien thoai co duoc xac nhan hay khong
     */
    public boolean validatePhoneNumber(String phoneNumber) {
    	
    	//So dien thoai khong duoc rong
    	if (phoneNumber == null) return false;
    	
    	//So dien thoai phai co do dai la 10 ky tu
    	if (phoneNumber.length() != 10) return false;
    	
    	//So dien thoai phai bat dau bang so 0
    	if (!phoneNumber.startsWith("0")) return false;
    	
    	//So dien thoai chi chua chu so, khong chua chu cai va cac ky tu dac biet 
    	try {
    		Integer.parseInt(phoneNumber);
    	} catch (NumberFormatException e) {
    		return false;
    	}
		return true;
    }
    
    /**
     * Phuong thuc xac nhan thong tin ve ten khach hang
     * @param name: Thong tin ve ten khach hang
     * @return: Thong tin ve ten khach hang co duoc xac nhan hay khong
     */
    public boolean validateName(String name) {
        
    	//Ten khong duoc rong
    	if (name == null) return false;
    	
    	//Ten chi chua cac chu cai va dau cach
    	if (Utils.letterCheck(name, "[^A-Za-z ]")) return false;
    	
    	return true;
    }
    
    /**
     * Phuong thuc xac nhan thong tin ve dia chi
     * @param address: Thong tin ve dia chi
     * @return: Thong tin ve dia chi co duoc xac nhan hay khong
     */
    public boolean validateAddress(String address) {
        
    	//Dia chi khong duoc rong
    	if (address == null) return false;
    	
    	//Dia chi khong duoc chua cac ky tu dac biet tru dau cach va dau "/"
    	if (Utils.letterCheck(address, "[^A-Za-z0-9 /]")) return false;
    	
    	return true;
    }
    
    /**
     * Phuong thuc xac nhan thong tin ve thoi gian giao hang
     * @param expectedTime: Thong tin ve thoi gian giao hang
     * @param currentTime: Thoi diem hien tai
     * @return: Thong tin ve thoi gian giao hang co duoc xac nhan hay khong
     */
    public boolean validateDeliveryTime(String currentTime, String expectedTime) {
    	
    	//Thoi gian giao hang khong duoc rong
        if (expectedTime == null) return false;
        //Chi chua cac so nguyen va dau "-", khong chua chu cai va cac ky tu dac biet khac
        String[] dateParts = expectedTime.split("-");
        //Phai co dinh dang "YYYY-MM-DD"
        if (dateParts.length != 3) return false;
        int year = 0, month = 0, date = 0;
        try {
	        year = Integer.parseInt(dateParts[0]);
	        month = Integer.parseInt(dateParts[1]);
	        date = Integer.parseInt(dateParts[2]);
        } catch (NumberFormatException e) {
        	return false;
        }
        //Ngay chi co the la so nguyen tu 1 den 31
        if (date < 1 || date > 31) return false;
        //Thang chi co the la so nguyen tu 1 den 12
        if (month < 1 || month > 12) return false;
        //Thang 2 chi co nhieu nhat 29 ngay (nam nhuan)
        if (month == 2 && date > 29) return false;
        //Thang 4, 6, 9, 11 chi co 30 ngay
        if ((month == 4 || month == 6 || month == 9 || month == 11) && date > 30) return false;
        //Thang 2 cua cac nam khong nhuan chi co 28 ngay
        if (!(year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) && month == 2 && date > 28) return false;
        //Ngay nhan hang phai sau ngay dat hang
        String[] currentDateParts = currentTime.split("-");
        int currentYear = Integer.parseInt(currentDateParts[0]);
        int currentMonth = Integer.parseInt(currentDateParts[1]);
        int currentDate = Integer.parseInt(currentDateParts[2]);
        if (currentYear > year) return false;
        else if (currentYear == year) {
        	if (currentMonth > month) return false;
        	else if (currentMonth == month) {
        		if (currentDate > date) return false;
        	}
        }
        return true;
    }
    
    /**
     * Phuong thuc xac nhan thong tin ve khu vuc tinh thanh
     * @param phoneNumber: Thong tin ve khu vuc tinh thanh
     * @return: Thong tin ve khu vuc tinh thanh co duoc xac nhan hay khong
     */
    public boolean validateProvince(String province) {
    	if (province == null) return false;
    	if (!province.equals("Hà Nội")) {
    		return false;
    	}
    	return true;
    }
    
    /**
     * Phuong thuc tinh phi giao hang cho don hang nhanh
     * @param amount Tong chi phi hoa don
     * @return Phi giao hang
     */
    public int updateShippingFee(int amount) {
		RushOrderShippingFeeCalculator cal = new RushOrderShippingFeeCalculator();
		return cal.calculateShippingFee(amount);
	}
}
