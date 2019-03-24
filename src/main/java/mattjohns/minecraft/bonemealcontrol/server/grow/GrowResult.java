package mattjohns.minecraft.bonemealcontrol.server.grow;

import mattjohns.common.immutable.Immutable;

// Need to use this result rather than just a flag because vanilla and custom bonemeal use
// need to be handled differently by the caller. 
public class GrowResult extends Immutable<GrowResult> {
	public final boolean isGrow;
	public final boolean isVanilla;

	protected GrowResult(boolean isGrow, boolean isVanilla) {
		this.isGrow = isGrow;
		this.isVanilla = isVanilla;
	}

	public static GrowResult of(boolean isGrow, boolean isVanilla) {
		return new GrowResult(isGrow, isVanilla);
	}

	public static GrowResult ofVanilla(boolean isGrow) {
		return new GrowResult(isGrow, true);
	}

	public static GrowResult ofCustom(boolean isGrow) {
		return new GrowResult(isGrow, false);
	}
}
