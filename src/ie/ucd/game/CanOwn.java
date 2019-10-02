package ie.ucd.game;
import java.util.*;

public abstract class CanOwn extends Square {
	private int mortgage;
	private int buyPrice;
	Player owner;
	
	public CanOwn(String name, int indexLocation, int mortgage, int buyPrice, Player owner) {
		super(name, indexLocation, true);
		this.mortgage = mortgage;
		this.buyPrice = buyPrice;
		this.owner = owner;
	}
	
	public int getMortgage() {
		return this.mortgage;
	}
	
	public int getPrice() {
		return this.buyPrice;
	}
	
	public Player getOwner() {
		return this.owner;
	}
	
	public void setMortgage(int mortgage) {
		this.mortgage = mortgage;
	}
	
	public void setPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	public void setOwner(Player player) {
		this.owner = player;
	}
	
	//FIXME PLEASE Consider whether it should take an argument or not, Refer to Trello
	public abstract void buy(Player player, List<Player> listPlayers);
	//The list of players is so you can use the auction method which will be made by Ciaran Nolan
	
	public void playerAuction(List<Player> listPlayers) {
	
		List<Player> biddingPlayers = listPlayers;
		int[] currentAuctionPrice = new int[] {0,0};
		Scanner auctionScanner = new Scanner(System.in);
		int biddingPoolSize = listPlayers.size();
		String rebidAcknowledgement = "nil";
		
		while(biddingPoolSize > 1){
			//update the bidding pool size 
			for (int i = 0; i< biddingPoolSize; i++) {
				//check user has enough funds to create a larger bid than the current highest
				if(biddingPlayers.get(i).getMoney()<= currentAuctionPrice[0]) {
					System.out.println(biddingPlayers.get(i).getName()+" does not have enough funds to make a winning bid on "+this.getName()+"\nCurrent bid: "+currentAuctionPrice[0]+"\nYour Funds:"+biddingPlayers.get(i).getMoney());
					biddingPlayers.remove(i);
					i -= 1;
					biddingPoolSize = biddingPlayers.size();
					continue;
				}
				//prompt user to confirm intention to bid
				else {
					System.out.println(biddingPlayers.get(i).getName()+" would you like to place a bid on "+this.getName()+"? (y/n)");
				}	
					
				//if a previous bid has already been made, display to user 
				if(currentAuctionPrice[0] > 0) {
					System.out.println("Current bid: "+currentAuctionPrice[0]+" by "+biddingPlayers.get(currentAuctionPrice[1]).getName());
				}
				//read intention to bid from user
				String bidAcknowledgement = auctionScanner.next();
				
				//check for correct input from user
				while(!(bidAcknowledgement.equalsIgnoreCase("y") || bidAcknowledgement.equalsIgnoreCase("n"))) {
					System.out.println(biddingPlayers.get(i).getName()+", please enter a valid response (y/n)");
					bidAcknowledgement = auctionScanner.next();
				}
				
				//user has indicated intention to bid
				if(bidAcknowledgement.equalsIgnoreCase("y")) {
					//prompt user to enter a bid
					System.out.println(biddingPlayers.get(i).getName() + " please enter your bid:");
					
					//read in user bid
					int temporaryBid = auctionScanner.nextInt();
					
					//check the user's bid is greater than current highest bid
					while(temporaryBid <= currentAuctionPrice[0]) {
						//bid is less than current highest bid, prompt for intention to re input bid
						System.out.println(biddingPlayers.get(i).getName() + " your bid must be greater than the current bid of: "+currentAuctionPrice[0]+".\nWould you like to make another bid? (y/n)");
						
						//read in intention to make another bid
						rebidAcknowledgement = auctionScanner.next();
						
						//check correct bid acknowledgement (y/n) has been made
						while(!(rebidAcknowledgement.equalsIgnoreCase("y") || rebidAcknowledgement.equalsIgnoreCase("n"))) {
							System.out.println(biddingPlayers.get(i).getName()+", please enter a valid response (y/n)");
							rebidAcknowledgement = auctionScanner.next();
						}
						
						//check if user has confirmed intention to bid again
						if(rebidAcknowledgement.equalsIgnoreCase("y")) {
							System.out.println(biddingPlayers.get(i).getName() + " please enter your bid:");
							temporaryBid = auctionScanner.nextInt();
						}
						//user has declared intention to NOT bid again, remove from list of current users in auction
						else {
							biddingPlayers.remove(i);
							i -= 1;
							//reset temp bid back to highest bid so it is not overwritten
							temporaryBid = currentAuctionPrice[0];
							break;
						}
						}
						currentAuctionPrice[0] = temporaryBid;
						currentAuctionPrice[1] = i;
					
				}
				else {
					biddingPlayers.remove(i);
					i -= 1;
					}
				
			biddingPoolSize = biddingPlayers.size();
			}
			if (biddingPoolSize == 1) {
				System.out.println(biddingPlayers.get(0).getName()+" has successfully won "+this.getName()+" at auction for: "+currentAuctionPrice[0]);
				this.setOwner(biddingPlayers.get(0));
				biddingPlayers.get(0).reduceMoney(currentAuctionPrice[0]);
				break;
			}
			}
		if(this.getOwner().getName() == "BANK") {
			System.out.println("There was no winning bid. "+this.getName()+" remains unpurchased.");
		}
		}

	
	public abstract void sell(Player player, CanOwn siteToSell, List<Player> listPlayers);
}
