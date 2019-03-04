package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;
import java.util.Date;

public class Price {

	private final double price;
	private final Date mydate;
	private final long productId;
	private final long shopId;

	public Price(double price, Date mydate, long productId, long shopId) {
		this.price       = price;
		this.mydate      = mydate;
		this.productId   = productId;
		this.shopId      = shopId;
	}

	public double getPrice(){
		return price;
	}

	public Date getMyDate() {
		return mydate;
	}

	public long getProductId() {
		return productId;
	}

	public long getShopId() {
		return shopId;
	}

}
