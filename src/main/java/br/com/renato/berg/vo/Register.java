package br.com.renato.berg.vo;

import java.time.LocalTime;

public class Register implements Comparable<Register> {

	private LocalTime time;
	private String pilotCode;
	private String pilotName;
	private Integer numberOfTurn;
	private LocalTime timeTurn;
	private Double averageLapSpeed;

	/**
	 * @return the time
	 */
	public LocalTime getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(LocalTime time) {
		this.time = time;
	}

	/**
	 * @return the pilotCode
	 */
	public String getPilotCode() {
		return pilotCode;
	}

	/**
	 * @param pilotCode the pilotCode to set
	 */
	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;
	}

	/**
	 * @return the pilotName
	 */
	public String getPilotName() {
		return pilotName;
	}

	/**
	 * @param pilotName the pilotName to set
	 */
	public void setPilotName(String pilotName) {
		this.pilotName = pilotName;
	}

	/**
	 * @return the numberOfTurn
	 */
	public Integer getNumberOfTurn() {
		return numberOfTurn;
	}

	/**
	 * @param numberOfTurn the numberOfTurn to set
	 */
	public void setNumberOfTurn(Integer numberOfTurn) {
		this.numberOfTurn = numberOfTurn;
	}

	/**
	 * @return the timeTurn
	 */
	public LocalTime getTimeTurn() {
		return timeTurn;
	}

	/**
	 * @param timeTurn the timeTurn to set
	 */
	public void setTimeTurn(LocalTime timeTurn) {
		this.timeTurn = timeTurn;
	}

	/**
	 * @return the averageLapSpeed
	 */
	public Double getAverageLapSpeed() {
		return averageLapSpeed;
	}

	/**
	 * @param averageLapSpeed the averageLapSpeed to set
	 */
	public void setAverageLapSpeed(Double averageLapSpeed) {
		this.averageLapSpeed = averageLapSpeed;
	}

	public int compareTo(Register anotherRegister) {
		if ((this.numberOfTurn > anotherRegister.getNumberOfTurn()) || (this.numberOfTurn == anotherRegister.getNumberOfTurn() && this.time.isBefore(anotherRegister.getTime()))) {
			return -1;
		}
		
		if ((this.numberOfTurn < anotherRegister.getNumberOfTurn()) || (this.numberOfTurn == anotherRegister.getNumberOfTurn() && this.time.isAfter(anotherRegister.getTime()))) {
			return 1;
		}
		
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((averageLapSpeed == null) ? 0 : averageLapSpeed.hashCode());
		result = prime * result + ((numberOfTurn == null) ? 0 : numberOfTurn.hashCode());
		result = prime * result + ((pilotCode == null) ? 0 : pilotCode.hashCode());
		result = prime * result + ((pilotName == null) ? 0 : pilotName.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((timeTurn == null) ? 0 : timeTurn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Register other = (Register) obj;
		if (averageLapSpeed == null) {
			if (other.averageLapSpeed != null)
				return false;
		} else if (!averageLapSpeed.equals(other.averageLapSpeed))
			return false;
		if (numberOfTurn == null) {
			if (other.numberOfTurn != null)
				return false;
		} else if (!numberOfTurn.equals(other.numberOfTurn))
			return false;
		if (pilotCode == null) {
			if (other.pilotCode != null)
				return false;
		} else if (!pilotCode.equals(other.pilotCode))
			return false;
		if (pilotName == null) {
			if (other.pilotName != null)
				return false;
		} else if (!pilotName.equals(other.pilotName))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (timeTurn == null) {
			if (other.timeTurn != null)
				return false;
		} else if (!timeTurn.equals(other.timeTurn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Register [time=" + time + ", pilotCode=" + pilotCode + ", pilotName=" + pilotName + ", numberOfTurn="
				+ numberOfTurn + ", timeTurn=" + timeTurn + ", averageLapSpeed=" + averageLapSpeed + "]";
	}

}
