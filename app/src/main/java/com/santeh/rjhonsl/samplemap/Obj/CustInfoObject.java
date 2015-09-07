package com.santeh.rjhonsl.samplemap.Obj;

import android.graphics.Bitmap;

public class CustInfoObject {
	
	private int id;
	private int ci_id;


	private String latitude;
	private String longtitude;
	private String contact_name;
	private String company;
	private String address;
	private String farmname;
	private String farmID;
	private String contact_number;
	private String cultureType;
	private String culturelevel;
	private String waterType;
	private String dateAddedToDB;
	private Bitmap bitmap;

	private int pondID;
	private int area;
	private int quantity;
	private int sizeofStock;


	private int totalStockOfFarm;
    private int pondIndex;


	private int currentweekofStock;

	private int startweekofStock;

	private int week;


	private String customerID;
	private String specie;
	private String culturesystem;
	private String dateStocked;
	private String remarks;
	private String allSpecie;
	private String currentfeedType;


	private String recommendedConsumption;
	private String actualConsumption;

	private double weeklyConsumptionInGrams;



	public double getWeeklyConsumptionInGrams() {
		return weeklyConsumptionInGrams;
	}

	public void setWeeklyConsumptionInGrams(double weeklyConsumptionInGrams) {
		this.weeklyConsumptionInGrams = weeklyConsumptionInGrams;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}


	public int getStartweekofStock() {
		return startweekofStock;
	}

	public void setStartweekofStock(int startweekofStock) {
		this.startweekofStock = startweekofStock;
	}


	public String getRecommendedConsumption() {
		return recommendedConsumption;
	}

	public void setRecommendedConsumption(String recommendedConsumption) {
		this.recommendedConsumption = recommendedConsumption;
	}

	public String getActualConsumption() {
		return actualConsumption;
	}

	public void setActualConsumption(String actualConsumption) {
		this.actualConsumption = actualConsumption;
	}




	public String getCurrentfeedType() {
		return currentfeedType;
	}

	public void setCurrentfeedType(String currentfeedType) {
		this.currentfeedType = currentfeedType;
	}

	public int getCurrentweekofStock() {
		return currentweekofStock;
	}

	public void setCurrentweekofStock(int currentweekofStock) {
		this.currentweekofStock = currentweekofStock;
	}


	public String getAllSpecie() {
		return allSpecie;
	}

	public void setAllSpecie(String allSpecie) {
		this.allSpecie = allSpecie;
	}


	public int getTotalStockOfFarm() {
		return totalStockOfFarm;
	}

	public void setTotalStockOfFarm(int totalStockOfFarm) {
		this.totalStockOfFarm = totalStockOfFarm;
	}



	public int getCi_id() {
		return ci_id;
	}

	public void setCi_id(int ci_id) {
		this.ci_id = ci_id;
	}


	public int getPondIndex() {
        return pondIndex;
    }

    public void setPondIndex(int pondIndex) {
        this.pondIndex = pondIndex;
    }

	public int getSizeofStock() {
		return sizeofStock;
	}

	public void setSizeofStock(int sizeofStock) {
		this.sizeofStock = sizeofStock;
	}

	public int getPondID() {
		return pondID;
	}

	public void setPondID(int pondID) {
		this.pondID = pondID;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getSpecie() {
		return specie;
	}

	public void setSpecie(String specie) {
		this.specie = specie;
	}

	public String getCulturesystem() {
		return culturesystem;
	}

	public void setCulturesystem(String culturesystem) {
		this.culturesystem = culturesystem;
	}

	public String getDateStocked() {
		return dateStocked;
	}

	public void setDateStocked(String dateStocked) {
		this.dateStocked = dateStocked;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getCulturelevel() {
		return culturelevel;
	}

	public void setCulturelevel(String culturelevel) {
		this.culturelevel = culturelevel;
	}

	public String getCultureType() {
		return cultureType;
	}

	public void setCultureType(String cultureType) {
		this.cultureType = cultureType;
	}

	public String getDateAddedToDB() {
		return dateAddedToDB;
	}

	public void setDateAddedToDB(String dateAddedToDB) {
		this.dateAddedToDB = dateAddedToDB;
	}

	public String getFarmID() {
		return farmID;
	}

	public void setFarmID(String farmID) {
		this.farmID = farmID;
	}

	public String getFarmname() {
		return farmname;
	}

	public void setFarmname(String farmname) {
		this.farmname = farmname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getWaterType() {
		return waterType;
	}

	public void setWaterType(String waterType) {
		this.waterType = waterType;
	}
}
