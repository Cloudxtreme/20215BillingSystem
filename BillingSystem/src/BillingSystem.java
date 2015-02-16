import java.text.ParseException;
import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;


/**
 * 
 */
public class BillingSystem {

    /**
     * 
     */
    public BillingSystem() {
        BookingIDCount = 0;
        CustomerIDCount = 0;
        CurrentIndividuals = 0;
        CurrentCorporates = 0;
        CurrentGroups = 0;
        AllBookings = new HashMap<Integer, Booking>();
        Customers = new HashMap<Integer, Customer>();
    }

    // Instance variable for billing system
    private static BillingSystem sys = new BillingSystem();

    private static final long TODAY = System.currentTimeMillis();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private static final String DEFAULT_ERR = "Invalid entry.";

    private static final String CONT_MESSAGE = "Hit return to continue.";

    private static Random r = new Random();

    private static ArrayList<String> Companies = new ArrayList<String>();

    private static final String TITLE = "╔════════════════╗\n" +
                                        "║ Billing System ║\n" +
                                        "╚════════════════╝";
    //
    private static final String MENU_OPTIONS = "╔════════════════╗\n" +
            "║ Billing System ║\n" +
            "╠═══╦════════════╩══════════════════════════════════════╗\n" +
            "║ 1 ║ Add new customer                                  ║\n" +
            "╟───╫───────────────────────────────────────────────────╢\n" +
            "║ 2 ║ Create booking for new customer                   ║\n" +
            "╟───╫───────────────────────────────────────────────────╢\n" +
            "║ 3 ║ Create booking for existing customer              ║\n" +
            "╟───╫───────────────────────────────────────────────────╢\n" +
            "║ 4 ║ Booking lookup (or view all bookings)             ║\n" +
            "╟───╫───────────────────────────────────────────────────╢\n" +
            "║ 5 ║ Customer search (or view all customers)           ║\n" +
            "╟───╫───────────────────────────────────────────────────╢\n" +
            "║ 6 ║ Remove booking                                    ║\n" +
            "╟───╫───────────────────────────────────────────────────╢\n" +
            "║ 7 ║ Manage customers                                  ║\n" +
            "╟───╫───────────────────────────────────────────────────╢\n" +
            "║ 8 ║ Generate bill                                     ║\n" +
            "╟───╫───────────────────────────────────────────────────╢\n" +
            "║ 9 ║ Quit                                              ║\n" +
            "╚═══╩═══════════════════════════════════════════════════╝\n";

    private static final String PROMPT = "╔╦══════════════════════════════════════════════════════╗\n" +
            "║║ ";

    private static final String LINE_START = "║║ ";


    private static Scanner getInput = new Scanner(System.in);

    // Maximum individual bookings at any time
    private static final int MAX_INDIVIDUALS = 20;
    // Maximum corporate bookings at any time
    private static final  int MAX_CORPORATES = 20;
    // Maximum group bookings at any time
    private static final int MAX_GROUPS = 10;

    // Total number of past bookings
    private static int BookingIDCount;
    // Total number of past customers
    private static int CustomerIDCount;

    // Current number of individuals staying
    private int CurrentIndividuals;
    // Current number of corporate guests staying
    private int CurrentCorporates;
    // Current number of groups staying
    private int CurrentGroups;

    // Every booking in the system
    private HashMap<Integer, Booking> AllBookings;
    // Every customer in the system
    private HashMap<Integer, Customer> Customers;

    public static void main(String[] args) {
        sys.populateSystemData();
        ArrayList<Customer> results = sys.CustomerLookupByName("");
//        for (Customer c: results) {
//            System.out.println(c.GetCustomerID() + " : " + c.GetCustomerName() + " : " + c.GetCustomerPostCode());
//        }
        while (true) {
            sys.mainMenu();
        }
    }

    public void clearConsole() {
        System.out.print(String.format("\033[2J"));
        System.out.println(TITLE);

    }

