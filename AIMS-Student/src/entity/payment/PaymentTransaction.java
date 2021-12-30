package entity.payment;

import java.sql.SQLException;
import java.sql.Statement;

import entity.db.AIMSDB;
import entity.paymentmethod.PaymentMethod;

public class PaymentTransaction {
	private String errorCode;
	private PaymentMethod card;
	private String transactionId;
	private String transactionContent;
	private int amount;
	private String createdAt;
	
	public PaymentTransaction(String errorCode, PaymentMethod card, String transactionId, String transactionContent,
			int amount, String createdAt) {
		super();
		this.errorCode = errorCode;
		this.card = card;
		this.transactionId = transactionId;
		this.transactionContent = transactionContent;
		this.amount = amount;
		this.createdAt = createdAt;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Phuong thuc dung de luu thong tin giao dich vao database
	 * @param orderID id cua don hang
	 */
	public void saveTransaction(int orderID) {
		String sql = "INSERT INTO \"Transaction\"(orderID, createAt, content) values(" + orderID
				+ ", \"" + createdAt + "\", \"" + transactionContent + "\");";
        try {
            Statement stm = AIMSDB.getConnection().createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
