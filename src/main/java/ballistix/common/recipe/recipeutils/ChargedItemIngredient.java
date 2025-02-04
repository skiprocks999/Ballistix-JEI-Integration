package ballistix.common.recipe.recipeutils;

import java.util.Arrays;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import ballistix.common.recipe.BallistixRecipeInit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

public class ChargedItemIngredient implements ICustomIngredient {

    public static final MapCodec<ChargedItemIngredient> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    //
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(instance -> instance.ingredient),
                    //
                    TransferPack.CODEC.fieldOf("charge").forGetter(instance -> instance.charge),
                    //
                    Codec.BOOL.fieldOf("isStrict").forGetter(instance -> instance.isStrict)
//

            ).apply(builder, ChargedItemIngredient::new)


    );

    private final Ingredient ingredient;
    private final TransferPack charge;
    private final boolean isStrict;

    public ChargedItemIngredient(Ingredient ingredient, TransferPack charge, boolean isStrict) {
        this.ingredient = ingredient;
        this.charge = charge;
        this.isStrict = isStrict;
    }

    @Override
    public boolean test(ItemStack stack) {

        if (!ingredient.test(stack) || !(stack.getItem() instanceof IItemElectric)) {
            return false;
        }

        IItemElectric electric = (IItemElectric) stack.getItem();

        TransferPack stored = electric.extractPower(stack, Double.MAX_VALUE, true);

        if (isStrict) {

            return stored.equals(charge);

        } else {

            return stored.getVoltage() >= charge.getVoltage() && stored.getJoules() >= charge.getJoules();


        }

    }

    @Override
    public Stream<ItemStack> getItems() {
        return Arrays.stream(ingredient.getItems());
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return BallistixRecipeInit.CHARGEDITEM_INGREDIENT_TYPE.get();
    }

    @Override
    public String toString() {
        return "items: " + Arrays.toString(ingredient.getItems()) + ", charge: " + charge.toString() + " is strict: " + isStrict;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChargedItemIngredient other) {
            return other.isStrict == isStrict && other.ingredient.equals(ingredient) && other.charge.equals(charge);
        }
        return false;
    }
}
