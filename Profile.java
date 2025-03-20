/**
 * This class implements a user profile.
 * @author Jiayan Zou, Gideon Edwards
 */
public class Profile implements Comparable <Profile>
{
    private String fname;
    private String lname;
    private Date dob;

    /**
     * This is the parameterized constructor for this Profile.java class
     * @param fname
     * @param lname
     * @param year
     * @param month
     * @param day
     */
    public Profile(String fname, String lname, int year, int month, int day)
    {
        this.fname = fname;
        this.lname = lname;
        this.dob = new Date(year, month, day);
    }

    /**
     * This is a getter method for the first name.
     * @return fname, the first name.
     */
    public String getFirstName()
    {
        return fname;
    }

    /**
     * This is the setter method for the first name.
     * @param fname
     */
    public void setFirstName(String fname)
    {
        this.fname = fname;
    }

    /**
     * This is a getter method for the last name.
     * @return fname, the first name.
     */
    public String getLastName()
    {
        return lname;
    }

    /**
     * This is a setter method for the last name.
     * @param lname
     */
    public void setLastName(String lname)
    {
        this.lname = lname;
    }

    /**
     * This is a getter method for the date of birth.
     * @return dob, a class variable.
     */
    public Date getDOB()
    {
        return dob;
    }

    /**
     * This is a setter method for the date of birth
     * @param dob
     */
    public void setDOB(Date dob)
    {
        this.dob = dob;
    }

    @Override
    public boolean equals(Object person)
    {
        Profile client = (Profile) person;

        if (!this.getFirstName().equalsIgnoreCase(client.getFirstName()))
        {
            return false;
        }

        if (!this.getLastName().equalsIgnoreCase(client.getLastName()))
        {
            return false;
        }

        if (!this.getDOB().equals(client.getDOB()))
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return this.getFirstName() + " " + this.getLastName() + " " + this.getDOB().toString();
    }

    @Override
    public int compareTo(Profile person)
    {
        if (this.getLastName().compareToIgnoreCase(person.getLastName()) > 0)
        {
            return 1;
        }

        else if (this.getLastName().compareToIgnoreCase(person.getLastName()) < 0)
        {
            return -1;
        }

        if (this.getFirstName().compareToIgnoreCase(person.getFirstName()) > 0)
        {
            return 1;
        }

        if (this.getFirstName().compareToIgnoreCase(person.getFirstName()) < 0)
        {
            return -1;
        }

        if (this.getDOB().compareTo(person.getDOB()) > 0)
        {
            return 1;
        }

        if (this.getDOB().compareTo(person.getDOB()) < 0)
        {
            return -1;
        }

        return 0;
    }

    /**
     * This is the main test method.
     * @param args
     */
    public static void main(String[] args)
    {
        //Let the user create two accounts.
        Profile[] profiles = new Profile[2];
        profiles[0] = new Profile(args[0], args[1], Integer.parseInt(args[2]),
                Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        profiles[1] = new Profile(args[5], args[6], Integer.parseInt(args[7]),
                Integer.parseInt(args[8]), Integer.parseInt(args[9]));

        System.out.println("Account 1: " + profiles[0]);
        System.out.println("Account 2: " + profiles[1] + "\n");

        if (profiles[0].equals(profiles[1]))
        {
            System.out.println("The accounts are the same. ");
        }

        else
        {
            if (profiles[0].compareTo(profiles[1]) > 0)
            {
                System.out.println("Account 1 comes after Account 2.");
            }

            else if (profiles[0].compareTo(profiles[1]) < 0)
            {
                System.out.println("Account 1 comes before Account 2.");
            }
        }
    }
}
