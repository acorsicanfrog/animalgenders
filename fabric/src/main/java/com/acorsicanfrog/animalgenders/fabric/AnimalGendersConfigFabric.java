package com.acorsicanfrog.animalgenders.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AnimalGendersConfigFabric
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static AnimalGendersConfigFabric INSTANCE;

    private List<String> blacklist = List.of("minecraft:bee");
    private List<String> whitelist = List.of();
    private double femaleChance = 0.8;

    public List<String> getBlacklist() { return blacklist; }
    public List<String> getWhitelist() { return whitelist; }
    public double getFemaleChance() { return femaleChance; }

    public static AnimalGendersConfigFabric get()
    {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    public static void load()
    {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("animalgenders.json");

        if (Files.exists(configPath))
        {
            try (Reader reader = Files.newBufferedReader(configPath))
            {
                INSTANCE = GSON.fromJson(reader, AnimalGendersConfigFabric.class);
                if (INSTANCE == null) INSTANCE = new AnimalGendersConfigFabric();
            }
            catch (IOException e)
            {
                INSTANCE = new AnimalGendersConfigFabric();
            }
        }
        else
        {
            INSTANCE = new AnimalGendersConfigFabric();
            save();
        }
    }

    private static void save()
    {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("animalgenders.json");

        try (Writer writer = Files.newBufferedWriter(configPath))
        {
            GSON.toJson(INSTANCE, writer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
