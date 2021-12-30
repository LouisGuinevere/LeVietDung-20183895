package views.screen.shipping;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import controller.BaseController;
import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.placerushorder.PlaceRushOrderScreenHandler;
import views.screen.popup.PopupScreen;

public class ShippingScreenHandler extends BaseScreenHandler implements Initializable {

	@FXML
	protected Label screenTitle;

	@FXML
	protected TextField name;

	@FXML
	protected TextField phone;

	@FXML
	protected TextField address;

	@FXML
	protected TextField instructions;
	
	@FXML
	private ToggleGroup rushOrder;

	@FXML
	protected ComboBox<String> province;

	protected Order order;

	public ShippingScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
		super(stage, screenPath);
		this.order = order;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
		name.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
		this.province.getItems().addAll(Configs.PROVINCES);
	}
	
	protected void addInfo(HashMap messages) {
		if (name.getText().equals("")) messages.put("name", null); 
		else {
			messages.put("name", name.getText());
		}
		if (phone.getText().equals("")) messages.put("phone", null); 
		else {
			messages.put("phone", phone.getText());
		}
		if (address.getText().equals("")) messages.put("address", null); 
		else {
			messages.put("address", address.getText());
		}
		if (instructions.getText().equals("")) messages.put("instructions", null); 
		else {
			messages.put("instructions", instructions.getText());
		}
		if (province.getSelectionModel().isEmpty()) messages.put("province", null);
		else {
			messages.put("province", province.getValue());
		}
	}
	
	protected void moveToNextScreen(BaseScreenHandler handler, String title, BaseController controller) {
		handler.setPreviousScreen(this);
		handler.setHomeScreenHandler(homeScreenHandler);
		handler.setScreenTitle(title);
		handler.setBController(controller);
		handler.show();
	}

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
		if (((RadioButton) rushOrder.getSelectedToggle()).getText().equals("Yes")){
			if (!((messages.get("province").toString()).equals("Hà Nội"))) {
				PopupScreen.error("Your province doesn't support rush order");
			} else {
				PlaceRushOrderController controller = new PlaceRushOrderController();
				BaseScreenHandler PlaceRushOrderScreenHandler = new PlaceRushOrderScreenHandler(this.stage, Configs.PLACE_RUSH_ORDER_SCREEN_PATH, messages, order);
				moveToNextScreen(PlaceRushOrderScreenHandler, "Place Rush Order Screen", controller);
			}
			return;
		}
		// calculate shipping fees
		int shippingFees = getBController().calculateShippingFee(order.getAmount());
		order.setShippingFees(shippingFees);
		order.setDeliveryInfo(messages);
		order.setType("Casual Order");
		
		// create invoice screen
		Invoice invoice = getBController().createInvoice(order);
		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		moveToNextScreen(InvoiceScreenHandler, "Invoice Screen", getBController());
	}

	public PlaceOrderController getBController(){
		return (PlaceOrderController) super.getBController();
	}

	public void notifyError(){
		// TODO: implement later on if we need
	}

}
