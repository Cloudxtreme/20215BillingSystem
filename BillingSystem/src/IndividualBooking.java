import java.util.ArrayList;

/**
 * Class for instantiating individual booking objects
 */
public class IndividualBooking extends Booking {

    /**
     * Constructs a new instance of IndividualBooking
     * @param BookingID
     * @param CustomerID
     * @param CheckInDate
     * @param CheckOutDate
     */
    public IndividualBooking(int BookingID, int CustomerID, long CheckInDate, long CheckOutDate) {
        super(BookingID, CustomerID, CheckInDate, CheckOutDate);
        this.BookingBill = new Bill(this);
    }

    public ArrayList<String> GenerateBill() {
        this.BookingBill.CalculateBill();
        return this.BookingBill.PrintBill();
    }

    public boolean isGroupBooking() {
        return false;
    }
    public boolean isCorporateBooking() {
        return false;
    }
}