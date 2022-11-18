/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.service;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import mmala.flooringmastery.dao.FlooringMasteryOrderDao;
import mmala.flooringmastery.dao.FlooringMasteryOrderDaoFileImpl;
import mmala.flooringmastery.dao.FlooringMasteryException;
import mmala.flooringmastery.dao.FlooringMasteryProductDao;
import mmala.flooringmastery.dao.FlooringMasteryProductDaoFileImpl;
import mmala.flooringmastery.dao.FlooringMasteryTaxDao;
import mmala.flooringmastery.dao.FlooringMasteryTaxDaoFileImpl;
import mmala.flooringmastery.dto.Order;
import mmala.flooringmastery.dto.Product;
import mmala.flooringmastery.dto.Tax;
import java.io.File;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 * @author 18437
 */
public class FlooringMasteryServiceLayerImplTest {
    
   // pathes 
    String testDataExportFile = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileDataTest\\BackupTest\\DataExportTest.txt";
    String testOrderFolder = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileDataTest\\OrdersTest\\";
    String testProductsFile = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileDataTest\\DataTest\\ProductsTest.txt";
    String testTaxesFile = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileDataTest\\DataTest\\TaxesTest.txt";
    
    
    FlooringMasteryProductDao testProductDao = new FlooringMasteryProductDaoFileImpl(testProductsFile);
    FlooringMasteryTaxDao testTaxDao = new FlooringMasteryTaxDaoFileImpl(testTaxesFile);
    FlooringMasteryOrderDao testOrderDao = new FlooringMasteryOrderDaoFileImpl(testDataExportFile,testOrderFolder);
    
    
    FlooringMasteryServiceLayer testService = new FlooringMasteryServiceLayerImpl(testOrderDao, testProductDao,testTaxDao);
                       
    
    public FlooringMasteryServiceLayerImplTest() {
    }
    @BeforeAll
    public static void setUpClass() {
    }
    @AfterAll
    public static void tearDownClass() {
    }
    @BeforeEach
    public void setUp() {
        //non-static method is run before each test method in the JUnit test class. 
    }
    @AfterEach
    public void tearDown() {
    }
    
    
    @Test 
    public void testCreateOrderFileNameFromDate() {
        //ARRANGE
        LocalDate date = LocalDate.parse("2000-12-06");
        
        //ACT
        String orderFileName = testService.createOrderFileNameFromDate(date);
        
        //ASSERT
        assertEquals(orderFileName, "Orders_12062000.txt","The order file name generated was incorrect"); 
}
    
    @Test 
    public void testCheckOrderFileExists() throws FlooringMasteryException {
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
        String orderFileName = "Orders_11172022.txt";
        //ACT - add order to new file
        testOrderDao.addOrderToNewFile(orderFileName, orderNum2, order2);
        //ASSERT
        try {
            testService.checkOrderFileExists(orderFileName);
        } catch (FlooringMasteryNoOrdersException e) {
            //If an exception was thrown, then the test will fail, if not then it passed.
            fail("Order file did exist. No Exception have been thrown");
        }
        //TEAR DOWN 
        File directory = new File(testOrderFolder);
        File [] files = directory.listFiles();
        for (File file: files) {
            file.delete();
        }   
        
        
    }
    
    @Test 
    public void testCheckOrderFileDoesNotExist() {
        //ACT - add order to new file
        String orderFileName = "Orders_11172045.txt";
        
        try {
            testService.checkOrderFileExists(orderFileName);
            //the file does not exist, an exxception should be thrown
            
            fail("Order file did not exist. Exception shuld have been thrown");
        } catch (FlooringMasteryNoOrdersException e) {
        } 
}

    @Test
    public void testCheckDateIsInFuture() {
        //ARRANGE
        LocalDate todaysDate = LocalDate.now();
        LocalDate aFutureDate = todaysDate.plusDays(10);
        LocalDate aPastDate = todaysDate.minusDays(10);
        
        //ACT correct date
        try {
            testService.checkDateIsInFuture(aFutureDate);
        } catch (FlooringMasteryDateErrorException e){
            //ASSERT
            fail("This date was in the future, no exception should have been thrown.");
        }
        //ACT wrong date
        try {
            testService.checkDateIsInFuture(aPastDate);
            //ASSERT
            fail("This date was in the past, an exception should have been thrown.");
        } catch (FlooringMasteryDateErrorException e){
        } 
    }
    
