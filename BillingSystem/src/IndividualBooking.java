import java.util.*;
import java.sql.Date;
import java.math.BigDecimal;

/**
 * 
 */
public class IndividualBooking extends Booking {

    /**
     * 
     */
    public IndividualBooking(int BookingID, int CustomerID, long CheckInDate, long CheckOutDate) {
        super(BookingID, CustomerID, CheckInDate, CheckOutDate);
        this.BookingBill = new Bill(this);
    }

    public ArrayList<String> GenerateBill() {
        this.BookingBill.CalculateBill();
        return this.BookingBill.PrintBill();
    }

    public boolean isIndividualBooking() {
        return true;
    }
    public boolean isGroupBooking() {
        return false;
    }
    public boolean isCorporateBooking() {
        return false;
    }

    public int getGroupSize() {
        return 1;
    }

}