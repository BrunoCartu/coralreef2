package coralreef2.observer.CoralStyleOGL2D;

import java.awt.Color;




import coralreef2.agent.Agent;
import coralreef2.common.Constants;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;

public class AgentStyleOGL2D extends DefaultStyleOGL2D{

// cool website for color coding 
// http://www.rapidtables.com/web/color/RGB_Color.htm

	@Override 
	public Color getColor(final Object obj){
		Agent  agent = (Agent) obj;
		
		return new Color((int)agent.getRed(), (int)agent.getGreen(), (int)agent.getBlue());
		
//		return super.getColor(agent);
	}
//	// determines the colour of the agents
//		@Override 
//		public Color getColor(final Object obj){
//			Agent  agent = (Agent) obj;
//			if (agent.substrateSubCategory == Constants.SUBSTRATE_SUBCATEGORY_LIVE_CORAL) { 					// depends on person's behavior
//				return new Color(agent.red, agent.green, agent.blue);
//			} else if (agent.substrateSubCategory == Constants.SUBSTRATE_SUBCATEGORY_DEAD_CORAL || 
//					   agent.substrateSubCategory == Constants.SUBSTRATE_SUBCATEGORY_BLEACHED_CORAL){ 				
//				return new Color(255,255,255);
//			} else if (agent.substrateCategory == Constants.SUBSTRATE_SUBCATEGORY_MACROALGAE) { 			// dark green
//				return new Color(20, 160, 0);
//			} else if (agent.substrateCategory == Constants.SUBSTRATE_SUBCATEGORY_TURF){
//				return new Color(178,255,102);
//			} else if (agent.substrateCategory == Constants.SUBSTRATE_SUBCATEGORY_CCA){
//				return new Color(255,178,102);	
//			} else if (agent.substrateCategory == Constants.SUBSTRATE_CATEGORY_BARREN_GROUND) { 	// grey
//				return new Color(160, 160, 160);
//			}
//			return super.getColor(agent);
//		}
		
		// for the shape of the agents
		@Override
		 public VSpatial getVSpatial(Object agent, VSpatial spatial) {
			    if (spatial == null) {
			      spatial = shapeFactory.createRectangle(15, 15);		
			    }
			    return spatial;
			  }

}