    @Test
    public void testInvalidCustomerName() {
        //ARRANGE
        //test invalid customer names
        String customerName = "";
        
        //ACT
        try {
            testService.validateCustomerName(customerName);
            //ASSERT
            fail("Customer's name is invalid. Exception should have been thrown.");
        } catch (FlooringMasteryCustomerNameErrorException e){
        }
    }
    @Test
    public void testInvalidCustomerName2() {
       
        String customerName = "        ";
        
        //ACT
        try {
            testService.validateCustomerName(customerName);
            //ASSERT
            fail("Customer's name is invalid. Exception should have been thrown.");
        } catch (FlooringMasteryCustomerNameErrorException e){
        }
    }
    @Test
    public void testValidCustomerName2() {
        //ARRANGE
        
        String customerName = "Maryia";
        
        //ACT
        try {
            testService.validateCustomerName(customerName);
        } catch (FlooringMasteryCustomerNameErrorException e){
            //ASSERT
            fail("Customer's name is valid. Exception should not have been thrown.");
        }
    }
    @Test
    public void testGetCustomerNamePlaceHolder() {
        //ARRANGE
        String name = "Irina, Inc";
        //ACT
        String nameWithPlaceHolder = testService.getCustomerNamePlaceHolder(name);
        //ASSERT
        assertEquals(nameWithPlaceHolder, "Irina#*~ Inc", "The name should include the placeholder in "
                + "replace of comma");
    }
    
    @Test
    public void testCheckValidStateAgainstTaxFile () throws FlooringMasteryException {
        //ARRANGE
        String stateAbbr = "TX";
        
        try {
            //ACT
            testService.checkStateAgainstTaxFile(stateAbbr);
        } catch (FlooringMasteryStateNotFoundException e){
            fail("The state with abbreviation TX exists. Exception should not have"
                    + "been thrown.");
        }
    }
    
    @Test
    public void testCheckInvalidStateAgainstTaxFile () throws FlooringMasteryException {
        //ARRANGE
        String stateAbbr = "NY";
        
        try {
            //ACT
            testService.checkStateAgainstTaxFile(stateAbbr);
            fail("The state with abbreviation NY does not exist. Exception should have"
                    + "been thrown.");
        } catch (FlooringMasteryStateNotFoundException e){
        }
    }
    
    @Test
    public void testGetAllProducts() throws FlooringMasteryException {
        //ARRANGE
        Product carpetClone = new Product("Carpet");
        carpetClone.setCostPerSquareFoot(new BigDecimal ("2.25"));
        carpetClone.setLaborCostPerSquareFoot(new BigDecimal ("2.10"));

        //ACT & ASSERT
        assertEquals(4,testService.getAllProducts().size(),"Should have 4 products");
        assertTrue(testService.getAllProducts().contains(carpetClone),"Product list should contain Kentucky."); 
    }
    
    @Test
    public void testCheckValidProductTypeAgainstProductsFile() throws FlooringMasteryException{
        //ARRANGE
        String productType = "Wood";
        
        //ACT
        try {
            testService.checkProductTypeAgainstProductsFile(productType);
        } catch (FlooringMasteryProductTypeNotFoundException e){
            //ASSERT
            fail("Wood does exist, exception should not have been thrown.");
        }
    }
    
    @Test
    public void testCheckInvalidProductTypeAgainstProductsFile() throws FlooringMasteryException{
        //ARRANGE
        String productType = "Linoleum";
        
        //ACT
        try {
            testService.checkProductTypeAgainstProductsFile(productType);
            //ASSERT
            fail("Linoleum does not exist, exception should have been thrown.");
        } catch (FlooringMasteryProductTypeNotFoundException e){
        }
    }
    
    @Test
    public void testGetProduct() throws FlooringMasteryException {
        //ARRANGE
        String productType = "Carpet";
        Product carpetClone = new Product("Carpet");
        carpetClone.setCostPerSquareFoot(new BigDecimal ("2.25"));
        carpetClone.setLaborCostPerSquareFoot(new BigDecimal ("2.10"));
        
        //ACT
        Product retrievedProduct = testService.getProduct(productType);
        
        //ASSERT
        assertEquals(retrievedProduct, carpetClone, "The product retrieved should be"
                + "carpet");
    }
    
