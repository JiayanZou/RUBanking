/**
 * This class defines the database for the existing accounts.
 * @author Jiayan Zou, Gideon Edwards.
 */
public class AccountDatabase
{
    private static final int INITIAL_CAPACITY = 4;
    private static final int GROWTH_INCREMENT = 4;
    private static final int NOT_FOUND = -1;
    private static final double MINIMUM = 2000;
    private static final double ZERO = 0.0;

    private Account[] accounts;
    private int size;
    private Archive archive;

    /**
     * Default constructor initializes the database.
     */
    public AccountDatabase()
    {
        this.accounts = new Account[INITIAL_CAPACITY];
        this.size = 0;// Needs to be zero otherwise null errors Ian.
        this.archive = new Archive();
    }

    /**
     * Returns the index of the target account, or NOT_FOUND (-1) if not found.
     * @param account the account to find.
     * @return index if found; -1 otherwise.
     */
    private int find(Account account)
    {
        for (int index = 0; index < size; index++)
        {
            if (accounts[index] != null && accounts[index].equals(account))
            {
                return index;
            }
        }

        return NOT_FOUND; //NUll Check as well.
    }

    /**
     * Increases the accounts array capacity by the growth increment.
     */
    private void grow()
    {
        Account[] newAccounts = new Account[accounts.length + GROWTH_INCREMENT]; //Correct me if Im wrong but .legnth seems like an easier way to do this,

        for (int index = 0; index < accounts.length; index++)
        {
            newAccounts[index] = accounts[index];
        }

        accounts = newAccounts;
    }

