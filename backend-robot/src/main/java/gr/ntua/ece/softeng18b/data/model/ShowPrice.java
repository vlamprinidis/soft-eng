package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;
import java.util.Date;
import java.util.List;
//import java.time.LocalDate;

public class ShowPrice {

	private final double price;
	private final String date;
	private final String productName;
	private final long productId;
	private final List<String> productTags;
	private final long shopId;
	private final String shopName;
	private final List<String> shopTags;
	private final String shopAddress;
	private final double shopDist;
	
	public ShowPrice(double price, String date, String productName, long productId, List<String> productTags, long shopId, String shopName, List<String> shopTags, String shopAddress, double shopDist) {
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

	public String getDate() {
		return date;
	}

	public String getproductName() {
		return productName;
	}
	
	public long getProductId() {
		return productId;
	}

	public List<String> getproductTags() {
		return productTags;
	}
	
	public long getShopId() {
		return shopId;
	}
	
	public String getshopName() {
		return shopName;
	}

	public List<String> getshopTags() {
		return shopTags;
	}
	
	public String getshopAddress() {
		return shopAddress;
	}
	
	public double getshopDist() {
		return shopDist;
	}


}
