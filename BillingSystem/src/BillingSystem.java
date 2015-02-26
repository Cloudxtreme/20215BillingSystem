import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;


/**
 * 
 */
public class BillingSystem {

    /**
     * Constructs a new instance of BillingSystem with no customers or bookings
     */
    public BillingSystem() {
        bookingIDCount = 0;
        customerIDCount = 0;
       allBookings = new HashMap<Integer, Booking>();
       customers = new HashMap<Integer, Customer>();
    }

    // Instance variable for billing system
    private static BillingSystem sys = new BillingSystem();
    // Total number of past bookings
    private static int bookingIDCount;
    // Total number of past customers
    private static int customerIDCount;
    // Every booking in the system
    private HashMap<Integer, Booking> allBookings = new HashMap<Integer, Booking>();
    // Every customer in the system
    private HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
    // The date format accepted for I/O
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    /**
     * Populates the billingSystem object with random customers and bookings, launches the main menu
     */
    public static void main(String[] args) {
        BillingSystemUtils.populateSystemData(sys);
        while (true) {
            sys.mainMenu();
        }
    }

    /**
     * Launches the main menu and waits for user input
     */
    public void mainMenu() {
        Scanner s = new Scanner(System.in);
        int menuChoice = 0;
        BillingSystemUtils.menu();
        menuSelection(getIntInput(9, "Menu choice (1-8): ", "Invalid menu choice."));
    }

    /**
     * Performs system actions based on the main menu choice the user made
     * @param menuChoice The validated integer menu selection (0-8)
     */
    public void menuSelection(int menuChoice) {
        switch (menuChoice) {
            case 1:
                sys.AddNewCustomer();
                break;
            case 2:
                sys.AddNewCustomer();
                AddNewBooking(CustomerLookupByID(customerIDCount).GetCustomerID());
                break;
            case 3:
                sys.SelectBookingCustomer();
                break;
            case 4:
                sys.getAllBookings();
                break;
            case 5:
                sys.CustomerLookupByName();
                break;
            case 6:
                sys.ManageBooking();
                break;
            case 7:
                sys.GenerateBill();
                break;
            case 8:
                System.exit(0);
                break;
        }
    }


    /**
     * Creates a new individual booking
     * @param CustomerID
     * @param CheckInDate 
     * @param CheckOutDate
     */
    public void AddNewBooking(int CustomerID, long CheckInDate, long CheckOutDate) {
        IndividualBooking b = new IndividualBooking(++bookingIDCount, CustomerID, CheckInDate, CheckOutDate);
        sys.CustomerLookupByID(CustomerID).AddBooking(b);
        sys.allBookings.put(b.GetBookingID(), b);
    }

    /**
     * Creates a new corporate booking
     * @param CompanyName
     * @param CustomerID
     * @param CheckInDate
     * @param CheckOutDate
     */
    public void AddNewBooking(String CompanyName, int CustomerID, long CheckInDate, long CheckOutDate) {
        CorporateBooking b = new CorporateBooking(++bookingIDCount, CompanyName, CustomerID, CheckInDate, CheckOutDate);
        sys.CustomerLookupByID(CustomerID).AddBooking(b);
        sys.allBookings.put(b.GetBookingID(), b);
    }

    /**
     * Creates a new group booking
     * @param CustomerID
     * @param GroupSize
     * @param CheckInDate
     * @param CheckOutDate
     */
    public void AddNewBooking(int CustomerID, int GroupSize, long CheckInDate, long CheckOutDate) {
        GroupBooking b = new GroupBooking(++bookingIDCount, CustomerID, GroupSize, CheckInDate, CheckOutDate);
        sys.CustomerLookupByID(CustomerID).AddBooking(b);
        sys.allBookings.put(b.GetBookingID(), b);
    }

    /**
     * Gets a user selected customer ID to create a booking for that customer
     */
    public void SelectBookingCustomer() {
        ArrayList<Customer> results = sys.CustomerLookupByName(getInput("Customer name: "));
        if (results.size()==0) {
            BillingSystemUtils.confirmContinue("No customers found.");
        } else {
            String[][] customerLines = new String[results.size()][];
            int customerCount = 0;
            for (Customer c: results) {
                String id = String.valueOf(c.GetCustomerID());
                String name = c.GetCustomerName();
                String postCode = c.GetCustomerPostCode();
                String[] customerData = new String[]{id, name, postCode};
                customerLines[customerCount++] = customerData;
            }
            BillingSystemUtils.printTable(new String[]{"ID", "Customer", "Post Code"}, customerLines);
            BillingSystemUtils.confirmContinue();
            int id = getIntInput(customerIDCount +1, "Enter customer ID to book: ", "Invalid customer ID.");
            sys.AddNewBooking(id);
        }
    }

