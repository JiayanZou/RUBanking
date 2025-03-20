public enum AccountType
{
    CHECKING("01"),
    SAVINGS("02"),
    MONEYMARKET("03");

    private final String code;

    AccountType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    @Override
    public String toString()
    {
        return name().toLowerCase(); // Returns the account type name in lowercase
    }

    public static AccountType fromString(String type)
    {
        for (AccountType at : values())
        {
            if (at.name().equalsIgnoreCase(type))
            {
                return at;
            }
        }

        return null; // Return null if no matching type is found
    }
}