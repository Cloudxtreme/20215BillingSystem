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
        BookingIDCount = 0;
        CustomerIDCount = 0;
        AllBookings = new HashMap<Integer, Booking>();
        Customers = new HashMap<Integer, Customer>();
    }

    // Instance variable for billing system
    private static BillingSystem sys = new BillingSystem();
    // Total number of past bookings
    private static int BookingIDCount;
    // Total number of past customers
    private static int CustomerIDCount;
    // Every booking in the system
    private HashMap<Integer, Booking> AllBookings = new HashMap<Integer, Booking>();
    // Every customer in the system
    private HashMap<Integer, Customer> Customers = new HashMap<Integer, Customer>();
    // The date format accepted for I/O
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    /**
     * Constructs a new instance of BillingSystem with no customers or bookings
     */
    public static void main(String[] args) {
        BillingSystemUtils.populateSystemData(sys);
        while (true) {
            sys.mainMenu();
        }
    }

    /**
     * Launches the main menu and waits for input
     */
    public void mainMenu() {
        Scanner s = new Scanner(System.in);
        int menuChoice = 0;
        BillingSystemUtils.menu();
        menuSelection(getIntInput(10, "Menu choice (1-9): ", "Invalid menu choice."));
    }

    /**
     * Performs system actions based on the main menu choice the user made
     * @param menuChoice The validated integer menu selection (0-9)
     */
    public void menuSelection(int menuChoice) {
        switch (menuChoice) {
            case 1:
                AddNewCustomer();
                break;
            case 2:
                AddNewCustomer();
                AddNewBooking(CustomerLookupByID(CustomerIDCount).GetCustomerID());
                break;
            case 3:
                SelectBookingCustomer();
                break;
            case 4:
                getAllBookings();
                break;
            case 5:
                CustomerLookupByName();
                break;
            case 6:
                ManageBooking();
                break;
            case 7:
                break;
            case 8:
                GenerateBill();
                break;
            case 9:
                System.exit(0);
        }
    }


    /**
     * Creates a new individual booking
     * @param CustomerID
     * @param CheckInDate 
     * @param CheckOutDate
     */
    public void AddNewBooking(int CustomerID, long CheckInDate, long CheckOutDate) {
        IndividualBooking b = new IndividualBooking(++BookingIDCount, CustomerID, CheckInDate, CheckOutDate);
        CustomerLookupByID(CustomerID).AddBooking(b);
        AllBookings.put(b.GetBookingID(), b);
    }

    /**
     * Creates a new corporate booking
     * @param CompanyName
     * @param CustomerID
     * @param CheckInDate
     * @param CheckOutDate
     */
    public void AddNewBooking(String CompanyName, int CustomerID, long CheckInDate, long CheckOutDate) {
        CorporateBooking b = new CorporateBooking(++BookingIDCount, CompanyName, CustomerID, CheckInDate, CheckOutDate);
        CustomerLookupByID(CustomerID).AddBooking(b);
        AllBookings.put(b.GetBookingID(), b);
    }

    /**
     * Creates a new group booking
     * @param CustomerID
     * @param GroupSize
     * @param CheckInDate
     * @param CheckOutDate
     */
    public void AddNewBooking(int CustomerID, int GroupSize, long CheckInDate, long CheckOutDate) {
        GroupBooking b = new GroupBooking(++BookingIDCount, CustomerID, GroupSize, CheckInDate, CheckOutDate);
        CustomerLookupByID(CustomerID).AddBooking(b);
        AllBookings.put(b.GetBookingID(), b);
    }

    /**
     * Gets a user selected customer ID to create a booking for that customer
     */
    public void SelectBookingCustomer() {
        ArrayList<Customer> results = CustomerLookupByName(getInput("Customer name: "));
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
            int id = getIntInput(CustomerIDCount+1, "Enter customer ID to book: ", "Invalid customer ID.");
            AddNewBooking(id);
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
                AddNewBooking(CustomerID, checkInDate, checkOutDate);
                break;
            case 2:
                int groupSize = getIntInput(6, "Enter group size: ", "Invalid group size (max 5).");
                AddNewBooking(CustomerID, groupSize, checkInDate, checkOutDate);
                break;
            case 3:
                String CompanyName = getInput("Enter company name: ");
                AddNewBooking(CompanyName, CustomerID, checkInDate, checkOutDate);
                break;
        }
    }

    public void EditBooking(int BookingID) {
        Booking currentBooking = AllBookings.get(BookingID);
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
        return CustomerIDCount;
    }

    private String dateFormat(long millis) {
        return sdf.format(new Date(millis));
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
                input = sdf.parse(s.nextLine()).getTime();
                if (input < before && input > after) {
                    BillingSystemUtils.clearConsole();
                    return input;
                }
            } catch (ParseException e) {}
            BillingSystemUtils.confirmContinue(errorText);
        }
    }


    public void GenerateBill(int bookingID) {
        Booking b = BookingLookupByID(bookingID);
        ArrayList<String> bill = b.GenerateBill();
        BillingSystemUtils.headingText("Billed: " + CustomerLookupByID(b.GetCustomerID()).GetCustomerName());
        for (String billLine: bill) {
            BillingSystemUtils.bodyText(billLine);
        }
        BillingSystemUtils.confirmContinue();
    }

    public void GenerateBill() {
        ArrayList<Customer> results = CustomerLookupByName(getInput("Customer name: "));
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
                    String checkIn = sdf.format(new Date(b.GetCheckInDate()));
                    String checkOut = sdf.format(new Date(b.GetCheckOutDate()));
                    String bookingType = b.isGroupBooking()? "Group: "+ ((GroupBooking)b).getGroupSize() :
                            b.isCorporateBooking()? "Corp: "+ ((CorporateBooking)b).GetCompanyName() : "Individual";
                    String[] bookingData = new String[]{name, postCode, bookingID, bookingType, checkIn, checkOut};
                    bookingLines[bookingCount++] = bookingData;
                }
            }
            if (bookingCount > 0) {
                BillingSystemUtils.printTable(
                        new String[]{"Customer", "Post Code", "Booking", "Type", "Check-In", "Check-Out"}, bookingLines);
                int id = getIntInput(BookingIDCount+1, "Enter booking ID to generate bill: ", "Invalid booking ID.");
                GenerateBill(id);
            }
        }
    }


    /**
     * @param BookingID
     */
    public void RemoveBooking(int BookingID) {
        Booking b = BookingLookupByID(BookingID);
        CustomerLookupByID(b.GetCustomerID()).RemoveBooking(b);
        AllBookings.remove(BookingID);
    }

    public void ManageBooking() {
        ArrayList<Customer> results = CustomerLookupByName(getInput("Customer name: "));
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
                    String checkIn = sdf.format(new Date(b.GetCheckInDate()));
                    String checkOut = sdf.format(new Date(b.GetCheckOutDate()));
                    String bookingType = b.isGroupBooking()? "Group: "+ ((GroupBooking)b).getGroupSize() :
                            b.isCorporateBooking()? "Corp: "+ ((CorporateBooking)b).GetCompanyName() : "Individual";
                    String[] bookingData = new String[]{name, postCode, bookingID, bookingType, checkIn, checkOut};
                    bookingLines[bookingCount++] = bookingData;
                }
            }
            if (bookingCount > 0) {
                BillingSystemUtils.printTable(
                        new String[]{"Customer", "Post Code", "Booking", "Type", "Check-In", "Check-Out"}, bookingLines);
                int id = getIntInput(BookingIDCount+1, "Enter booking ID to edit it: ", "Invalid booking ID.");
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
        Customer c = new Customer(++CustomerIDCount, CustomerName, CustomerPhone, CustomerAddress, CustomerPostCode);
        Customers.put(c.GetCustomerID(), c);
    }

    public void AddNewCustomer() {
        String customerName, customerPhone, customerAddress, customerPostCode;
        customerName = getInput("Customer name: ");
        customerPhone = getInput("Phone number: ");
        customerAddress = getInput("Street address and city: ");
        customerPostCode = getInput("Post/ZIP code: ");
        AddNewCustomer(customerName, customerPhone, customerAddress, customerPostCode);
    }

    /**
     * @param CustomerID
     */
    public Customer CustomerLookupByID(int CustomerID) {
        for(Map.Entry<Integer, Customer> c : Customers.entrySet()){
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
        for(Map.Entry<Integer, Customer> c : Customers.entrySet()){
            if (c.getValue().GetCustomerName().toLowerCase().contains(Name.toLowerCase())) {
                searchResults.add(c.getValue());
            }
        }
        return searchResults;
    }

    public void CustomerLookupByName() {
        ArrayList<Customer> results = CustomerLookupByName(getInput("Customer name: "));
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
        for(Map.Entry<Integer, Booking> b : AllBookings.entrySet()){
            if (b.getKey().equals(BookingID)) {
                return b.getValue();
            }
        }
        return null;
    }

    public void getAllBookings() {
        BillingSystemUtils.clearConsole();
        if (AllBookings.size()==0) {
            BillingSystemUtils.headingText("No bookings in the system.");
            new Scanner(System.in).nextLine();
        } else {
            String[][] bookingLines = new String[AllBookings.size()][];
            int bookingCount = 0;
            for (Map.Entry<Integer, Booking> bEntry: AllBookings.entrySet()) {
                Booking b = bEntry.getValue();
                String id = String.valueOf(b.GetBookingID());
                String name = CustomerLookupByID(b.GetCustomerID()).GetCustomerName();
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
        for(Map.Entry<Integer, Customer> c : Customers.entrySet()){
            if (c.getKey().equals(CustomerID)) {
                return c.getValue().GetBookings();
            }
        }
        return null;
    }




}