package sim;
/**
 * Implement your team competition agent here
 * Rename the class by substituting "ABC" by your team's initials,
 * e.g. BudgetMRTK
 *
 */

public class BudgetAMMN extends Agent{

	public BudgetAMMN(int id, int value, int budget) {
		super(id, value, budget);
	}

	@Override
	public int initialBid(int reserve) {
		// TODO Auto-generated method stub
        // If value is less than reserve price don't bid
        if(reserve>getValue())
            return 0;

        // If budget left is less than reserve bid budget left
        // return the budget left
        else if(reserve>getBudgetLeft())
        {
            int tempBudgetLeft = getBudgetLeft();
            setBudgetLeft(0);
            return tempBudgetLeft;
        }
        return 0;
	}

	@Override
	public int bid(int t, History history, int reserve) {
		// TODO Auto-generated method stub
        if(reserve<=getValue() && getBudgetLeft()>=reserve)
            return getValue();

		return 0;
	}

}
