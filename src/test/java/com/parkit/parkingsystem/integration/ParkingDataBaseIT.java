package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
    	
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
    	
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
        
    }

    @AfterAll
    private static void tearDown(){

    	dataBasePrepareService.clearDataBaseEntries();
    	
    }

    @Test
    public void testParkingACar(){
    	
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        parkingService.processIncomingVehicle();
        
        //Check that a ticket is actualy saved in DB and 
        
        Ticket ticketTest = ticketDAO.getTicket("ABCDEF");
        ParkingSpot parkingSpotTest = ticketTest.getParkingSpot();
        
        assertNotNull(ticketTest);
        assertNotNull(ticketTest.getInTime());
        assertNull(ticketTest.getOutTime());
        assertEquals(ticketTest.getPrice(), 0);
        //Parking table is updated with availability
        assertEquals(parkingSpotTest.getId(), 1);
        
    }

    @Test
    public void testParkingLotExit() {
    	
        testParkingACar();
        
        try {
        	
			TimeUnit.SECONDS.sleep(1);
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			
		}
        
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        parkingService.processExitingVehicle();
        
        //Check that the fare generated and out time are populated correctly in the database
        Ticket ticketTest = ticketDAO.getTicket("ABCDEF");
        
        assertNotNull(ticketTest);
        assertNotNull(ticketTest.getOutTime());
        
        //Vehicle stay less than 30min so it's free time
        assertEquals(ticketTest.getPrice(), 0); 
        
    }
    
    @Test
    public void testParkingLotExitRecurringUser(){
    	
    	testParkingLotExit();
    	testParkingACar();
    	
        try {
        	
			TimeUnit.SECONDS.sleep(1);
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			
		}
    	
    	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	
    	parkingService.processExitingVehicle();
    	
    	//If equal to 2 their will be the 5% reduc
    	assertEquals(ticketDAO.getNbTicket("ABCDEF"), 2); 
    	
    }
    
}
