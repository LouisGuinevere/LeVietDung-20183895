package entity.paymentmethod;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import entity.db.AIMSDB;

public class CreditCard extends PaymentMethod{
	private String cardCode;
	private String owner;
	private int cvvCode;
	private String dateExpired;

	public CreditCard(String cardCode, String owner, int cvvCode, String dateExpired) {
		super();
		this.cardCode = cardCode;
		this.owner = owner;
		this.cvvCode = cvvCode;
		this.dateExpired = dateExpired;
	}
	
	/**
	 * Phuong thuc dung de luu thong tin cua the tin dung vao trong database
	 */
	public void saveCard() {
		String sql = "INSERT INTO Card(cardNumber, holderName, expirationDate, securityCode, userID) "
				+ "values(\"" + cardCode + "\", \"" + owner + "\", \""
				+ dateExpired + "\", \"" + cvvCode + "\", " + "1" + ");";
        try {
            Statement stm = AIMSDB.getConnection().createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (e.getMessage().contains("Abort due to constraint violation")) {
				return;
			}
			e.printStackTrace();
		}
	}
}
