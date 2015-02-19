import java.util.*;
import java.sql.Date;
import java.math.BigDecimal;

/**
 * 
 */
public class GroupBooking extends Booking {

    /**
     * @param BookingID
     * @param CustomerID
     * @param GroupSize
     * @param CheckInDate
     * @param CheckOutDate
     */
    public GroupBooking(int BookingID, int CustomerID, int GroupSize, long CheckInDate, long CheckOutDate) {
        super(BookingID, CustomerID, CheckInDate, CheckOutDate);
        this.GroupSize = GroupSize;
        this.BookingBill = new GroupBill(this);
    }

    /**
     * 
     */
    private int GroupSize;


    /**
     * @param GroupSize
     */
    public void EditGroupSize(int GroupSize) {
        // TODO implement here
    }

    public ArrayList<String> GenerateBill() {
        this.BookingBill.CalculateBill();
        return this.BookingBill.PrintBill();
    }

    /**
     * 
     */
    public int getGroupSize() {
        return GroupSize;
    }

    public boolean isIndividualBooking() {
        return false;
    }
    public boolean isGroupBooking() {
        return true;
    }
    public boolean isCorporateBooking() {
        return false;
    }
}