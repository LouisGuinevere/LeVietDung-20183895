package entity.order;

import java.sql.SQLException;
import java.sql.Statement;

import entity.db.AIMSDB;
import entity.media.Media;

public class OrderMedia {
    
    private Media media;
    private int price;
    private int quantity;

    public OrderMedia(Media media, int quantity, int price) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "{" +
            "  media='" + media + "'" +
            ", quantity='" + quantity + "'" +
            ", price='" + price + "'" +
            "}";
    }
    
    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Phuong thuc dung de luu thong tin ve cac san pham co trong don hang
     * @param id id cua don hang
     */
    public void saveOrderMedia(int id) {
    	String sql = "INSERT INTO OrderMedia values (" + getMedia().getId() + ", " + id + ", " 
				+ getPrice() + ", " + getQuantity() + ");";
		try {
		    Statement stm = AIMSDB.getConnection().createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
