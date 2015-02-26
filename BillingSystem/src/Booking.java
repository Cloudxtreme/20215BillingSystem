import java.util.*;
import java.math.BigDecimal;

/**
 * Class for instantiating booking objects
 */
public abstract class Booking {

    /**
     * Constructs a new instance of Booking
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
    public int GetBookingID() {
        return this.BookingID;
    }

    public int GetCustomerID() {
        return this.CustomerID;
    }

    public abstract ArrayList<String> GenerateBill();
    public abstract boolean isGroupBooking();
    public abstract boolean isCorporateBooking();
}