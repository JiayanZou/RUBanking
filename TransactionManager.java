import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Calendar;

public class TransactionManager
{

    private AccountDatabase db;

    public TransactionManager()
    {
        db = new AccountDatabase();
    }

    public void run()
    {
        Scanner scanner = new Scanner(System.in);
        StringBuilder inputText = new StringBuilder();

        System.out.println("Transaction Manager is running.\n");

        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine().trim();

            if (line.equals("Q"))
            {
                break;
            }

            inputText.append(line).append("\n");
        }

        scanner.close();

        String[] lines = inputText.toString().split("\n");

        for (String line : lines)
        {
            if (line.isEmpty())
            {
                continue;
            }

            StringTokenizer st = new StringTokenizer(line);
            String command = st.nextToken();

            switch (command)
            {
                case "O": handleOpen(st); break;
                case "C": handleClose(st); break;
                case "D": handleDeposit(st); break;
                case "W": handleWithdraw(st); break;
                case "P":
                case "PA":
                case "PB":
                case "PH":
                case "PT":
                    if (isDatabaseEmpty())
                    {
                        System.out.println("Account database is empty!");
                    }

                    else
                    {
                        handlePrintCommand(command);
                    }

                    break;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
        }

        System.out.println("Transaction Manager is terminated.");
    }

    private void handlePrintCommand(String command)
    {
        switch (command)
        {
            case "P":
                handlePrint();
                break;
            case "PA":
                db.printArchive();
                break;
            case "PB":
                db.printByBranch();
                break;
            case "PH":
                db.printByHolder();
                break;
            case "PT":
                db.printByType();
                break;
        }
    }

    private boolean isDatabaseEmpty()
    {
        return db.getCount() == 0;
    }

    /**
     * Returns the current date as a Date object using the custom Date class.
     */
    private Date getCurrentDate()
    {
        Calendar today = Calendar.getInstance();

        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based.
        int day = today.get(Calendar.DAY_OF_MONTH);

        return new Date(year, month, day);
    }

    /**
     * Processes the "O" command to open a new account.
     * Expected format:
     *   O <accountType> <branchCity> <firstName> <lastName> <mm/dd/yyyy> <initialDeposit>
     */
    private void handleOpen(StringTokenizer st)
    {
        if (st.countTokens() < 6)
        {
            System.out.println("Invalid open account command. Not enough parameters.");
            return;
        }

        String accountTypeStr = st.nextToken();

        AccountType accountType = AccountType.fromString(accountTypeStr);

        if (accountType == null)
        {
            System.out.println(accountTypeStr + " - invalid account type.");
            return;
        }

        String branchCity = st.nextToken();

        Branch branch = Branch.fromCityName(branchCity);

        if (branch == null)
        {
            System.out.println(branchCity + " - invalid branch.");
            return;
        }

        String firstName = st.nextToken();
        String lastName = st.nextToken();
        String dobStr = st.nextToken(); // expected format: mm/dd/yyyy
        Date dob = parseAndValidateDOB(dobStr);

        if (dob == null)
        {
            return; // Error message already printed.
        }

        if (!isAtLeast18(dob))
        {
            System.out.println("Not eligible to open: " + dobStr + " under 18.");
            return;
        }

        String depositStr = st.nextToken();
        double deposit;

        try
        {
            deposit = Double.parseDouble(depositStr);
        }

        catch (NumberFormatException e)
        {
            System.out.println("For input string: \"" + depositStr + "\" - not a valid amount.");
            return;
        }

        // Adjust error messages to match expected output:
        if (deposit < 0)
        {
            System.out.println("Initial deposit must be positive.");
            return;
        }

        if (deposit == 0)
        {
            System.out.println("Initial deposit cannot be 0 or negative.");
            return;
        }

        if (accountType == AccountType.MONEYMARKET && deposit < 2000)
        {
            System.out.println("Minimum of $2,000 to open a Money Market account.");
            return;
        }

        Profile profile = new Profile(firstName, lastName, dob.getYear(), dob.getMonth(), dob.getDay());

        if (hasDuplicateAccount(profile, accountType))
        {
            System.out.println(firstName + " " + lastName + " already has a "
                    + accountType.name().toLowerCase() + " account.");
            return;
        }

        AccountNumber acctNum = new AccountNumber(branch, accountType);
        Account account = new Account(acctNum, profile, deposit);
        db.add(account);

        System.out.println(account.getNumber().getAccountType().name() + " account "
                + account.getNumber().toString() + " has been opened.");
    }

    /**
     * Parses the given date string and validates it using the custom Date class.
     */
    private Date parseAndValidateDOB(String dobStr)
    {
        String[] tokens = dobStr.split("/");

        if (tokens.length != 3)
        {
            System.out.println("DOB invalid: " + dobStr + " not a valid calendar date!");
            return null;
        }

        int month, day, year;

        try
        {
            month = Integer.parseInt(tokens[0]);
            day = Integer.parseInt(tokens[1]);
            year = Integer.parseInt(tokens[2]);
        }

        catch (NumberFormatException e)
        {
            System.out.println("DOB invalid: " + dobStr + " not a valid calendar date!");
            return null;
        }

        Date dob = new Date(year, month, day);

        if (!dob.isValid())
        {
            System.out.println("DOB invalid: " + dobStr + " not a valid calendar date!");
            return null;
        }

        // Ensure DOB is not today or in the future
        Date currentDate = getCurrentDate();
        if (dob.compareTo(currentDate) >= 0)
        {
            System.out.println("DOB invalid: " + dobStr + " cannot be today or a future day.");
            return null;
        }

        return dob;
    }

