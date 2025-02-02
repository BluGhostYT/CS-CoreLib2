package io.github.thebusybiscuit.cscorelib2.config;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import lombok.Getter;

public class Config {
	
	@Getter
	private File file;
	
	protected FileConfiguration config;
	
	/**
	 * Creates a new Config Object for the config.yml File of
	 * the specified Plugin
	 *
	 * @param  plugin The Instance of the Plugin, the config.yml is referring to
	 */
	public Config(Plugin plugin) {
		plugin.saveDefaultConfig();
		this.file = new File("plugins/" + plugin.getName().replace(" ", "_") + "/config.yml");
		this.config = YamlConfiguration.loadConfiguration(this.file);
		config.options().copyDefaults(true);
	}
	
	public Config(Plugin plugin, String name) {
		this.file = new File("plugins/" + plugin.getName().replace(" ", "_") + "/" + name);
		this.config = YamlConfiguration.loadConfiguration(this.file);
		config.options().copyDefaults(true);
	}
	
	/**
	 * Creates a new Config Object for the specified File
	 *
	 * @param  file The File for which the Config object is created for
	 */
	public Config(File file) {
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(this.file);
		config.options().copyDefaults(true);
	}
	
	/**
	 * Creates a new Config Object for the specified File and FileConfiguration
	 *
	 * @param  file The File to save to
	 * @param  config The FileConfiguration
	 */
	public Config(File file, FileConfiguration config) {
		this.file = file;
		this.config = config;
		config.options().copyDefaults(true);
	}
	
	/**
	 * Creates a new Config Object for the File with in
	 * the specified Location
	 *
	 * @param  path The Path of the File which the Config object is created for
	 */
	public Config(String path) {
		this.file = new File(path);
		this.config = YamlConfiguration.loadConfiguration(this.file);
		config.options().copyDefaults(true);
	}
	
	/**
	 * Converts this Config Object into a plain FileConfiguration Object
	 *
	 * @return      The converted FileConfiguration Object
	 */ 
	public FileConfiguration getConfiguration() {
		return this.config;
	}
	
	protected void store(String path, Object value) {
		this.config.set(path, value);
	}
	
	/**
	 * Sets the Value for the specified Path
	 *
	 * @param  path The path in the Config File
	 * @param  value The Value for that Path
	 */
	public void setValue(String path, Object value) {
		if (value == null) {
			this.store(path, value);
		}
		else if (value instanceof Inventory) {
			this.store(path + ".size", ((Inventory) value).getSize());
			for (int i = 0; i < ((Inventory) value).getSize(); i++) {
				this.store(path + "." + i, ((Inventory) value).getItem(i));
			}
		}
		else if (value instanceof Date) {
			this.store(path, String.valueOf(((Date) value).getTime()));
		}
		else if (value instanceof Long) {
			this.store(path, String.valueOf(value));
		}
		else if (value instanceof UUID) {
			this.store(path, value.toString());
		}
		else if (value instanceof Sound) {
			this.store(path, String.valueOf(value));
		}
		else if (value instanceof Location) {
			this.store(path + ".x", ((Location) value).getX());
			this.store(path + ".y", ((Location) value).getY());
			this.store(path + ".z", ((Location) value).getZ());
			this.store(path + ".pitch", ((Location) value).getPitch());
			this.store(path + ".yaw", ((Location) value).getYaw());
			this.store(path + ".world", ((Location) value).getWorld().getName());
		}
		else if (value instanceof Chunk) {
			this.store(path + ".x", ((Chunk) value).getX());
			this.store(path + ".z", ((Chunk) value).getZ());
			this.store(path + ".world", ((Chunk) value).getWorld().getName());
		}
		else if (value instanceof World) {
			this.store(path, ((World) value).getName());
		}
		else this.store(path, value);
	}
	
