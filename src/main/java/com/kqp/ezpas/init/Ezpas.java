package com.kqp.ezpas.init;

import net.fabricmc.api.ModInitializer;
import com.kqp.ezpas.block.pullerpipe.DiamondPullerPipeBlock;
import com.kqp.ezpas.block.pullerpipe.EnderPullerPipeBlock;
import com.kqp.ezpas.block.pullerpipe.GoldPullerPipeBlock;
import com.kqp.ezpas.block.pullerpipe.IronPullerPipeBlock;
import com.kqp.ezpas.block.PipeBlock;
import com.kqp.ezpas.block.entity.pullerpipe.DiamondPullerPipeBlockEntity;
import com.kqp.ezpas.block.entity.pullerpipe.EnderPullerPipeBlockEntity;
import com.kqp.ezpas.block.entity.pullerpipe.GoldPullerPipeBlockEntity;
import com.kqp.ezpas.block.entity.pullerpipe.IronPullerPipeBlockEntity;
import com.kqp.ezpas.item.PipeProbeItem;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Ezpas implements ModInitializer {
    public static final String ID = "ezpas";

    public static final Item PIPE_PROBE = register("pipe_probe", new PipeProbeItem());

    public static final Block IRON_PP = register("iron_puller_pipe", new IronPullerPipeBlock());
    public static final Block GOLD_PP = register("gold_puller_pipe", new GoldPullerPipeBlock());
    public static final Block DIAMOND_PP = register("diamond_puller_pipe", new DiamondPullerPipeBlock());
    public static final Block ENDER_PP = register("ender_puller_pipe", new EnderPullerPipeBlock());

    public static BlockEntityType<IronPullerPipeBlockEntity> IRON_PP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ID, "iron_puller_pipe"),
            BlockEntityType.Builder.create(IronPullerPipeBlockEntity::new, IRON_PP).build(null));

    public static BlockEntityType<GoldPullerPipeBlockEntity> GOLD_PP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ID, "gold_puller_pipe"),
            BlockEntityType.Builder.create(GoldPullerPipeBlockEntity::new, GOLD_PP).build(null));

    public static BlockEntityType<DiamondPullerPipeBlockEntity> DIAMOND_PP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ID, "diamond_puller_pipe"),
            BlockEntityType.Builder.create(DiamondPullerPipeBlockEntity::new, DIAMOND_PP).build(null));

    public static BlockEntityType<EnderPullerPipeBlockEntity> ENDER_PP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ID, "ender_puller_pipe"),
            BlockEntityType.Builder.create(EnderPullerPipeBlockEntity::new, ENDER_PP).build(null));

    public static final Block PIPE = register("pipe", new PipeBlock());

    public static final Block[] COLORED_PIPES = new Block[DyeColor.values().length];

    @Override
    public void onInitialize() {
    }

    private static Block register(String name, Block block) {
        Registry.BLOCK.add(new Identifier(ID, name), block);
        Registry.ITEM.add(new Identifier(ID, name), new BlockItem(block, new Item.Settings().group(ItemGroup.REDSTONE)));

        return block;
    }

    private static Item register(String name, Item item) {
        Registry.ITEM.add(new Identifier(ID, name), item);

        return item;
    }

    static {
        for (int i = 0; i < DyeColor.values().length; i++) {
            DyeColor color = DyeColor.values()[i];

            COLORED_PIPES[i] = register(color.getName() + "_stained_pipe", new PipeBlock());
        }
    }
}
