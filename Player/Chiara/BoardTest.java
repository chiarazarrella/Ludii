import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import game.Game;

import game.types.board.SiteType;
import other.GameLoader;
import other.concept.ConceptKeyword;
import other.topology.Topology;
import other.topology.TopologyElement;
import other.topology.Vertex;

public class BoardTest {
	
	
    @ParameterizedTest
    @ValueSource(ints = { 2 }) // Provide test values
	public void LineLessOrEqualThanBoardSide(int line) {
		
		Game game = GameLoader.loadGameFromName("Tic-Tac-Toe.lud");
		BitSet bs = game.booleanConcepts();
		System.out.println(bs.toString());
		System.out.println(bs.size());
		System.out.println(ConceptKeyword.values().length);
		for (int i = 0; i < ConceptKeyword.values().length; i++) {
		    if (bs.get(i)) {
		        System.out.println(ConceptKeyword.values()[i].id() + "	" + ConceptKeyword.values()[i].description());
		    }
		}
		/// VERIFY THERE IS THE LINE LUDEME
		//boolean line = game.booleanConcepts().get(ConceptKeyword.Line.id());
		
		System.out.println(line);
		
		/*if (line) {
			fail("Line concept is not present");
		} else {
			System.out.println("Line concept is present");
		}*/
		
		
		
		/// RETRIEVE THE LENTGH OF ONE SIDE OF THE BOARD
		Topology top = game.board().topology();
		List<TopologyElement> listLeft = top.left(SiteType.Vertex);
		List<TopologyElement> listRight = top.right(SiteType.Vertex);
		Vertex left = (Vertex) listLeft.get(0);
		Vertex right = (Vertex) listRight.get(0);
		int side = right.index() - left.index();
		
        System.out.println("Line: " + line + ", Board Side Length: " + side);
	
        assertTrue(line <= side, "Line should be less than or equal to board side");
	}

}
