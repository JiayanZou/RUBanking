import java.util.Random;

/**
 * This AccountNumber.java generates and stores a bank account number for the user.
 * @author Jiayan Zou Gideon Edwards
 */
public class AccountNumber implements Comparable <AccountNumber>
{
    private static final int SEED = 9999;
    private static final Random RAND = new Random(SEED);

    private static final int THOUSAND = 1000;
    private static final int HUNDRED = 100;
    private static final int TEN = 10;
    private static final int ZERO = 0;

    private Branch branch;
    private AccountType type;
    private String number; //a 4-digit number generated by the Random object

    /**
     * This is the parameterized constructor for this class.
     * @param branch
     * @param type
     */
    public AccountNumber(Branch branch, AccountType type)
    {
        this.branch = branch;
        this.type = type;

        int randomNum = RAND.nextInt(SEED);

        if (randomNum >= THOUSAND)
        {
            this.number = String.valueOf(randomNum);
        }

        else if ((randomNum < THOUSAND) && (randomNum >= HUNDRED))
        {
            this.number = "0" + String.valueOf(randomNum);
        }

        else if ((randomNum < HUNDRED) && (randomNum >= TEN))
        {
            this.number = "00" + String.valueOf(randomNum);
        }

        else if ((randomNum < TEN) && (randomNum > ZERO))
        {
            this.number = "000" + String.valueOf(randomNum);
        }
    }

    /**
     * This is the getter method for the branch.
     * @return branch
     */
    public Branch getBranch()
    {
        return this.branch;
    }

    /**
     * This is the getter method for the account type.
     * @return branch.
     */
    public AccountType getAccountType()
    {
        return this.type;
    }

    /**
     * This is the setter method for the account type.
     * @param type
     */
    public void setAccountType(AccountType type)
    {
        this.type = type;
    }

    /**
     * This is the getter method of the number for the 4-digit random number.
     * @return number
     */
    public String getNumber()
    {
        return this.number;
    }

    @Override
    public boolean equals(Object otherAccountNumber)
    {
        AccountNumber other = (AccountNumber) otherAccountNumber;

        if (!this.number.equalsIgnoreCase(other.number))
        {
            return false;
        }

        if (!this.branch.getZip().equalsIgnoreCase(other.branch.getZip()))
        {
            return false;
        }

        if (!this.branch.getBranchCode().equalsIgnoreCase(other.branch.getBranchCode()))
        {
            return false;
        }

        if (!this.branch.getCounty().equalsIgnoreCase(other.branch.getCounty()))
        {
            return false;
        }

        if (!this.type.getCode().equalsIgnoreCase(other.type.getCode()))
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return this.branch.getBranchCode() + this.type.getCode() + this.getNumber();
    }

    @Override
    public int compareTo(AccountNumber otherAccountNumber)
    {
        String thisCombination = this.toString();
        String otherCombination = otherAccountNumber.toString();

        for (int index = 0; index < thisCombination.length(); index++)
        {
            if (thisCombination.charAt(index) - '0' < otherCombination.charAt(index) - '0')
            {
                return -1;
            }

            if (thisCombination.charAt(index) - '0' > otherCombination.charAt(index) - '0')
            {
                return 1;
            }
        }

        return 0;
    }

    /**
     * This is the main testbed.
     * @param args
     */
    public static void main(String[] args)
    {
        //Setting up the user profiles.
        AccountNumber acctNumber1 = new AccountNumber(Branch.PRINCETON, AccountType.SAVINGS);
        AccountNumber acctNumber2 = new AccountNumber(Branch.PRINCETON, AccountType.SAVINGS);

        System.out.println("Account number 1: " + acctNumber1);
        System.out.println("Account number 2: " + acctNumber2);
        System.out.println("\n" + acctNumber1.equals(acctNumber2));
    }
}
