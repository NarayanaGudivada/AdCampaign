package org.example.ws.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Advertising {

	private BigInteger partnerId;

	private long duration;

	private String adContent;

	private LocalDateTime createdDateAndTime = LocalDateTime.now();

	private String expiresIn;
	
	private String statusMessage;
	
	private HttpStatus statusCode;

	public Advertising() {
 
	}

	public BigInteger getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(BigInteger partnerId) {
		this.partnerId = partnerId;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getAdContent() {
		return adContent;
	}

	public void setAdContent(String adContent) {
		this.adContent = adContent;
	}

	@JsonIgnore
	public String getCreatedDateAndTime() {
		return createdDateAndTime.toString();
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getExpiresIn() {
		if (createdDateAndTime.plusSeconds(duration).isBefore(LocalDateTime.now())) {
			this.expiresIn = "Already Expired";
		} else {
			this.expiresIn = ChronoUnit.SECONDS.between(LocalDateTime.now(), createdDateAndTime.plusSeconds(duration))
					+ " Seconds";
		}
		return this.partnerId == null ? null : expiresIn;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

}
