/**
 * This is the class of the user accounts.
 * @author Jiayan Zou, Gideon Edwards
 */
public class Account implements Comparable <Account>
{
    private static final int MARKETMIN = 2000;

    private AccountNumber number;
    private Profile holder;
    private double balance;

    /**
     * This is the constructor for the Account.java class.
     * @param number
     * @param holder
     * @param balance
     */
    public Account(AccountNumber number, Profile holder, double balance)
    {
        this.number = number;
        this.holder = holder;
        this.balance = balance;
    }

    /**
     * This is the getter method for the account number.
     * @return number
     */
    public AccountNumber getNumber()
    {
        return number;
    }

    /**
     * This is the getter method for the user profile.
     * @return holder
     */
    public Profile getProfile()
    {
        return holder;
    }

    /**
     * THis is the getter method for the profile balance.
     * @return balance
     */
    public double getBalance()
    {
        return balance;
    }

    /**
     * This is the setter method for the profile balance.
     * @param balance
     */
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object otherAccount)
    {
        Account other = (Account) otherAccount;

        if (!this.getNumber().equals(other.getNumber()))
        {
            return false;
        }

        if (!this.getProfile().equals(other.getProfile()))
        {
            return false;
        }

        if (this.getBalance() != other.getBalance())
        {
            return false;
        }

        return true;
    }

    /**
     * This method gets the location name.
     * @return the name of the location.
     */
    public String getLocation(AccountNumber number)
    {
        if (number.getBranch().getBranchCode().equalsIgnoreCase("100"))
        {
            return "EDISON";
        }

        if (number.getBranch().getBranchCode().equalsIgnoreCase("200"))
        {
            return "BRIDGEWATER";
        }

        if (number.getBranch().getBranchCode().equalsIgnoreCase("300"))
        {
            return "PRINCETON";
        }

        if (number.getBranch().getBranchCode().equalsIgnoreCase("400"))
        {
            return "PISCATAWAY";
        }

        if (number.getBranch().getBranchCode().equalsIgnoreCase("500"))
        {
            return "WARREN";
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "Account#[" + this.getNumber() + "] Holder[" + this.getProfile()
                + "] Balance[$" + this.getBalance() + "0] Branch[" + getLocation(this.getNumber()) + "]";
    }

    /**
     * This function compares the accounts by branch.
     * @param otherAccount
     * @return 1, 0, or -1
     */
    public int compareByBranch(Account otherAccount)
    {
        if (this.getNumber().getBranch().getCounty().compareTo
                (otherAccount.getNumber().getBranch().getCounty()) > 0)
        {
            return 1;
        }

        if (this.getNumber().getBranch().getCounty().compareTo
                (otherAccount.getNumber().getBranch().getCounty()) < 0)
        {
            return -1;
        }

        if (this.getNumber().getBranch().getBranchCode().compareTo
                (otherAccount.getNumber().getBranch().getBranchCode()) > 0)
        {
            return 1;
        }

        if (this.getNumber().getBranch().getBranchCode().compareTo
                (otherAccount.getNumber().getBranch().getBranchCode()) < 0)
        {
            return -1;
        }

        return 0;
    }

    /**
     * This function compares the accounts by account type then number.
     * @param otherAccount
     * @return 1, 0, or -1
     */
    public int compareByAccount(Account otherAccount)
    {
        if (this.getNumber().getAccountType().getCode().compareTo
                (otherAccount.getNumber().getAccountType().getCode()) > 0)
        {
            return 1;
        }

        if (this.getNumber().getAccountType().getCode().compareTo
                (otherAccount.getNumber().getAccountType().getCode()) < 0)
        {
            return -1;
        }

        if (this.getNumber().compareTo(otherAccount.getNumber()) > 0)
        {
            return 1;
        }

        if (this.getNumber().compareTo(otherAccount.getNumber()) < 0)
        {
            return -1;
        }

        return 0;
    }

    @Override
    public int compareTo(Account otherAccount)
    {
        if (this.getProfile().compareTo(otherAccount.getProfile()) > 0)
        {
            return 1;
        }

        if (this.getProfile().compareTo(otherAccount.getProfile()) < 0)
        {
            return -1;
        }

        if (this.getNumber().compareTo(otherAccount.getNumber()) > 0)
        {
            return 1;
        }

        if (this.getNumber().compareTo(otherAccount.getNumber()) < 0)
        {
            return -1;
        }

        return 0;
    }

    /**
     * This function withdraws the amount if it is legitimate.
     * @param amount
     */
    public void withdraw(double amount)
    {
        if (amount <= this.getBalance())
        {
            this.setBalance(this.getBalance() - amount);

            if (this.getNumber().getAccountType().getCode().equals("03") && this.getBalance() < MARKETMIN)
            {
                this.getNumber().setAccountType(AccountType.SAVINGS);
            }
        }
    }

    /**
     * This function deposit some money, if it is legitimate.
     * @param amount
     */
    public void deposit(double amount)
    {
        if (amount > 0)
        {
            this.setBalance(this.getBalance() + amount);
        }
    }
}
