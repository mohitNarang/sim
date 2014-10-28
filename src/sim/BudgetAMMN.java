package sim;

import java.util.ArrayList;

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
        return reserve;
	}

	@Override
	public int bid(int t, History history, int reserve) {
		
		ArrayList<Integer> targetInfo = targetSlot(t, history, reserve);

	    int targetSlotId = targetInfo.get(0);
	    int minBid = targetInfo.get(1);
	    int maxBid = targetInfo.get(2);
	    int bid = (minBid + maxBid)/2;
	   
		
        if(reserve<=getValue() && getBudgetLeft()>=reserve && bid < getValue()) {
        	return (minBid + maxBid)/2;
        }
        
    
		return 0;
	}

	/**
	 * This function returns a list of Pairs (minBid,maxBid) for each slot (i.e. slot1,slot2, ...).
	 * (minBid,maxBid)_i defines the miminum and maximum bid to win slot i, given that all other
	 * agents bid the same amount as in the last round
	 * @param t, the time step
	 * @param history, the object in which the information of all previous rounds is stored
	 * @param reserve, the reserve price
	 * @return list of Pairs (minBid,maxBid)_i for all slots i
	 */
	public ArrayList<Pair> slotInfo(int t, History history, int reserve){
		ArrayList<Pair> info = new ArrayList<Pair>();
		
		//get bids from last round
		ArrayList<Integer> lastBids = (ArrayList<Integer>)history.getBids(t-1).clone();
		lastBids.remove(getId());


		//get clicks from last rounds
		int numSlots = history.getSlotClicks(t-1).size();
		for (int slot = 0; slot<numSlots; slot++){
			info.add(GSP.bidRangeForSlot(slot, reserve, lastBids));
		}
		return info;
	}

	
	/**
	 * This function always targets the last slot
	 * @param t, the time step
	 * @param history, the object in which the information of all previous rounds is stored
	 * @param reserve, the reserve price
	 * @return a triple (slot,minBid,maxBid), where slot is the target slot, minBid and maxBid
	 * are the minimum and maximum bid to win this slot assuming all other bid the same amount as in the last round
	 */
	public ArrayList<Integer> targetSlot(int t, History history, int reserve){
		
		ArrayList<Pair> slots = slotInfo(t, history, reserve);
		
		ArrayList<Integer> targetSlot = new ArrayList<Integer>();
		
		int target = slots.size()-1;
		targetSlot.add(target);
		
		ArrayList<Pair> info = slotInfo(t, history, reserve);
		targetSlot.add(info.get(target).getFirst());
		targetSlot.add(info.get(target).getSecond());

		return targetSlot;
	}
}
