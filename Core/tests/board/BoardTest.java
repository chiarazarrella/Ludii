package board;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


import game.Game;

import game.types.board.SiteType;
import other.GameLoader;
import other.concept.Concept;
import other.concept.ConceptKeyword;
import other.topology.Topology;
import other.topology.TopologyElement;
import other.topology.Vertex;

public class BoardTest {
	
	/**
     * @param lineLength
     * @param gameName
     */
    @ParameterizedTest
    //@MethodSource("parametersProvider")
    @CsvSource({"Tic-Tac-Toe.lud , 3"})
	public void lineLessOrEqualThanBoardSide(String gameName, int lineLength) {
    	
    	
        // GAME LOADING
		Game game = GameLoader.loadGameFromName(gameName);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
		
		/// VERIFY THERE IS THE LINE LUDEME
		boolean lineConcept = concepts.get(Concept.Line.id());
		if (!lineConcept) {
			fail("Line concept is not present");
		} else {
			System.out.println("Line concept is present");
		}
		
		/// RETRIEVE THE LENTGH OF ONE SIDE OF THE BOARD
		Topology top = game.board().topology();
		List<TopologyElement> listLeft = top.left(SiteType.Vertex);
		List<TopologyElement> listRight = top.right(SiteType.Vertex);
		Vertex left = (Vertex) listLeft.get(0);
		Vertex right = (Vertex) listRight.get(0);
		int side = right.index() - left.index();
		
        //System.out.println("Line: " + lineLength + ", Board Side Length: " + side);
        assertTrue(lineLength <= side, "Line should be less than or equal to board side");
	}
    
    /*
    public static Stream<Object[]> parametersProvider(String name, int lineLength) {
        return Stream.of(new Object[][]{{}});
    }*/
 

}
