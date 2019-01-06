package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;
import java.util.Date;
//import java.time.LocalDate;

public class ShowPrice {

	private final double price;
	private final Date date;
	private final String productName;
	private final long productId;
	private final String productTags;
	private final long shopId;
	private final String shopName;
	private final String shopTags;
	private final String shopAddress;
	private final double shopDist;
	
	public ShowPrice(double price, Date date, String productName, long productId, String productTags, long shopId, String shopName, String shopTags, String shopAddress, double shopDist) {
		this.price          = price;
		this.date   = date;
		this.productName = productName;
		this.productId   = productId;
		this.productTags = productTags;
		this.shopId      = shopId;
		this.shopName = shopName;
		this.shopTags = shopTags;
		this.shopAddress = shopAddress;
		this.shopDist = shopDist;
	}

	public double getprice() {
		return price;
	}

	public Date getDate() {
		return date;
	}

	public String getproductName() {
		return productName;
	}
	
	public long getProductId() {
		return productId;
	}

	public String getproductTags() {
		return productTags;
	}
	
	public long getShopId() {
		return shopId;
	}
	
	public String getshopName() {
		return shopName;
	}

	public String getshopTags() {
		return shopTags;
	}
	
	public String getshopAddress() {
		return shopAddress;
	}
	
	public double getshopDist() {
		return shopDist;
	}


}
