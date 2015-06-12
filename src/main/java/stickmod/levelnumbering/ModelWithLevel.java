package stickmod.levelnumbering;

import com.google.common.primitives.Ints;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import stickmod.items.ItemStick;

import javax.vecmath.Matrix4f;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EveryoneElse on 5/06/2015.
 * Used to render digits showing the items' level - GUI view only.
 * GUI mode is switched on when isGUI() is called, and switched off when handlePerspective() is called
 * If GUI mode is on and the item has a level, a couple of baked quads are added corresponding to the digits
 */
public class ModelWithLevel implements ISmartItemModel, IPerspectiveAwareModel {

  public ModelWithLevel(IBakedModel i_underlyingItemModel) {
    underlyingItemModel = i_underlyingItemModel;
  }

  @Override
  public IBakedModel handleItemState(ItemStack stack) {
    level = ItemStick.DOESNT_HAVE_XP;
    if (stack != null && stack.getItem() instanceof ItemStick) {
      ItemStick itemStick = (ItemStick)stack.getItem();
      Pair<Integer,Integer> levelAndRemainderXP = itemStick.getLevelAndRemainderXP(stack);
      if (levelAndRemainderXP.getLeft() != ItemStick.DOESNT_HAVE_XP) {
        level = levelAndRemainderXP.getLeft();
        int xpToNextLevel = itemStick.xpToReachNextLevel(level);
        levelFraction = (xpToNextLevel == 0) ? 1.0F : (levelAndRemainderXP.getRight() + 1 ) / (float)xpToNextLevel;
        if (levelFraction > 1.0F) levelFraction = 1.0F;
      }
    }
    return this;
  }

  @Override
  public Pair<IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
    isInventoryPerspective = (cameraTransformType == ItemCameraTransforms.TransformType.GUI);

    switch(cameraTransformType)  {  // copied straight from ForgeHooksClient.handleCameraTransforms
      case FIRST_PERSON:
        RenderItem.applyVanillaTransform(underlyingItemModel.getItemCameraTransforms().firstPerson);
        break;
      case GUI:
        RenderItem.applyVanillaTransform(underlyingItemModel.getItemCameraTransforms().gui);
        break;
      case HEAD:
        RenderItem.applyVanillaTransform(underlyingItemModel.getItemCameraTransforms().head);
        break;
      case THIRD_PERSON:
        RenderItem.applyVanillaTransform(underlyingItemModel.getItemCameraTransforms().thirdPerson);
        break;
      default:
        break;
    }

