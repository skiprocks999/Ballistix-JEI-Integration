package ballistix.registers;

import ballistix.References;
import ballistix.common.inventory.container.ContainerFireControlRadar;
import ballistix.common.inventory.container.ContainerMissileSilo;
import ballistix.common.inventory.container.ContainerSAMTurret;
import ballistix.common.inventory.container.ContainerSearchRadar;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BallistixMenuTypes {

	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, References.ID);

	public static final DeferredHolder<MenuType<?>,MenuType<ContainerMissileSilo>> CONTAINER_MISSILESILO = register("missilesilo", ContainerMissileSilo::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerFireControlRadar>> CONTAINER_FIRECONTROLRADAR = register("firecontrolradar", ContainerFireControlRadar::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerSearchRadar>> CONTAINER_SEARCHRADAR = register("searchradar", ContainerSearchRadar::new);
	public static final DeferredHolder<MenuType<?>, MenuType<ContainerSAMTurret>> CONTAINER_SAMTURRET = register("samturret", ContainerSAMTurret::new);

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, MenuSupplier<T> supplier) {
		return MENU_TYPES.register(id, () -> new MenuType<>(supplier, FeatureFlags.VANILLA_SET));
	}

}
