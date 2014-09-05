package blargerist.cake.witherexpansion;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ModConfig
{
	private static Configuration config;

	public static void init(File file)
	{
		config = new Configuration(file);

		load();
		
		save();
	}

	public static void save()
	{
		config.save();
	}

	public static void load()
	{
		config.load();
	}
}
