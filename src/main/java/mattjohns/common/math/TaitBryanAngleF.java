package mattjohns.common.math;

/**
 * Roll pitch and yaw angles.  The formal name is Tait Bryan Angles.
 */
public class TaitBryanAngleF {
	public float yaw;
	public float roll;
	public float pitch;

	public TaitBryanAngleF(float yaw, float pitch, float roll) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public TaitBryanAngleF clampGet() {
		return new TaitBryanAngleF(General.angleClamp(yaw), General.angleClamp(pitch), General.angleClamp(roll));
	}
}
