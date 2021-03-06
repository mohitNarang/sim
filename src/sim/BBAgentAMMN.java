package sim;

import java.util.ArrayList;

/**
 * This class implements the balanced bidding agent
 * Rename the class by substituting "TeamABC" by your team's initials,
 * e.g. BBAgentMRTK
 */
public class BBAgentAMMN extends Agent{

	public BBAgentAMMN(int id, int value, int budget){
		super(id,value,budget);
	}
	
	@Override
	public int initialBid(int reserve) {
		//return getValue(); // gets the value of the agent and returns its half
        return getValue();
	}

    @Override
    public int bid(int t, History history, int reserve) {
        ArrayList<Integer> targetInfo = targetSlot(t, history, reserve);

        int targetSlotId = targetInfo.get(0);
        int minBid = targetInfo.get(1);
        int maxBid = targetInfo.get(2);
        int bid = getValue();

        /*TODO compute the bid according to the balanced bidding strategy:
         * a) find the clickrates for the target and the next higher slot and
         * b) solve the balanced bidding equation s_i* (w_i-p_i*) = s_(i*-1)(w_i-b_i)
         *
         * hint 1: use history.getSlotClicks(t-1)
         * hint 2: as described in the assignment, clicks_(targetSlotId-1) = 2*clicks_(target_slot) if targetSlotId == 0
         */
        int clickRateTargetSlot = history.getSlotClicks(t-1).get(targetSlotId);

        int clickRateHigherSlot = 0;
        if (targetSlotId == 0)
            clickRateHigherSlot = 2*history.getSlotClicks(t-1).get(targetSlotId);
        else
            clickRateHigherSlot = history.getSlotClicks(t-1).get(targetSlotId-1);
        bid = getValue() -
                clickRateTargetSlot*
                        (getValue() - history.getSlotClicks(t-1).get(targetSlotId))
                        /clickRateHigherSlot;
        if(bid>getValue())
        bid= getValue(); // We bid truthfully in case the resulting bid is greater than our true value

        return bid;
        //return getValue();
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
	 * The function computes the expected utilities for all slots, assuming that all other
	 * agents bid the same amount as in the last round
	 * @param t, the time step (of the current round, not last round)
	 * @param history, the object in which the information of all previous rounds is stored
	 * @param reserve, the reserve price
	 * @return a list of integers (eu1, eu2, ...), in which eu_i is the agent's expected utility of winning slot i 
	 */
	public ArrayList<Integer> expectedUtilities(int t, History history, int reserve){
		ArrayList<Integer> expectedUtils = new ArrayList<Integer>();
		
		/* TODO compute the expected utilities for each available slot, assuming that the other bidders
		 * keep their bids constants from the previous round
		 * 
		 * hint: use history.getSlotClicks(t-1), and slotInfo(t, history, reserve)
		 */
        ArrayList<Integer> listOfPrevPayments =  history.getSlotPayments(t-1);
        ArrayList<Integer> listOfSlotClicks = history.getSlotClicks(t-1); // number of clicks on a slot

        ArrayList<Pair> slots = slotInfo(t, history, reserve);


        for (int numberOfSlot = 0; numberOfSlot<slots.size(); numberOfSlot++)
        {
            expectedUtils.add(
                    (getValue()* listOfSlotClicks.get(numberOfSlot)) - listOfPrevPayments.get(numberOfSlot) );
        }
        return expectedUtils;
	}
	
	/**
	 * This function computes the slot with the highest expected utility and returns
	 * this slotId together with (minBid,maxBid)_i
	 * @param t, the time step
	 * @param history, the object in which the information of all previous rounds is stored
	 * @param reserve, the reserve price
	 * @return a triple (slot,minBid,maxBid), where slot is the target slot, minBid and maxBid
	 * are the minimum and maximum bid to win this slot assuming all other bid the same amount as in the last round
	 */
	public ArrayList<Integer> targetSlot(int t, History history, int reserve){
		ArrayList<Integer> targetSlot = new ArrayList<Integer>();
		ArrayList<Integer> expectedUtils = expectedUtilities(t, history, reserve);
		int target = 0;
		for (int i = 1; i<expectedUtils.size(); i++){
			if(expectedUtils.get(target) < expectedUtils.get(i)){
				target = i;
			}
		}
		targetSlot.add(target);
		
		ArrayList<Pair> info = slotInfo(t, history, reserve);
		targetSlot.add(info.get(target).getFirst());
		targetSlot.add(info.get(target).getSecond());
		return targetSlot;
	}

}
