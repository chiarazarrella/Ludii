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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import controller.execution.parameter.ParametersContextProvider;
import game.Game;
import game.equipment.component.Component;
import game.players.Player;
import game.players.Players;
import game.types.board.SiteType;
import metadata.graphics.util.ContainerStyleType;
import other.GameLoader;
import other.concept.Concept;
import other.context.Context;
import other.state.container.ContainerState;
import other.topology.TopologyElement;
import other.trial.Trial;
@ExtendWith(ParametersContextProvider.class)
public class PlayerTest {
	
	/**
	 * @param gameName
	 */
    @TestTemplate
    //@ParameterizedTest
	//@ValueSource(strings = { "Grand Trictrac.lud" })
    @Tag("Static")
	public void noUndeclaredPlayerReference(String gameName) {
		
		Game game = GameLoader.loadGameFromName(gameName);
		
		Context context = null;
		int pDeclared = game.players().count();

		if(game.hasSubgames()) {
			
			context = new Context(game, new Trial(game));
	  		game.start(context);
	  		pDeclared = context.state().numPlayers();
	  		
		}else {
			
			pDeclared = game.players().count();

		}
		
		
		String description = game.description().rawGameDescription();
		
		description = description.replaceAll("\"[^\"]*\"|'[^']*'", "");

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
    
    //@ParameterizedTest
	//@ValueSource(strings = { "Amazons.lud" })
  	@Tag("Static")
  	@TestTemplate
  	public void equalNumberOfPiecesOnTheBoard(String gameName) {
  		
  		Game game = GameLoader.loadGameFromName(gameName);
  		Context context = new Context(game, new Trial(game));
  		game.start(context);
  		BitSet concepts = game.computeBooleanConcepts();
  		
  		boolean edgeConcept = concepts.get(Concept.Edge.id());
  		//System.out.println("edgeCon " + edgeConcept);
  		
  		boolean vertexConcept = concepts.get(Concept.Vertex.id());
  		//System.out.println("verConcept " + vertexConcept);
  		
  		boolean cellConcept = concepts.get(Concept.Cell.id());
  		//System.out.println("cellConcept " + cellConcept);
  		
  		SiteType type = null;
  		List<? extends TopologyElement> sites = new ArrayList<>();
  		if(edgeConcept) {
  			
  			type = SiteType.Edge;
  			sites = context.board().topology().edges();
  			
  		}else if(vertexConcept) {
  			
  			type = SiteType.Vertex;
  			sites = context.board().topology().vertices();
  			
  		}else if(cellConcept) {
  			
  			type = SiteType.Cell;
  			sites = context.board().topology().cells();
  			//System.out.println("sites length " + sites.size());
  		}
  			
  		ContainerState[] containerState = context.state().containerStates();
  		//System.out.println("containers size: " + containerState.length);
  		
  		int numPlayers = context.state().numPlayers();
  		
  		Integer[] piecesForPlayer = new Integer[numPlayers];// vec[N-1] : number of pieces for player N
  		
  		for (int j = 0; j < numPlayers; j++) {
  			piecesForPlayer[j] = 0;
  		}
  		
  		for(ContainerState cs: containerState) {
  			
  			if (cs.container().style() != ContainerStyleType.Board) {
  				continue;
  			}
  			
  			for (int i = 0; i < sites.size(); i++) {
  				
  				int ownerIdx = cs.who(i, type);
  				if(ownerIdx > 0 && ownerIdx <= numPlayers) {
  					piecesForPlayer[ownerIdx - 1]++;
  				}			
  			}
  			
  		}
  		
  		if(piecesForPlayer.length == 0) {
  			fail("There are no pieces on the board");
  		}
  		
  		for(int i = 1; i < piecesForPlayer.length; i++) {
  			if(!(piecesForPlayer[i] == piecesForPlayer[0])) {
  				fail("Players do not have the same number of pieces");
  			}
  		}
  		
  				
  	}
    
    /*@TestTemplate
	@Tag("Dynamic")
	public void dynTestPlayer(String gameName) {
		assert (true);
	}*/
    
    
    
	
}
