import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class BillingSystem objects - formatting, validation and I/O
 */
public final class BillingSystemUtils {

    private static final Scanner pause = new Scanner(System.in);

    public static final String MENU_OPTIONS =
            "╔════════════════╗\n" +
            "║ Billing System ║\n" +
            "╠═══╦════════════╩════════════════════════════════════════════════════════════╗\n" +
            "║ 1 ║ Add new customer                                                        ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 2 ║ Create booking for new customer                                         ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 3 ║ Create booking for existing customer                                    ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 4 ║ View all bookings                                                       ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 5 ║ Customer lookup                                                         ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 6 ║ Manage bookings                                                         ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 7 ║ Generate bill                                                           ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 8 ║ Quit                                                                    ║\n" +
            "╚═══╩═════════════════════════════════════════════════════════════════════════╝\n";

    public static final String TITLE =
            "╔════════════════╗\n" +
            "║ Billing System ║\n" +
            "╚════════════════╝";

    public static final String PROMPT =
            "╔╦════════════════════════════════════════════════════════════════════════════╗\n" +
            "║║ ";

    public static final String LINE_START = "║║ ";

    public static final String CONT_MESSAGE = "Hit return to continue.";
    public static final String DEFAULT_ERR = "Invalid entry.";

    // Instance of random for populating the data set
    private static Random r = new Random();
    // List of Companies for data generation
    private static ArrayList<String> Companies = new ArrayList<String>();

    // The date format accepted for I/O
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
    /**
     * Converts a given date from storage format into output format
     * @param milliseconds
     */
    public static String dateFormat(long milliseconds) {
        return dateFormat.format(new Date(milliseconds));
    }


    public static String padRight(String s, int column) {
        return String.format("%1$-" + column + "s", s);
    }

    public static void printTable(String[] headings, String[][] cells) {
        int cols = headings.length;
        int colSizes[] = new int[cols];
        for (int i=0; i<cells[0].length; i++) {
            colSizes[i] = headings[i].length();
            for (int j=0; j<cells.length; j++) {
                if (cells[j][i].length() > colSizes[i]) {
                    colSizes[i] = cells[j][i].length();
                }
            }
        }
        System.out.print(PROMPT);
        for (int i=0; i<headings.length; i++) {
            System.out.print(padRight(headings[i], colSizes[i]+2));
        }
        System.out.println();
        for (int j=0; j<cells.length; j++) {
            System.out.print(LINE_START);
            for (int i = 0; i < headings.length; i++) {
                System.out.print(padRight(cells[j][i],colSizes[i]+2));
            }
            System.out.println();
        }
    }

    public static void clearConsole() {
        System.out.print(String.format("\033[2J"));
        System.out.println(TITLE);

    }

    public static void menu() {
        System.out.print(String.format("\033[2J"));
        System.out.println(MENU_OPTIONS);
    }

    public static void confirmContinue() {
        System.out.println(PROMPT + CONT_MESSAGE);
        pause.nextLine();
    }

    public static void confirmContinue(String message) {
        System.out.println(PROMPT + message + " - " + CONT_MESSAGE);
        pause.nextLine();
    }

    public static void headingText(String message) {
        System.out.print(PROMPT + message);
    }

    public static void bodyText(String message) {
        System.out.print(LINE_START + message);
    }

