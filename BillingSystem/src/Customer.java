import java.util.*;
import java.sql.Date;
import java.math.BigDecimal;
import java.lang.IllegalArgumentException;

/**
 * Class for instantiating customer objects
 */
public class Customer {

    /**
     * Constructs a new instance of Customer
     * @param CustomerID
     * @param CustomerName
     */
    public Customer(int CustomerID, String CustomerName, String CustomerPhone, String CustomerAddress, String CustomerPostCode) {
        this.CustomerID = CustomerID;
        this.CustomerName = CustomerName;
        this.CustomerPhone = CustomerPhone;
        this.CustomerAddress = CustomerAddress;
        this.CustomerPostCode = CustomerPostCode;
    }

    private int CustomerID;
    private String CustomerName;
    private String CustomerPhone;
    private String CustomerAddress;
    private String CustomerPostCode;
    private ArrayList<Booking> CustomerBookings = new ArrayList<Booking>();


    /**
     * @param CustomerName
     */
    public void EditName(String CustomerName) {
        if (CustomerName.length() > 0) {
            this.CustomerName = CustomerName;
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    /**
     * @param CustomerPhone
     */
    public void EditPhone(String CustomerPhone) {
        this.CustomerPhone = CustomerPhone;
    }

    /**
     * @param CustomerAddress
     */
    public void EditCustomerAddress(String CustomerAddress) {
        this.CustomerAddress = CustomerAddress;
    }

    /**
     * @param CustomerPostCode
     */
    public void EditCustomerPostCode(String CustomerPostCode) {
        this.CustomerPostCode = CustomerPostCode;
    }

    /**
     * 
     */
    public int GetCustomerID() {
        return CustomerID;
    }

    /**
     * 
     */
    public String GetCustomerName() {
        return CustomerName;
    }

    /**
     * 
     */
    public String GetCustomerPhone() {
        return CustomerPhone;
    }

    /**
     * 
     */
    public String GetCustomerAddress() {
        return CustomerAddress;
    }

    /**
     * 
     */
    public String GetCustomerPostCode() {
        return CustomerPostCode;
    }

    /**
     *
     */
    public void AddBooking(Booking newBooking) {
        CustomerBookings.add(newBooking);
    }

    /**
     *
     */
    public void RemoveBooking(Booking removeBooking) {
        CustomerBookings.remove(removeBooking);
    }

    /**
     *
     */
    public ArrayList<Booking> GetBookings() {
        return CustomerBookings;
    }

}