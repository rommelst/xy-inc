package br.com.zup.xyinc.controller.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private final Integer status;
	private final String msg;
	private final Long timeStamp;
	
	public StandardError(Integer status, String msg, Long timeStamp) {
		super();
		this.status = status;
		this.msg = msg;
		this.timeStamp = timeStamp;
	}

	public Integer getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	@JsonFormat(pattern="dd/MM/yyyy HH:mm")
	public Long getTimeStamp() {
		return timeStamp;
	}

}
