import java.util.*;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

/**
 * 
 */
public class CorporateBill extends Bill {

    /**
     * 
     */
    public CorporateBill(CorporateBooking BillBooking) {
        super(BillBooking);
    }

    /**
     * 
     */
    private static final float CORPORATE_MULTIPLIER = 0.2f;

    /**
     * 
     */
    private static final float WEEKEND_MULTIPLIER = 0.5f;

    /**
     * 
     */
    private int WeekendDays;



    /**
     * 
     */
    @Override
    public void CalculateBill() {
        Date checkIn = new Date(CheckInDate);
        Date checkOut = new Date(CheckOutDate);
        Calendar cal = new GregorianCalendar();
        cal.setTime(checkIn);
        do {
            DaysStayed++;
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                WeekendDays++;
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }  while (cal.before(checkOut));

        BigDecimal weekdayBill = STANDARD_RATE.multiply(new BigDecimal((DaysStayed - WeekendDays) * CORPORATE_MULTIPLIER));
        BigDecimal weekendBill = STANDARD_RATE.multiply(new BigDecimal(WeekendDays * WEEKEND_MULTIPLIER));
        TotalBill = weekdayBill.add(weekendBill);
        TotalBill.setScale(2,BigDecimal.ROUND_UP);

    }

    @Override
    public ArrayList<String> PrintBill() {
        ArrayList<String> formattedBill = new ArrayList<String>();
        formattedBill.add("CORPORATE BILL");
        formattedBill.add(String.format("Booking reference: %d.", BillBooking.GetBookingID()));
        formattedBill.add(String.format("Total nights: %d at £%s per night.", DaysStayed, STANDARD_RATE.toString()));
        formattedBill.add(String.format("Payable: %s.", TotalBill.toString(), BillBooking));
        //formattedBill.add(String.format("Billed to %s.", BillBooking.GetCustomer().GetCustomerName()));
        return formattedBill;
    }


}