package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
	private static ParkingSpotDAO parkingSpotDAO;
	
	private static ParkingType parkingType;
	
	@BeforeAll
	private static void setUp() throws Exception {
		
		parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
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
	public void testForGettingTheNextAvaibleParkingSlot() {
		
		parkingType = ParkingType.CAR;
		
		int parkingNumberTest = parkingSpotDAO.getNextAvailableSlot(parkingType);
		
		assertEquals(parkingNumberTest, 1);
		
	}
	


}
