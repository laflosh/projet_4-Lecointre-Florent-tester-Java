package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
    	
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
        	
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
            
        }

        double inTime = ticket.getInTime().getTime();
        double outTime = ticket.getOutTime().getTime();

        double duration = outTime - inTime;
        duration = duration / 3600000; //Convertion milliseconde in hour
        
        if(duration <= 0.5) { //0.5 hour is equal to 30min, it's free fro 30min or less
        	
        	ticket.setPrice(0);
        	
        } else {
        	
            switch (ticket.getParkingSpot().getParkingType()){
            
            case CAR: {
            	
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
                
            }
            
            case BIKE: {
            	
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
                
            }
            
            default: throw new IllegalArgumentException("Unkown Parking Type");
            
            }
        	
        }
        
    }

	public void calculateFare(Ticket ticket, boolean discountTicket) {
		// TODO Auto-generated method stub
		
	}
    
}