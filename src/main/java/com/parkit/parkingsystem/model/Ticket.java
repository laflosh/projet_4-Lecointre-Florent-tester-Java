package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
	
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public int getId() {
    	
        return id;
        
    }

    public void setId(int id) {
    	
        this.id = id;
        
    }

    public ParkingSpot getParkingSpot() {
    	
        return parkingSpot;
        
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
    	
        this.parkingSpot = parkingSpot;
        
    }

    public String getVehicleRegNumber() {
    	
        return vehicleRegNumber;
        
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
    	
        this.vehicleRegNumber = vehicleRegNumber;
        
    }

    public double getPrice() {
    	
        return price;
        
    }

    public void setPrice(double price) {
    	
    	double scale = Math.pow(10, 3);
    	double roundPrice = Math.round(price * scale) / scale; 
        this.price = roundPrice;
        
    }

    public Date getInTime() {
    	
        return inTime;
        
    }

    public void setInTime(Date inTime) {
    	
        this.inTime = inTime;
        
    }

    public Date getOutTime() {
    	
        return outTime;
        
    }

    public void setOutTime(Date outTime) {
    	
        this.outTime = outTime;
        
    }
}
