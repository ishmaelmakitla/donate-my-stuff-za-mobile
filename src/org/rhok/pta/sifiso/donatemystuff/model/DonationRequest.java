package org.rhok.pta.sifiso.donatemystuff.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.google.gson.JsonObject;

public class DonationRequest implements Serializable{
	
	private static final long serialVersionUID = 786065079901106981L;
	String id;
    String beneficiaryid;
    String donationofferid;
    Date requestdate;
    DonatedItem item;
    int quantity;
    boolean collect;
    
    public DonationRequest(){
    	this.id = UUID.randomUUID().toString();
    }
    
        
    /**
	 * @return the item
	 */
	public DonatedItem getItem() {
		return item;
	}


	/**
	 * @return the collect
	 */
	public boolean isCollect() {
		return collect;
	}


	/**
	 * @param collect the collect to set
	 */
	public void setCollect(boolean collect) {
		this.collect = collect;
	}


	/**
	 * @param item the item to set
	 */
	public void setItem(DonatedItem item) {
		this.item = item;
	}


	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getBeneficriaryId() {
		return beneficiaryid;
	}



	public void setBeneficriaryId(String beneficriaryId) {
		this.beneficiaryid = beneficriaryId;
	}



	public String getDonationOfferId() {
		return donationofferid;
	}



	public void setDonationOfferId(String donationOfferId) {
		this.donationofferid = donationOfferId;
	}



	public Date getRequestDate() {
		return requestdate;
	}



	public void setRequestDate(Date requestDate) {
		this.requestdate = requestDate;
	}

	public DonatedItem getRequestedDonationItem() {
		return item;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public void setRequestedDonationItem(DonatedItem requestedDonationItem) {
		this.item = requestedDonationItem;
	}



	/**
	 * This method returns a JSON version of the instantiated DonationOffer object
	 * @return
	 */
	public JsonObject asJSON(){
		JsonObject json = new JsonObject();
		json.addProperty("id", id);
		json.addProperty("requestdate", requestdate.toString());
		json.addProperty("beneficiaryid", beneficiaryid);
		json.addProperty("donationofferid", donationofferid);
		json.addProperty("quantity", quantity);
		//donated item
		JsonObject donatedItemJson = null;
		if(item != null){
			donatedItemJson = item.asJSON();	
		}
		
		json.add("item", donatedItemJson);
		
		System.out.println(json.toString());
		
		return json;
	}
	
}
