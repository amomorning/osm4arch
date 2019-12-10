package utils;

import wblut.geom.WB_Point;

public class Gpoi {
	private String placeid;
	private float lat;
	private float lng;
	private float rating;
	private int userRatingsTotal;
	private boolean isChinese;
	private String name;
	private String type;
	private String typeDetail;
	
	
	public String getPlaceid() {
		return placeid;
	}
	public void setPlaceid(String placeid) {
		this.placeid = placeid;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public int getUserRatingsTotal() {
		return userRatingsTotal;
	}
	public void setUserRatingsTotal(int userRatingsTotal) {
		this.userRatingsTotal = userRatingsTotal;
	}
	public boolean isChinese() {
		return isChinese;
	}
	public void setChinese(boolean isChinese) {
		this.isChinese = isChinese;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeDetail() {
		return typeDetail;
	}
	public void setTypeDetail(String typeDetail) {
		this.typeDetail = typeDetail;
	}
	
}
