/**
 * This Archive.class stores the list of deleted accounts.
 * @author Jiayan Zou, Gideon Edwards
 */
public class Archive
{
    /**
     * This is the inner class for the AccountNode.
     */
    class AccountNode
    {
        Account account;
        AccountNode next;

        //Paramaterized constructor.
        AccountNode(Account account)
        {
            this.account = account;
        }

        //This is the accessor for the account.
        public Account getAccount()
        {
            return account;
        }

        //This is the mutator for the account.
        public void setAccount(Account account)
        {
            this.account = account;
        }

        //This is the accessor for the next node.
        public AccountNode getNext()
        {
            return next;
        }

        //This is the mutator for the next node.
        public void setNext(AccountNode next)
        {
            this.next = next;
        }
    }

    private AccountNode first;

    /**
     * Add to the beginning of the account node.
     * @param account
     */
    public void add(Account account)
    {
        AccountNode oldFirst = first;
        first = new AccountNode(account);
        first.setNext(oldFirst);
    }

    /**
     * This method prints the linked list.
     */
    public void print()
    {
        System.out.println("\n*List of closed accounts in the archive.");

        for (AccountNode current = first; current != null; current = current.getNext())
        {
            System.out.println(current.getAccount());
        }

        System.out.println("*end of list.\n");
    }
}
