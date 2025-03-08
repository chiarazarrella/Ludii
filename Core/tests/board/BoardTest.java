package board;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import annotation.DefaultParameter;
import game.Game;
import game.players.Players;
import game.types.board.SiteType;
import other.GameLoader;
import other.concept.Concept;
import other.concept.ConceptKeyword;
import other.topology.Topology;
import other.topology.TopologyElement;
import other.topology.Vertex;
import parameterResolver.UserInputTestProvider;

@ExtendWith(UserInputTestProvider.class)
public class BoardTest {

	
	/**
     * @param lineLength
     * @param gameName
     */
	@CsvSource({"test.lud, 3"})
	@TestTemplate
	public void lineLessOrEqualThanBoardSide(String gameName, 
			@DefaultParameter("3") int lineLength) {
    	
        // GAME LOADING
		Game game = GameLoader.loadGameFromName(gameName);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
		
		/// VERIFY THERE IS THE LINE LUDEME
		boolean lineConcept = concepts.get(Concept.Line.id());
		if (!lineConcept) {
			System.out.println("Line concept is NOT present");
			fail("Line concept is not present");
		}
		
		/// RETRIEVE THE LENTGH OF ONE SIDE OF THE BOARD
		int side = game.board().graph().maxDim();
		
        assertTrue(lineLength <= side, "Line should be less than or equal to board side");
	}
    
    
    
}

