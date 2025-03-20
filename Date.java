//package rubank;

/**
 * This Date.java class stores a valid date format in mm/dd/yyyy/, solely for storage.
 * @author Jiayan Zou, Gideon Edwards
 */
public class Date implements Comparable <Date>
{
    private static final int QUADRENNIAL = 4;
    private static final int CENTENNIAL = 100;
    private static final int QUATERCENTENNIAL = 400;

    private static final int JANUARY = 1;
    private static final int FEBRUARY = 2;
    private static final int APRIL = 4;
    private static final int JUNE = 6;
    private static final int SEPTEMBER = 9;
    private static final int NOVEMBER = 11;
    private static final int DECEMBER = 12;

    private static final int MIN_DAY = 1;
    private static final int ABSOLUTE_MAX_DAY = 31;
    private static final int SMALL_DAY = 30;
    private static final int LEAP_FEB_DAY = 29;
    private static final int NON_LEAP_FEB_DAY = 28;

    private int year;
    private int month;
    private int day;

    /**
     * This is the parameterized constructor for this src file.
     * @param year
     * @param month
     * @param day
     */
    public Date(int year, int month, int day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * This helper methods checks whether the year is a leap year.
     * We do not need this to be accessible by other classes, so make it private.
     * @return true if it is, false if it is not.
     */
    private boolean isLeapYear()
    {
        if (this.year % QUADRENNIAL != 0)
        {
            return false;
        }

        if (this.year % CENTENNIAL != 0)
        {
            return true;
        }

        if (this.year % QUATERCENTENNIAL != 0)
        {
            return false;
        }

        return true;
    }

    /**
     * This is a getter method for the year.
     * @return the value of the year.
     */
    public int getYear()
    {
        return this.year;
    }

    /**
     * This is the setter method for the year.
     * @param year
     */
    public void setYear(int year)
    {
        this.year = year;
    }

    /**
     * This is a getter method for the month.
     * @return the value of the month.
     */
    public int getMonth()
    {
        return this.month;
    }

    /**
     * This is the setter method for the month.
     * @param month
     */
    public void setMonth(int month)
    {
        this.month = month;
    }

    /**
     * This is the getter method for the day.
     * @return the day of the month.
     */
    public int getDay()
    {
        return this.day;
    }

    /**
     * This is the setter method for the day.
     * @param day
     */
    public void setDay(int day)
    {
        this.day = day;
    }

    @Override
    public boolean equals(Object date)
    {
        Date other = (Date) date;

        if (this.getYear() != other.getYear())
        {
            return false;
        }

        if (this.getMonth() != other.getMonth())
        {
            return false;
        }

        if (this.getDay() != other.getDay())
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return this.getMonth() + "/" + this.getDay() + "/" + this.getYear();
    }

    @Override
    public int compareTo(Date other)
    {
        if (this.getYear() > other.getYear())
        {
            return 1;
        }

        if (this.getYear() < other.getYear())
        {
            return -1;
        }

        if (this.getYear() == other.getYear())
        {
            if (this.getMonth() > other.getMonth())
            {
                return 1;
            }

            if (this.getMonth() < other.getMonth())
            {
                return -1;
            }

            if (this.getMonth() == other.getMonth())
            {
                if (this.getDay() > other.getDay())
                {
                    return 1;
                }

                if (this.getDay() < other.getDay())
                {
                    return -1;
                }
            }
        }

        return 0;
    }

    /**
     * This method evaluates if the given date is valid.
     * @return true if valid, false if invalid.
     */
    public boolean isValid()
    {
        if (this.getMonth() < JANUARY || this.getMonth() > DECEMBER)
        {
            return false;
        }

        if (this.getDay() < MIN_DAY || this.getDay() > ABSOLUTE_MAX_DAY)
        {
            return false;
        }

        if ((this.getDay() > SMALL_DAY) && (this.getMonth() == FEBRUARY || this.getMonth() == APRIL
                || this.getMonth() == JUNE || this.getMonth() == SEPTEMBER || this.getMonth() == NOVEMBER))
        {
            return false;
        }

        if ((this.isLeapYear()) && (this.getMonth() == FEBRUARY && this.getDay() > LEAP_FEB_DAY))
        {
            return false;
        }

        if ((!this.isLeapYear()) && (this.getMonth() == FEBRUARY && this.getDay() > NON_LEAP_FEB_DAY))
        {
            return false;
        }

        return true;
    }

    /**
     * This is the main test bed.
     * @param args
     */
    public static void main(String[] args)
    {
        //Case 1: Invalid Date - 2/29 on a leap year
    }
}

