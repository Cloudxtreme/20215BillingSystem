import java.util.*;
import java.math.BigDecimal;

/**
 * 
 */
public abstract class Booking {

    /**
     * @param BookingID
     * @param CustomerID
     * @param CheckInDate
     * @param CheckOutDate
     */
    public Booking(int BookingID, int CustomerID, long CheckInDate, long CheckOutDate) {
        this.BookingID = BookingID;
        this.CustomerID = CustomerID;
        this.CheckInDate = CheckInDate;
        this.CheckOutDate = CheckOutDate;
        //this.BookingCustomer.AddBooking(this);
    }

    protected int BookingID;
    protected long CheckInDate;
    protected long CheckOutDate;
    protected int CustomerID;
    protected Bill BookingBill;

    /**
     * @param CheckInDate
     */
    public void EditCheckInDate(long CheckInDate) {
            this.CheckInDate = CheckInDate;
    }

    /**
     * @param CheckOutDate
     */
    public void EditCheckOutDate(long CheckOutDate) {
            this.CheckOutDate = CheckOutDate;
    }

    /**
     * 
     */
    public long GetCheckInDate() {
        return this.CheckInDate;
    }

    /**
     * 
     */
    public long GetCheckOutDate() {
        return this.CheckOutDate;
    }

    /**
     * 
     */
    public abstract ArrayList<String> GenerateBill();
    /**
     * 
     */
    public int GetBookingID() {
        return this.BookingID;
    }

    public int GetCustomerID() {
        return this.CustomerID;
    }

    public abstract boolean isIndividualBooking();
    public abstract boolean isGroupBooking();
    public abstract boolean isCorporateBooking();
}