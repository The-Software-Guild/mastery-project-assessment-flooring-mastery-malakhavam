/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.dao;

import mmala.flooringmastery.dto.Order;
import mmala.flooringmastery.service.FlooringMasteryOrderFileNotExistException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author 18437
 */
public class FlooringMasteryOrderDaoFileImpl implements FlooringMasteryOrderDao {
    private Map <Integer, Order> orders = new HashMap<>();
    private final String DELIMITER = ",";
    private final String DATA_EXPORT_FILE;
    private final String customerNamePlaceHolder = "#*~";
    private final String orderFolder;
    private final Path orderFolder2 = Paths.get("C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileData\\Orders");
    
    public FlooringMasteryOrderDaoFileImpl() {
        this.DATA_EXPORT_FILE = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileData\\Backup\\DataExport.txt";
        this.orderFolder = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileData\\Orders";
    }
    
    
    public FlooringMasteryOrderDaoFileImpl (String dataExportFile, String orderFolder) {
        this.DATA_EXPORT_FILE = dataExportFile;
        this.orderFolder = orderFolder;
    }
    
    @Override
    public List<Order> getAllOrdersForADate(String orderFile) throws FlooringMasteryException {
        loadOrders(orderFile);
        List<Order> allOrdersForADate = new ArrayList(orders.values());
        this.orders.clear();
        return allOrdersForADate;
    }
    
    @Override
    public List<Integer> getAllOrderNumsForADate(String orderFile) throws FlooringMasteryException {
        loadOrders(orderFile);
        Set<Integer> orderNums = orders.keySet();
        List<Integer> listOfOrderNums = new ArrayList<>(orderNums);
        this.orders.clear();
        return listOfOrderNums;
    }
    
    @Override 
    public List<Integer> getAllOrderNums() throws FlooringMasteryException {
        
        String [] allOrderFiles= listAllOrderFiles();
        
        List<Integer> allOrderNums = new ArrayList<>();
       
        for (String orderFile : allOrderFiles) {
            List<Integer> orderNumsPerFile = getAllOrderNumsForADate(orderFile);
            orderNumsPerFile.forEach(orderNum -> {
                allOrderNums.add(orderNum);
            });  
        }

        orders.clear();
        return allOrderNums;
        
    }

    @Override
    public Order getOrder (String orderFile, int orderNum) throws FlooringMasteryException {
        loadOrders(orderFile);
        Order orderToGet = orders.get(orderNum);
        orders.clear();
        return orderToGet;
    }
    
    @Override
    public List<Order> getAllOrders() throws FlooringMasteryException { 
        
        String [] allOrderFiles = listAllOrderFiles();
        
        List<Order> allOrders = new ArrayList<>();
        
        for (String orderFile : allOrderFiles) {
            List<Order> ordersForADate = getAllOrdersForADate(orderFile);
            ordersForADate.forEach(order -> {
                allOrders.add(order);
            });
        }
        return allOrders;
    }

    @Override
    public Order addOrderToExistingFile(String orderFile, int orderNumber, Order order) throws FlooringMasteryException {
        loadOrders(orderFile);
        Order newOrder = orders.put(orderNumber, order);
        writeOrders(orderFile);
        orders.clear();
        return newOrder;
    }
    
    @Override
    public Order addOrderToNewFile(String orderFile, int orderNumber, Order order) throws FlooringMasteryException {
       
        Order newOrder = orders.put(orderNumber, order);
        writeOrders(orderFile);
        orders.clear();
        return newOrder;
    }
    
    
    @Override 
    public Order editOrder(String orderFile, Order updatedOrder)

        throws FlooringMasteryException{
        loadOrders(orderFile);
        Order orderToEdit = orders.get(updatedOrder.getOrderNumber());
        orderToEdit.setCustomerName(updatedOrder.getCustomerName());
        orderToEdit.setStateAbbr(updatedOrder.getStateAbbr());
        orderToEdit.setTaxRate(updatedOrder.getTaxRate());
        orderToEdit.setProductType(updatedOrder.getProductType());
        orderToEdit.setArea(updatedOrder.getArea());
        orderToEdit.setCostPerSquareFoot(updatedOrder.getCostPerSquareFoot());
        orderToEdit.setLaborCostPerSquareFoot(updatedOrder.getLaborCostPerSquareFoot());
        orderToEdit.setMaterialCost(updatedOrder.getMaterialCost());
        orderToEdit.setLaborCost(updatedOrder.getLaborCost());
        orderToEdit.setTax(updatedOrder.getTax());
        orderToEdit.setTotal(updatedOrder.getTotal());
        writeOrders(orderFile);
        return orderToEdit;
    }

    @Override
    public Order removeOrder(String orderFile, int orderNumber) throws FlooringMasteryException {
        loadOrders(orderFile);
        Order orderToRemove = orders.remove(orderNumber);
        writeOrders(orderFile);
        return orderToRemove;
    }

