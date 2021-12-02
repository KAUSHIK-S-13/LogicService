package com.coherent.unnamed.logic.controller;

import com.coherent.unnamed.logic.Response.BaseResponse;
import com.coherent.unnamed.logic.dto.AttendenceDTO;
import com.coherent.unnamed.logic.dto.DetailsDTO;
import com.coherent.unnamed.logic.dto.TimeLogsDTO;
import com.coherent.unnamed.logic.service.LogicService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*@EnableScheduling*/
@RestController
@RequestMapping(value ="/unnamed-logic-service/data")
public class LogicController {

	@Autowired
	private LogicService logicService;


	@PostMapping(value ="/registerattendance")
	@ApiOperation(value = "Attendance punch", authorizations = {
			@Authorization(value = "Bearer")})
	public BaseResponse saveTimeLogs(@RequestBody TimeLogsDTO timeLogsDTO){
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setData(logicService.saveTimeLogs(timeLogsDTO));
		return baseResponse;
	}

	@PostMapping(value = "/verifylocation")
	@ApiOperation(value = "location verify", authorizations = {
			@Authorization(value = "Bearer")})
	public BaseResponse verifyLocation(@RequestParam String longitude, @RequestParam String latitude){
		String response=logicService.verifyLocation(longitude,latitude);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setData(response);
		return baseResponse;
	}


	@GetMapping(value = "/listbydaysbymonth")
	@ApiOperation(value = "month", authorizations = {
			@Authorization(value = "Bearer")})
	public BaseResponse<List<AttendenceDTO>> listByDayMonth(@RequestParam int year,@RequestParam int month) {
		BaseResponse<List<AttendenceDTO>> baseResponse = null;
		baseResponse = BaseResponse.<List<AttendenceDTO>>builder().Data(logicService.listByDayMonth(year, month)).build();
		return baseResponse;
	}

	@GetMapping(value = "/listbydate")
	@ApiOperation(value = "date", authorizations = {
			@Authorization(value = "Bearer")})
	public BaseResponse<DetailsDTO> listByDate(@RequestParam int date) {
		BaseResponse<DetailsDTO> baseResponse = null;
		baseResponse = BaseResponse.<DetailsDTO>builder().Data(logicService.listByDate(date)).build();
		return baseResponse;
	}

	@Scheduled(fixedDelay = 5000)
	public void calculatehours(){
		logicService.calculatehours();
	}




}
