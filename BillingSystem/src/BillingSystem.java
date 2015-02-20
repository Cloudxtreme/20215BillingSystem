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
        AllBookings = new HashMap<Integer, Booking>();
        Customers = new HashMap<Integer, Customer>();
    }

    // Instance variable for billing system
    private static BillingSystem sys = new BillingSystem();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private static Random r = new Random();

    private static ArrayList<String> Companies = new ArrayList<String>();

    // Total number of past bookings
    private static int BookingIDCount;
    // Total number of past customers
    private static int CustomerIDCount;

    // Every booking in the system
    private HashMap<Integer, Booking> AllBookings;
    // Every customer in the system
    private HashMap<Integer, Customer> Customers;

    public static void main(String[] args) {
        sys.populateSystemData();
        while (true) {
            sys.mainMenu();
        }
    }

    public void mainMenu() {
        Scanner s = new Scanner(System.in);
        int menuChoice = 0;
        BillingSystemUtils.menu();
        BillingSystemUtils.headingText("Menu choice: ");
        try {
            menuChoice = s.nextInt();
            s.nextLine();
            System.out.println();
            menuSelection(menuChoice);
        } catch (InputMismatchException e) {
        } catch (NoSuchElementException e) {}
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

    public void EditBooking(int BookingID) {
        Booking currentBooking = AllBookings.get(BookingID);
        BillingSystemUtils.headingText("Current check-in date is " + dateFormat(currentBooking.GetCheckInDate()));
        long checkInDate = getDateInput("Enter new check-in date dd/mm/yyyy: ");
        BillingSystemUtils.headingText("Current check-out date is " + dateFormat(currentBooking.GetCheckOutDate()));
        long checkOutDate = getDateInput("Enter new check-out date dd/mm/yyyy: ",
                "Check-out must be a valid date after check-in",
                Long.MAX_VALUE, checkInDate);
        currentBooking.EditCheckInDate(checkInDate);
        currentBooking.EditCheckOutDate(checkOutDate);

        if (currentBooking.isGroupBooking()) {
            BillingSystemUtils.headingText("Current group size is: " + ((GroupBooking) currentBooking).getGroupSize());
            int groupSize = getIntInput(6, "Enter new group size: ", "Invalid group size (max 5).");
            ((GroupBooking) currentBooking).EditGroupSize(groupSize);
        } else if (currentBooking.isCorporateBooking()) {
            BillingSystemUtils.headingText("Current company name is: " + ((CorporateBooking)currentBooking).GetCompanyName());
            String CompanyName = getInput("Enter company name: ");
            ((CorporateBooking)currentBooking).EditCompanyName(CompanyName);
        }
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
                            b.isCorporateBooking()? "Corporate" : "Individual";
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
                    String bookingType = b.isGroupBooking()? "Group: " + ((GroupBooking)b).getGroupSize() :
                            b.isCorporateBooking()? "Corporate" : "Individual";
                    String[] bookingData = new String[]{name, postCode, bookingID, bookingType, checkIn, checkOut};
                    bookingLines[bookingCount++] = bookingData;
                }
            }
            if (bookingCount > 0) {
                BillingSystemUtils.printTable(
                        new String[]{"Customer", "Post Code", "Booking", "Type", "Check-In", "Check-Out"}, bookingLines);
                int id = getIntInput(BookingIDCount+1, "Enter booking ID to edit it: ", "Invalid booking ID.");
                int editOrRemove = getIntInput(3, "Enter 1 to edit booking or 2 to remove.");
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
                String bookingType = b.isGroupBooking()? "Group: " + ((GroupBooking)b).getGroupSize() : b.isCorporateBooking()? "Corporate" : "Individual";
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



    public void populateSystemData() {
        sys.Companies.add("Starbucks");
        sys.Companies.add("Costa Coffee");
        sys.Companies.add("Caffè Nero");
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
        sys.AddNewCustomer("Colton Maine", "03682 983662", "29 Feugiat St., Aylesbury, Buckinghamshire","RO1P 6QO");
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

        for (int i=0; i<2*CustomerIDCount; i++) {
            int customerID = r.nextInt(CustomerIDCount)+1;
            int bookingType = r.nextInt(3)+1;
            long checkIn, checkOut;
            checkIn = dateGenerate();
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

    private long dateGenerate() {
        return System.currentTimeMillis() + r.nextInt(262800000)*10 - 2628000000l;
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
}