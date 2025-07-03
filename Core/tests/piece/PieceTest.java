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
import game.equipment.container.Container;
import game.equipment.container.board.Board;
import game.equipment.container.other.Hand;
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
/**
 * Test suite verifying the Piece Concept.
 */
@ExtendWith(ParametersContextProvider.class)
public class PieceTest {
	
	 /**
     * Checks that pieces declared with the "Each" ludeme have appropriate player-specific definitions.
     * <p>
     * The test ensures that non-neutral, non-shared pieces appear in the game description 
     * with player-specific suffixes (e.g., piece1, piece2), verifying that "Each" usage is consistent.
     * It also verifies compatibility with "Hand" containers if present.
     * </p>
     * 
     * @param gameName The name of the game to load and test.
     * @throws AssertionError if no pieces declared as Each exist or player-specific piece declarations are missing.
     */
    @TestTemplate
    @Tag("Static")
	public void pieceDeclaredAsEach(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
		
		Context context = null;
		Component[] components = null;
		Container[] containers = null;
		
		if(game.hasSubgames()) {
			
			context = new Context(game, new Trial(game));
	  		game.start(context);
	  		components = context.equipment().components();
	  		containers = context.equipment().containers();
		}else {
			
			components = game.equipment().components();
			containers = game.equipment().containers();
		}

		
		List<String> pieces = new ArrayList<String>();
		
		String description = game.description().expanded();

		for(Component c : components) {
			if (c instanceof Piece && c.role() != RoleType.Neutral && c.role() != RoleType.Shared) {
				
					pieces.add(c.getNameWithoutNumber());
					
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
		
		
		BitSet concepts = game.computeBooleanConcepts();
	
		boolean handConcept = concepts.get(Concept.Hand.id());
		if (handConcept) {
			// if there is Hand ludeme, we need to check that "Each piece" can be declared for Hand -> usually with the form (place "piece" Hand")
			List<String> validPieces2 = new ArrayList<>();
			validPieces2.addAll(validPieces);
			
			for (Container container : containers) {
				if (container instanceof Hand) {
					
					for (String piece : validPieces) {
						String regex = "place\\s+\"" + Pattern.quote(piece) + "\"\\s+\"Hand\"";
					    if (Pattern.compile(regex).matcher(description).find()) {
					    	validPieces2.remove(piece);
					    }
					}
					
				}
			}
			
			if (validPieces2.isEmpty()) {
				return;
			} else {
				validPieces.clear();
				validPieces.addAll(validPieces2);
			}
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
				fail(String.format("Ludeme Each for the piece %s requires at least one definition of %sN, with N > 0", piece, piece));
		}
		
			
	}
	
	
    /**
     * Validates that pieces declared as "Shared" exist and appear multiple times in the game description.
     * <p>
     * Shared pieces must be declared once and referenced again (e.g., in rules or moves).
     * The test fails if no shared pieces exist or if any shared piece appears less than twice.
     * </p>
     * 
     * @param gameName The name of the game to load and test.
     * @throws AssertionError if shared pieces are missing or incorrectly referenced.
     */
    @TestTemplate
    @Tag("Static")
    public void pieceDeclaredAsShared(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
		
		Context context = null;
		Component[] components = null;
		
		if(game.hasSubgames()) {
			
			context = new Context(game, new Trial(game));
	  		game.start(context);
	  		components = context.equipment().components();
	  		
		}else {
			
			components = game.equipment().components();

		}
		
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
     * Ensures that neutral pieces are properly declared and referenced in the game description.
     * <p>
     * Neutral pieces must appear with the "0" suffix (e.g., piece0), excluding the base piece 
     * (usually "Disc0"), which is ignored.
     * The test fails if no neutral pieces exist or if any neutral piece is not correctly referenced.
     * </p>
     * 
     * @param gameName The name of the game to load and test.
     * @throws AssertionError if neutral pieces are missing or improperly referenced.
     */
    @TestTemplate
    @Tag("Static")
	public void pieceDeclaredAsNeutral(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
				
		Context context = null;
		Component[] components = null;
		
		if(game.hasSubgames()) {
			
			context = new Context(game, new Trial(game));
	  		game.start(context);
	  		components = context.equipment().components();
	  		
		}else {
			
			components = game.equipment().components();

		}
		
		List<String> pieces = new ArrayList<>();
		String basePieceName = "Disc"; // each game has Disc0 as the base piece declared as Neutral
		for(Component c : components) {
			
			if (c instanceof Piece && c.role() == RoleType.Neutral) {
				
				if (c.getNameWithoutNumber().equals(basePieceName)) {
					continue;
				}
			
				pieces.add(c.getNameWithoutNumber());
			}
			
		}
		
		if (pieces.isEmpty()) {
			fail("The game does not have any piece declared as Neutral");
		    return; 
		}
		
		String description = game.description().expanded();
		
		for(String piece : pieces) {
		
			if(!description.contains(piece + '0')) {
				fail("A piece declared Neutral must be called as piece0");
			}	
		}
		
	}
	
}
