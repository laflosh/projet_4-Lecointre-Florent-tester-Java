package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
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

    	//dataBasePrepareService.clearDataBaseEntries();
    	
    }

    @Test
    public void testParkingACar(){
    	
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        parkingService.processIncomingVehicle();
        
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        
        assertEquals(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR), 2); //If the second place is return so the first place is taken by the vehicle
        Ticket ticketTest = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticketTest);
        
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
        
        //TODO: check that the fare generated and out time are populated correctly in the database
        Ticket ticketTest = ticketDAO.getTicket("ABCDEF");
        
        assertNotNull(ticketTest);
        assertNotNull(ticketTest.getOutTime());
        assertEquals(ticketTest.getPrice(), 0); //Vehicle stay less than 30min so it's free time
        
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
    	
    	assertEquals(ticketDAO.getNbTicket("ABCDEF"), 2); //If equal to 2 thier will be the 5% reduc
    	
    }
    
}
