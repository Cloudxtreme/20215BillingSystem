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

    BigDecimal weekdayBill;
    BigDecimal weekendBill;

    /**
     * 
     */
    @Override
    public void CalculateBill() {
        Date checkIn = new Date(CheckInDate);
        Date checkOut = new Date(CheckOutDate);
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date(CheckOutDate-CheckInDate));
        int days = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(checkIn);
        for (int i=1; i < days; i++) {
            System.out.println(DaysStayed);
            DaysStayed++;
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == 6 || day == 7) {
                WeekendDays++;
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        weekdayBill = STANDARD_RATE.multiply(new BigDecimal((DaysStayed - WeekendDays) * (1-CORPORATE_MULTIPLIER))).setScale(2,BigDecimal.ROUND_UP);
        weekendBill = STANDARD_RATE.multiply(new BigDecimal(WeekendDays * (1-WEEKEND_MULTIPLIER))).setScale(2,BigDecimal.ROUND_UP);
        TotalBill = weekdayBill.add(weekendBill);
        TotalBill.setScale(2,BigDecimal.ROUND_UP);

    }

    @Override
    public ArrayList<String> PrintBill() {
        ArrayList<String> formattedBill = new ArrayList<String>();
        formattedBill.add("CORPORATE BILL");
        formattedBill.add(String.format("Booking reference: %d.", BillBooking.GetBookingID()));
        formattedBill.add(String.format("Total nights: %d weekdays £%s per night and %d weekend days at £%s per night.",
                (DaysStayed-WeekendDays), STANDARD_RATE.multiply(new BigDecimal(1 - CORPORATE_MULTIPLIER)).toString(),
                WeekendDays, STANDARD_RATE.multiply(new BigDecimal(WEEKEND_MULTIPLIER))).toString());
        formattedBill.add(String.format("Payable: £%s.", TotalBill.toString()));
        return formattedBill;
    }


}