	/**
	 * Saves the Config Object to its File
	 */ 
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the Config Object to a File
	 * 
	 * @param  file The File you are saving this Config to
	 */ 
	public void save(File file) {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Sets the Value for the specified Path 
	 * (IF the Path does not yet exist)
	 *
	 * @param  path The path in the Config File
	 * @param  value The Value for that Path
	 */
	public void setDefaultValue(String path, Object value) {
		if (!contains(path)) setValue(path, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOrSetDefault(String path, T value) {
		Object val = getValue(path);
		
		if (value.getClass().isInstance(val)) {
			return (T) val;
		}
		else {
			setValue(path, value);
			return value;
		}
	}
	
	/**
	 * Checks whether the Config contains the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      True/false
	 */ 
	public boolean contains(String path) {
		return config.contains(path);
	}
	
	/**
	 * Returns the Object at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Value at that Path
	 */ 
	public Object getValue(String path) {
		return config.get(path);
	}
	
	/**
	 * Returns the ItemStack at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The ItemStack at that Path
	 */ 
	public ItemStack getItem(String path) {
		return config.getItemStack(path);
	}
	
	/**
	 * Returns the String at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The String at that Path
	 */ 
	public String getString(String path) {
		return config.getString(path);
	}
	
	/**
	 * Returns the Integer at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Integer at that Path
	 */ 
	public int getInt(String path) {
		return config.getInt(path);
	}
	
	/**
	 * Returns the Boolean at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Boolean at that Path
	 */ 
	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}
	
	/**
	 * Returns the StringList at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The StringList at that Path
	 */ 
	public List<String> getStringList(String path) {
		return config.getStringList(path);
	}
	
	/**
	 * Returns the IntegerList at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The IntegerList at that Path
	 */ 
	public List<Integer> getIntList(String path) {
		return config.getIntegerList(path);
	}
	
	/**
	 * Recreates the File of this Config
	 */ 
	public void createFile() {
		try {
			this.file.createNewFile();
		} catch (IOException e) {
		}
	}
	
	/**
	 * Returns the Float at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Float at that Path
	 */ 
	public Float getFloat(String path) {
		return Float.valueOf(String.valueOf(getValue(path)));
	}
	
	/**
	 * Returns the Long at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Long at that Path
	 */ 
	public Long getLong(String path) {
		return Long.valueOf(String.valueOf(getValue(path)));
	}

	/**
	 * Returns the Sound at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Sound at that Path
	 */ 
	public Sound getSound(String path) {
		return Sound.valueOf(getString(path));
	}
	
	/**
	 * Returns the Date at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Date at that Path
	 */ 
	public Date getDate(String path) {
		return new Date(getLong(path));
	}
	
	/**
	 * Returns the Chunk at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Chunk at that Path
	 */ 
	public Chunk getChunk(String path) {
		return Bukkit.getWorld(getString(path + ".world")).getChunkAt(getInt(path + ".x"), getInt(path + ".z"));
	}
	
	/**
	 * Returns the UUID at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The UUID at that Path
	 */ 
	public UUID getUUID(String path) {
		return UUID.fromString(getString(path));
	}
	
	/**
	 * Returns the World at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The World at that Path
	 */ 
	public World getWorld(String path) {
		return Bukkit.getWorld(getString(path));
	}
	
	/**
	 * Returns the Double at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Double at that Path
	 */ 
	public Double getDouble(String path) {
		return config.getDouble(path);
	}
	
	/**
	 * Returns the Location at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Location at that Path
	 */ 
	public Location getLocation(String path) {
		return new Location(
				Bukkit.getWorld(
				getString(path + ".world")),
				getDouble(path + ".x"),
				getDouble(path + ".y"),
				getDouble(path + ".z"),
				getFloat(path + ".yaw"),
				getFloat(path + ".pitch")
		);
	}
	
	/**
	 * Gets the Contents of an Inventory at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @param  size The Size of the Inventory
	 * @param  title The Title of the Inventory
	 * @return      The generated Inventory
	 */ 
	public Inventory getInventory(String path, int size, String title) {
		Inventory inventory = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));
		for (int i = 0; i < size; i++) {
			inventory.setItem(i, getItem(path + "." + i));
		}
		return inventory;
	}
	
	/**
	 * Gets the Contents of an Inventory at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The generated Inventory
	 */ 
	public Inventory getInventory(String path, String title) {
		int size = getInt(path + ".size");
		Inventory inventory = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));
		
		for (int i = 0; i < size; i++) {
			inventory.setItem(i, getItem(path + "." + i));
		}
		
		return inventory;
	}
	
	/**
	 * Returns all Paths in this Config
	 *
	 * @return      All Paths in this Config
	 */ 
	public Set<String> getKeys() {
		return config.getKeys(false);
	}
	
	/**
	 * Returns all Sub-Paths in this Config
	 *
	 * @param  path The path in the Config File
	 * @return      All Sub-Paths of the specified Path
	 */ 
	public Set<String> getKeys(String path) {
		return config.getConfigurationSection(path).getKeys(false);
	}
	
	/**
	 * Reloads the Configuration File
	 */ 
	public void reload() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}
}
