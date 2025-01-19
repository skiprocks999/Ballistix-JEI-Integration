package ballistix.client.guidebook.chapters;

import ballistix.Ballistix;
import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.prefab.utils.BallistixTextUtils;
import ballistix.registers.BallistixItems;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import net.minecraft.network.chat.MutableComponent;

public class ChapterMissileSilo extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.missilesilo));

	public ChapterMissileSilo(Module module) {
		super(module);
	}

	@Override
	public ItemWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return BallistixTextUtils.guidebook("chapter.missilesilo");
	}

	@Override
	public void addData() {
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missilesilo.l1")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missilesilo.range", BallistixTextUtils.guidebook("chapter.missilesilo.close"), 3000)).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missilesilo.range", BallistixTextUtils.guidebook("chapter.missilesilo.medium"), 10000)).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missilesilo.range", BallistixTextUtils.guidebook("chapter.missilesilo.long"), BallistixTextUtils.guidebook("chapter.missilesilo.unlimited"))).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missilesilo.l2")).setSeparateStart());
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missilesilo.l3")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Ballistix.rl("textures/screen/guidebook/silo1.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Ballistix.rl("textures/screen/guidebook/silo2.png")));

	}

}
