package com.psa.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.psa.entity.Prediction;
import com.psa.entity.Prediction.SpeedChange;
import com.psa.entity.SpeedRecord;
import com.psa.entity.Vessel;

public class VesselDTO {
	private int id;
	private String vesselName;
	private String inVoyNo;
	private String outVoyNo;
	private String berthingTime;
	private String depatureTime;
	private String berthNo;
	private String status;
	private int shiftSeqNo;

	private boolean hasPrediction;

	private String avgSpeed;
	private String maxSpeed;
	private String distance;
	private String predictedTime;
	private boolean isLate;
	private SpeedChange isSpeedIncrease;

	private String[] label;
	private double[] data;
	private int progress;

	private String nextPort;
	private String lastPort;
	private double distanceToGo;
	private double distanceTravelled;

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

	/**
	 * Encapsulates Vessel with multiple inheritance to a single Object
	 * 
	 * @param vessel a Vessel object
	 * @return a VesselDTO
	 */
	public static VesselDTO convertToVesselDTO(Vessel vessel) {
		VesselDTO vesselDTO = new VesselDTO();
		vesselDTO.id = vessel.getId();
		vesselDTO.vesselName = vessel.getFullName();
		vesselDTO.inVoyNo = vessel.getInVoyNo();
		vesselDTO.outVoyNo = vessel.getOutVoyNo();
		vesselDTO.berthingTime = vessel.getBerthingDate().format(DATEFORMAT);
		vesselDTO.depatureTime = vessel.getUnBerthingDate().format(DATEFORMAT);
		vesselDTO.berthNo = vessel.getBerthNo();
		vesselDTO.status = vessel.getStatus();
		vesselDTO.shiftSeqNo = vessel.getShiftSeqNo();

		Prediction prediction = vessel.getPrediction();
		if (prediction != null) {
			vesselDTO.hasPrediction = true;
			prediction.sortByTimestamp();
			vesselDTO.maxSpeed = prediction.getMaxSpeed() + "";
			vesselDTO.distance = prediction.getDistance() + "";
			vesselDTO.avgSpeed = prediction.getLatestSpeedRecord() + "";
			vesselDTO.isSpeedIncrease = prediction.isSpeedIncrease();
			vesselDTO.predictedTime = prediction.getPredBerthing().format(DATEFORMAT);
			vesselDTO.isLate = prediction.isLate(vessel.getBerthingDate());

			//to get the progress bar

			double progressPercentage = prediction.getDistanceTravelled() / (prediction.getDistanceToGo() + prediction.getDistanceTravelled());
			vesselDTO.progress = (int)(progressPercentage*100);

			vesselDTO.nextPort = prediction.getNextPort();
			vesselDTO.lastPort = prediction.getLastPort();
			vesselDTO.distanceToGo = prediction.getDistanceToGo();
			vesselDTO.distanceTravelled = prediction.getDistanceTravelled();

			List<SpeedRecord> records = prediction.getSpeedRecords();
			vesselDTO.label = new String[records.size()];
			vesselDTO.data = new double[records.size()];

			int idx = records.size() - 1;
			for (SpeedRecord record: records) {
				vesselDTO.label[idx] = record.getTimestamp().format(DATEFORMAT);
				vesselDTO.data[idx] = record.getAverageSpeed();
				idx--;
			}
		} else {
			vesselDTO.isSpeedIncrease = SpeedChange.UNCHANGED;
		}
		return vesselDTO;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getInVoyNo() {
		return inVoyNo;
	}

	public void setInVoyNo(String inVoyNo) {
		this.inVoyNo = inVoyNo;
	}

	public String getOutVoyNo() {
		return outVoyNo;
	}

	public void setOutVoyNo(String outVoyNo) {
		this.outVoyNo = outVoyNo;
	}

	public String getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public SpeedChange isSpeedIncrease() {
		return isSpeedIncrease;
	}

	public void setSpeedIncrease(SpeedChange isSpeedIncrease) {
		this.isSpeedIncrease = isSpeedIncrease;
	}

	public String getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public String getDistance() {
		return distance;
	}
	
	public void setDistance(String distance) {
		this.distance = distance;
	}

	public double getDistanceToGo(){
		return distanceToGo;
	}

	public void setDistanceToGo(double distanceToGo){
		this.distanceToGo = distanceToGo;
	}

	

	public String getBerthingTime() {
		return berthingTime;
	}

	public void setBerthingTime(String berthingTime) {
		this.berthingTime = berthingTime;
	}

	public String getDepatureTime() {
		return depatureTime;
	}

	public void setDepatureTime(String depatureTime) {
		this.depatureTime = depatureTime;
	}

	public String getBerthNo() {
		return berthNo;
	}

	public void setBerthNo(String berthNo) {
		this.berthNo = berthNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SpeedChange getIsSpeedIncrease() {
		return isSpeedIncrease;
	}

	public void setIsSpeedIncrease(SpeedChange isSpeedIncrease) {
		this.isSpeedIncrease = isSpeedIncrease;
	}

	public int getShiftSeqNo() {
		return shiftSeqNo;
	}

	public void setShiftSeqNo(int shiftSeqNo) {
		this.shiftSeqNo = shiftSeqNo;
	}

	public String getPredictedTime() {
		return predictedTime;
	}

	public void setPredictedTime(String predictedTime) {
		this.predictedTime = predictedTime;
	}

	public boolean isLate() {
		return isLate;
	}

	public void setLate(boolean isLate) {
		this.isLate = isLate;
	}

	public String[] getLabel() {
		return label;
	}

	public void setLabel(String[] label) {
		this.label = label;
	}

	public double[] getData() {
		return data;
	}

	public void setData(double[] data) {
		this.data = data;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getNextPort() {
		return nextPort;
	}

	public void setNextPort(String nextPort) {
		this.nextPort = nextPort;
	}

	public String getLastPort() {
		return lastPort;
	}

	public void setLastPort(String lastPort) {
		this.lastPort = lastPort;
	}

	public boolean isHasPrediction() {
		return hasPrediction;
	}

	public void setHasPrediction(boolean hasPrediction) {
		this.hasPrediction = hasPrediction;
	}

	public double getDistanceTravelled() {
		return distanceTravelled;
	}

	public void setDistanceTravelled(double distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}

}
