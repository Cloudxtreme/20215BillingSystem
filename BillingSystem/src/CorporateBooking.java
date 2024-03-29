import java.util.ArrayList;

/**
 * Class for instantiating corporate booking objects
 */
public class CorporateBooking extends Booking {

    /**
     * Constructs a new instance of CorporateBooking
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

    public ArrayList<String> GenerateBill() {
        this.BookingBill.CalculateBill();
        return this.BookingBill.PrintBill();
    }

    public boolean isGroupBooking() {
        return false;
    }
    public boolean isCorporateBooking() {
        return true;
    }

}