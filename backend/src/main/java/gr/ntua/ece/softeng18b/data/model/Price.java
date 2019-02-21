package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;
import java.util.Date;

public class Price {

	private final long id;
	private final double value;
	private final Date dateFrom;
	private final Date dateTo;
	private final long productId;
	private final long shopId;

	public Price(long id, double value, Date dateFrom, Date dateTo, long productId, long shopId) {
		this.id          = id;
		this.value       = value;
		this.dateFrom   = dateFrom;
		this.dateTo     = dateTo;
		this.productId   = productId;
		this.shopId      = shopId;
	}

	public long getId() {
		return id;
	}

	public double getValue(){
		return value;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
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
