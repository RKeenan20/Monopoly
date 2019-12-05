package ie.ucd.squares;

import ie.ucd.cards.TitleDeed;
import ie.ucd.game.Player;
import ie.ucd.operations.Checks;
import ie.ucd.operations.InputOutput;

public class Train extends PublicSquare {
	
	public Train(String name, int indexLocation) {
		super(name, indexLocation, Square.SquareType.TRAIN);
	}

	public void buy(Player player) {
		//check user has enough funds to purchase
		TitleDeed titleDeedCard = this.getTitleDeedCard();
		//The player doesn't have enough money to purchase it
		if(!Checks.enoughFunds(player, titleDeedCard.getPriceBuy())) {
			System.err.println("You do not have the necessary funds to purchase this train.\nYour Funds: "+player.getMoney()+"\nProperty Price: "+titleDeedCard.getPriceBuy());
			//player does not have enough funds to buy property, automatically enter auction
			this.getTitleDeedCard().playerAuction(null);
		}
		//Property is already owned
		else if(!(Checks.canBuy(this.getTitleDeedCard()))){
			System.err.println("This property is already owned!");
		}
		//They can purchase it
		else if(InputOutput.yesNoInput(player.getName()+", would you like to purchase "
				+this.getName()+" for €"+titleDeedCard.getPriceBuy()+"?", player, null)) {
			//user has passed all necessary checks to purchase a property, reduce the price from users funds
			System.out.println("You have purchased "+this.getName()+" for "+titleDeedCard.getPriceBuy());
			player.reduceMoney(titleDeedCard.getPriceBuy(), null);
			//add property to users property list
			player.addPurchasedTitleDeed(this.getTitleDeedCard());
		}
		//Send to auction
		else {
			this.getTitleDeedCard().playerAuction(null);
		}
	}

}
