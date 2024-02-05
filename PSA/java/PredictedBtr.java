import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PredictedBtr extends Vessel {

	private double averageSpeed;
	private double distance;
	private boolean isPatching;
	private double maxSpeed;
	private LocalDateTime patchingPredictedBtr;
	private LocalDateTime predictedBtr;
	private final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public PredictedBtr(String lName, String sName, String inVoyN, String outVoyN, double averageSpeed, double distance,
			boolean isPatching, double maxSpeed, String patchingPredictedBtr, String predictedBtr) {
		super(lName, sName, inVoyN, outVoyN);
		this.averageSpeed = averageSpeed;
		this.distance = distance;
		this.isPatching = isPatching;
		this.maxSpeed = maxSpeed;
		this.patchingPredictedBtr = LocalDateTime.parse(patchingPredictedBtr, DATEFORMAT);
		this.predictedBtr = LocalDateTime.parse(predictedBtr, DATEFORMAT);
	}

	public double getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isPatching() {
		return isPatching;
	}

	public void setPatching(boolean isPatching) {
		this.isPatching = isPatching;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public LocalDateTime getPatchingPredictedBtr() {
		return patchingPredictedBtr;
	}

	public void setPatchingPredictedBtr(LocalDateTime patchingPredictedBtr) {
		this.patchingPredictedBtr = patchingPredictedBtr;
	}

	public LocalDateTime getPredictedBtr() {
		return predictedBtr;
	}

	public void setPredictedBtr(LocalDateTime predictedBtr) {
		this.predictedBtr = predictedBtr;
	}

	@Override
	public String toString() {
		return "PredictedBtr [" + super.toString() + "], averageSpeed=" + averageSpeed + ", distance=" + distance
				+ ", isPatching=" + isPatching + ", maxSpeed=" + maxSpeed + ", patchingPredictedBtr="
				+ patchingPredictedBtr + ", predictedBtr=" + predictedBtr + "]";
	}
}
