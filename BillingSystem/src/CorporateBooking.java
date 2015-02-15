import java.util.*;
import java.math.BigDecimal;

/**
 * 
 */
public class CorporateBooking extends Booking {

    /**
     * @param BookingID
     * @param CompanyName
     * @param CustomerID
     * @param CheckInDate
     * @param CheckOutDate
     */
    public CorporateBooking(int BookingID, String CompanyName, int CustomerID, long CheckInDate, long CheckOutDate) {
        super(BookingID, CustomerID, CheckInDate, CheckOutDate);
        this.BookingBill = new CorporateBill(this);
        this.CompanyName = CompanyName;
    }

    /**
     * 
     */
    private String CompanyName;


    /**
     * @param CompanyName
     */
    public void EditCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    /**
     * 
     */
    public String GetCompanyName() {
       return this.CompanyName;
    }

    public void GenerateBill() {
        this.BookingBill.CalculateBill();
    }

}