    /**
     * Gets user input to create a booking for a specified customer
     * @param CustomerID
     */
    public void AddNewBooking(int CustomerID) {
        long checkInDate = getDateInput("Enter check-in date dd/mm/yy: ");
        long checkOutDate = getDateInput("Enter check-out date dd/mm/yy: ",
                "Check-out must be a valid date after check-in",
                Long.MAX_VALUE, checkInDate);
        int bookingType = getIntInput(4, "1:  Individual, 2: Group, 3: Corporate. ");
        switch (bookingType) {
            case 1:
                sys.AddNewBooking(CustomerID, checkInDate, checkOutDate);
                break;
            case 2:
                int groupSize = getIntInput(6, "Enter group size: ", "Invalid group size (max 5).");
                sys.AddNewBooking(CustomerID, groupSize, checkInDate, checkOutDate);
                break;
            case 3:
                String CompanyName = getInput("Enter company name: ");
                sys.AddNewBooking(CompanyName, CustomerID, checkInDate, checkOutDate);
                break;
        }
    }

    public void EditBooking(int BookingID) {
        Booking currentBooking = sys.allBookings.get(BookingID);
        BillingSystemUtils.headingText("Current check-in date is " + dateFormat(currentBooking.GetCheckInDate()));
        System.out.println();
        long checkInDate = getDateInput("Enter new check-in date dd/mm/yy: ");
        BillingSystemUtils.headingText("Current check-out date is " + dateFormat(currentBooking.GetCheckOutDate()));
        System.out.println();
        long checkOutDate = getDateInput("Enter new check-out date dd/mm/yy: ",
                "Check-out must be a valid date after check-in",
                Long.MAX_VALUE, checkInDate);
        currentBooking.EditCheckInDate(checkInDate);
        currentBooking.EditCheckOutDate(checkOutDate);
        if (currentBooking.isGroupBooking()) {
            BillingSystemUtils.headingText("Current group size is: " + ((GroupBooking)currentBooking).getGroupSize());
            System.out.println();
            int groupSize = getIntInput(6, "Enter new group size: ", "Invalid group size (max 5).");
            ((GroupBooking)currentBooking).EditGroupSize(groupSize);
        } else if (currentBooking.isCorporateBooking()) {
            BillingSystemUtils.headingText("Current company name is: " + ((CorporateBooking)currentBooking).GetCompanyName());
            System.out.println();
            String CompanyName = getInput("Enter company name: ");
            ((CorporateBooking)currentBooking).EditCompanyName(CompanyName);
        }
    }

    public int getCustomerIDCount() {
        return sys.customerIDCount;
    }

    private String dateFormat(long millis) {
        return dateFormat.format(new Date(millis));
    }

    public int getIntInput(int upperBound, String inputText) {
        return getIntInput(upperBound, inputText, BillingSystemUtils.DEFAULT_ERR);
    }

    public int getIntInput(int upperBound, String inputText, String errorText) {
        int input;
        Scanner s = new Scanner(System.in);
        while (true) {
            BillingSystemUtils.headingText(inputText);
            try {
                input = s.nextInt();
                s.nextLine();
                if (input < upperBound && input > 0) {
                    BillingSystemUtils.clearConsole();
                    return input;
                }
            } catch (InputMismatchException e) {}
            BillingSystemUtils.confirmContinue(errorText);
        }
    }

    public String getInput(String inputText) {
        return getInput(inputText, BillingSystemUtils.DEFAULT_ERR);
    }

    public String getInput(String inputText, String errorText) {
        String input;
        Scanner s = new Scanner(System.in);
        while (true) {
            BillingSystemUtils.headingText(inputText);
            input = s.nextLine();
            if (input.length() > 0) {
                BillingSystemUtils.clearConsole();
                return input;
            }
            BillingSystemUtils.confirmContinue(errorText);
        }
    }

    public long getDateInput(String inputText) {
        return getDateInput(inputText, BillingSystemUtils.DEFAULT_ERR, Long.MAX_VALUE, 0l);
    }

