package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;

public class Shop {

	private final long id;
	private final String name;
	private final String address;
	private final double lng;
	private final double lat;
	private final boolean withdrawn;
	private final String tags;

	public Shop(long id, String name, String address, double lng, double lat, boolean withdrawn, String tags) {
		this.id          = id;
		this.name        = name;
		this.address     = address;
		this.lng         = lng;
		this.lat         = lat;
		this.withdrawn   = withdrawn;
		this.tags        = tags;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getLng() {
		return lng;
	}

	public String getLat() {
		return lat;
	}

	public boolean isWithdrawn() {
		return withdrawn;
	}

	public String getTags() {
                return tags;
        }

	@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Shop shop = (Shop) o;
			return id == shop.id;
		}

	@Override
		public int hashCode() {
			return Objects.hash(id);
		}
}
