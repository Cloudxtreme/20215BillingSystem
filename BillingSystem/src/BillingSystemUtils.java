import java.util.Scanner;

/**
 * Created by Damask on 19/02/15.
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
            "║ 4 ║ Booking lookup (or view all bookings)                                   ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 5 ║ Customer search (or view all customers)                                 ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 6 ║ Remove booking                                                          ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 7 ║ Manage customers                                                        ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 8 ║ Generate bill                                                           ║\n" +
            "╟───╫─────────────────────────────────────────────────────────────────────────╢\n" +
            "║ 9 ║ Quit                                                                    ║\n" +
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


    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
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
}
