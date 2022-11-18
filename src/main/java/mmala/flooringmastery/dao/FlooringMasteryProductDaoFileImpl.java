/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.dao;

import mmala.flooringmastery.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author 18437
 */
public class FlooringMasteryProductDaoFileImpl implements FlooringMasteryProductDao{
    private Map<String, Product> products = new HashMap<>();
    private final String DELIMITER = ",";
    private final String PRODUCTS_FILE;
    
    
    public FlooringMasteryProductDaoFileImpl() {
        this.PRODUCTS_FILE = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileData\\Data\\Products.txt";
    }
               
    //Constructor for testing
    public FlooringMasteryProductDaoFileImpl (String productsTextFile) {
        this.PRODUCTS_FILE = productsTextFile;
    }    
    
    @Override
    public List<Product> getAllProducts()throws FlooringMasteryException {
        loadProducts();
        return new ArrayList(products.values());
    }    
    
    @Override 
    public Product getProduct (String productType) throws FlooringMasteryException {
        loadProducts();
        return products.get(productType);
    }
    
    
    private String marshallProduct(Product aProduct) {
        //Get product type, cost per sq ft and labor cost per sq ft.
        String productAsText = aProduct.getProductType();
        productAsText += aProduct.getCostPerSquareFoot().toString();
        productAsText += aProduct.getLaborCostPerSquareFoot().toString();
        return productAsText;
    }
    
    
    private Product unmarshallProduct(String productAsText) {
       
        String [] productTokens = productAsText.split(DELIMITER);
        
        String productType = productTokens[0];
        Product productFromFile = new Product(productType);
        
        productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));

        return productFromFile;
    }
    
    private void loadProducts() throws FlooringMasteryException {
        //Open File
        Scanner scanner;
        try {
            scanner = new Scanner(
            new BufferedReader(
            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryException(
            "-_- Could not load product data into memory",e);
        }
        
        //Read from file
        String currentLine;
        Product currentProduct; 
        
        int iteration = 0;
        
        while (scanner.hasNextLine()) {
            // skip the first line, bc it is header
            if (iteration == 0) {
                String headings = scanner.nextLine();
                iteration ++;
                continue;
            }
           
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            
            products.put(currentProduct.getProductType(), currentProduct);
            iteration++;
        }
        //Clean up
        scanner.close();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
