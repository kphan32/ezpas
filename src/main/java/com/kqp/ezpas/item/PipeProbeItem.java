package com.kqp.ezpas.item;

import com.kqp.ezpas.block.entity.FilteredPipeBlockEntity;
import com.kqp.ezpas.block.entity.pullerpipe.PullerPipeBlockEntity;
import com.kqp.ezpas.network.OpenAdvancedFilterScreenS2C;
import com.kqp.ezpas.pipe.InsertionPoint;
import com.kqp.ezpas.pipe.filter.Filter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public class PipeProbeItem extends Item {
    public PipeProbeItem() {
        super(new Item.Settings().group(ItemGroup.REDSTONE).maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if (!world.isClient) {
            BlockPos pos = context.getBlockPos();
            BlockEntity be = world.getBlockEntity(pos);

            if (be instanceof PullerPipeBlockEntity) {
                PullerPipeBlockEntity ppBe = ((PullerPipeBlockEntity) be);
                List<InsertionPoint> ips = ppBe.getInsertionPoints();

                Consumer<Text> send = (text) -> {
                    context.getPlayer().sendMessage(text, false);
                };

                Consumer<String> sendText = (string) -> {
                    send.accept(new LiteralText(string));
                };

                send.accept(new TranslatableText("pipe_probe.message_header"));
                if (ips.isEmpty()) {
                    send.accept(new TranslatableText("pipe_probe.none"));
                } else {
                    for (InsertionPoint inventory : ips) {
                        send.accept(new TranslatableText(world.getBlockState(inventory.blockPos)
                            .getBlock()
                            .getTranslationKey()).append(String.format(
                            "@(%d, %d, %d)",
                            inventory.blockPos.getX(),
                            inventory.blockPos.getY(),
                            inventory.blockPos.getZ()
                        )));

                        send.accept(new LiteralText("  ").append(new TranslatableText(
                            "pipe_probe.insert_direction.header").append(new TranslatableText(
                            "pipe_probe.insert_direction." + inventory.side.asString()))));
                        send.accept(new LiteralText("  ").append(new TranslatableText("pipe_probe.priority").append(new LiteralText(
                            "" + inventory.priority))));
                        send.accept(new LiteralText("  ").append(new TranslatableText("pipe_probe.distance").append(new LiteralText(
                            "" + inventory.distance))));

                        if (!inventory.filters.isEmpty()) {
                            send.accept(new LiteralText("  ").append(new TranslatableText("pipe_probe.filters_header")));

                            for (Filter filter : inventory.filters) {
                                send.accept(new LiteralText("    ").append(new TranslatableText(filter.type.localizationKey)));

                                send.accept(new LiteralText("      ").append(new TranslatableText("pipe_probe.flags")));
                                for (int i = 0; i < filter.flags.length; i++) {
                                    if (filter.flags[i]) {
                                        send.accept(new LiteralText("        ").append(new TranslatableText(
                                            "container.filtered_pipe.advanced.flag" + i + ".help")));
                                    }
                                }

                                send.accept(new LiteralText("      ").append(new TranslatableText("pipe_probe.stacks")));
                                for (ItemStack stack : filter.itemStacks) {
                                    if (!stack.isEmpty()) {
                                        send.accept(new LiteralText("        ").append(new TranslatableText(stack.getTranslationKey())));
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (be instanceof FilteredPipeBlockEntity) {
                OpenAdvancedFilterScreenS2C.sendToPlayer(
                    (ServerPlayerEntity) context.getPlayer(),
                    context.getBlockPos(),
                    ((FilteredPipeBlockEntity) be).flags
                );
            }
        }

        return ActionResult.SUCCESS;
    }
}
