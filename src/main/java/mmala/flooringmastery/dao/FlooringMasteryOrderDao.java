/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.dao;

import mmala.flooringmastery.dto.Order;
import mmala.flooringmastery.service.FlooringMasteryOrderFileNotExistException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 18437
 */
public interface FlooringMasteryOrderDao {
    
    List <Order> getAllOrdersForADate(String orderFile)throws FlooringMasteryException;
    
    List<Integer> getAllOrderNumsForADate(String orderFile) throws FlooringMasteryException;
    
    Order getOrder (String orderFile, int orderNum) throws FlooringMasteryException;
    
    Order addOrderToExistingFile(String orderFile, int orderNumber, Order order) throws FlooringMasteryException;
    
    Order addOrderToNewFile(String orderFile, int orderNumber, Order order) throws FlooringMasteryException;
    
    Order removeOrder(String orderFile, int orderNumber) throws FlooringMasteryException;

    String [] listAllOrderFiles();
    
    List<Order> getAllOrders() throws FlooringMasteryException;

    List<Integer> getAllOrderNums() throws FlooringMasteryException;
    
    Order editOrder(String orderFile, Order updatedOrder)throws FlooringMasteryException;
    
    Map<String,List<Order>> getExportData() throws FlooringMasteryException;
    
    void exportAllData() throws FlooringMasteryException;
    
    public void deleteOrderFile(String fileName)throws FlooringMasteryOrderFileNotExistException;
    
}

