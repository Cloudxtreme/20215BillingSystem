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
     * Converts a given date from storage format into output format
     * @param milliseconds
     */
    private String dateFormat(long milliseconds) {
        return dateFormat.format(new Date(milliseconds));
    }

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
        menuSelection(BillingSystemUtils.getIntInput(9, "Menu choice (1-8): ", "Invalid menu choice."));
    }

    /**
     * Performs system actions based on the main menu choice the user made
     * @param menuChoice The validated integer menu selection (0-8)
     */
    public void menuSelection(int menuChoice) {
        switch (menuChoice) {
            case 1:
                sys.newCustomer();
                break;
            case 2:
                sys.newCustomer();
                addNewBooking(customerLookupByID(customerIDCount).GetCustomerID());
                break;
            case 3:
                sys.selectBookingCustomer();
                break;
            case 4:
                sys.getAllBookings();
                break;
            case 5:
                sys.customerSearch();
                break;
            case 6:
                sys.manageBooking();
                break;
            case 7:
                sys.printBill();
                break;
            case 8:
                System.exit(0);
                break;
        }
    }

    // MENU CALLED METHODS

    /**
     * Gets user inputted customer details to create a new customer
     */
    public void newCustomer() {
        String customerName, customerPhone, customerAddress, customerPostCode;
        customerName = BillingSystemUtils.getInput("Customer name: ");
        customerPhone = BillingSystemUtils.getInput("Phone number: ");
        customerAddress = BillingSystemUtils.getInput("Street address and city: ");
        customerPostCode = BillingSystemUtils.getInput("Post/ZIP code: ");
        sys.addNewCustomer(customerName, customerPhone, customerAddress, customerPostCode);
    }

    /**
     * Prompts the user for booking details while booking for a pre-specified customer
     * @param CustomerID
     */
    public void addNewBooking(int CustomerID) {
        long checkInDate = BillingSystemUtils.getDateInput("Enter check-in date dd/mm/yy: ");
        long checkOutDate = BillingSystemUtils.getDateInput("Enter check-out date dd/mm/yy: ",
                "Check-out must be a valid date after check-in",
                Long.MAX_VALUE, checkInDate);
        int bookingType = BillingSystemUtils.getIntInput(4, "1:  Individual, 2: Group, 3: Corporate. ");
        switch (bookingType) {
            case 1:
                sys.addNewBooking(CustomerID, checkInDate, checkOutDate);
                break;
            case 2:
                int groupSize = BillingSystemUtils.getIntInput(6, "Enter group size: ", "Invalid group size (max 5).");
                sys.addNewBooking(CustomerID, groupSize, checkInDate, checkOutDate);
                break;
            case 3:
                String CompanyName = BillingSystemUtils.getInput("Enter company name: ");
                sys.addNewBooking(CompanyName, CustomerID, checkInDate, checkOutDate);
                break;
        }
    }

    /**
     * Gets a user selected customer to create a booking for that customer
     */
    public void selectBookingCustomer() {
        ArrayList<Customer> results = sys.customerLookupByName(BillingSystemUtils.getInput("Customer name: "));
        if (results.size()==0) {
            BillingSystemUtils.confirmContinue("No customers found.");
        } else { // Print all results matching the customer search for the user to select from
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
            int id = BillingSystemUtils.getIntInput(customerIDCount + 1, "Enter customer ID to book: ", "Invalid customer ID.");
            sys.addNewBooking(id);
        }
    }

    /**
     * Prints a list of all booking records in the system
     */
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
                String name = sys.customerLookupByID(b.GetCustomerID()).GetCustomerName();
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
     * Gets a (name) search term from the user and prints a list of matching customer records
     */
    public void customerSearch() {
        ArrayList<Customer> results = sys.customerLookupByName(BillingSystemUtils.getInput("Customer name: "));
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
     * Allows a user edit or delete a booking for a specific customer
     */
    public void manageBooking() {
        ArrayList<Customer> results = sys.customerLookupByName(BillingSystemUtils.getInput("Customer name: "));
        if (results.size() == 0) {
            BillingSystemUtils.confirmContinue("No customers found");
        } else if (displayFilteredBookings(results) > 0) {
            int id = BillingSystemUtils.getIntInput(bookingIDCount + 1,
                    "Enter booking ID to edit it: ", "Invalid booking ID.");
            int editOrRemove = BillingSystemUtils.getIntInput(3, "Enter 1 to edit booking or 2 to remove booking: ");
            switch (editOrRemove) {
                case 1:
                    sys.editBooking(id);
                    break;
                case 2:
                    sys.removeBooking(id);
                    break;
            }
        } else {
            BillingSystemUtils.confirmContinue("No bookings found for selected customer");
        }
    }

    /**
     * Allows a user to generate/print a bill for a specific customer
     */
    public void printBill() {
        ArrayList<Customer> results = sys.customerLookupByName(BillingSystemUtils.getInput("Customer name: "));
        if (results.size()==0) {
            BillingSystemUtils.confirmContinue("No customers found");
        } else if (displayFilteredBookings(results) > 0) {
            int id = BillingSystemUtils.getIntInput(bookingIDCount + 1,
                    "Enter booking ID to generate bill: ", "Invalid booking ID.");
            sys.generateBill(id);
        } else {
            BillingSystemUtils.confirmContinue("No bookings found for selected customer");
        }
    }

    // END OF MENU METHODS



    // SYSTEM BOOKING METHODS

    /**
     * Creates a new individual booking (addNewBooking is overloaded for every booking type)
     * @param CustomerID
     * @param CheckInDate
     * @param CheckOutDate
     */
    public void addNewBooking(int CustomerID, long CheckInDate, long CheckOutDate) {
        IndividualBooking b = new IndividualBooking(++bookingIDCount, CustomerID, CheckInDate, CheckOutDate);
        sys.customerLookupByID(CustomerID).AddBooking(b);
        sys.allBookings.put(b.GetBookingID(), b);
    }

    /**
     * Creates a new corporate booking (addNewBooking is overloaded for every booking type)
     * @param CompanyName
     * @param CustomerID
     * @param CheckInDate
     * @param CheckOutDate
     */
    public void addNewBooking(String CompanyName, int CustomerID, long CheckInDate, long CheckOutDate) {
        CorporateBooking b = new CorporateBooking(++bookingIDCount, CompanyName, CustomerID, CheckInDate, CheckOutDate);
        sys.customerLookupByID(CustomerID).AddBooking(b);
        sys.allBookings.put(b.GetBookingID(), b);
    }

    /**
     * Creates a new group booking (addNewBooking is overloaded for every booking type)
     * @param CustomerID
     * @param GroupSize
     * @param CheckInDate
     * @param CheckOutDate
     */
    public void addNewBooking(int CustomerID, int GroupSize, long CheckInDate, long CheckOutDate) {
        GroupBooking b = new GroupBooking(++bookingIDCount, CustomerID, GroupSize, CheckInDate, CheckOutDate);
        sys.customerLookupByID(CustomerID).AddBooking(b);
        sys.allBookings.put(b.GetBookingID(), b);
    }

    /**
     * Returns the booking matching a specified booking ID
     * @param BookingID
     */
    public Booking bookingLookupByID(int BookingID) {
        for(Map.Entry<Integer, Booking> b : sys.allBookings.entrySet()){
            if (b.getKey().equals(BookingID)) {
                return b.getValue();
            }
        }
        return null;
    }

    /**
     * Prompts the user for booking details to amend an existing pre-specified booking
     * @param BookingID
     */
    public void editBooking(int BookingID) {
        Booking currentBooking = sys.allBookings.get(BookingID);
        // Amend check-in
        BillingSystemUtils.headingText("Current check-in date is " +
                dateFormat(currentBooking.GetCheckInDate()));
        System.out.println();
        long checkInDate = BillingSystemUtils.getDateInput("Enter new check-in date dd/mm/yy: ");
        currentBooking.EditCheckInDate(checkInDate);

        // Amend check-out
        BillingSystemUtils.headingText("Current check-out date is " +
                dateFormat(currentBooking.GetCheckOutDate()));
        System.out.println();
        long checkOutDate = BillingSystemUtils.getDateInput("Enter new check-out date dd/mm/yy: ",
                "Check-out must be a valid date after check-in",
                Long.MAX_VALUE, checkInDate);
        currentBooking.EditCheckOutDate(checkOutDate);

        // Amend additional booking type-specific details
        if (currentBooking.isGroupBooking()) {
            // Amend group size
            BillingSystemUtils.headingText("Current group size is: " +
                    ((GroupBooking)currentBooking).getGroupSize());
            System.out.println();
            int groupSize = BillingSystemUtils.getIntInput(6, "Enter new group size: ", "Invalid group size (max 5).");
            ((GroupBooking)currentBooking).EditGroupSize(groupSize);
        } else if (currentBooking.isCorporateBooking()) {
            // Amend company name
            BillingSystemUtils.headingText("Current company name is: " +
                    ((CorporateBooking)currentBooking).GetCompanyName());
            System.out.println();
            String CompanyName = BillingSystemUtils.getInput("Enter company name: ");
            ((CorporateBooking)currentBooking).EditCompanyName(CompanyName);
        }
    }

    /**
     * Deletes a booking from the system and from its associated customer
     * @param bookingID
     */
    public void removeBooking(int bookingID) {
        Booking b = bookingLookupByID(bookingID);
        sys.customerLookupByID(b.GetCustomerID()).RemoveBooking(b);
        sys.allBookings.remove(bookingID);
    }

    /**
     * Generates a bill for a specified booking and 'prints' it to the console
     * @param bookingID
     */
    public void generateBill(int bookingID) {
        Booking b = sys.bookingLookupByID(bookingID);
        ArrayList<String> bill = b.GenerateBill();
        BillingSystemUtils.headingText("Billed: " + customerLookupByID(b.GetCustomerID()).GetCustomerName());
        System.out.println();
        for (String billLine: bill) {
            BillingSystemUtils.bodyText(billLine);
            System.out.println();
        }
        BillingSystemUtils.confirmContinue();
    }

    /**
     * Allows a user to search for all bookings by customers whose name matches a search term
     * @param customerResults
     */
    public int displayFilteredBookings(ArrayList<Customer> customerResults) {
        int totalBookings = 0;
        for (Customer c: customerResults) {
            totalBookings += c.GetBookings().size();
        }
        String[][] bookingLines = new String[totalBookings][];
        int bookingCount = 0;
        for (Customer c: customerResults) {
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
            // Print all results matching the customer search for the user to select from
            BillingSystemUtils.printTable(
                    new String[]{"Customer", "Post Code", "Booking", "Type", "Check-In", "Check-Out"}, bookingLines);
        }
        return bookingCount;
    }

    // END OF SYSTEM BOOKING METHODS



    // SYSTEM CUSTOMER METHODS

    /**
     * Adds a new customer and their details to the system
     * @param CustomerName
     * @param CustomerPhone
     * @param CustomerAddress
     * @param CustomerPostCode
     */
    public void addNewCustomer(String CustomerName, String CustomerPhone, String CustomerAddress, String CustomerPostCode) {
        Customer c = new Customer(++customerIDCount, CustomerName, CustomerPhone, CustomerAddress, CustomerPostCode);
        sys.customers.put(c.GetCustomerID(), c);
    }

    /**
     * Returns the customer matching a specified customer ID
     * @param CustomerID
     */
    public Customer customerLookupByID(int CustomerID) {
        for(Map.Entry<Integer, Customer> c : sys.customers.entrySet()){
            if (c.getKey().equals(CustomerID)) {
                return c.getValue();
            }
        }
        return null;
    }

    /**
     * Returns a list of customer records whose names match a search term
     * @param Name
     */
    public ArrayList<Customer> customerLookupByName(String Name) {
        ArrayList<Customer> searchResults = new ArrayList<Customer>();
        for(Map.Entry<Integer, Customer> c : sys.customers.entrySet()){
            if (c.getValue().GetCustomerName().toLowerCase().contains(Name.toLowerCase())) {
                searchResults.add(c.getValue());
            }
        }
        return searchResults;
    }

    /**
     * Returns the current number of customer records held in the system
     */
    public int getCustomerIDCount() {
        return sys.customerIDCount;
    }

    // END OF SYSTEM CUSTOMER METHODS
}