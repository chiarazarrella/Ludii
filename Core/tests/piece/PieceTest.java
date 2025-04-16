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

import controller.execution.parameter.InputTestProvider;
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
import other.GameLoader;
import other.concept.Concept;
import other.move.Move;
import other.state.State;
import other.state.container.ContainerState;
import other.topology.Cell;
import other.topology.Topology;

@ExtendWith(InputTestProvider.class)
public class PieceTest {
	
	/**
	 * Verifies that a game correctly uses the "Each" ludeme for its pieces.
	 * <p>
	 * The test checks if non-neutral, non-shared pieces are defined for at least one player
	 * (i.e., their name appears in the game description with a player number). 
	 * Fails if any required piece definition is <missing.
	 * 
	 * @param gameName The name of the game being tested.
	 */
	//@ParameterizedTest
	//@ValueSource(strings = { "Amazons.lud" })
    @TestTemplate
    @Tag("Static")
	public void pieceDeclaredAsEach(String gameName) {
		
		Game game = init(gameName);

		Component[] components = game.equipment().components();
		
		List<String> pieces = new ArrayList<String>();
		
		for(Component c : components) {
			if (c instanceof Piece && c.role() != RoleType.Neutral && c.role() != RoleType.Shared) {
					pieces.add(c.getNameWithoutNumber());
			}
		}
		
		if (pieces.isEmpty()) {
			fail("The game does not have any piece declared as Each");
			return;
		}
		
		String description = game.description().expanded();

		int numPlayers = game.players().count();
		boolean eachDeclared;
		
		for(String piece: pieces) {
			
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
		
		
		System.out.println("The game correctly use Each Ludeme");
		
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
		
		Game game = init(gameName);
		
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
		
		System.out.println("The game correctly use Shared Ludeme");
		
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
		
		Game game = init(gameName); // loading and checking for Piece Ludeme
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
	    Game game = init(gameName); // loading and checking for Piece Ludeme
	    
	    Component[] components = game.equipment().components();
	    
	    List<String> pieces = new ArrayList<String>();
	    
	    for(Component c : components) {
	        if (c instanceof Piece && c.role() != RoleType.Neutral && c.role() != RoleType.Shared) {
	            pieces.add(c.name()); // Piece1
	        }
	    }
	    
	    if (pieces.isEmpty()) {
	        fail("The game does not have any piece declared as Each");
	        return;
	    }
	    
	    String description = game.description().expanded();
	    
	    Map<String, Integer> pieceCounts = new HashMap<>();
	    
	    for (String piece : pieces) {
	    	
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
	
	@ParameterizedTest
	@ValueSource(strings = { "Amazons.lud" })
	@Tag("Static")
	public void equalNumberOfPieces(String gameName) {
		
		Game game = init(gameName);
		Topology topology = game.board().topology();
		Players players = game.players();
		
		
		
		List<Cell> cells = game.board().topology().cells();
		State state = game.stateReference();
	    
		ContainerState[] containerState = state.containerStates();
		
		int i;
		for(ContainerState cs: containerState) {
			
			for (i = 0; i < cells.size(); i++) {
				
				Cell c = cells.get(i);
				System.out.println(c.index());
				System.out.println(i);
				System.out.println("index component: " + cs.whatCell(i) + " owner: " + cs.whoCell(i));
				
			}
			
		}
		
		
		Component[] components = game.equipment().components();
		
		for (Component c : components) {
			
			if(c instanceof Piece) {
				
			}
		}
		
		
		
		
		
		
	}
	
	
	private static Game init(String name) {
		Game game = GameLoader.loadGameFromName(name);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
				
		/// VERIFY THERE IS THE PIECE LUDEME - can I have a game without PIECE? NO. Because there is an implicit piece (DISC neutral) in every game
		boolean pieceConcept = concepts.get(Concept.Piece.id());
		if (!pieceConcept) {
			fail("Piece concept is not present"); // this will NEVER fail
		}
		
		return game;
	}
	

	

}
