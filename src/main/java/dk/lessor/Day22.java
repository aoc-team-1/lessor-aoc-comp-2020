package dk.lessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day22 {

    public static void main(String[] args) {
        new Day22().runner();
    }

    public void runner() {

        List<String> input = FileUtilsKt.readFile("day_22.txt");
        PlayerDeck playerDeckDeckOne = new PlayerDeck();
        PlayerDeck playerDeckDeckTwo = new PlayerDeck();

        int currentPlayers = 0;
        for (String data : input) {
            if (data.startsWith("Player ")) {
                currentPlayers++;
                continue;
            }
            if (data.isEmpty()) {
                continue;
            }

            if (currentPlayers == 1) {
                playerDeckDeckOne.addCards(Integer.parseInt(data));
            } else {
                playerDeckDeckTwo.addCards(Integer.parseInt(data));
            }
        }
        puzzleOne(playerDeckDeckOne, playerDeckDeckTwo);
        puzzleTwo(playerDeckDeckOne, playerDeckDeckTwo);
    }

    private void puzzleOne(PlayerDeck playerDeckDeckOne, PlayerDeck playerDeckDeckTwo) {

        runGame(playerDeckDeckOne, playerDeckDeckTwo);

        calculateScore(playerDeckDeckOne, playerDeckDeckTwo);
    }

    private void puzzleTwo(PlayerDeck playerDeckDeckOne, PlayerDeck playerDeckDeckTwo) {

        runRecursiveGame(playerDeckDeckOne, playerDeckDeckTwo);

        calculateScore(playerDeckDeckOne, playerDeckDeckTwo);
    }

    private void calculateScore(PlayerDeck playerDeckDeckOne, PlayerDeck playerDeckDeckTwo) {
        long result = 0;

        List<Integer> cards = playerDeckDeckOne.getCards().size() > 0 ? playerDeckDeckOne.getCards() : playerDeckDeckTwo.getCards();
        for (int i = 0; i < cards.size(); i++) {
            int i1 = (cards.size() - i) * cards.get(i);
            result += i1;
        }
        System.out.println(result);
    }

    private void runGame(PlayerDeck playerDeckDeckOne, PlayerDeck playerDeckDeckTwo) {
        while (playerDeckDeckOne.getCards().size() > 0 && playerDeckDeckTwo.getCards().size() > 0) {

            Integer playerOneCard = playerDeckDeckOne.getCards().remove(0);
            Integer playerTwoCard = playerDeckDeckTwo.getCards().remove(0);

            boolean playerOneWins = playerOneCard > playerTwoCard;

            if (playerOneWins) {
                playerDeckDeckOne.addCards(playerOneCard);
                playerDeckDeckOne.addCards(playerTwoCard);
            } else {
                playerDeckDeckTwo.addCards(playerTwoCard);
                playerDeckDeckTwo.addCards(playerOneCard);
            }
        }
    }

    private boolean runRecursiveGame(PlayerDeck playerDeckDeckOne, PlayerDeck playerDeckDeckTwo) {
        boolean playerOneForceWin = false;
        List<String> playedDecks = new ArrayList<>();
        while (playerDeckDeckOne.getCards().size() > 0 && playerDeckDeckTwo.getCards().size() > 0) {
            String cards = playerDeckDeckOne.getCards().stream().map(Object::toString).collect(Collectors.joining(", "));
            if (playedDecks.contains(cards)) {
                playerOneForceWin = true;
                break;
            }
            playedDecks.add(cards);

            Integer playerOneCard = playerDeckDeckOne.getCards().remove(0);
            Integer playerTwoCard = playerDeckDeckTwo.getCards().remove(0);

            boolean playerOneWins = playerOneCard > playerTwoCard;
            if (playerDeckDeckOne.getCards().size() >= playerOneCard && playerDeckDeckTwo.getCards().size() >= playerTwoCard) {
                playerOneWins = runRecursiveGame(PlayerDeck.fromCards(new ArrayList<>(playerDeckDeckOne.getCards()).subList(0, playerOneCard)), PlayerDeck.fromCards(new ArrayList<>(playerDeckDeckTwo.getCards()).subList(0, playerTwoCard)));
            }
            if (playerOneWins) {
                playerDeckDeckOne.addCards(playerOneCard);
                playerDeckDeckOne.addCards(playerTwoCard);
            } else {
                playerDeckDeckTwo.addCards(playerTwoCard);
                playerDeckDeckTwo.addCards(playerOneCard);
            }
        }

        return playerDeckDeckOne.getCards().size() > playerDeckDeckTwo.getCards().size() || playerOneForceWin;
    }

    static class PlayerDeck {

        private List<Integer> cards = new ArrayList<>();

        public List<Integer> getCards() {
            return cards;
        }

        public void addCards(Integer card) {
            cards.add(card);
        }

        public static PlayerDeck fromCards(List<Integer> cards) {
            PlayerDeck deck = new PlayerDeck();
            deck.setCards(cards);
            return deck;
        }

        public void setCards(List<Integer> cards) {
            this.cards = cards;
        }
    }
}
