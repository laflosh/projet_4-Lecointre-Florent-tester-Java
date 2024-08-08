package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
	private static TicketDAO ticketDAO;
	
	
	@BeforeAll
	private static void setUp() throws Exception {
		
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
		
	}
	
    @BeforeEach
    private void setUpPerTest() throws Exception {
    	
        dataBasePrepareService.clearDataBaseEntries();
        
    }

    @AfterAll
    private static void tearDown(){

    	//dataBasePrepareService.clearDataBaseEntries();
    	
    }
	
	@Test
	public void testForSavingATicketInTheDataBase() {
		
		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, false);
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        
        ticketTest.setParkingSpot(parkingSpotTest);
        ticketTest.setVehicleRegNumber("ABCDEF");
        ticketTest.setPrice(0);
        ticketTest.setInTime(inTime);
        ticketTest.setOutTime(null);
        
        boolean responseTest = ticketDAO.saveTicket(ticketTest);
        
        assertFalse(responseTest);
		
	}

}
