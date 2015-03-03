import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * Class for instantiating bill objects (requires a booking)
 */
public class Bill {

    /**
     * Constructs a new instance of Bill from a Booking
     */
    public Bill(Booking BillBooking) {
        this.BillBooking = BillBooking;
        this.CheckInDate = BillBooking.GetCheckInDate();
        this.CheckOutDate = BillBooking.GetCheckOutDate();
    }

    protected static final BigDecimal STANDARD_RATE = new BigDecimal("100.00").setScale(2,BigDecimal.ROUND_HALF_EVEN);
    protected BigDecimal TotalBill;
    protected Booking BillBooking;
    protected long CheckInDate;
    protected long CheckOutDate;
    protected int DaysStayed;


    /**
     * 
     */
    public void CalculateBill() {
        DaysStayed = (int)TimeUnit.MILLISECONDS.toDays(CheckOutDate - CheckInDate);
        TotalBill = STANDARD_RATE.multiply(new BigDecimal(DaysStayed));
        TotalBill.setScale(2,BigDecimal.ROUND_HALF_EVEN);
    }

    public ArrayList<String> PrintBill() {
        ArrayList<String> formattedBill = new ArrayList<String>();
        formattedBill.add("INDIVIDUAL BILL");
        formattedBill.add(String.format("Customer ID: %d", BillBooking.GetCustomerID()));
        formattedBill.add(String.format("Booking reference: %d", BillBooking.GetBookingID()));
        formattedBill.add(String.format("Total nights: %d at £%s per night.", DaysStayed, STANDARD_RATE.toString()));
        formattedBill.add(String.format("Payable: £%s", TotalBill.toString()));
        return formattedBill;
    }
}