    public static int getIntInput(int upperBound, String inputText, String errorText) {
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

    public static int getIntInput(int upperBound, String inputText) {
        return getIntInput(upperBound, inputText, BillingSystemUtils.DEFAULT_ERR);
    }

    public static String getInput(String inputText) {
        return getInput(inputText, BillingSystemUtils.DEFAULT_ERR);
    }

    public static String getInput(String inputText, String errorText) {
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

    public static long getDateInput(String inputText) {
        return getDateInput(inputText, BillingSystemUtils.DEFAULT_ERR, Long.MAX_VALUE, 0l);
    }


    public static long getDateInput(String inputText, String errorText, long before, long after) {
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



    public static void populateSystemData(BillingSystem sys) {
        Companies.add("Starbucks");
        Companies.add("Costa");
        Companies.add("Caffè Nero");
        sys.addNewCustomer("Kiara Kirk", "02678 001547", "7 Molestie St.,Jedburgh, Roxburghshire", "V7C 2NA");
        sys.addNewCustomer("Milly Peck", "09271 533863", "5B Ante Av.,Portsmouth, Hampshire", "W3 5QC");
        sys.addNewCustomer("Adeline Avery", "05991 642782", "118 North St.,Portree, Inverness", "QX1F 8FC");
        sys.addNewCustomer("Chad Mathews", "04351 374396", "6061 Mauris. St.,Crewe, Cheshire", "YQ0I 1SZ");
        sys.addNewCustomer("Sasha Walker", "08699 397440", "48 Mountainside Road,Lockerbie, Dumfriesshire", "PJ4 6XE");
        sys.addNewCustomer("Nita Mccullough", "01053 186773", "1725 Eget Rd.,Southwell, Nottinghamshire", "NG42 7CJ");
        sys.addNewCustomer("Melvin Delaney", "04394 839128", "45 Aliquam Rd, Bromyard, Herefordshire", "PC9 6UN");
        sys.addNewCustomer("Griffith Mullins", "07188 153600", "77 Seaside Ave., Bridlington,Yorkshire", "Y8D 0QQ");
        sys.addNewCustomer("Sebastian Mason", "01714 170624", "175 Bay St., Kinross, Kinross-shire", "MV99 9AI");
        sys.addNewCustomer("Fay Black", "01684 038533", "2350 Sed, St., Maidstone, Kent", "L0 9NA");
        sys.addNewCustomer("Tatiana Sears", "03463 610913", "4 Hendrerit Av.,Tewkesbury, Gloucestershire", "KX46 1NT");
        sys.addNewCustomer("Rowan Wright", "09519 543273", "34 Velvet Rd., Barrow-in-Furness,Lancashire", "S3 4NY");
        sys.addNewCustomer("Craig Stanton", "05795 566600", "7D Eugene Ave, Llandrindod Wells, Radnorshire", "I2 6NX");
        sys.addNewCustomer("Colton Maine", "03682 983662", "29 Feugiat St., Aylesbury, Buckinghamshire", "RO1P 6QO");
        sys.addNewCustomer("Abigail Petersen", "08199 208651", "80 Dolor St., Dundee, Angus", "JL6 8EA");
        sys.addNewCustomer("Kenneth Wilkinson", "04170 723460", "6860 Risus. Rd., Trowbridge, Wiltshire", "EL39 8RH");
        sys.addNewCustomer("Dominique Pitts", "09516 368779", "286 Alford Avenue, Leominster, Herefordshire", "OU6C 4IY");
        sys.addNewCustomer("Mariam Mullen", "03450 255848", "37 Convallis St., Stonehaven, Kincardineshire", "CT7 4LW");
        sys.addNewCustomer("Meghan Barlow", "05694 203694", "151 Suspendisse Ave.,Nairn, Nairnshire", "Q6 9KY");
        sys.addNewCustomer("Clark Stein", "08038 056955", "5 Neque Ave, Sromness, Orkney", "IT9L 1TH");
        sys.addNewCustomer("Lucy Howard", "04877 577526", "39 Sollicitudin Street, Wimbledon, Surrey", "SW14 3SL");
        sys.addNewCustomer("Cole Falderon", "01468 244318", "14 Elementum, Av.,Wrexham, Denbighshire", "WX36 1ZV");
        sys.addNewCustomer("Jordan Guzman", "06088 484468", "22A Convallis Street, Truro, Cornwall", "BU7 4QX");
        sys.addNewCustomer("Daphne Willis", "05056 497556", "64 Noon Road, Newtown, Montgomeryshire", "MO1 2LC");
        sys.addNewCustomer("Judith Reilly", "08390 478150", "67 South Avenue, Broxburn, West Lothian", "I48 3MM");
        int CustomerIDCount = sys.getCustomerIDCount();
        for (int i=0; i<2*CustomerIDCount; i++) {
            int customerID = r.nextInt(CustomerIDCount)+1;
            int bookingType = r.nextInt(3)+1;
            long checkIn, checkOut;
            checkIn = dateGenerate();
            checkOut = checkIn + randomStayLength();
            switch (bookingType) {
                case 1:
                    sys.addNewBooking(customerID, checkIn, checkOut);
                    break;
                case 2:
                    sys.addNewBooking(customerID, randomGroupSize(), checkIn, checkOut);
                    break;
                case 3:
                    sys.addNewBooking(randomCompanyName(), customerID, checkIn, checkOut);
                    break;
            }
        }
    }

    private static long dateGenerate() {
        return System.currentTimeMillis() + r.nextInt(262800000)*10 - 2628000000l;
    }
    private static long randomStayLength() {
        return 86400000 * (r.nextInt(14)+1);
    }
    private static int randomGroupSize() {
        return r.nextInt(4)+2;
    }
    private static String randomCompanyName() {
        return Companies.get(r.nextInt(Companies.size()));
    }
}