    public String getDateFromOrderFileName(String orderFile) throws FlooringMasteryException {
        
        String [] fileNameTokens = orderFile.split("_");
        String [] dateTokens = fileNameTokens[1].split("\\.");
        String date = dateTokens[0];
        String mm = date.substring(0, 2); 
        String dd = date.substring(2, 4);
        String yyyy = date.substring(4, 8);
        
        return mm+"-"+dd+"-"+yyyy;
    }
    
    @Override
    public Map<String,List<Order>> getExportData() throws FlooringMasteryException  {
        Map<String,List<Order>> exportDataMap = new HashMap<>();
            String [] allOrderFiles = this.listAllOrderFiles();
        
        for (String orderFile: allOrderFiles) {
             String dateString = this.getDateFromOrderFileName(orderFile);
             List<Order> allOrdersForFile = this.getAllOrdersForADate(orderFile);
            
            exportDataMap.put(dateString,allOrdersForFile);
        }
        return exportDataMap;
    }
    
    @Override
    public void exportAllData() throws FlooringMasteryException {
        this.writeDataExport();
    }

   @Override
    public String [] listAllOrderFiles() {
        
        FilenameFilter filter = (file, fileName) -> {
        return fileName.contains(".");
        };
        
        
        String [] orderFiles = new File(orderFolder).list(filter);
        return orderFiles;
    }
    
    @Override
    public void deleteOrderFile(String fileName) throws FlooringMasteryOrderFileNotExistException {
        Path pathOfFile = Paths.get(this.orderFolder + "\\" + fileName);
        try {
            Files.deleteIfExists(pathOfFile);
        } catch (IOException e) {
            throw new FlooringMasteryOrderFileNotExistException(
            "Could not delete order file.",e);
          
        }
      

    }  


    private String marshallOrder(Order aOrder) {
      
        String orderAsText = String.valueOf(aOrder.getOrderNumber()) + DELIMITER;
        orderAsText += aOrder.getCustomerName() + DELIMITER;
        orderAsText += aOrder.getStateAbbr() + DELIMITER;
        orderAsText += aOrder.getTaxRate().toString() + DELIMITER;
        orderAsText += aOrder.getProductType() + DELIMITER;
        orderAsText += aOrder.getArea().toString() + DELIMITER;
        orderAsText += aOrder.getCostPerSquareFoot().toString() + DELIMITER;
        orderAsText += aOrder.getLaborCostPerSquareFoot().toString() + DELIMITER;
        orderAsText += aOrder.getMaterialCost().toString() + DELIMITER;
        orderAsText += aOrder.getLaborCost().toString() + DELIMITER;
        orderAsText += aOrder.getTax().toString() + DELIMITER;
        orderAsText += aOrder.getTotal().toString();
        return orderAsText;
    }
    
   
    private Order unmarshallOrder(String orderAsText) {
        
        String [] orderTokens = orderAsText.split(DELIMITER);
        
        int orderNumber = Integer.parseInt(orderTokens[0]);
        Order orderFromFile = new Order(orderNumber);
        
        String customerName = orderTokens[1];
        customerName = customerName.replace(customerNamePlaceHolder, ",");
        
        orderFromFile.setCustomerName(customerName);
        orderFromFile.setStateAbbr(orderTokens[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));
        orderFromFile.setProductType(orderTokens[4]);
        orderFromFile.setArea(new BigDecimal (orderTokens[5]));
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));

        return orderFromFile;
    }
    
    private void loadOrders(String orderFile) throws FlooringMasteryException {
       
        Scanner scanner;
        try {
            scanner = new Scanner(
                new BufferedReader(
                    new FileReader(this.orderFolder+"\\"+orderFile)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryException(
            "-_- Could not load order data into memory",e);
        }
        
        //Read from file
        String currentLine; 
        Order currentOrder;  
        
        while (scanner.hasNextLine()) {
            
            currentLine = scanner.nextLine();
           
            if (currentLine.startsWith("OrderNumber")) {
                continue;
            }
           
            currentOrder = unmarshallOrder(currentLine);
            
            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        //Clean up
        scanner.close();
    }
    
    private void writeOrders(String orderFile) throws FlooringMasteryException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(this.orderFolder+"\\"+orderFile));
        } catch (IOException e) {
            throw new FlooringMasteryException("Could not save order data",e);
        }
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
        String orderAsText;
        List <Order> orderList = this.getAllOrdersForADate(orderFile);
        for (Order currentOrder : orderList) {
            orderAsText = marshallOrder(currentOrder);
            out.println(orderAsText);
            out.flush();
        }
        //Clean up
        out.close();
    }
    
    
    private void writeDataExport() throws FlooringMasteryException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(DATA_EXPORT_FILE));
        } catch (IOException e) {
            throw new FlooringMasteryException("Could not save order data",e);
        }
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,Date");

        Map<String, List<Order>> exportData = getExportData();

        String exportDataAsText;
        
        for (String date : exportData.keySet()) {
            List<Order> currentOrderList = exportData.get(date);
            for (Order order : currentOrderList) {
                exportDataAsText = marshallOrder(order) + "," + date;
                out.println(exportDataAsText);
                out.flush();
            }
        }
        out.close();
    }
    
}



    
    
   