    return new ImmutablePair<IBakedModel, Matrix4f>(this, null);
  }

  @Override
  public List getFaceQuads(EnumFacing enumFacing) {
    return underlyingItemModel.getFaceQuads(enumFacing);
  }

  @Override
  public List getGeneralQuads() {
    if (!isInventoryPerspective || level == ItemStick.DOESNT_HAVE_XP) {
      return underlyingItemModel.getGeneralQuads();
    }

    List<BakedQuad> bakedQuadList = new ArrayList<BakedQuad>(underlyingItemModel.getGeneralQuads());
    TextureAtlasSprite digitsTexture = Minecraft.getMinecraft().getTextureMapBlocks()
                                                    .getAtlasSprite("stickmod:items/digits");

    // "builtin/generated" items, which are generated from the 2D texture by adding a thickness in the z direction
    //    (i.e. north<-->south thickness) are centred around the z=0.5 plane.
    final float BUILTIN_GEN_ITEM_THICKNESS = 1/16.0F;
    final float BUILTIN_GEN_ITEM_Z_CENTRE = 0.5F;
    final float BUILTIN_GEN_ITEM_Z_MAX = BUILTIN_GEN_ITEM_Z_CENTRE + BUILTIN_GEN_ITEM_THICKNESS / 2.0F;
    final float BUILTIN_GEN_ITEM_Z_MIN = BUILTIN_GEN_ITEM_Z_CENTRE - BUILTIN_GEN_ITEM_THICKNESS / 2.0F;
    final float SOUTH_FACE_POSITION = 1.0F;  // the south face of the cube is at z = 1.0F
    final float NORTH_FACE_POSITION = 0.0F;  // the north face of the cube is at z = 0.0F
    // http://greyminecraftcoder.blogspot.co.at/2014/12/blocks-18.html

    final float DISTANCE_BEHIND_SOUTH_FACE = SOUTH_FACE_POSITION - BUILTIN_GEN_ITEM_Z_MAX;
    final float DISTANCE_BEHIND_NORTH_FACE = BUILTIN_GEN_ITEM_Z_MIN - NORTH_FACE_POSITION;

    final float digitCentrePositionY = 4.0F/16.0F;
    final float DIGIT_WIDTH_X = 8.0F / 16.0F;
    final float DIGIT_HEIGHT_Y = 8.0F / 16.0F;
    final float DIGIT_WIDTH_U = 8.0F / 32.0F;
    final float DIGIT_HEIGHT_V = 8.0F / 32.0F;

    final int ITEM_RENDER_LAYER_NONE_SPECIFIED = -1;
    final int ITEM_RENDER_LAYER0 = 0;
    final int ITEM_RENDER_LAYER1 = 1;

    // make a baked quad for each side of the chessboard i.e. front and back (south and north)
    final float DELTA_FOR_OVERLAP = 0.001F;  // add a small overlap to stop the quad from lying exactly on top of the
    //   existing face, which leads to "z-fighting" where the two quads
    //   fight each other to be on top.  looks awful.

    final int LEVEL_COLOUR = Color.BLUE.getRGB();   // DOESN'T WORK!
    final float [] digitUmin = {0.00F, 0.25F, 0.50F, 0.75F, 0.00F, 0.25F, 0.50F, 0.75F, 0.00F, 0.25F};
    final float [] digitVmin = {0.00F, 0.00F, 0.00F, 0.00F, 0.25F, 0.25F, 0.25F, 0.25F, 0.50F, 0.50F};

    if (level >= 100) {
      int digit = level / 100;
      if (digit > 9) digit = 9;
      float digitCentrePositionX = 1.0F/ 16.0F;
      BakedQuad hundredsDigit = createBakedQuadForFace(digitCentrePositionX, DIGIT_WIDTH_X,
                                                        digitCentrePositionY, DIGIT_HEIGHT_Y,
                                                        -DISTANCE_BEHIND_SOUTH_FACE + DELTA_FOR_OVERLAP * 3,
                                                        digitsTexture,
                                                        digitUmin[digit], DIGIT_WIDTH_U, digitVmin[digit], DIGIT_HEIGHT_V,
                                                        ITEM_RENDER_LAYER0, EnumFacing.SOUTH,
                                                        LEVEL_COLOUR);
      bakedQuadList.add(hundredsDigit);
    }
    if (level >= 10) {
      int digit = (level / 10) % 10;
      float digitCentrePositionX = 7.0F/ 16.0F;
      BakedQuad tensDigit = createBakedQuadForFace(digitCentrePositionX, DIGIT_WIDTH_X,
                                                       digitCentrePositionY, DIGIT_HEIGHT_Y,
                                                       -DISTANCE_BEHIND_SOUTH_FACE + DELTA_FOR_OVERLAP * 3,
                                                       digitsTexture,
                                                       digitUmin[digit], DIGIT_WIDTH_U, digitVmin[digit], DIGIT_HEIGHT_V,
                                                       ITEM_RENDER_LAYER0, EnumFacing.SOUTH,
                                                       LEVEL_COLOUR);
      bakedQuadList.add(tensDigit);
    }

    int digit = level % 10;
    float digitCentrePositionX = 13.0F/ 16.0F;
    BakedQuad onesDigit = createBakedQuadForFace(digitCentrePositionX, DIGIT_WIDTH_X,
                                                     digitCentrePositionY, DIGIT_HEIGHT_Y,
                                                     -DISTANCE_BEHIND_SOUTH_FACE + DELTA_FOR_OVERLAP * 3,
                                                     digitsTexture,
                                                     digitUmin[digit], DIGIT_WIDTH_U, digitVmin[digit], DIGIT_HEIGHT_V,
                                                     ITEM_RENDER_LAYER0, EnumFacing.SOUTH,
                                                     LEVEL_COLOUR);
    bakedQuadList.add(onesDigit);

    float XP_BAR_XMIN = 0.0F;
    float XP_BAR_XWIDTH = 1.0F;
    float XP_BAR_YMIN = 15.0F / 16.0F;
    float XP_BAR_YHEIGHT = 1.0F / 16.0F;

    float XP_BAR_GREY_UMIN = 0.0F;
    float XP_BAR_VMIN = 0.75F;

    BakedQuad experienceBarShadow = createBakedQuadForFace(XP_BAR_XMIN + XP_BAR_XWIDTH / 2.0F, XP_BAR_XWIDTH,
                                                 XP_BAR_YMIN + XP_BAR_YHEIGHT / 2.0F, XP_BAR_YHEIGHT,
                                                 -DISTANCE_BEHIND_SOUTH_FACE + DELTA_FOR_OVERLAP,
                                                 digitsTexture,
                                                 XP_BAR_GREY_UMIN, DIGIT_WIDTH_U, XP_BAR_VMIN, DIGIT_HEIGHT_V,
                                                 ITEM_RENDER_LAYER0, EnumFacing.SOUTH,
                                                 Color.WHITE.getRGB());
    bakedQuadList.add(experienceBarShadow);

    float xBarWidth = XP_BAR_XWIDTH * levelFraction;
    float XP_BAR_YELLOW_UMIN = 0.25F;
    BakedQuad experienceBar = createBakedQuadForFace(XP_BAR_XMIN + xBarWidth / 2.0F, xBarWidth,
                                                           XP_BAR_YMIN + XP_BAR_YHEIGHT / 2.0F, XP_BAR_YHEIGHT,
                                                           -DISTANCE_BEHIND_SOUTH_FACE + DELTA_FOR_OVERLAP * 2,
                                                           digitsTexture,
                                                           XP_BAR_YELLOW_UMIN, DIGIT_WIDTH_U, XP_BAR_VMIN, DIGIT_HEIGHT_V,
                                                           ITEM_RENDER_LAYER0, EnumFacing.SOUTH,
                                                           Color.WHITE.getRGB());
    bakedQuadList.add(experienceBar);

    return bakedQuadList;
  }


  @Override
  public boolean isAmbientOcclusion() {
    return underlyingItemModel.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    isInventoryPerspective = true;
    return underlyingItemModel.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return underlyingItemModel.isBuiltInRenderer();
  }

  @Override
  public TextureAtlasSprite getTexture() {
    return underlyingItemModel.getTexture();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return underlyingItemModel.getItemCameraTransforms();
  }

  /**
   // Creates a baked quad for the given face.
   // When you are directly looking at the face, the quad is centred at [centreLR, centreUD]
   // The left<->right "width" of the face is width, the bottom<-->top "height" is height.
   // The amount that the quad is displaced towards the viewer i.e. (perpendicular to the flat face you can see) is forwardDisplacement
   //   - for example, for an EAST face, a value of 0.00 lies directly on the EAST face of the cube.  a value of 0.01 lies
   //     slightly to the east of the EAST face (at x=1.01).  a value of -0.01 lies slightly to the west of the EAST face (at x=0.99).
   // The orientation of the faces is as per the diagram on this page
   //   http://greyminecraftcoder.blogspot.com.au/2014/12/block-models-texturing-quads-faces.html
   // Read this page to learn more about how to draw a textured quad
   //   http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
   * @param centreLR the centre point of the face left-right
   * @param width    width of the face
   * @param centreUD centre point of the face top-bottom
   * @param height height of the face from top to bottom
   * @param forwardDisplacement the displacement of the face (towards the front)
   * @param texture the texture to use for the quad
   * @param umin the u coordinate within the texture [0 - 1]
   * @param uwidth the u coordinate width within the texture [0 - 1]
   * @param vmin the v coordinate within the texture [0 - 1]
   * @param vheight the v coordinate height within the texture [0 - 1]
   * @param itemRenderLayer which item layer the quad is on
   * @param face the face to draw this quad on
   * @param colourRGB
   * @return
   */
  private BakedQuad createBakedQuadForFace(float centreLR, float width, float centreUD, float height,
                                           float forwardDisplacement,
                                           TextureAtlasSprite texture, float umin, float uwidth, float vmin,
                                           float vheight,
                                           int itemRenderLayer, EnumFacing face, int colourRGB)
  {
    float x1, x2, x3, x4;
    float y1, y2, y3, y4;
    float z1, z2, z3, z4;
    final float CUBE_MIN = 0.0F;
    final float CUBE_MAX = 1.0F;

    switch (face) {
      case UP: {
        x1 = x2 = centreLR + width/2.0F;
        x3 = x4 = centreLR - width/2.0F;
        z1 = z4 = centreUD + height/2.0F;
        z2 = z3 = centreUD - height/2.0F;
        y1 = y2 = y3 = y4 = CUBE_MAX + forwardDisplacement;
        break;
      }
      case DOWN: {
        x1 = x2 = centreLR + width/2.0F;
        x3 = x4 = centreLR - width/2.0F;
        z1 = z4 = centreUD - height/2.0F;
        z2 = z3 = centreUD + height/2.0F;
        y1 = y2 = y3 = y4 = CUBE_MIN - forwardDisplacement;
        break;
      }
      case WEST: {
        z1 = z2 = centreLR + width/2.0F;
        z3 = z4 = centreLR - width/2.0F;
        y1 = y4 = centreUD - height/2.0F;
        y2 = y3 = centreUD + height/2.0F;
        x1 = x2 = x3 = x4 = CUBE_MIN - forwardDisplacement;
        break;
      }
      case EAST: {
        z1 = z2 = centreLR - width/2.0F;
        z3 = z4 = centreLR + width/2.0F;
        y1 = y4 = centreUD - height/2.0F;
        y2 = y3 = centreUD + height/2.0F;
        x1 = x2 = x3 = x4 = CUBE_MAX + forwardDisplacement;
        break;
      }
      case NORTH: {
        x1 = x2 = centreLR - width/2.0F;
        x3 = x4 = centreLR + width/2.0F;
        y1 = y4 = centreUD - height/2.0F;
        y2 = y3 = centreUD + height/2.0F;
        z1 = z2 = z3 = z4 = CUBE_MIN - forwardDisplacement;
        break;
      }
      case SOUTH: {
        x1 = x2 = centreLR + width/2.0F;
        x3 = x4 = centreLR - width/2.0F;
        y1 = y4 = centreUD - height/2.0F;
        y2 = y3 = centreUD + height/2.0F;
        z1 = z2 = z3 = z4 = CUBE_MAX + forwardDisplacement;
        break;
      }
      default: {
        assert false : "Unexpected facing in createBakedQuadForFace:" + face;
        return null;
      }
    }

    return new BakedQuad(Ints.concat(vertexToInts(x1, y1, z1, colourRGB, texture, umin+uwidth, vmin+vheight),
                                     vertexToInts(x2, y2, z2, colourRGB, texture, umin+uwidth, vmin),
                                     vertexToInts(x3, y3, z3, colourRGB, texture, umin, vmin),
                                     vertexToInts(x4, y4, z4, colourRGB, texture, umin, vmin+vheight)),
                         itemRenderLayer, face);
  }

  /**
   * Converts the vertex information to the int array format expected by BakedQuads.
   * @param x x coordinate
   * @param y y coordinate
   * @param z z coordinate
   * @param color RGBA colour format - white for no effect, non-white to tint the face with the specified colour
   * @param texture the texture to use for the face
   * @param u u-coordinate within the texture [0-1] corresponding to [x,y,z]
   * @param v v-coordinate within the texture [0-1] corresponding to [x,y,z]
   * @return
   */
  private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
  {
    return new int[] {
            Float.floatToRawIntBits(x),
            Float.floatToRawIntBits(y),
            Float.floatToRawIntBits(z),
            color,
            Float.floatToRawIntBits(texture.getInterpolatedU(u * 16)),
            Float.floatToRawIntBits(texture.getInterpolatedV(v * 16)),
            0
    };
  }


private IBakedModel underlyingItemModel;
  private boolean isInventoryPerspective;
  private int level;
  private float levelFraction;
}
