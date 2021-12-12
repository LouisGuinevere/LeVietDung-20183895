package common.exception;

import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import utils.Utils;
import views.screen.popup.PopupScreen;

;

/**
 * The InvalidDeliveryInfoException wraps all unchecked exceptions You can use this
 * exception to inform
 * 
 * @author nguyenlm
 */
public class InvalidDeliveryInfoException extends AimsException {

	private static final long serialVersionUID = 1091337136123906298L;

	public InvalidDeliveryInfoException() {

	}

	public InvalidDeliveryInfoException(String message) throws IOException {
		super(message);
		PopupScreen.error((message));
	}
}