    public void mainMenu() {
        Scanner s = new Scanner(System.in);
        int menuChoice = 0;
        boolean valid = true;
        clearConsole();
        System.out.println(MENU_OPTIONS);
        System.out.print(PROMPT + "Menu choice: ");
        try {
            menuChoice = s.nextInt();
            s.nextLine();
            System.out.println();
            menuSelection(menuChoice);
        } catch (InputMismatchException e) {  // Invalid menu entry
            valid = false;
        } catch (NoSuchElementException e) { // Scanner closed
            valid = false;
        }


    }

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
                AddNewBooking();
                break;
            case 4:
                getAllBookings();
                break;
            case 5:
                CustomerLookupByName();
                break;
            case 6:
                RemoveBooking();
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                System.exit(0);
        }
    }


    /**
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

    public void AddNewBooking() {
        String searchName = getInput("Enter customer name: ");
        ArrayList<Customer> results = CustomerLookupByName(searchName);
        System.out.println(PROMPT + "Customer : ID");
        if (results.size()==0) {
            System.out.println(PROMPT + "No customers found. " + CONT_MESSAGE);
            new Scanner(System.in).nextLine();
        } else {
            for (Customer c: results) {
                System.out.println(LINE_START + c.GetCustomerName() + " : " + c.GetCustomerID());
            }
            int id = getIntInput(CustomerIDCount+1, "Enter customer ID to book: ", "Invalid customer ID.");
            AddNewBooking(id);
        }
    }

    public void AddNewBooking(int CustomerID) {
        long checkInDate = getDateInput("Enter check-in date dd/mm/yyyy: ");
        long checkOutDate = getDateInput("Enter check-out date dd/mm/yyyy: ",
                "Check-out must be a valid date after check-in",
                Long.MAX_VALUE, checkInDate);


        int bookingType = getIntInput(4, "1:  Individual, 2: Group, 3: corporate. ");
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

    public int getIntInput(int upperBound, String inputText) {
        return getIntInput(upperBound, inputText, DEFAULT_ERR);
    }

    public int getIntInput(int upperBound, String inputText, String errorText) {
        int input;
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print(PROMPT + inputText);
            try {
                input = s.nextInt();
                if (input < upperBound && input > 0) {
                    clearConsole();
                    return input;
                }
            } catch (InputMismatchException e) {}
            System.out.println(PROMPT + errorText);
            System.out.println(PROMPT + CONT_MESSAGE);
            s.nextLine();
        }
    }

    public String getInput(String inputText) {
        return getInput(inputText, DEFAULT_ERR);
    }

    public String getInput(String inputText, String errorText) {
        String input;
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print(PROMPT + inputText);
            input = s.nextLine();
            if (input.length() > 0) {
                clearConsole();
                return input;
            }
            System.out.println(PROMPT + errorText);
            System.out.println(PROMPT + CONT_MESSAGE);
            s.nextLine();
        }
    }

    public long getDateInput(String inputText) {
        return getDateInput(inputText, DEFAULT_ERR, Long.MAX_VALUE, 0l);
    }

    public long getDateInput(String inputText, String errorText, long before, long after) {
        long input;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print(PROMPT + inputText);
            try {
                input = sdf.parse(s.nextLine()).getTime();
                if (input < before && input > after) {
                    clearConsole();
                    return input;
                }
            } catch (ParseException e) {}
            System.out.println(PROMPT + errorText);
            System.out.println(PROMPT + CONT_MESSAGE);
            s.nextLine();
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

    public void RemoveBooking() {
        String searchName = getInput("Enter customer name: ");
        ArrayList<Customer> results = CustomerLookupByName(searchName);
        if (results.size()==0) {
            System.out.println(PROMPT + "No customers found. " + CONT_MESSAGE);
            new Scanner(System.in).nextLine();
        } else {
            for (Customer c: results) {
                System.out.println(PROMPT + "Bookings for " + c.GetCustomerID() + ": " + c.GetCustomerName());
                for (Booking b: c.GetBookings()) {
                    System.out.println(LINE_START + b.GetBookingID() + " : " +
                        sdf.format(new Date(b.GetCheckInDate())) + "-" + sdf.format(new Date(b.GetCheckOutDate())));
                }
            }
            int id = getIntInput(BookingIDCount+1, "Enter booking ID to cancel it: ", "Invalid booking ID.");
            RemoveBooking(id);
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
        ArrayList<Customer> results = CustomerLookupByName(getInput("Name: "));
        clearConsole();
        if (results.size()==0) {
            System.out.println(PROMPT + "No matches found." + CONT_MESSAGE);
            new Scanner(System.in).nextLine();
        } else {
            System.out.println(PROMPT + "ID" + "-" + "Customer");
            for (Customer c: results) {
                System.out.println(LINE_START + String.format("%02d", c.GetCustomerID()) + " : " + c.GetCustomerName());
            }
            System.out.println(PROMPT + CONT_MESSAGE);
            new Scanner(System.in).nextLine();
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
        clearConsole();
        if (AllBookings.size()==0) {
            System.out.println(PROMPT + "No bookings in the system.");
            new Scanner(System.in).nextLine();
        } else {
            System.out.println(PROMPT + "ID : Customer : Dates");
            for (Map.Entry<Integer, Booking> bEntry: AllBookings.entrySet()) {
                Booking b = bEntry.getValue();
                String name = CustomerLookupByID(b.GetCustomerID()).GetCustomerName();
                System.out.println(LINE_START + String.format("%03d", b.GetBookingID()) + " : " +
                        name  + " : " +
                        dateFormat(b.GetCheckInDate()) + " - " +
                        dateFormat(b.GetCheckOutDate()));
            }
            System.out.println(PROMPT + CONT_MESSAGE);
            new Scanner(System.in).nextLine();
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

    public void populateSystemData() {
        sys.Companies.add("Second Bridge");
        sys.Companies.add("Weir Lounge");
        sys.Companies.add("Zero Zero");

        sys.AddNewCustomer("Kiara Kirk","02678 001547", "7 Molestie St.,Jedburgh, Roxburghshire", "V7C 2NA");
        sys.AddNewCustomer("Milly Peck","09271 533863","5B Ante Av.,Portsmouth, Hampshire","W3 5QC");
        sys.AddNewCustomer("Adeline Avery","05991 642782","118 North St.,Portree, Inverness","QX1F 8FC");
        sys.AddNewCustomer("Chad Mathews","04351 374396","6061 Mauris. St.,Crewe, Cheshire", "YQ0I 1SZ");
        sys.AddNewCustomer("Sasha Walker","08699 397440", "48 Mountainside Road,Lockerbie, Dumfriesshire","PJ4 6XE");
        sys.AddNewCustomer("Nita Mccullough","01053 186773","1725 Eget Rd.,Southwell, Nottinghamshire","NG42 7CJ");
        sys.AddNewCustomer("Melvin Delaney","04394 839128", "45 Aliquam Rd, Bromyard, Herefordshire","PC9 6UN");
        sys.AddNewCustomer("Griffith Mullins" ,"07188 153600" ,"77 Seaside Ave., Bridlington,Yorkshire" ,"Y8D 0QQ");
        sys.AddNewCustomer("Sebastian Mason" ,"01714 170624", "175 Bay St., Kinross, Kinross-shire", "MV99 9AI");
        sys.AddNewCustomer("Fay Black", "01684 038533","2350 Sed, St., Maidstone, Kent", "L0 9NA");
        sys.AddNewCustomer("Tatiana Sears","03463 610913", "4 Hendrerit Av.,Tewkesbury, Gloucestershire" , "KX46 1NT");
        sys.AddNewCustomer("Rowan Wright","09519 543273", "34 Velvet Rd., Barrow-in-Furness,Lancashire", "S3 4NY");
        sys.AddNewCustomer("Craig Stanton","05795 566600","7D Eugene Ave, Llandrindod Wells, Radnorshire","I2 6NX");
        sys.AddNewCustomer("Colton Mendoza", "03682 983662", "29 Feugiat St., Aylesbury, Buckinghamshire","RO1P 6QO");
        sys.AddNewCustomer("Abigail Petersen", "08199 208651", "80 Dolor St., Dundee, Angus","JL6 8EA");
        sys.AddNewCustomer("Kenneth Wilkinson" ,"04170 723460","6860 Risus. Rd., Trowbridge, Wiltshire","EL39 8RH");
        sys.AddNewCustomer("Dominique Pitts","09516 368779", "286 Alford Avenue, Leominster, Herefordshire","OU6C 4IY");
        sys.AddNewCustomer("Mariam Mullen" ,"03450 255848","37 Convallis St., Stonehaven, Kincardineshire","CT7 4LW");
        sys.AddNewCustomer("Meghan Barlow","05694 203694","151 Suspendisse Ave.,Nairn, Nairnshire","Q6 9KY");
        sys.AddNewCustomer("Clark Stein","08038 056955","5 Neque Ave, Sromness, Orkney","IT9L 1TH");
        sys.AddNewCustomer("Lucy Howard","04877 577526","39 Sollicitudin Street, Wimbledon, Surrey","SW14 3SL");
        sys.AddNewCustomer("Cole Falderon","01468 244318", "14 Elementum, Av.,Wrexham, Denbighshire", "WX36 1ZV");
        sys.AddNewCustomer("Jordan Guzman","06088 484468","22A Convallis Street, Truro, Cornwall", "BU7 4QX");
        sys.AddNewCustomer("Daphne Willis","05056 497556", "64 Noon Road, Newtown, Montgomeryshire" ,"MO1 2LC");
        sys.AddNewCustomer("Judith Reilly","08390 478150", "67 South Avenue, Broxburn, West Lothian", "I48 3MM");


        for (int customerID=1; customerID <= CustomerIDCount; customerID++) {
            int bookingType = r.nextInt(3)+1;
            long checkIn, checkOut;
            checkIn = dateGenerator();
            checkOut = checkIn + randomStayLength();
            switch (bookingType) {
                case 1:
                    AddNewBooking(customerID, checkIn, checkOut);
                    break;
                case 2:
                    AddNewBooking(customerID, randomGroupSize(), checkIn, checkOut);
                    break;
                case 3:
                    AddNewBooking(randomCompanyName(), customerID, checkIn, checkOut);
                    break;
            }
        }
    }

    private long dateGenerator() {
        return TODAY + r.nextInt(262800000)*10 - 2628000000l;
    }

    private long randomStayLength() {
        return 86400000 * (r.nextInt(14)+1);
    }

    private int randomGroupSize() {
        return r.nextInt(4)+2;
    }

    private String randomCompanyName() {
        return Companies.get(r.nextInt(Companies.size()));
    }

    private String dateFormat(long millis) {
        return sdf.format(new Date(millis));
    }
}