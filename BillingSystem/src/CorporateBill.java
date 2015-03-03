import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Class for instantiating corporate bill objects
 */
public class CorporateBill extends Bill {

    /**
     * Constructs a new instance of CorporateBill from a CorporateBooking
     */
    public CorporateBill(CorporateBooking BillBooking) {
        super(BillBooking);
    }


    private static final float CORPORATE_MULTIPLIER = 0.2f;
    private static final float WEEKEND_MULTIPLIER = 0.5f;
    private int WeekendDays;
    private static final BigDecimal WEEKDAY_RATE = STANDARD_RATE.multiply(new BigDecimal(1 - CORPORATE_MULTIPLIER))
            .setScale(2,BigDecimal.ROUND_HALF_EVEN);
    private static final BigDecimal WEEKEND_RATE = STANDARD_RATE.multiply(new BigDecimal(1 - WEEKEND_MULTIPLIER))
            .setScale(2,BigDecimal.ROUND_HALF_EVEN);
    BigDecimal weekdayBill;
    BigDecimal weekendBill;


    /**
     * 
     */
    @Override
    public void CalculateBill() {
        Calendar cal = new GregorianCalendar();
        DaysStayed = (int) TimeUnit.MILLISECONDS.toDays(CheckOutDate - CheckInDate);

        cal.setTime(new Date(CheckInDate));
        for (int i=1; i < DaysStayed+1 ; i++) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == 6 || day == 7) {
                WeekendDays++;
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        weekdayBill = WEEKDAY_RATE.multiply(new BigDecimal(DaysStayed - WeekendDays))
                .setScale(2, BigDecimal.ROUND_CEILING);
        weekendBill = WEEKEND_RATE.multiply(new BigDecimal(WeekendDays))
                .setScale(2, BigDecimal.ROUND_CEILING);
        TotalBill = weekdayBill.add(weekendBill)
                .setScale(2, BigDecimal.ROUND_CEILING);

    }

    @Override
    public ArrayList<String> PrintBill() {
        ArrayList<String> formattedBill = new ArrayList<String>();
        formattedBill.add("CORPORATE BILL");
        formattedBill.add(String.format("Customer ID: %d", BillBooking.GetCustomerID()));
        formattedBill.add(String.format("Booking reference: %d", BillBooking.GetBookingID()));
        formattedBill.add(String.format("Total nights: %d weekday(s) at £%s per night. %d weekend day(s) at £%s.",
                (DaysStayed - WeekendDays), WEEKDAY_RATE, WeekendDays, WEEKEND_RATE));
        formattedBill.add(String.format("Payable: £%s", TotalBill.toString()));
        return formattedBill;
    }
}