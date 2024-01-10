package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_Plates extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_Plates(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        final int tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 60 : 15;
        final ItemStack ingotStackOne = material.getIngot(1);
        final ItemStack ingotStackTwo = material.getIngot(2);
        final ItemStack ingotStackThree = material.getIngot(3);
        final ItemStack ingotStackNine = material.getIngot(9);
        final ItemStack shape_Mold = ItemList.Shape_Mold_Plate.get(0);
        final ItemStack plate_Single = material.getPlate(1);
        final ItemStack plate_SingleTwo = material.getPlate(2);
        final ItemStack plate_SingleNine = material.getPlate(9);
        final ItemStack plate_Double = material.getPlateDouble(1);
        final ItemStack plate_Dense = material.getPlateDense(1);
        final ItemStack foil_SingleFour = material.getFoil(4);
        final ItemStack block = material.getBlock(1);

        Logger.WARNING("Generating Plate recipes for " + material.getLocalizedName());

        // Forge Hammer
        if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Single))
            if (addForgeHammerRecipe(
                    ingotStackThree,
                    plate_SingleTwo,
                    (int) Math.max(material.getMass(), 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Forge Hammer Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Forge Hammer Recipe: " + material.getLocalizedName() + " - Failed");
                    }
        // Bender
        if (ItemUtils.checkForInvalidItems(ingotStackOne) && ItemUtils.checkForInvalidItems(plate_Single))
            if (addBenderRecipe(
                    ingotStackOne,
                    plate_Single,
                    (int) Math.max(material.getMass() * 1L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Failed");
                    }
        if (ItemUtils.checkForInvalidItems(ingotStackOne) && ItemUtils.checkForInvalidItems(foil_SingleFour))
            if (addBenderRecipe(
                    ingotStackOne,
                    CI.getNumberedCircuit(10),
                    foil_SingleFour,
                    (int) Math.max(material.getMass() * 2L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Failed");
                    }
        // Alloy Smelter
        if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Single))
            if (GT_Values.RA.addAlloySmelterRecipe(
                    ingotStackTwo,
                    shape_Mold,
                    plate_Single,
                    (int) Math.max(material.getMass() * 2L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Alloy Smelter Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Alloy Smelter Recipe: " + material.getLocalizedName() + " - Failed");
                    }
        // Cutting Machine
        if (ItemUtils.checkForInvalidItems(block) && ItemUtils.checkForInvalidItems(plate_Single))
            if (GT_Values.RA.addCutterRecipe(
                    block,
                    plate_SingleNine,
                    null,
                    (int) Math.max(material.getMass() * 10L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Cutting Machine Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Cutting Machine Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        // Making Double Plates
        if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Double))
            if (addBenderRecipe(
                    ingotStackTwo,
                    plate_Double,
                    (int) Math.max(material.getMass() * 2L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        if (ItemUtils.checkForInvalidItems(plate_SingleTwo) && ItemUtils.checkForInvalidItems(plate_Double))
            if (addBenderRecipe(
                    plate_SingleTwo,
                    plate_Double,
                    (int) Math.max(material.getMass() * 2L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        // Bender
        if (ItemUtils.checkForInvalidItems(material.getPlate(1)) && ItemUtils.checkForInvalidItems(material.getFoil(1)))
            if (addBenderRecipe(
                    material.getPlate(1),
                    material.getFoil(4),
                    (int) Math.max(material.getMass(), 1L),
                    material.vVoltageMultiplier)) {
                        GregTech_API.registerCover(
                                material.getFoil(1),
                                new GT_RenderedTexture(
                                        material.getTextureSet().mTextures[70],
                                        material.getRGBA(),
                                        false),
                                null);
                        Logger.WARNING("Bender Foil Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Bender Foil Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        // Making Dense Plates
        if (ItemUtils.checkForInvalidItems(ingotStackNine) && ItemUtils.checkForInvalidItems(plate_Dense))
            if (addBenderRecipe(
                    ingotStackNine,
                    plate_Dense,
                    (int) Math.max(material.getMass() * 2L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        if (ItemUtils.checkForInvalidItems(plate_SingleNine) && ItemUtils.checkForInvalidItems(plate_Dense))
            if (addBenderRecipe(
                    plate_SingleNine,
                    plate_Dense,
                    (int) Math.max(material.getMass() * 2L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Failed");
                    }
    }

    public static boolean addBenderRecipe(final ItemStack aInput1, final ItemStack aOutput1, int aDuration,
            final int aEUt) {
        return GT_Values.RA.addBenderRecipe(aInput1, aOutput1, aDuration, aEUt);
    }

    public static boolean addBenderRecipe(final ItemStack aInput1, final ItemStack aCircuit, final ItemStack aOutput1,
            int aDuration, final int aEUt) {
        return GT_Values.RA.addBenderRecipe(aInput1, aCircuit, aOutput1, aDuration, aEUt);
    }

    public static boolean addExtruderRecipe(final ItemStack aInput, final ItemStack aShape, final ItemStack aOutput,
            int aDuration, final int aEUt) {
        if ((aInput == null) || (aShape == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("extruder", aOutput, aDuration)) <= 0) {
            return false;
        }
        RecipeMaps.extruderRecipes.addRecipe(
                true,
                new ItemStack[] { aInput, aShape },
                new ItemStack[] { aOutput },
                null,
                null,
                null,
                aDuration,
                aEUt,
                0);
        return true;
    }

    public static boolean addForgeHammerRecipe(final ItemStack aInput1, final ItemStack aOutput1, final int aDuration,
            final int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("forgehammer", aOutput1, true)) {
            return false;
        }
        RecipeMaps.hammerRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1 },
                new ItemStack[] { aOutput1 },
                null,
                null,
                null,
                aDuration,
                aEUt,
                0);
        return true;
    }
}
