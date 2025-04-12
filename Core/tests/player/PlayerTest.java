package player;

import static org.junit.Assert.fail;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.CsvSource;

import game.Game;
import game.players.Player;
import game.players.Players;
import other.GameLoader;
import other.concept.Concept;
import parameterResolver.UserInputTestProvider;
@ExtendWith(UserInputTestProvider.class)
public class PlayerTest {
	
	/**
	 * @param gameName
	 */
    @TestTemplate
    @Tag("Static")
	public void playerNotDeclared(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
		
		int pDeclared = game.players().count();
		
		String description = game.description().rawGameDescription();
		
		List<Integer> pUsed = new ArrayList<>();

        String[] parts = description.split("[\\n(){}\\[\\]]+");
        Pattern pattern = Pattern.compile("P\\d+"); // Pattern for "P" followed by numbers
        
        for (String part : parts) {
        	
            Matcher matcher = pattern.matcher(part);
            
            while (matcher.find()) {
            	
            	String numStr = matcher.group().substring(1);
            	int num = Integer.parseInt(numStr);
            	pUsed.add(num);
                
            }
        }
        
        for(Integer i : pUsed) {
        	if(i > pDeclared) {
        		fail("P" + i + " is not declared");
        	}
        }
        
	}
    
    
    @CsvSource({"test.lud"})
    public void equalNumberOfPieces(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
				
		List<Player> players = game.players().players();
		int numPlayers = players.size();
		System.out.println("players " + numPlayers);
		game.noPieceOwnedBySpecificPlayer(); // this means that there is no way to calculate the number of pieces outside the description
		
		
		
	}
	
}
