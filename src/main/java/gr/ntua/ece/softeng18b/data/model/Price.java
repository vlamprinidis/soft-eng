package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;

public class Price {

	private final long id;
	private final Date date_from;
	private final Date date_to;
	private final long productId;
	private final long shopId;

	public Price(long id, Date date_from, Date date_to, long productId, long shopId) {
		this.id          = id;
		this.date_from   = date_from;
		this.date_to     = date_to;
		this.productId   = productId;
		this.shopId      = shopId;
	}

	public long getId() {
		return id;
	}

	public String getDate_from() {
		return date_from;
	}

	public String getDate_to() {
		return date_to;
	}

	public long getProductId() {
		return productId;
	}

	public long getShopId() {
		return shopId;
	}

	@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Price price = (Price) o;
			return id == price.id;
		}

	@Override
		public int hashCode() {
			return Objects.hash(id);
		}
}
