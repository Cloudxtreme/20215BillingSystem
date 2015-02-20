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
        if (CheckInDate < this.CheckOutDate) {
            this.CheckInDate = CheckInDate;
        } else {
            throw new java.lang.IllegalArgumentException("Check-in date cannot be on or after check-out date.");
        }
    }

    /**
     * @param CheckOutDate
     */
    public void EditCheckOutDate(long CheckOutDate) {
        if (CheckOutDate > this.CheckInDate) {
            this.CheckOutDate = CheckOutDate;
        } else {
            throw new java.lang.IllegalArgumentException("Check-out date cannot be on or before check-in date.");
        }
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