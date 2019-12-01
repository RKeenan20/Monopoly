package ie.ucd.game;
import java.util.*;

public class Chance extends Card {
	
	public Chance(String cardType, String cardDesc, int cardValue) {
		super(cardType, cardDesc, cardValue);
	}

	//method to deal with a card from one of the game's decks
	public void dealWithCard(Player player) {
		ArrayList<TitleDeed> titleDeedList = player.getTitleDeedList();

		//print card details for the user
		System.out.println("The chance card reads: "+this.getCardDesc());
		//implement the card
		switch(this.getCardType()) {
			case "MOVE":
				//cover case where players move backwards
				if(this.getCardValue()<0){
					player.moveToSquare(player.getLocation()+this.getCardValue());
					Checks.checkSquare(player.getLocation(), player);
				}
				//move player forwards
				else {
					player.moveToSquare(this.getCardValue());
					Checks.checkSquare(player.getLocation(), player);
				}
				break;
			case "JAIL":
				//send player to jail
				Jail.sendToJail(player);
				break;
			case "PAY":
				//pay a fine
				//check if the fine is per house/hotel
				if(this.getCardDesc().contains("repairs")) {
					//Check number of houses and hotels to accumulate in the payment for each ownable site
					for(TitleDeed titleDeed : titleDeedList) {
						CanOwn property = titleDeed.getOwnableSite();
						if(property instanceof Property) {
							//This will get the card value and multiply the number of houses or hotels depending on if its of Property Class
							player.reduceMoney(this.getCardValue()*((Property) property).getNumHouses(),null);
							player.reduceMoney(4*this.getCardValue()*((Property) property).getNumHotels(),null);
							//If there is no hotels, it will not take any money away at all
						}
					}
					break;
				}
				else {
					//reduce the amount of the fine
					player.reduceMoney(this.getCardValue(), null);
					break;
				}
			case "INCOME":
				//add money to the player
				player.addMoney(this.getCardValue());
				break;
			case "GET_OUT_OF_JAIL":
				//add get out of jail free card
				player.addJailCard(this);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + this.getCardType());
		}
	}
}