    /**
     * Checks whether the database contains a given account.
     * @param account the account to check.
     * @return true if found; false otherwise.
     */
    public boolean contains(Account account) //WORKS FOR ME!
    {
        for (int index = 0; index < size; index++)
        {
            if (account.equals(accounts[index]))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds an account to the end of the array.
     * @param account the account to add.
     */
    public void add(Account account)
    {
        if (account == null)
        {
            return;
        }

        if (size == accounts.length)
        {
            grow();
        }

        accounts[size] = account;
        size++;
    }

    /**
     * Removes an account from the database and adds it to the archive.
     * Replacement is done with the last account in the array.
     * @param account the account to remove.
     */
    public void remove(Account account)
    {
        for (int index = 0; index < size; index++)
        {
            if (accounts[index].equals(account))
            {
                account.setBalance(this.ZERO);

                accounts[index] = accounts[size - 1];
                accounts[size - 1] = null;
                size--;
                archive.add(account);
                break;
            }
        }
    }

    /**
     * Withdraws a specified amount from the account with the given number.
     * If the account is a money market account and the new balance falls below MINIMUM,
     * the account is downgraded to Savings.
     * @param number the account number.
     * @param amount the amount to withdraw.
     * @return true if withdrawal succeeds; false otherwise.
     */
    public boolean withdraw(AccountNumber number, double amount) //made boolean for transaction manager.
    {
        if (amount < 0) return false;

        for (int index = 0; index < size; index++)
        {
            if (accounts[index] != null && accounts[index].getNumber().equals(number))
            {
                if (accounts[index].getBalance() < amount)
                {
                    return false;
                }

                accounts[index].setBalance(accounts[index].getBalance() - amount);

                if (accounts[index].getNumber().getAccountType().getCode().equals("03"))
                {
                    if (accounts[index].getBalance() < MINIMUM)
                    {
                        accounts[index].getNumber().setAccountType(AccountType.SAVINGS);
                    }
                }

                return true;
            }
        }

        return false;
    }
    /**
     * Deposits a specified amount into the account with the given number.
     * @param number the account number.
     * @param amount the amount to deposit.
     */
    public void deposit(AccountNumber number, double amount)
    { //refactored to prevent my NUll pointer pains.
        if (amount < 0)
        {
            return;
        }

        for (int i = 0; i < size; i++)
        {
            if (accounts[i] != null && accounts[i].getNumber().equals(number))
            {
                accounts[i].setBalance(accounts[i].getBalance() + amount);
                return;
            }
        }
    }

    /**
     * Returns the array of accounts.
     */
    public Account[] getAccounts()
    { //needed way to get accounts
        return accounts;
    }

    /**
     * Prints the archived accounts.
     */
    public void printArchive()
    {
        archive.print();
    }

    /**
     * Returns the number of accounts currently stored.
     * @return the count of accounts.
     */
    public int getCount()
    {
        return size;
    }

    /**
     * Prints the accounts in their natural (stored) order.
     * The output includes a header and footer.
     */
    public void print()
    {
        System.out.println("\n*List of accounts in the account database.");

        for (int i = 0; i < getCount(); i++)
        {
            System.out.println(accounts[i].toString());
        }

        System.out.println("*end of list.\n");
    }

    /**
     * Prints the accounts sorted by branch (first by county then branch code).
     * The output includes a header and footer.
     */
    public void printByBranch()
    {
        // Insertion sort by branch.
        for (int i = 1; i < size; i++)
        {
            Account key = accounts[i];
            int j = i - 1;

            while (j >= 0 && accounts[j].compareByBranch(key) > 0)
            {
                accounts[j + 1] = accounts[j];
                j--;
            }

            accounts[j + 1] = key;
        }

        System.out.println("\n*List of accounts ordered by branch location (county, city).\nCounty: Mercer");
        String county = accounts[0].getNumber().getBranch().getCounty();

        for (int i = 0; i < size; i++)
        {
            if (!county.equalsIgnoreCase(accounts[i].getNumber().getBranch().getCounty()))
            {
                System.out.println("County: " + accounts[i].getNumber().getBranch().getCounty());
                county = accounts[i].getNumber().getBranch().getCounty();
            }

            System.out.println(accounts[i].toString());
        }

        System.out.println("*end of list.\n");
    }

    /**
     * Prints the accounts sorted by holder's name.
     * The output includes a header and footer.
     */
    public void printByHolder()
    {
        for (int i = 1; i < size; i++)
        {
            Account key = accounts[i];
            int j = i - 1;

            while (j >= 0 && accounts[j].compareTo(key) > 0)
            {
                accounts[j + 1] = accounts[j];
                j--;
            }

            accounts[j + 1] = key;
        }

        System.out.println("\n*List of accounts ordered by account holder and number.");

        for (int i = 0; i < size; i++)
        {
            System.out.println(accounts[i].toString());
        }

        System.out.println("*end of list.\n");
    }

    /**
     * This function informs of a type chance when printing.
     * @param code
     */
    private void informType(String code)
    {
        if (code.equals("01"))
        {
            System.out.println("Account Type: CHECKING");
        }

        else if (code.equals("02"))
        {
            System.out.println("Account Type: SAVINGS");
        }

        else if (code.equals("03"))
        {
            System.out.println("Account Type: MONEY_MARKET");
        }
    }

    /**
     * Prints the accounts sorted by account type, then by account number.
     * The output includes a header and footer.
     */
    public void printByType()
    {
        for (int i = 1; i < size; i++)
        {
            Account key = accounts[i]; int j = i - 1;

            while (j >= 0 && accounts[j].compareByAccount(key) > 0)
            {
                accounts[j + 1] = accounts[j]; j--;
            }

            accounts[j + 1] = key;
        }

        System.out.println("\n*List of accounts ordered by account type and number.\nAccount Type: CHECKINGS");
        String type = accounts[0].getNumber().getAccountType().getCode();

        for (int i = 0; i < size; i++)
        {
            if (!type.equalsIgnoreCase(accounts[i].getNumber().getAccountType().getCode()))
            {
                this.informType(accounts[i].getNumber().getAccountType().getCode());
                type = accounts[i].getNumber().getAccountType().getCode();
            }

            System.out.println(accounts[i].toString());
        }

        System.out.println("*end of list.\n");
    }
}