    public long getDateInput(String inputText, String errorText, long before, long after) {
        long input;
        Scanner s = new Scanner(System.in);
        while (true) {
            BillingSystemUtils.headingText(inputText);
            try {
                input = dateFormat.parse(s.nextLine()).getTime();
                if (input < before && input > after) {
                    BillingSystemUtils.clearConsole();
                    return input;
                }
            } catch (ParseException e) {}
            BillingSystemUtils.confirmContinue(errorText);
        }
    }


    public void GenerateBill(int bookingID) {
        Booking b = sys.BookingLookupByID(bookingID);
        ArrayList<String> bill = b.GenerateBill();
        BillingSystemUtils.headingText("Billed: " + CustomerLookupByID(b.GetCustomerID()).GetCustomerName());
        for (String billLine: bill) {
            BillingSystemUtils.bodyText(billLine);
        }
        BillingSystemUtils.confirmContinue();
    }

    public void GenerateBill() {
        ArrayList<Customer> results = sys.CustomerLookupByName(getInput("Customer name: "));
        if (results.size()==0) {
            BillingSystemUtils.confirmContinue("No customers found");
        } else {
            int totalBookings = 0;
            for (Customer c: results) {
                totalBookings += c.GetBookings().size();
            }
            String[][] bookingLines = new String[totalBookings][];
            int bookingCount = 0;
            for (Customer c: results) {
                String name = c.GetCustomerName();
                String postCode = c.GetCustomerPostCode();
                for (Booking b: c.GetBookings()) {
                    String bookingID = String.valueOf(b.GetBookingID());
                    String checkIn = dateFormat.format(new Date(b.GetCheckInDate()));
                    String checkOut = dateFormat.format(new Date(b.GetCheckOutDate()));
                    String bookingType = b.isGroupBooking()? "Group: "+ ((GroupBooking)b).getGroupSize() :
                            b.isCorporateBooking()? "Corp: "+ ((CorporateBooking)b).GetCompanyName() : "Individual";
                    String[] bookingData = new String[]{name, postCode, bookingID, bookingType, checkIn, checkOut};
                    bookingLines[bookingCount++] = bookingData;
                }
            }
            if (bookingCount > 0) {
                BillingSystemUtils.printTable(
                        new String[]{"Customer", "Post Code", "Booking", "Type", "Check-In", "Check-Out"}, bookingLines);
                int id = getIntInput(bookingIDCount +1, "Enter booking ID to generate bill: ", "Invalid booking ID.");
                sys.GenerateBill(id);
            }
        }
    }


    /**
     * @param BookingID
     */
    public void RemoveBooking(int BookingID) {
        Booking b = BookingLookupByID(BookingID);
        sys.CustomerLookupByID(b.GetCustomerID()).RemoveBooking(b);
        sys.allBookings.remove(BookingID);
    }

    public void ManageBooking() {
        ArrayList<Customer> results = sys.CustomerLookupByName(getInput("Customer name: "));
        if (results.size()==0) {
            BillingSystemUtils.confirmContinue("No customers found");
        } else {
            int totalBookings = 0;
            for (Customer c: results) {
                totalBookings += c.GetBookings().size();
            }
            String[][] bookingLines = new String[totalBookings][];
            int bookingCount = 0;
            for (Customer c: results) {
                String name = c.GetCustomerName();
                String postCode = c.GetCustomerPostCode();
                for (Booking b: c.GetBookings()) {
                    String bookingID = String.valueOf(b.GetBookingID());
                    String checkIn = dateFormat.format(new Date(b.GetCheckInDate()));
                    String checkOut = dateFormat.format(new Date(b.GetCheckOutDate()));
                    String bookingType = b.isGroupBooking()? "Group: "+ ((GroupBooking)b).getGroupSize() :
                            b.isCorporateBooking()? "Corp: "+ ((CorporateBooking)b).GetCompanyName() : "Individual";
                    String[] bookingData = new String[]{name, postCode, bookingID, bookingType, checkIn, checkOut};
                    bookingLines[bookingCount++] = bookingData;
                }
            }
            if (bookingCount > 0) {
                BillingSystemUtils.printTable(
                        new String[]{"Customer", "Post Code", "Booking", "Type", "Check-In", "Check-Out"}, bookingLines);
                int id = getIntInput(bookingIDCount +1, "Enter booking ID to edit it: ", "Invalid booking ID.");
                int editOrRemove = getIntInput(3, "Enter 1 to edit booking or 2 to remove booking: ");
                switch (editOrRemove) {
                    case 1:
                        EditBooking(id);
                        break;
                    case 2:
                        RemoveBooking(id);
                        break;
                }
            }
        }
    }

