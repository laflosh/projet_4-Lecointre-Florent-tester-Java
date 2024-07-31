package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.constants.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;
    private Ticket ticket;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
    	
        try {
        	
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            ticket = new Ticket();
            
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            
        } catch (Exception e) {
        	
            e.printStackTrace();
            
            throw  new RuntimeException("Failed to set up test mock objects");
            
        }
        
    }

    @Test
    public void processExitingVehicleTestWithoutDiscount(){
    	
    	when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
    	
        parkingService.processExitingVehicle();
        
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
        
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getNbTicket(any());
        
    }
    
    @Test
    public void processExitingVehicleTestWithDiscount(){
    	
    	when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(3);
    	
        parkingService.processExitingVehicle();
        
        assertEquals(ticket.getPrice(), Math.round((0.95 * Fare.CAR_RATE_PER_HOUR) * Math.pow(10, 3)) / Math.pow(10, 3));
        
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getNbTicket(any());
        
    }
    
    
    public void testProcessIncomingVehicle() {
    	
    	when(inputReaderUtil.readSelection()).thenReturn(1);
    	when(parkingSpotDAO.getNextAvailableSlot("CAR").thenReturn(1);
    	when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
    	when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
    	
    	parkingService.processIncomingVehicle();
    	
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getNbTicket(any());
    	
    }

}
