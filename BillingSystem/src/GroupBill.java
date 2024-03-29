import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Class for instantiating group bill objects
 */
public class GroupBill extends Bill {

    /**
     * Constructs a new instance of GroupBill from a GroupBooking
     */
    public GroupBill(GroupBooking BillBooking) {
        super(BillBooking);
        this.GroupSize = ((GroupBooking)BillBooking).getGroupSize();
        adjustedRate = STANDARD_RATE.multiply(new BigDecimal(1 + GROUP_MULTIPLIER * (GroupSize-1))).setScale(2,BigDecimal.ROUND_HALF_EVEN);
    }

    private static final float GROUP_MULTIPLIER = 0.1f;
    private int GroupSize;
    private BigDecimal adjustedRate;

    /**
     * 
     */
    @Override
    public void CalculateBill() {
        DaysStayed = (int) TimeUnit.MILLISECONDS.toDays(CheckOutDate - CheckInDate);
        TotalBill = adjustedRate.multiply(new BigDecimal(DaysStayed)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
    }

    @Override
    public ArrayList<String> PrintBill() {
        ArrayList<String> formattedBill = new ArrayList<String>();
        formattedBill.add("GROUP BILL");
        formattedBill.add(String.format("Customer ID: %d", BillBooking.GetCustomerID()));
        formattedBill.add(String.format("Booking reference: %d", BillBooking.GetBookingID()));
        formattedBill.add(String.format("Total nights: %d at £%s per night (%d guests.)",
                DaysStayed, adjustedRate.toString(), GroupSize));
        formattedBill.add(String.format("Payable: £%s", TotalBill.toString()));
        return formattedBill;
    }
}