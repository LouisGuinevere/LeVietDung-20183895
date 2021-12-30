package views.screen.placerushorder;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import calculator.RushOrderShippingFeeCalculator;
import controller.PlaceRushOrderController;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.placerushorder.PlaceRushOrderScreenHandler;
import views.screen.shipping.ShippingScreenHandler;

/**
 * Class handler phuc vu cho use case Place Rush Order
 * Date 11/12/2021
 * @author levietdung
 *
 */
public class PlaceRushOrderScreenHandler extends ShippingScreenHandler implements Initializable {
	
	/**
	 * Thuoc tinh dai dien cho DatePicker trong file fxml
	 */
	@FXML
	private DatePicker deliveryTime;

	/**
	 * @param stage: 
	 * @param screenPath: Duong dan toi file fxml
	 * @param messages: Thong tin giao hang
	 * @param order: Don hang khach hang muon dat
	 * @throws IOException
	 */
	public PlaceRushOrderScreenHandler(Stage stage, String screenPath, HashMap<String, String> messages, Order order) throws IOException {
		super(stage, screenPath, order);
		this.name.setText(messages.get("name").toString());
		this.phone.setText(messages.get("phone").toString());
		this.address.setText(messages.get("address").toString());
		this.province.setValue(messages.get("province").toString());
		if (messages.get("instructions") != null){
			this.instructions.setText(messages.get("instructions").toString());
		}
	}

	/**
	 * Phuong thuc dung de luu thong tin giao hang
	 * @param: messages: Doi tuong de luu thong tin giao hang
	 */
	@Override
	protected void addInfo(HashMap messages) {
		super.addInfo(messages);
		if (deliveryTime.getValue() == null) {
			messages.put("deliveryTime", null);
		} else {
			messages.put("deliveryTime", deliveryTime.getValue().toString());
		}
	}

	/**
	 * Phuong thuc dung de gui thong tin giao hang khi khach hang submit thong tin giao hang
	 * @param event: Su kien khach hang submit thong tin giao hang
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@FXML
	void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

		// add info to messages
		HashMap messages = new HashMap<>();
		addInfo(messages);
		
		try {
			// process and validate delivery info
			getBController().processDeliveryInfo(messages);
		} catch (InvalidDeliveryInfoException e) {
			return;
		}
	
		// calculate shipping fees
		int shippingFees = getBController().updateShippingFee(order.getAmount());
		order.setShippingFees(shippingFees);
		order.setDeliveryInfo(messages);
		order.setType("Rush Order");
		
		// create invoice screen
		Invoice invoice = getBController().createInvoice(order);
		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		InvoiceScreenHandler.setPreviousScreen(this);
		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
		InvoiceScreenHandler.setBController(getBController());
		InvoiceScreenHandler.show();
	}
	
	/**
	 * Phuong thuc dung de cap nhat phi giao hang cho don giao hang nhanh
	 * @param amount Tong gia tri hoa don
	 * @return Phi giao hang
	 */
	public int updateShippingFee(int amount) {
		RushOrderShippingFeeCalculator cal = new RushOrderShippingFeeCalculator();
		return cal.calculateShippingFee(amount);
	}

	/**
	 * Phuong thuc tra ve controller cua handler
	 */
	public PlaceRushOrderController getBController(){
		return (PlaceRushOrderController) super.getBController();
	}
}
