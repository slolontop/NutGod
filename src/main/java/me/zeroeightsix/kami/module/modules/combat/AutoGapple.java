package me.zeroeightsix.kami.module.modules.combat;


import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Module.Info(name = "AutoGapple", category = Module.Category.COMBAT)
public class AutoGapple extends Module {
    private int gapples;
    private boolean moving = false;
    private boolean returnI = false;

    private Setting<Boolean> soft = register(Settings.b("Soft", false));
    private Setting<Boolean> totem_disable = register(Settings.b("TotemOnDisable", true));


    @Override
    public void onEnable() {
        if (totem_disable.getValue()) {
            ModuleManager.getModuleByName("AutoTotem").disable();
        } else {
            return;
        }
    }

    @Override
    public void onDisable() {
        if (totem_disable.getValue()) {
            ModuleManager.getModuleByName("AutoTotem").enable();
        } else {
            return;
        }
    }

    @Override
    public void onUpdate() {
        if (mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (returnI) {
            int t = -1;
            for (int i = 0; i < 45; i++) {
                if (mc.player.inventory.getStackInSlot(i).isEmpty) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            returnI = false;
        }
        gapples = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            gapples++;
        } else {
            if (soft.getValue() && !mc.player.getHeldItemOffhand().isEmpty) {
                return;
            }
            if (moving) {
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                moving = false;
                if (!mc.player.inventory.itemStack.isEmpty()) {
                    returnI = true;
                }
                return;
            }
            if (mc.player.inventory.itemStack.isEmpty()) {
                if (gapples == 0) {
                    return;
                }
                int t = -1;
                for (int i = 0; i < 45; i++) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return; // Should never happen!
                }
                mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
                moving = true;
            } else if (!soft.getValue()) {
                int t = -1;
                for (int i = 0; i < 45; i++) {
                    if (mc.player.inventory.getStackInSlot(i).isEmpty) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            }
        }
    }

    @Override
    public String getHudInfo() {
        return String.valueOf(gapples);
    }



}
