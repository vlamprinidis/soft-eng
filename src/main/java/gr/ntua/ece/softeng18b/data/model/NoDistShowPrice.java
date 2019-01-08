package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;
import java.util.Date;
//import java.time.LocalDate;
import java.util.List;

public class NoDistShowPrice {

	private final double price;
	private final Date date;
	private final String productName;
	private final long productId;
	private final List<String> productTags;
	private final long shopId;
	private final String shopName;
	private final List<String> shopTags;
	private final String shopAddress;
	
	public NoDistShowPrice(double price, Date date, String productName, long productId, List<String> productTags, long shopId, String shopName, List<String> shopTags, String shopAddress) {
		this.price          = price;
		this.date   = date;
		this.productName = productName;
		this.productId   = productId;
		this.productTags = productTags;
		this.shopId      = shopId;
		this.shopName = shopName;
		this.shopTags = shopTags;
		this.shopAddress = shopAddress;
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
	


}
