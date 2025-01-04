package ballistix.common.recipe;

import ballistix.common.recipe.recipeutils.ChargedItemIngredient;
import electrodynamics.api.References;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BallistixRecipeInit {

    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, References.ID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<ChargedItemIngredient>> CHARGEDITEM_INGREDIENT_TYPE = INGREDIENT_TYPES.register("chargeditemingredient", () -> new IngredientType<>(ChargedItemIngredient.CODEC));


}
