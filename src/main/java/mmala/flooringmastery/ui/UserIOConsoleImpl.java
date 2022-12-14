/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 *
 * @author 18437
 */
public class UserIOConsoleImpl implements UserIO {
    final private Scanner console = new Scanner(System.in);

    
    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    
    @Override
    public String readString(String msgPrompt) {
        System.out.println(msgPrompt);
        return console.nextLine();
    }

   
    @Override
    public int readInt(String msgPrompt) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                // print the Prompt 
                String stringValue = this.readString(msgPrompt);
                // Get the input line
                num = Integer.parseInt(stringValue); 
                invalidInput = false; 
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    
    @Override
    public int readInt(String msgPrompt, int min, int max) {
        int result;
        do {
            result = readInt(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

 
    @Override
    public long readLong(String msgPrompt) {
        while (true) {
            try {
                return Long.parseLong(this.readString(msgPrompt));
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    
    @Override
    public long readLong(String msgPrompt, long min, long max) {
        long result;
        do {
            result = readLong(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

   
    @Override
    public float readFloat(String msgPrompt) {
        while (true) {
            try {
                return Float.parseFloat(this.readString(msgPrompt));
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

  
    @Override
    public float readFloat(String msgPrompt, float min, float max) {
        float result;
        do {
            result = readFloat(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

   
    @Override
    public double readDouble(String msgPrompt) {
        while (true) {
            try {
                return Double.parseDouble(this.readString(msgPrompt));
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

   
    @Override
    public double readDouble(String msgPrompt, double min, double max) {
        double result;
        do {
            result = readDouble(msgPrompt);
        } while (result < min || result > max);
        return result;
    }

    
    @Override
    public BigDecimal readBigDecimal(String prompt) {
        BigDecimal bigDecimalInput = null;
        boolean invalidInput = true;
            while (invalidInput) {
            try {
                System.out.println(prompt);
                String stringInput = console.nextLine();
                bigDecimalInput = new BigDecimal(stringInput); // if its not a number, it'll break
                invalidInput = false;
            } catch (NumberFormatException e) {
                
                this.print("Input error. Please enter numbers only again."); 
            }
        }
        return bigDecimalInput;
    }    
    
    @Override
     public LocalDate readDate (String prompt){
         LocalDate date = null;
         boolean invalidInput = true;
         
         while (invalidInput) {
             try {
                 System.out.println(prompt); 
                 String stringInput = console.nextLine();
                 date = LocalDate.parse(stringInput); //if the date is in wrong format -> break
                 invalidInput = false; 
             } catch (DateTimeException e) {
                 this.print("Input error. Date is not in the correct format");
             }
         }
         return date;
     }
    
}
