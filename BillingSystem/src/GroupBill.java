import java.util.*;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.GregorianCalendar;

/**
 * 
 */
public class GroupBill extends Bill {

    /**
     * 
     */
    public GroupBill(Booking BillBooking) {
        super(BillBooking);
    }

    /**
     * 
     */
    private static final float GROUP_MULTIPLIER = 0.1f;

    /**
     * 
     */
    private int GroupSize;


    /**
     * 
     */
    @Override
    public void CalculateBill() {
        DaysStayed = (int) TimeUnit.MILLISECONDS.toDays(CheckOutDate - CheckInDate);
        float groupCharge = (GROUP_MULTIPLIER * GroupSize) + 1;
        TotalBill = STANDARD_RATE.multiply(new BigDecimal(DaysStayed*groupCharge));
        TotalBill.setScale(2, BigDecimal.ROUND_UP);
    }

    @Override
    public ArrayList<String> PrintBill() {
        ArrayList<String> formattedBill = new ArrayList<String>();
        formattedBill.add("GROUP BILL");
        formattedBill.add(String.format("Booking reference: %d.", BillBooking.GetBookingID()));
        formattedBill.add(String.format("Total nights: %d at £%s per night for %d guests.",
                     DaysStayed, STANDARD_RATE.multiply(new BigDecimal(0.1 * GroupSize)).toString()));
        formattedBill.add(String.format("Payable: %s.", TotalBill.toString(), GroupSize));
        //formattedBill.add(String.format("Billed to %s.", BillBooking.GetCustomer().GetCustomerName()));
        return formattedBill;
    }
}