    @Test
    public void testCalculateMaterialCost(){
        //ARRANGE
        BigDecimal area = new BigDecimal("4");
        BigDecimal costPerSquareFoot = new BigDecimal("10");
        
        //ACT
        BigDecimal materialCost = testService.calculateMaterialCost(area, costPerSquareFoot);
        
        //ASSERT
        assertEquals(materialCost, new BigDecimal("40.00"),"Material cost should be 40.00"); 
    }
    @Test
    public void testCalculateLaborCost(){
        //ARRANGE
        BigDecimal area = new BigDecimal("4");
        BigDecimal laborCostPerSquareFoot = new BigDecimal("50");
        
        //ACT
        BigDecimal laborCost = testService.calculateMaterialCost(area, laborCostPerSquareFoot);
        
        //ASSERT
        assertEquals(laborCost, new BigDecimal("200.00"),"labor cost should be 200.00"); 
    }
    @Test
    public void testCalculateTax(){
        //ARRANGE
        BigDecimal materialCost = new BigDecimal("4");
        BigDecimal laborCost = new BigDecimal("50");
        BigDecimal taxRate = new BigDecimal("7.50");
        
        //ACT
        BigDecimal tax = testService.calculateTax(materialCost, laborCost, taxRate);
        
        //ASSERT
        assertEquals(tax, new BigDecimal("4.05"),"Tax should be 4.05"); 
    }
    
    @Test
    public void testCalculateTotal() {
        //ARRANGE
        BigDecimal materialCost = new BigDecimal("5.00");
        BigDecimal laborCost = new BigDecimal("75.25");
        BigDecimal tax = new BigDecimal("9.25");
        
        //ACT
        BigDecimal total = testService.calculateTotal(materialCost, laborCost, tax);
        
        //ASSERT
        assertEquals(total, new BigDecimal("89.50"),"The total should be 89.50");
        
    }
    @Test
    public void testCheckAreaOverMinOrder() {
        //ARRANGE
        BigDecimal areaOverMin = new BigDecimal("101");
        BigDecimal areaBelowMin = new BigDecimal("99");
        BigDecimal areaMin = new BigDecimal("100");
        
        //ACT - invalid area (below the min)
        try {
            testService.checkAreaOverMinOrder(areaBelowMin);
            fail("The area is below the minimum, exception should have been"
                    + "thrown");
        } catch (FlooringMasteryAreaBelowMinException e) {
        }
        
        //ACT - valid area (over the min)
        try {
            testService.checkAreaOverMinOrder(areaOverMin);
        } catch (FlooringMasteryAreaBelowMinException e) {
            fail("The area is above the minimum, exception should not have been"
                    + "thrown");
        }
        //ACT - valid area (the min)
        try {
            testService.checkAreaOverMinOrder(areaMin);
        } catch (FlooringMasteryAreaBelowMinException e) {
            fail("The area is the minimum, exception should not have been"
                    + "thrown");
        } 
    }
    @Test
    public void testGetTax() throws FlooringMasteryException {
       //ARRANGE
       Tax kentuckyClone = new Tax("KY");
       kentuckyClone.setStateName("Kentucky");
       kentuckyClone.setTaxRate(new BigDecimal("6.00"));
       
       Tax retrievedTax = testService.getTax("KY");
       
       //ASSERT
       assertEquals(retrievedTax, kentuckyClone,"The retrieved tax should be "
               + "kentucky");
    }
    
    @Test
    public void testGenerateNewOrderNum() throws FlooringMasteryException {
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
    
    //Add orders 1 & 2 to the same file, then create new files for 3 & 4
    testOrderDao.addOrderToNewFile("Orders_11182022.txt", orderNum, order);
    testOrderDao.addOrderToExistingFile("Orders_11182022.txt", orderNum2, order2);
    testOrderDao.addOrderToNewFile("Orders_11192922.txt", orderNum3, order3);
    testOrderDao.addOrderToNewFile("Orders_11202022.txt", orderNum4, order4);
    
    //ACT
    int orderNumTest = testService.generateNewOrderNum();
    
    //ASSERT
    assertEquals(orderNumTest, 5,"The next order number should be 5.");
    
    
    //TEAR DOWN 
    File directory = new File(testOrderFolder);
    File [] files = directory.listFiles();
    for (File file: files) {
        file.delete();
    }   
    }
    
    
    @Test
    public void testCheckForEdit() {
        //ARRANGE
        String updatedInfoEmpty = "";
        String updatedInfoNull = null;
        String updatedInfoBlank = "                ";
        String updatedInfoValid = "Maryia";

        //ACT
        String updated1 = testService.checkForEdit(updatedInfoEmpty);
        String updated2= testService.checkForEdit(updatedInfoNull);
        String updated3= testService.checkForEdit(updatedInfoBlank); 
        String updated4= testService.checkForEdit(updatedInfoValid); 
        
        //ASSERT
        assertEquals(updated1, null, "updated1 should be null");
        assertEquals(updated2, null, "updated2 should be null");
        assertEquals(updated3, null, "updated3 should be null");
        assertEquals(updated4, updatedInfoValid, "updated4 should be Maryia");

    }
    
}