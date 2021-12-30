package entity.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.db.AIMSDB;
import entity.media.Media;
import utils.Configs;

public class Order {
    
	private int id;
    private int shippingFees;
    private List lstOrderMedia;
    private HashMap<String, String> deliveryInfo;
    private String type;

    public Order(){
        this.lstOrderMedia = new ArrayList<>();
    }

    public Order(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }
    
    public int getId() {
    	return this.id;
    }

    public void addOrderMedia(OrderMedia om){
        this.lstOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om){
        this.lstOrderMedia.remove(om);
    }

    public List getlstOrderMedia() {
        return this.lstOrderMedia;
    }

    public void setlstOrderMedia(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public HashMap getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(HashMap deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }
    
    public String getType() {
    	return type;
    }
    
    public void setType(String type) {
    	this.type = type;
    }

    public int getAmount(){
        double amount = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }
    
    /**
     * Phuong thuc dung de luu dong hang vao database
     */
    public void saveOrder() {
    	String sql = "INSERT INTO \"Order\"(email, address, phone, userID, shipping_fee, type)"
    			+ " values(" + "\"abc@gmail.com\"" + ", \"" + deliveryInfo.get("address") + "\", \""
    			+ deliveryInfo.get("phone") + "\", " + 1 + ", " + shippingFees + ", \"" + type + "\");";
    	String sql2 = "SELECT LAST_INSERT_ROWID();";
        try {
            Statement stm = AIMSDB.getConnection().createStatement();
			stm.executeUpdate(sql);
			ResultSet rs = stm.executeQuery(sql2);
			while(rs.next()) {
				this.id = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for (Object object: lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            om.saveOrderMedia(id);
        }
    }

    /**
     * Phuong thuc dung de cap nhat so luong hang ton kho cua cac san pham co trong hoa don
     * @throws SQLException
     */
	public void updateQuantity() throws SQLException {
		for (Object object: lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            om.getMedia().updateQuanity(om.getQuantity());
		}
	}
}