    /**
     * Checks if the given DOB represents an age of at least 18.
     */
    private boolean isAtLeast18(Date dob)
    {
        Date currentDate = getCurrentDate();
        int age = currentDate.getYear() - dob.getYear();
        if (currentDate.getMonth() < dob.getMonth() ||
                (currentDate.getMonth() == dob.getMonth() && currentDate.getDay() < dob.getDay()))
        {
            age--;
        }

        return age >= 18;
    }

    /**
     * Checks whether an account for the given profile with the specified account type already exists.
     */
    private boolean hasDuplicateAccount(Profile profile, AccountType accountType) {
        Account[] accounts = db.getAccounts();
        int count = db.getCount();

        for (int i = 0; i < count; i++)
        {
            if (accounts[i] != null)
            {
                Profile existingProfile = accounts[i].getProfile();
                AccountType existingType = accounts[i].getNumber().getAccountType();
                if (existingProfile.equals(profile) && existingType.equals(accountType))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Processes the "C" command to close account(s).
     * Two forms are supported:
     *   C <accountNumber>        (close a single account)
     *   C <firstName> <lastName> <mm/dd/yyyy>  (close all accounts for a profile)
     */
    private void handleClose(StringTokenizer st)
    {
        if (!st.hasMoreTokens())
        {
            System.out.println("Invalid close command.");
            return;
        }

        String token = st.nextToken();

        if (token.matches("\\d{9}"))
        {
            Account account = findAccountByNumber(token);

            if (account == null)
            {
                System.out.println(token + " account does not exist.");
            }

            else
            {
                db.remove(account);
                System.out.println(token + " is closed and moved to archive; balance set to 0.");
            }
        }

        else
        {
            // Treat tokens as profile information.
            String firstName = token;

            if (!st.hasMoreTokens())
            {
                System.out.println("Invalid close command for profile.");
                return;
            }

            String lastName = st.nextToken();

            if (!st.hasMoreTokens())
            {
                System.out.println("Invalid close command for profile.");
                return;
            }

            String dobStr = st.nextToken();

            Date dob = parseAndValidateDOB(dobStr);

            if (dob == null)
            {
                return;
            }

            Profile profile = new Profile(firstName, lastName, dob.getYear(), dob.getMonth(), dob.getDay());
            boolean found = false;
            Account[] accounts = db.getAccounts();
            int currentCount = db.getCount();

            for (int i = 0; i < currentCount; i++)
            {
                if (accounts[i] != null && accounts[i].getProfile().equals(profile))
                {
                    db.remove(accounts[i]);
                    found = true;
                    i--; // adjust index due to removal
                    currentCount = db.getCount();
                }
            }

            if (!found)
            {
                System.out.println(profile + " does not have any accounts in the database.");
            }

            else
            {
                System.out.println("All accounts for " + profile
                        + " are closed and moved to archive; balance set to 0.");
            }
        }
    }

    /**
     * Processes the "D" command to deposit money.
     * Expected format:
     *   D <accountNumber> <amount>
     */
    private void handleDeposit(StringTokenizer st)
    {
        if (st.countTokens() < 2)
        {
            System.out.println("Invalid deposit command.");
            return;
        }

        String accountNumStr = st.nextToken();
        String amountStr = st.nextToken();
        double amount;

        try
        {
            amount = Double.parseDouble(amountStr);
        }

        catch (NumberFormatException e)
        {
            System.out.println("For input string: \"" + amountStr + "\" - not a valid amount.");
            return;
        }

        if (amount <= 0)
        {
            System.out.println(amount + " - deposit amount cannot be 0 or negative.");
            return;
        }

        Account account = findAccountByNumber(accountNumStr);

        if (account == null)
        {
            System.out.println(accountNumStr + " account does not exist.");
            return;
        }

        db.deposit(account.getNumber(), amount);
        System.out.println("$" + String.format("%.2f", amount) + " deposited to "
                + account.getNumber().toString());
    }

    /**
     * Processes the "W" command to withdraw money.
     * Expected format:
     *   W <accountNumber> <amount>
     */
    private void handleWithdraw(StringTokenizer st)
    {
        if (st.countTokens() < 2)
        {
            System.out.println("Invalid withdraw command."); return;
        }

        String accountNumStr = st.nextToken(), amountStr = st.nextToken();
        double amount;

        try
        {
            amount = Double.parseDouble(amountStr);
        }

        catch (NumberFormatException e)
        {
            System.out.println("For input string: \"" + amountStr + "\" - not a valid amount."); return;
        }

        if (amount <= 0)
        {
            System.out.println(amount + " withdrawal amount cannot be 0 or negative."); return;
        }

        Account account = findAccountByNumber(accountNumStr);

        if (account == null)
        {
            System.out.println(accountNumStr + " does not exist."); return;
        }

        String tempNum = account.getNumber().toString();
        String tempType = account.getNumber().getAccountType().getCode();

        if (!db.withdraw(account.getNumber(), amount))
        {
            System.out.println(account.getNumber().toString() + " - insufficient funds.");
        }

        else
        {
            if ((tempType.equals("03")) && (account.getBalance() < 2000))
            {
                System.out.print(tempNum + " is downgraded to SAVINGS - ");
            }

            System.out.println("$" + String.format("%.2f", amount) + " withdrawn from " + tempNum);
        }
    }

    /**
     * Processes the "P" command to print the accounts in their natural order.
     */
    private void handlePrint()
    {
        db.print();
    }

    /**
     * Finds and returns an account with the specified account number.
     */
    private Account findAccountByNumber(String accountNumStr)
    {
        Account[] accounts = db.getAccounts();
        int currentCount = db.getCount();

        for (int i = 0; i < currentCount; i++)
        {
            if (accounts[i] != null && accounts[i].getNumber().toString().equals(accountNumStr))
            {
                return accounts[i];
            }
        }

        return null;
    }
}