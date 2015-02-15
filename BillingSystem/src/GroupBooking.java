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
        this.BookingBill = new GroupBill(this);
        this.GroupSize = GroupSize;
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

    public void GenerateBill() {
        this.BookingBill.CalculateBill();
    }

    /**
     * 
     */
    public int GetGroupSize() {
        return GroupSize;
    }

}