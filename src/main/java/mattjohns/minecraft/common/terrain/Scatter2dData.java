package mattjohns.minecraft.common.terrain;

public class Scatter2dData {
	public int positionX;
	public int positionZ;

	public int offsetToGridX;
	public int offsetToGridZ;

	public int offsetToCenterX;
	public int offsetToCenterZ;

	public int halfSizeX;
	public int halfSizeY;
	public int halfSizeZ;

	public int groundY;
	public int centerGroundY;

	public Scatter2dData() {
	}

	public Scatter2dData(Scatter2dData source) {
		this.positionX = source.positionX;
		this.positionZ = source.positionZ;
		this.offsetToGridX = source.offsetToGridX;
		this.offsetToGridZ = source.offsetToGridZ;
		this.offsetToCenterX = source.offsetToCenterX;
		this.offsetToCenterZ = source.offsetToCenterZ;
		this.halfSizeX = source.halfSizeX;
		this.halfSizeY = source.halfSizeY;
		this.halfSizeZ = source.halfSizeZ;
		this.groundY = source.groundY;
		this.centerGroundY = source.centerGroundY;
	}
}
