package piece;

import static org.junit.Assert.fail;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import controller.execution.parameter.ParametersContextProvider;
import game.Game;
import game.equipment.component.Component;
import game.equipment.component.Piece;
import game.equipment.container.board.Board;
import game.players.Player;
import game.players.Players;
import game.rules.Rules;
import game.rules.phase.Phase;
import game.rules.play.moves.Moves;
import game.types.board.SiteType;
import game.types.play.RoleType;
import main.collections.ChunkSet;
import metadata.graphics.util.ContainerStyleType;
import other.GameLoader;
import other.concept.Concept;
import other.context.Context;
import other.move.Move;
import other.state.State;
import other.state.container.ContainerState;
import other.topology.Cell;
import other.topology.Edge;
import other.topology.Topology;
import other.topology.TopologyElement;
import other.topology.Vertex;
import other.trial.Trial;

@ExtendWith(ParametersContextProvider.class)
public class PieceTest {
	
	/**
	 * Verifies that a game correctly uses the "Each" ludeme for its pieces.
	 * <p>
	 * The test checks if non-neutral, non-shared pieces are defined for at least one player
	 * (i.e., their name appears in the game description with a player number). 
	 * Fails if any required piece definition is missing.
	 * 
	 * @param gameName The name of the game being tested.
	 */
	@ParameterizedTest
	@ValueSource(strings = { "Amazons.lud" })
    //@TestTemplate
    @Tag("Static")
	public void pieceDeclaredAsEach(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);

		Component[] components = game.equipment().components();
		
		List<String> pieces = new ArrayList<String>();
		
		String description = game.description().expanded();

		for(Component c : components) {
			if (c instanceof Piece && c.role() != RoleType.Neutral && c.role() != RoleType.Shared) {
				
					pieces.add(c.getNameWithoutNumber());
					//System.out.println(c.name());	
			}
		}
		
		
		List<String> validPieces = new ArrayList<>();
		for (String piece : pieces) {
		    String regex = "piece\\s+\"" + Pattern.quote(piece) + "\"\\s+Each\\b";
		    if (Pattern.compile(regex).matcher(description).find()) {
		        validPieces.add(piece);
		    }
		}

		if (validPieces.isEmpty()) {
		    fail("The game does not have any piece declared as Each");
		}

		int numPlayers = game.players().count();
		boolean eachDeclared;
		
		for(String piece: validPieces) {
			
			eachDeclared = false;
			
			for (int i = 1; i <= numPlayers; i++) {
				// if there is at least one pieceN, with 0 < N <= numPlayers -> OK
			    if (description.contains(piece + i)) {
			    	eachDeclared = true;
			    	break;
			    }
			    
			}
			
			if(!eachDeclared)
				fail("Ludeme Each for a Piece requires at least one definition of PieceN, with N > 0");
		}
		
			
	}
	
	
    /**
     * Verifies that a game correctly uses the "Shared" ludeme for its pieces.
     * <p>
     * The test checks if shared pieces are declared and ensures that each appears 
     * at least twice in the game description (once in the definition and once in the rules).
     * Fails if a shared piece is missing or incorrectly declared.
     * 
     * @param gameName The name of the game being tested.
     */
	//@ParameterizedTest
	//@ValueSource(strings = { "Amazons.lud" })
    @TestTemplate
    @Tag("Static")
    public void pieceDeclaredAsShared(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
		
		Component[] components = game.equipment().components();
		
		List<String> pieces = new ArrayList<>();
		
		for(Component c : components) {
			if (c instanceof Piece && c.role() == RoleType.Shared) {
					pieces.add(c.getNameWithoutNumber());
			}
		}
		
		if (pieces.isEmpty()) {
			fail("The game does not have any piece declared as Shared");
		    return; 
		}
		
		String description = game.description().expanded();
		
		for(String piece: pieces) {
			
			int firstIndex = description.indexOf(piece);
		    int secondIndex = description.indexOf(piece, firstIndex + 1); 
		    
		    // one call when it is declared as Piece and the second in the rules usually
		    if (firstIndex == -1 || secondIndex == -1) {
		        fail("A Shared Piece must appear at least twice in the description");
		    }
			
		}
		
				
	}
	 
	
    /**
     * Verifies that a game correctly uses the "Neutral" ludeme for its pieces.
     * <p>
     * The test checks if neutral pieces are declared and ensures each is referenced 
     * as "piece0" in the game description. Fails if a neutral piece is missing or incorrectly named.
     * 
     * @param gameName The name of the game being tested.
     */
	//@ParameterizedTest
	//@ValueSource(strings = { "Amazons.lud" })
	@TestTemplate
	@Tag("Static")
	public void pieceDeclaredAsNeutral(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
		int counterBasePiece = 0;
				
		Component[] components = game.equipment().components();
		
		List<String> pieces = new ArrayList<>();
		
		for(Component c : components) {
			
			if (c instanceof Piece && c.role() == RoleType.Neutral) {
					if(counterBasePiece == 0) {
						counterBasePiece++;
						continue;
					}
					pieces.add(c.getNameWithoutNumber());
			}
			
		}
		
		if (pieces.isEmpty()) {
		    return; 
		}
		
		String description = game.description().expanded();
		
		for(String piece : pieces) {
		
			if(!description.contains(piece + '0')) {
				fail("A piece declared Neutral must be called as piece0");
			}	
		}
		

	}
	
	//@ParameterizedTest
	//@ValueSource(strings = { "Amazons.lud" })
	@TestTemplate
	@Tag("Static")
	public void eachPieceReferenceNotDuplicated(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
	    
	    Component[] components = game.equipment().components();
	    
	    List<String> pieces = new ArrayList<String>();
	    
	    String description = game.description().expanded();
	    
	    for(Component c : components) {
	        if (c instanceof Piece && c.role() != RoleType.Neutral && c.role() != RoleType.Shared) {
	            pieces.add(c.name()); // Piece1
	        }
	    }
	    
	    List<String> validPieces = new ArrayList<>();
		for (String piece : pieces) {
		    String regex = "piece\\s+\"" + Pattern.quote(piece) + "\"\\s+Each\\b";
		    if (Pattern.compile(regex).matcher(description).find()) {
		        validPieces.add(piece);
		    }
		}

		if (validPieces.isEmpty()) {
		    fail("The game does not have any piece declared as Each");
		}
	    
	    Map<String, Integer> pieceCounts = new HashMap<>();
	    
	    for (String piece : validPieces) {
	    	
	        pieceCounts.put(piece, 0);
	        
	        Pattern pattern = Pattern.compile("\\b" + piece + "\\b"); // to find exact word e.g."Piece1"
	        Matcher matcher = pattern.matcher(description);
	        
	        while (matcher.find()) {
	            pieceCounts.put(piece, pieceCounts.get(piece) + 1);
	        }
	        
	        if (pieceCounts.get(piece) > 1) {
	            fail("Ludeme Each for a Piece requires that the reference to " + piece + " is not duplicated. Found " + pieceCounts.get(piece) + " occurrences.");
	        }
	    }
	}
	
	
	/*@TestTemplate
	@Tag("Dynamic")
	public void dynTestPiece(String gameName) {
		assert (true);
	}*/
	

}
