/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.dao;

import mmala.flooringmastery.dto.Tax;
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
public class FlooringMasteryTaxDaoFileImpl implements FlooringMasteryTaxDao {
    private Map <String, Tax> taxes = new HashMap<>();
    private final String DELIMITER = ",";
    private final String TAXES_FILE;
    
    public FlooringMasteryTaxDaoFileImpl() {
        //this.ORDER_FILE = "order.txt";
        this.TAXES_FILE = "C:\\Users\\18437\\training_repos\\mastery-project-assessment-flooring-mastery-malakhavam\\SampleFileData\\Data\\Taxes.txt";
    }
    
 
    public FlooringMasteryTaxDaoFileImpl (String taxesTextFile) {
        this.TAXES_FILE = taxesTextFile;
    }    
    
    @Override
    public List<Tax> getAllTaxes()throws FlooringMasteryException {
        loadTaxes();
        return new ArrayList(taxes.values());
    }
    
    
    @Override 
    public Tax getTax(String stateAbbreviationInput)throws FlooringMasteryException {
    loadTaxes();
        return taxes.get(stateAbbreviationInput);
    }
    
    private Tax unmarshallTax(String taxAsText) throws FlooringMasteryException {
           
        
        String[] taxTokens = taxAsText.split(DELIMITER);
        String stateAbbreviation = taxTokens[0];
        
        Tax taxFromFile = new Tax(stateAbbreviation);
        
        taxFromFile.setStateName(taxTokens[1]);
        taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));
        return taxFromFile;
    }
    
    private void loadTaxes() throws FlooringMasteryException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryException(
            "-_- Could not load tax data into memory.", e);
        }
        String currentLine;
        Tax currentTax;
        
        int iteration = 0;
        
        while (scanner.hasNextLine()) {
            // skip first line, bc it is header
            if (iteration == 0) {
                String headings = scanner.nextLine();
                iteration ++;
                continue;
            }
        
            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);
            
            
            taxes.put(currentTax.getStateAbbr(), currentTax);
            iteration++;
        }
        //Clean up
        scanner.close();
        }

    
    
    
}
