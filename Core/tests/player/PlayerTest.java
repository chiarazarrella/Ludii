package player;

import static org.junit.Assert.fail;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import game.Game;
import other.GameLoader;
import parameterResolver.UserInputTestProvider;
@ExtendWith(UserInputTestProvider.class)
public class PlayerTest {
	
	/**
	 * @param gameName
	 */
    @TestTemplate
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
	
}
