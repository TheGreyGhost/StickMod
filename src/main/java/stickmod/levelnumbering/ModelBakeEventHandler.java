package stickmod.levelnumbering;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by TheGreyGhost on 19/04/2015.
 *
 // ModelBakeEvent will be used to add our ISmartItemModel to the ModelManager's registry (the
 //  registry used to map all the ModelResourceLocations to IBlockModels).
 */
public class ModelBakeEventHandler {
  public ModelBakeEventHandler(ModelResourceLocation i_modelToAdd)
  {
    modelToAdd = i_modelToAdd;
  }

  // Called after all the other baked models have been added to the modelRegistry
  // Allows us to manipulate the modelRegistry before BlockModelShapes caches them.
  @SubscribeEvent
  public void onModelBakeEvent(ModelBakeEvent event)
  {
    // Find the existing mapping for ChessboardSmartItemModel - we added it in StartupClientOnly.initClientOnly(), which
    //   caused it to be loaded from resources (model/items/mbe15_item_chessboard.json) just like an ordinary item
    // Replace the mapping with our ISmartBlockModel, using the existing mapped model as the base for the smart model.
    Object object =  event.modelRegistry.getObject(modelToAdd);
    if (object instanceof IBakedModel) {
      IBakedModel existingModel = (IBakedModel)object;
      ModelWithLevel customModel = new ModelWithLevel(existingModel);
      event.modelRegistry.putObject(modelToAdd, customModel);
    }
  }

  private ModelResourceLocation modelToAdd;
}
