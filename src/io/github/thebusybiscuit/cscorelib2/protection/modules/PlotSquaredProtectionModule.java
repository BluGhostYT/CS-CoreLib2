package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import com.github.intellectualsites.plotsquared.plot.config.Captions;
import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;
import com.github.intellectualsites.plotsquared.plot.util.Permissions;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;


public class PlotSquaredProtectionModule implements ProtectionModule {

	@Override
	public String getName() {
		return "PlotSquared";
	}
	
	@Override
	public boolean hasPermission(OfflinePlayer p, org.bukkit.Location l, Action action) {
		Block b = l.getBlock();
		
		Plot plot = new Location(b.getWorld().getName(), b.getX(), b.getY(), b.getZ()).getOwnedPlot();
		return plot == null || plot.isAdded(p.getUniqueId()) || check(p, action);
	}

	private boolean check(OfflinePlayer p, Action action) {
		switch (action) {
			case ACCESS_INVENTORIES:
				return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.PERMISSION_ADMIN_INTERACT_UNOWNED);
			case BREAK_BLOCK:
			case PLACE_BLOCK:
			default:
				return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.PERMISSION_ADMIN_BUILD_UNOWNED);
		}
	}
}
	
