package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

@Module.Info(name = "TagESP", category = Module.Category.RENDER);
public class TagESP extends Module {
	private Setting<Boolean> show_durability = register(Settings.b("Durability", false));
}