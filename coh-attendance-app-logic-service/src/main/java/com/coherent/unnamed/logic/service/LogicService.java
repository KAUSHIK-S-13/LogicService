package com.coherent.unnamed.logic.service;

import com.coherent.unnamed.logic.Response.BaseResponse;
import com.coherent.unnamed.logic.dto.AttendenceDTO;
import com.coherent.unnamed.logic.dto.DetailsDTO;
import com.coherent.unnamed.logic.dto.TimeLogsDTO;

import java.util.List;

public interface LogicService {


	String saveTimeLogs(TimeLogsDTO timeLogsDTO);

	List<AttendenceDTO> listByDayMonth(int year,int month);

	String verifyLocation(String longitude, String latitude);

	DetailsDTO listByDate(int date);

	void calculatehours();

}
