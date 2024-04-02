package me.dueris.genesismc.factory;

import org.bukkit.Bukkit;
import org.bukkit.Fluid;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

@Deprecated(forRemoval = true)
public class TagRegistryParser {

    public static ArrayList<String> available_types = new ArrayList<>();
    public static HashMap<String, ArrayList<String>> registered = new HashMap();

    static {
        available_types.add("blocks");
        available_types.add("items");
        available_types.add("fluid");
        available_types.add("entity");
        available_types.add("biome");
        available_types.add("entity_type");
        available_types.add("damage");
    }

    public static void runParse() {
        File DatapackDir = new File(Bukkit.getServer().getPluginManager().getPlugin("GenesisMC").getDataFolder() + File.separator + ".." + File.separator + ".." + File.separator + Bukkit.getServer().getWorlds().get(0).getName() + File.separator + "datapacks");
        File[] datapacks = DatapackDir.listFiles();
        if (datapacks == null) return;

        for (File datapack : datapacks) {
            if (!datapack.isDirectory()) continue;
            File dataDir = new File(datapack.getAbsolutePath() + File.separator + "data");
            if (!dataDir.isDirectory()) continue;
            for (File mainDIR : dataDir.listFiles()) {
                if (!mainDIR.isDirectory()) continue;
                for (File insideDIR : mainDIR.listFiles()) {
                    if (!insideDIR.isDirectory()) continue;
                    if (!insideDIR.getName().equalsIgnoreCase("tags")) continue;
                    for (File tagFolder : insideDIR.listFiles()) {
                        for (String av : available_types) {
                            if (tagFolder.getName().equalsIgnoreCase(av)) {
                                for (File jsonTagFile : tagFolder.listFiles()) {
                                    if (jsonTagFile.getName().endsWith(".json")) {
                                        try (FileReader reader = new FileReader(jsonTagFile)) {
                                            JSONParser jsonParser = new JSONParser();
                                            String fileTag = mainDIR.getName() + ":" + jsonTagFile.getName().replace(".json", "");
                                            JSONObject jsE = (JSONObject) jsonParser.parse(reader);
                                            JSONObject jsonObject = jsE;
                                            if (jsonObject.containsKey("values")) {
                                                Object valE = jsonObject.get("values");
                                                if (valE instanceof JSONArray valeEarray) {
                                                    for (Object value : valeEarray) {
                                                        String valueStr = value.toString();
                                                        if (valueStr.startsWith("#minecraft:")) {
                                                            processMinecraftTag(valueStr, av, fileTag);
                                                        } else {
                                                            if (registered.containsKey(fileTag)) {
                                                                registered.get(fileTag).add(valueStr);
                                                            } else {
                                                                registered.put(fileTag, new ArrayList<>());
                                                                registered.get(fileTag).add(valueStr);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void processMinecraftTag(String tag, String registry, String fileTag) {
        String[] tagS = tag.split("#")[1].split(":");
        NamespacedKey saplingsKey = new NamespacedKey(tagS[0], tagS[1]);
        if (registry.equals("entity_type")) {
            if (Bukkit.getServer().getTag("entity_type", saplingsKey, EntityType.class) == null) return;
            Bukkit.getServer().getTag(registry, saplingsKey, EntityType.class);
            for (EntityType s : Bukkit.getServer().getTag("entity_type", saplingsKey, EntityType.class).getValues()) {
                if (registered.containsKey(fileTag)) {
                    registered.get(fileTag).add(s.getKey().toString());
                } else {
                    registered.put(fileTag, new ArrayList<>());
                    registered.get(fileTag).add(s.getKey().toString());
                }
            }
        } else if (registry.equals("biome")) {
            if (Bukkit.getServer().getTag("biome", saplingsKey, Biome.class) == null) return;
            Bukkit.getServer().getTag(registry, saplingsKey, Biome.class);
            for (Biome s : Bukkit.getServer().getTag("biome", saplingsKey, Biome.class).getValues()) {
                if (registered.containsKey(fileTag)) {
                    registered.get(fileTag).add(s.getKey().toString());
                } else {
                    registered.put(fileTag, new ArrayList<>());
                    registered.get(fileTag).add(s.getKey().toString());
                }
            }
        } else if (registry.equals("fluid")) {
            if (Bukkit.getServer().getTag("fluid", saplingsKey, Fluid.class) == null) return;
            Bukkit.getServer().getTag(registry, saplingsKey, Fluid.class);
            for (Fluid s : Bukkit.getServer().getTag("fluid", saplingsKey, Fluid.class).getValues()) {
                if (registered.containsKey(fileTag)) {
                    registered.get(fileTag).add(s.getKey().toString());
                } else {
                    registered.put(fileTag, new ArrayList<>());
                    registered.get(fileTag).add(s.getKey().toString());
                }
            }
        } else {
            Bukkit.getServer().getTag(registry, saplingsKey, Material.class);
            if (Bukkit.getServer().getTag("blocks", saplingsKey, Material.class) == null) return;
            for (Material s : Bukkit.getServer().getTag("blocks", saplingsKey, Material.class).getValues()) {
                if (registered.containsKey(fileTag)) {
                    registered.get(fileTag).add(s.getKey().toString());
                } else {
                    registered.put(fileTag, new ArrayList<>());
                    registered.get(fileTag).add(s.getKey().toString());
                }
            }
        }
    }

    public static ArrayList<String> getRegisteredTagFromFileKey(String fileTag) {
        if (registered.containsKey(fileTag)) {
            return registered.get(fileTag);
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
