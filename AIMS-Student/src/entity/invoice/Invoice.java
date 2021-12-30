package entity.invoice;

import java.sql.SQLException;
import java.sql.Statement;

import entity.db.AIMSDB;
import entity.order.Order;

public class Invoice {

    private Order order;
    private int amount;
    
    public Invoice(){

    }

    public Invoice(Order order){
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * Phuong thuc dung de luu hoa don vao trong database
     */
    public void saveInvoice() {
    	String sql = "INSERT INTO Invoice(totalAmount, orderID) values(" + amount + ", " + order.getId() + ");";
        try {
            Statement stm = AIMSDB.getConnection().createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