    /**
     * @param BookingID
     */
    public void PrintBill(int BookingID) {
        // TODO implement here
    }

    /**
     * @param CustomerName
     */
    public void AddNewCustomer(String CustomerName, String CustomerPhone, String CustomerAddress, String CustomerPostCode) {
        Customer c = new Customer(++customerIDCount, CustomerName, CustomerPhone, CustomerAddress, CustomerPostCode);
        sys.customers.put(c.GetCustomerID(), c);
    }

    public void AddNewCustomer() {
        String customerName, customerPhone, customerAddress, customerPostCode;
        customerName = getInput("Customer name: ");
        customerPhone = getInput("Phone number: ");
        customerAddress = getInput("Street address and city: ");
        customerPostCode = getInput("Post/ZIP code: ");
        sys.AddNewCustomer(customerName, customerPhone, customerAddress, customerPostCode);
    }

    /**
     * @param CustomerID
     */
    public Customer CustomerLookupByID(int CustomerID) {
        for(Map.Entry<Integer, Customer> c : sys.customers.entrySet()){
            if (c.getKey().equals(CustomerID)) {
                return c.getValue();
            }
        }
        return null;
    }

    /**
     * @param Name
     */
    public ArrayList<Customer> CustomerLookupByName(String Name) {
        ArrayList<Customer> searchResults = new ArrayList<Customer>();
        for(Map.Entry<Integer, Customer> c : sys.customers.entrySet()){
            if (c.getValue().GetCustomerName().toLowerCase().contains(Name.toLowerCase())) {
                searchResults.add(c.getValue());
            }
        }
        return searchResults;
    }

    public void CustomerLookupByName() {
        ArrayList<Customer> results = sys.CustomerLookupByName(getInput("Customer name: "));
        Scanner s = new Scanner(System.in);
        BillingSystemUtils.clearConsole();
        if (results.size()==0) {
            BillingSystemUtils.confirmContinue("No customers found");
        } else {
            String[][] customerLines = new String[results.size()][];
            int customerCount = 0;
            for (Customer c: results) {
                String id = String.valueOf(c.GetCustomerID());
                String name = c.GetCustomerName();
                String postCode = c.GetCustomerPostCode();
                String[] customerData = new String[]{id, name, postCode};
                customerLines[customerCount++] = customerData;
            }
            BillingSystemUtils.printTable(new String[]{"Customer ID", "Name", "Post Code"}, customerLines);
            BillingSystemUtils.confirmContinue();
        }
    }

    /**
     * @param BookingID
     */
    public Booking BookingLookupByID(int BookingID) {
        for(Map.Entry<Integer, Booking> b : sys.allBookings.entrySet()){
            if (b.getKey().equals(BookingID)) {
                return b.getValue();
            }
        }
        return null;
    }

    public void getAllBookings() {
        BillingSystemUtils.clearConsole();
        if (sys.allBookings.size()==0) {
            BillingSystemUtils.headingText("No bookings in the system.");
            new Scanner(System.in).nextLine();
        } else {
            String[][] bookingLines = new String[sys.allBookings.size()][];
            int bookingCount = 0;
            for (Map.Entry<Integer, Booking> bEntry: sys.allBookings.entrySet()) {
                Booking b = bEntry.getValue();
                String id = String.valueOf(b.GetBookingID());
                String name = sys.CustomerLookupByID(b.GetCustomerID()).GetCustomerName();
                String checkIn = dateFormat(b.GetCheckInDate());
                String checkOut = dateFormat(b.GetCheckOutDate());
                String bookingType = b.isGroupBooking()? "Group: "+ ((GroupBooking)b).getGroupSize() :
                        b.isCorporateBooking()? "Corp: "+ ((CorporateBooking)b).GetCompanyName() : "Individual";
                String[] bookingData = new String[]{id, name, bookingType, checkIn, checkOut};
                bookingLines[bookingCount++] = bookingData;
            }
            BillingSystemUtils.printTable(new String[]{"ID", "Customer", "Type", "Check-In", "Check-Out"}, bookingLines);
            BillingSystemUtils.confirmContinue();
        }
    }

    /**
     * @param CustomerID
     */
    public ArrayList<Booking> BookingLookupByCustomer(int CustomerID) {
        for(Map.Entry<Integer, Customer> c : sys.customers.entrySet()){
            if (c.getKey().equals(CustomerID)) {
                return c.getValue().GetBookings();
            }
        }
        return null;
    }




}