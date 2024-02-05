import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BerthingDetails extends Vessel {

	private LocalDateTime btrDate;
	private String berthN;
	private String status;

	public BerthingDetails(String lName, String sName, String inVoyN, String outVoyN, String btrDate, String berthN,
			String status) {
		super(lName, sName, inVoyN, outVoyN);
		this.btrDate = LocalDateTime.parse(btrDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		this.berthN = berthN;
		this.status = status;
	}

	public LocalDateTime getBtrDate() {
		return btrDate;
	}

	public void setBtrDate(LocalDateTime btrDate) {
		this.btrDate = btrDate;
	}

	public String getBerthNo() {
		return berthN;
	}

	public void setBerthN(String berthN) {
		this.berthN = berthN;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BerthingDetails [" + super.toString() + ", berthN=" + berthN + ", btrDate=" + btrDate + ", status="
				+ status + "]";
	}

}