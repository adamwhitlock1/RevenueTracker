package com.managed.revenuetracker.to;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Revenue implements Parcelable {

	private int id;
	private String platform;
	private Date date;
	private double amt;

	public Revenue() {
		super();
	}

	private Revenue(Parcel in) {
		super();
		this.id = in.readInt();
		this.platform = in.readString();
		this.date = new Date(in.readLong());
		this.amt = in.readDouble();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	@Override
	public String toString() {
		return "Revenue [id=" + id + ", platform=" + platform + ", date="
				+ date + ", amount=" + amt + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Revenue other = (Revenue) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(getId());
		parcel.writeString(getPlatform());
		parcel.writeLong(getDate().getTime());
		parcel.writeDouble(getAmt());
	}

	public static final Creator<Revenue> CREATOR = new Creator<Revenue>() {
		public Revenue createFromParcel(Parcel in) {
			return new Revenue(in);
		}

		public Revenue[] newArray(int size) {
			return new Revenue[size];
		}
	};

}
