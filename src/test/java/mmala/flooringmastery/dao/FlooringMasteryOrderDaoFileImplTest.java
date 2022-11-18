/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.dao;
import mmala.flooringmastery.dto.Order;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author 18437
 */
public class FlooringMasteryOrderDaoFileImplTest {
    String testDataExportFile = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileDataTest\\BackupTest\\DataExportTest.txt";
    String testOrderFolder = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileDataTest\\OrdersTest\\";
    FlooringMasteryOrderDao testOrderDao = new FlooringMasteryOrderDaoFileImpl(testDataExportFile,testOrderFolder);
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {

        
    }
    
    @AfterEach
    public void tearDown() {
        File directory = new File(testOrderFolder);
        //deletes all the files in the testOrderFolder
        File [] files = directory.listFiles();
        for (File file: files) {
            file.delete();
        }
    }

    @Test
    public void testAddGetAllOrdersForADate() throws FlooringMasteryException {
        //testing function: getAllOrdersForADate & addOrderToExistingFile & addOrderToNewFile
        int orderNum2 = 3;
        Order order2 = new Order(orderNum2);
        order2.setCustomerName("Maryia Malakhava");
        order2.setStateAbbr("WA");
        order2.setProductType("Wood");
        order2.setTaxRate(new BigDecimal("9.25"));
        order2.setArea(new BigDecimal("243.00"));
        order2.setCostPerSquareFoot(new BigDecimal("5.15"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.33"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));
        
        //ACT 
        testOrderDao.addOrderToNewFile("Orders_11172022.txt", orderNum2, order2);
        
        //ARRANGE
        
        String testOrdersFile1 = "Orders_11172022.txt";  

        int orderNum3 = 4;
        Order order3 = new Order(orderNum3);
        order3.setCustomerName("Ernesto Hidalgo");
        order3.setStateAbbr("TX");
        order3.setProductType("Tile");
        order3.setTaxRate(new BigDecimal("25.00"));
        order3.setArea(new BigDecimal("100"));
        order3.setCostPerSquareFoot(new BigDecimal("3.50"));
        order3.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order3.setMaterialCost(new BigDecimal("871.50"));
        order3.setLaborCost(new BigDecimal("1033.33"));
        order3.setTax(new BigDecimal("476.21"));
        order3.setTotal(new BigDecimal("2381.06"));
        
        int orderNum4 = 5;
        Order order4 = new Order(orderNum4);
        order4.setCustomerName("Ana Reza");
        order4.setStateAbbr("Calfornia");
        order4.setProductType("Tile");
        order4.setTaxRate(new BigDecimal("25.00"));
        order4.setArea(new BigDecimal("100"));
        order4.setCostPerSquareFoot(new BigDecimal("3.50"));
        order4.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order4.setMaterialCost(new BigDecimal("871.50"));
        order4.setLaborCost(new BigDecimal("1033.33"));
        order4.setTax(new BigDecimal("476.21"));
        order4.setTotal(new BigDecimal("2381.06"));
        
        //ACT
        //Test add order to existing File
        testOrderDao.addOrderToExistingFile(testOrdersFile1,orderNum3,order3);
        testOrderDao.addOrderToExistingFile(testOrdersFile1,orderNum4,order4);
        
        //Get the orders from the file
        List<Order> ordersForADate = testOrderDao.getAllOrdersForADate(testOrdersFile1);
        
        //ASSERT
       
        assertNotNull(ordersForADate,"The list of orders should not be null");
        assertEquals(ordersForADate.size(),3,"The list should contain three orders"); 
        
        //Check that the list contains the three orders
        assertTrue(ordersForADate.contains(order2),"the list of orders should contain order2");
        assertTrue(ordersForADate.contains(order3),"the list of orders should contain order3");
        assertTrue(ordersForADate.contains(order4),"The list of orders should contain order4");
        
       
        File directory = new File(testOrderFolder);
        File [] files = directory.listFiles();
        for (File file: files) {
            file.delete();
        }
    }
    

    @Test
    public void testAddNewOrderFile() throws FlooringMasteryException {
        //testing function:  addNewOrderFile()
        //ARRANGE
        int orderNum = 4;
        Order order = new Order(orderNum);
        order.setCustomerName("Ernesto Hidalgo");
        order.setStateAbbr("TX");
        order.setProductType("Tile");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("871.50"));
        order.setLaborCost(new BigDecimal("1033.33"));
        order.setTax(new BigDecimal("476.21"));
        order.setTotal(new BigDecimal("2381.06"));
        
        //ACT
        //Test add order to existing File
        String newOrderFile = "newOrderFile.txt";
        testOrderDao.addOrderToNewFile(newOrderFile,orderNum,order);
        //testDao.addOrderToNewFile(newOrderFile,orderNum2,order2);
        
        //Test getting the order numbers 
        List<Order> allOrders = testOrderDao.getAllOrdersForADate(newOrderFile);
        
        //ASSERT
        //check that there are two order nums in the list
        assertEquals(allOrders.size(),1,"the list of order numbers should contain two items.");
        assertTrue(allOrders.contains(order),"The order numbers list should contain the order.");
        
        //TEAR DOWN 
        File directory = new File(testOrderFolder);
        File [] files = directory.listFiles();
        for (File file: files) {
            file.delete();
        }

    }
    
    @Test
    public void testGetOrder() throws FlooringMasteryException{
        //testing function: getOrder
        //ARRANGE
        
        int orderNum2 = 3;
        Order order2 = new Order(orderNum2);
        order2.setCustomerName("Maryia Malakhava");
        order2.setStateAbbr("WA");
        order2.setProductType("Wood");
        order2.setTaxRate(new BigDecimal("9.25"));
        order2.setArea(new BigDecimal("243.00"));
        order2.setCostPerSquareFoot(new BigDecimal("5.15"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.33"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));
        
        //ACT
        testOrderDao.addOrderToNewFile("Orders_11172022.txt", orderNum2, order2);
        //ARRANGE
        String testOrdersFile1 = "Orders_11172022.txt";
        
        int orderNum = 4;
        Order order = new Order(orderNum);
        order.setCustomerName("Ernesto Hidalgo");
        order.setStateAbbr("TX");
        order.setProductType("Wood");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("871.50"));
        order.setLaborCost(new BigDecimal("1033.33"));
        order.setTax(new BigDecimal("476.21"));
        order.setTotal(new BigDecimal("2890.06"));
        
        //Test add order to existing File
        testOrderDao.addOrderToExistingFile(testOrdersFile1,orderNum,order);
        
        //ACT
        Order retrievedOrder1 = testOrderDao.getOrder(testOrdersFile1, orderNum);
        Order retrievedOrder2 = testOrderDao.getOrder(testOrdersFile1, orderNum2);
        
        //ASSERT
        //Check that the data is equal
        assertEquals(retrievedOrder1, order,"The order1 added and the order retrieved should be equal.");
        assertEquals(retrievedOrder2, order2,"The order2 added and the order retrieved should be equal.");
        
        //TEAR DOWN 
        File directory = new File(testOrderFolder);
        File [] files = directory.listFiles();
        for (File file: files) {
            file.delete();
        }
    }
    
    @Test
    public void testGetAllOrders() throws FlooringMasteryException {
    //testing function - getAllOrders()
    //ARRANGE
    
    int orderNum = 1;
    Order order = new Order(orderNum);
    order.setCustomerName("Maryia Malakhava");
    order.setStateAbbr("WA");
    order.setProductType("Wood");
    order.setTaxRate(new BigDecimal("9.25"));
    order.setArea(new BigDecimal("243.00"));
    order.setCostPerSquareFoot(new BigDecimal("5.15"));
    order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    order.setMaterialCost(new BigDecimal("871.50"));
    order.setLaborCost(new BigDecimal("1033.33"));
    order.setTax(new BigDecimal("476.21"));
    order.setTotal(new BigDecimal("2381.06"));
    
    int orderNum2 = 2;
    Order order2 = new Order(orderNum2);
    order2.setCustomerName("Maryia Malakhava");
    order2.setStateAbbr("WA");
    order2.setProductType("Wood");
    order2.setTaxRate(new BigDecimal("9.25"));
    order2.setArea(new BigDecimal("243.00"));
    order2.setCostPerSquareFoot(new BigDecimal("5.15"));
    order2.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    order2.setMaterialCost(new BigDecimal("871.50"));
    order2.setLaborCost(new BigDecimal("1033.33"));
    order2.setTax(new BigDecimal("476.21"));
    order2.setTotal(new BigDecimal("2381.06"));
    
    int orderNum3 = 3;
    Order order3 = new Order(orderNum3);
    order3.setCustomerName("Maryia Malakhava");
    order3.setStateAbbr("WA");
    order3.setProductType("Wood");
    order3.setTaxRate(new BigDecimal("9.25"));
    order3.setArea(new BigDecimal("243.00"));
    order3.setCostPerSquareFoot(new BigDecimal("5.15"));
    order3.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    order3.setMaterialCost(new BigDecimal("871.50"));
    order3.setLaborCost(new BigDecimal("1033.33"));
    order3.setTax(new BigDecimal("476.21"));
    order3.setTotal(new BigDecimal("2381.06"));
    
    int orderNum4 = 4;
    Order order4 = new Order(orderNum4);
    order4.setCustomerName("Maryia Malakhava");
    order4.setStateAbbr("WA");
    order4.setProductType("Wood");
    order4.setTaxRate(new BigDecimal("9.25"));
    order4.setArea(new BigDecimal("243.00"));
    order4.setCostPerSquareFoot(new BigDecimal("5.15"));
    order4.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    order4.setMaterialCost(new BigDecimal("871.50"));
    order4.setLaborCost(new BigDecimal("1033.33"));
    order4.setTax(new BigDecimal("476.21"));
    order4.setTotal(new BigDecimal("2381.06"));
    
    //Add orders 1 & 2 to the same file, after that create new files for 3 & 4
    testOrderDao.addOrderToNewFile("Orders_11182022.txt", orderNum, order);
    testOrderDao.addOrderToExistingFile("Orders_11182022.txt", orderNum2, order2);
    testOrderDao.addOrderToNewFile("Orders_11192022.txt", orderNum3, order3);
    testOrderDao.addOrderToNewFile("Orders_11202022.txt", orderNum4, order4);
    
    List<Order> allOrderList = testOrderDao.getAllOrders();
    
    //ASSERT
    assertTrue(allOrderList.contains(order)&& allOrderList.contains(order2)&& allOrderList.contains(order3)&&allOrderList.contains(order4),""
            + "The Order List should contain order, order2, order3 and order4.");
    assertEquals(allOrderList.size(),4,"The allOrderList should contain 4 orders");
    
    //TEAR DOWN 
    File directory = new File(testOrderFolder);
    File [] files = directory.listFiles();
    for (File file: files) {
        file.delete();
    }    
}
    
 

    @Test
    public void testRemoveOrder() throws FlooringMasteryException{
        // testing function - removeOrder(Order order);
        //ARRANGE
        //Add two orders to a file
        int orderNum = 4;
        Order order = new Order(orderNum);
        order.setCustomerName("Ernesto Hidalgo");
        order.setStateAbbr("TX");
        order.setProductType("Tile");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("871.50"));
        order.setLaborCost(new BigDecimal("1033.33"));
        order.setTax(new BigDecimal("476.21"));
        order.setTotal(new BigDecimal("2381.06"));
        
        int orderNum2 = 5;
        Order order2 = new Order(orderNum2);
        order2.setCustomerName("Ana Reza");
        order2.setStateAbbr("Calfornia");
        order2.setProductType("Tile");
        order2.setTaxRate(new BigDecimal("25.00"));
        order2.setArea(new BigDecimal("100"));
        order2.setCostPerSquareFoot(new BigDecimal("3.50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.33"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));

        String testOrdersFile1 = "Orders_001012020.txt";
        //ACT
        //add order to existing File
        testOrderDao.addOrderToNewFile(testOrdersFile1,orderNum,order);
        testOrderDao.addOrderToExistingFile(testOrdersFile1,orderNum2,order2);
        
        //remove order from file 
        Order removedOrder = testOrderDao.removeOrder(testOrdersFile1, orderNum);
        
        //check that the correct object was removed
        assertEquals(removedOrder, order, "The removed order should be order number 1");
        
        //Get all the orders from the file
        List<Order> allOrders = testOrderDao.getAllOrdersForADate(testOrdersFile1);
        
        //First, check general contents of the list
        assertNotNull(allOrders, "All Orders list should not be null");
        assertEquals(1,allOrders.size(),"The all orders list should contain 1 order");
        
        //The list should still contain order num 2 but not order num 1
        assertFalse(allOrders.contains(order),"All orders should NOT include order number 1");
        assertTrue(allOrders.contains(order2),"All order should include order number 2");
        
        //Remove the order 2
        Order removedOrder2 = testOrderDao.removeOrder(testOrdersFile1, orderNum2);
        //check the correct order removed
        assertEquals(removedOrder2,order2,"The removed order should be order number 2");
        
        //Get all orders again and check
        allOrders = testOrderDao.getAllOrdersForADate(testOrdersFile1);
        
        //check contents
        assertTrue(allOrders.isEmpty(),"The retrieved list of orders should be empty.");
        
        
        Order retrievedOrder = testOrderDao.getOrder(testOrdersFile1,orderNum2);
        assertNull(retrievedOrder, "Order number 2 was removed, should be null");
        
        retrievedOrder = testOrderDao.getOrder(testOrdersFile1, orderNum);
        assertNull(retrievedOrder, "Order number 1 was removed, should be null");
        
        //TEAR DOWN 
        File directory = new File(testOrderFolder);
        File [] files = directory.listFiles();
        for (File file: files) {
            file.delete();
    }  
    }
    
    @Test
    public void testListAllOrderFiles(){
        
    }
    
    
    @Test
    public void testGetExportAllData() throws FlooringMasteryException{
    //testing function - getExportData()
    //ARRANGE
        //Create four orders:
    int orderNum = 1;
    Order order = new Order(orderNum);
    order.setCustomerName("Maryia Malakhava");
    order.setStateAbbr("WA");
    order.setProductType("Wood");
    order.setTaxRate(new BigDecimal("9.25"));
    order.setArea(new BigDecimal("243.00"));
    order.setCostPerSquareFoot(new BigDecimal("5.15"));
    order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    order.setMaterialCost(new BigDecimal("871.50"));
    order.setLaborCost(new BigDecimal("1033.33"));
    order.setTax(new BigDecimal("476.21"));
    order.setTotal(new BigDecimal("2381.06"));
    
    int orderNum2 = 2;
    Order order2 = new Order(orderNum2);
    order2.setCustomerName("Maryia Malakhava");
    order2.setStateAbbr("WA");
    order2.setProductType("Wood");
    order2.setTaxRate(new BigDecimal("9.25"));
    order2.setArea(new BigDecimal("243.00"));
    order2.setCostPerSquareFoot(new BigDecimal("5.15"));
    order2.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    order2.setMaterialCost(new BigDecimal("871.50"));
    order2.setLaborCost(new BigDecimal("1033.33"));
    order2.setTax(new BigDecimal("476.21"));
    order2.setTotal(new BigDecimal("2381.06"));
    
    int orderNum3 = 3;
    Order order3 = new Order(orderNum3);
    order3.setCustomerName("Maryia Malakhava");
    order3.setStateAbbr("WA");
    order3.setProductType("Wood");
    order3.setTaxRate(new BigDecimal("9.25"));
    order3.setArea(new BigDecimal("243.00"));
    order3.setCostPerSquareFoot(new BigDecimal("5.15"));
    order3.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    order3.setMaterialCost(new BigDecimal("871.50"));
    order3.setLaborCost(new BigDecimal("1033.33"));
    order3.setTax(new BigDecimal("476.21"));
    order3.setTotal(new BigDecimal("2381.06"));
    
    int orderNum4 = 4;
    Order order4 = new Order(orderNum4);
    order4.setCustomerName("Maryia Malakhava");
    order4.setStateAbbr("WA");
    order4.setProductType("Wood");
    order4.setTaxRate(new BigDecimal("9.25"));
    order4.setArea(new BigDecimal("243.00"));
    order4.setCostPerSquareFoot(new BigDecimal("5.15"));
    order4.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    order4.setMaterialCost(new BigDecimal("871.50"));
    order4.setLaborCost(new BigDecimal("1033.33"));
    order4.setTax(new BigDecimal("476.21"));
    order4.setTotal(new BigDecimal("2381.06"));
    
    //Add orders 1 & 2 to the same file, then create new files for 3 & 4
    testOrderDao.addOrderToNewFile("Orders_11182022.txt", orderNum, order);
    testOrderDao.addOrderToExistingFile("Orders_11182022.txt", orderNum2, order2);
    testOrderDao.addOrderToNewFile("Orders_11192022.txt", orderNum3, order3);
    testOrderDao.addOrderToNewFile("Orders_11202022.txt", orderNum4, order4);
    
    //ACT
    //Get the export data
    Map<String,List<Order>> exportData = testOrderDao.getExportData();
    
    //ASSERT
    
    //get the arraylists of orders:
    Collection<List<Order>> orderLists = exportData.values();
    //convert the arraylists into one list of orders
    ArrayList<Order> orders = new ArrayList<>();
    for (List<Order> orderList:orderLists) {
        for (Order orderi:orderList){
            orders.add(orderi);
        }
    }
    //check that the orders were added
    assertTrue(orders.contains(order)&& orders.contains(order2)
            && orders.contains(order3) && orders.contains(order4));
    
    //check that the export data keys contain the string version of the dates
    assertTrue(exportData.containsKey("11-18-2022") && exportData.containsKey("11-19-2022")
            && exportData.containsKey("11-20-2022"));
    
    //TEAR DOWN 
        File directory = new File(testOrderFolder);
        File [] files = directory.listFiles();
        for (File file: files) {
            file.delete();
        }
    }

    
}

