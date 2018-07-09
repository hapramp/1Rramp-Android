package hapramp.walletinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConversionModel {

		@Expose
		@SerializedName("ConversionType")
		private ConversionType ConversionType;
		@Expose
		@SerializedName("FirstValueInArray")
		private boolean FirstValueInArray;
		@Expose
		@SerializedName("TimeFrom")
		private int TimeFrom;
		@Expose
		@SerializedName("TimeTo")
		private int TimeTo;
		@Expose
		@SerializedName("Data")
		private List<Data> Data;
		@Expose
		@SerializedName("Aggregated")
		private boolean Aggregated;
		@Expose
		@SerializedName("Type")
		private int Type;
		@Expose
		@SerializedName("Response")
		private String Response;

		public static class ConversionType {
				@Expose
				@SerializedName("conversionSymbol")
				private String conversionSymbol;
				@Expose
				@SerializedName("type")
				private String type;

				public String getConversionSymbol() {
						return conversionSymbol;
				}

				public String getType() {
						return type;
				}
		}

		public static class Data {
				@Expose
				@SerializedName("volumeto")
				private double volumeto;
				@Expose
				@SerializedName("volumefrom")
				private double volumefrom;
				@Expose
				@SerializedName("open")
				private double open;
				@Expose
				@SerializedName("low")
				private double low;
				@Expose
				@SerializedName("high")
				private double high;
				@Expose
				@SerializedName("close")
				private double close;
				@Expose
				@SerializedName("time")
				private int time;

				public double getVolumeto() {
						return volumeto;
				}

				public double getVolumefrom() {
						return volumefrom;
				}

				public double getOpen() {
						return open;
				}

				public double getLow() {
						return low;
				}

				public double getHigh() {
						return high;
				}

				public double getClose() {
						return close;
				}

				public int getTime() {
						return time;
				}
		}

		public ConversionModel.ConversionType getConversionType() {
				return ConversionType;
		}

		public boolean isFirstValueInArray() {
				return FirstValueInArray;
		}

		public int getTimeFrom() {
				return TimeFrom;
		}

		public int getTimeTo() {
				return TimeTo;
		}

		public List<ConversionModel.Data> getData() {
				return Data;
		}

		public boolean isAggregated() {
				return Aggregated;
		}

		public int getType() {
				return Type;
		}

		public String getResponse() {
				return Response;
		}
}
