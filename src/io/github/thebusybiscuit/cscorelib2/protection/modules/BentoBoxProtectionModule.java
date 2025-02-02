package io.github.thebusybiscuit.cscorelib2.protection.modules;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;
import world.bentobox.bentobox.managers.IslandsManager;

/**
 * Provides protection handling using the BentoBox API.
 * @author Poslovitch
 * @author TheBusyBiscuit
 */
public class BentoBoxProtectionModule implements ProtectionModule {
	
	private IslandsManager manager = BentoBox.getInstance().getIslands();
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		Optional<Island> island = manager.getIslandAt(l);
        return island.map(value -> value.isAllowed(User.getInstance(p), convert(action))).orElse(false);
	}
	
	private Flag convert(Action action) {
		switch (action) {
		case ACCESS_INVENTORIES:
			return Flags.CONTAINER;
		case BREAK_BLOCK:
			return Flags.BREAK_BLOCKS;
		case PLACE_BLOCK:
		default:
			return Flags.PLACE_BLOCKS;
		}
	}

    @Override
    public String getName() {
        return "BentoBox";
    }
}
