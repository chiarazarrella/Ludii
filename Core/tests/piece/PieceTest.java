package piece;

import static org.junit.Assert.fail;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import game.Game;
import game.equipment.component.Component;
import game.equipment.component.Piece;
import game.rules.Rules;
import game.rules.phase.Phase;
import game.rules.play.moves.Moves;
import game.types.board.SiteType;
import game.types.play.RoleType;
import other.GameLoader;
import other.concept.Concept;
import other.move.Move;
import other.topology.Topology;


public class PieceTest {
	
	/**
	 * @param gameName
	 */
	@ParameterizedTest
	@ValueSource(strings = { "Amazons.lud" })
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
		    return; 
		}
		
		String description = game.description().rawGameDescription();
		int numPlayers = game.players().count();
		boolean eachDeclared = true;
		
		for(String piece: pieces) {
			for (int i = 1; i <= numPlayers; i++) {
				// if there is at least one pieceN, with 0 < N <= numPlayers -> OK
			    if (!description.contains(piece + i)) {
			    	eachDeclared = false;
			        break;
			    }
			}
			
			if(!eachDeclared)
				fail("Ludeme Each for a Piece requires at least one definition of PieceN, with N > 0");
		}
		
		
		
		
	}
	
	
	/**
	 * @param gameName
	 */
	@ParameterizedTest
	@ValueSource(strings = { "Amazons.lud" })
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
		    return; 
		}
		
		String description = game.description().rawGameDescription();
		
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
	 * @param gameName
	 */
	@ParameterizedTest
	@ValueSource(strings = { "Amazons.lud" })
	public void pieceDeclaredAsNeutral(String gameName) {
		
		Game game = init(gameName); // loading and checking for Piece Ludeme
		
		
		System.out.println(game.description().rawGameDescription());
		
		Component[] components = game.equipment().components();
		
		List<String> pieces = new ArrayList<>();
		
		for(Component c : components) {
			if (c instanceof Piece && c.role() == RoleType.Neutral) {
					pieces.add(c.getNameWithoutNumber());
					System.out.println("Piece: 	" + c.getNameWithoutNumber());
			}
		}
		
		if (pieces.isEmpty()) {
		    return; 
		}
		
		String description = game.description().rawGameDescription();
		
		for(String piece : pieces) {
		
			if(!description.contains(piece + '0')) {
				System.out.println("Error on: " + piece);
				System.out.println("A piece declared Neutral must be called as piece0");
				//fail("A piece declared Neutral must be called as piece0");
			}	
		}
	}
	
	
	private static Game init(String name) {
		Game game = GameLoader.loadGameFromName(name);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
				
		/// VERIFY THERE IS THE PIECE LUDEME - can I have a game without PIECE?
		boolean pieceConcept = concepts.get(Concept.Piece.id());
		if (!pieceConcept) {
			//System.out.println("Piece concept is NOT present");
			fail("Piece concept is not present");
		}
		
		return game;
	}
	
	// the flip values of each face of the piece have to be given
	/**
	 * @param gameName
	 */
	/*@ParameterizedTest
	@ValueSource(strings = { "Reversi.lud" })
	public void flipHasValidState(String gameName) {
		
		Game game = init(gameName); // loading and checking for Piece Ludeme
		
		BitSet concepts = game.computeBooleanConcepts();
		boolean flipConcept = concepts.get(Concept.Flip.id());
		if(!flipConcept) {
			fail("Flip concept is not present");
		}
		
		boolean siteStateConcept = concepts.get(Concept.SiteState.id());
		if(!siteStateConcept) {
			fail("SiteState concept is not present");
		}
		
		Rules rules = game.rules();
		Phase[] phases = rules.phases();
		System.out.println(phases.length);
		for(Phase phase: phases) {
			Moves moves = phase.play().moves();
			System.out.print(moves.toString());
			for(Move m: moves.moves()) {
				int n = m.state();
				System.out.print(n);
			}
			
		}
		
	}*/

}
