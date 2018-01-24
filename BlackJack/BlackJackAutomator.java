package OOD.BlackJack;
import java.util.*;

public class BlackJackAutomator {
    /*模拟BlackJack；
    简化： 所有玩家采用dealer模式，即只要手上的牌分数不到16自动继续抓牌：
    相当于初始发牌后只玩一轮；
    但是每个玩家在这一轮里只需要不停的抓牌+判断；
    */
    private Deck deck;
    private BlackJackHand[] hands;
    private static final int HIT_UNTIL = 16;

    public BlackJackAutomator(int numPlayers) {
        hands = new BlackJackHand[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            hands[i] = new BlackJackHand();
        }
    }


    void initializeDeck() {
        deck = new Deck();
        deck.shuffle();
    }


    boolean dealInitial() {
        for (BlackJackHand hand : hands) {
            Card[] cards = deck.dealHand(2);
            if (cards == null) {
                return false;
            }
            hand.addCards(cards);
        }
        return true;
    }


    List<Integer> getBlackJacks() {
        List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < hands.length; i++) {
            if (hands[i].isBlackJack()) {
                winners.add(i);
            }
        }
        return winners;
    }


    boolean playHand(BlackJackHand hand) {
        while (hand.score() < HIT_UNTIL) {
            Card card = deck.dealCard();
            if (card == null) {
                return false;
            }
            hand.addCards(new Card[]{card});
        }
        return true;
    }



    boolean playAllHands() {
        for (BlackJackHand hand : hands) {
            if (!playHand(hand)) {
                return false;
            }
        }
        return true;
    }


    List<Integer> getWinners() {
        List<Integer> winners = new ArrayList<>();
        int winningScore = 0;
        for (int i = 0; i < hands.length; i++) {
            BlackJackHand hand = hands[i];
            if (!hand.busted()) {
                if (hand.score() > winningScore) {
                    winningScore = hand.score();
                    winners.clear();
                    winners.add(i);
                } else if (hand.score() == winningScore) {
                    winners.add(i);
                }
            }
        }
        return winners;
    }



    void printHandsAndScore() {
        for (int i = 0; i < hands.length; i++) {
            System.out.print("Hand" + i +"(" + hands[i].score() + "):");
//            hands[i].print();
            System.out.println();
        }
    }



    public void simulate() {
        initializeDeck();

        boolean success = dealInitial();
        if (!success) {
            System.out.println("Error. Out of cards.");
        } else {
            System.out.println("--Initial--");
            printHandsAndScore();
            List<Integer> blackjacks = getBlackJacks();
            if (blackjacks.size() > 0) {
                System.out.print("Blackjack at");
                for (int i : blackjacks) {
                    System.out.print(i + " ");
                }
                System.out.println("done.");
            } else {
                success = playAllHands();
                if (!success) {
                    System.out.println("Error. Out of cards.");
                } else {
                    System.out.println("\n--Completed Game--");
                    printHandsAndScore();
                    List<Integer> winners = getWinners();
                    if (winners.size() > 0) {
                        System.out.print("Winners:");
                        for (int i : winners) {
                            System.out.print(i + " ");
                        }
                        System.out.println();
                    } else {
                        System.out.println("Draw. All players have busted.");
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        BlackJackAutomator automator = new BlackJackAutomator(5);
        automator.simulate();
    }
}
