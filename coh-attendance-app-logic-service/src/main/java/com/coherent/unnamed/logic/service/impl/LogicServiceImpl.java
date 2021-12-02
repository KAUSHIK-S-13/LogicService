package com.coherent.unnamed.logic.service.impl;

import com.coherent.unnamed.logic.Config.UserContextHolder;
import com.coherent.unnamed.logic.Constants.Constants;
import com.coherent.unnamed.logic.Exception.CustomException;
import com.coherent.unnamed.logic.Response.BaseResponse;
import com.coherent.unnamed.logic.dto.*;
import com.coherent.unnamed.logic.model.Attendance;
import com.coherent.unnamed.logic.model.TimeLogs;
import com.coherent.unnamed.logic.model.Users;
import com.coherent.unnamed.logic.repository.AttendanceRepository;
import com.coherent.unnamed.logic.repository.TimeLogsRepository;
import com.coherent.unnamed.logic.repository.UsersRepository;
import com.coherent.unnamed.logic.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LogicServiceImpl implements LogicService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private TimeLogsRepository timeLogsRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Override
	public void calculatehours(){
		ArrayList<Long> hours = new ArrayList<>();
		List<Users> users = usersRepository.findAll();
		final long[] h = {0};
		users.stream().forEachOrdered(users1 -> {
			Date date  = new Date(System.currentTimeMillis()-24*60*60*1000);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String strDate= formatter.format(date);
			List<TimeLogs> timeLogs =  timeLogsRepository.findByUserId(strDate ,users1.getId());
			ArrayList<Long> punchin = new ArrayList<>();
			ArrayList<Long> punchout = new ArrayList<>();
			final int[] g = {0};
			timeLogs.stream().forEachOrdered(timeLogs1 -> {
				g[0] = timeLogs1.getIsLogged();
			});
			if(g[0]==1){
				Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
				TimeLogs timeLogs1 = new TimeLogs();
				timeLogs1.setCreatedAt(timeStamp);
				timeLogs1.setModifiedAt(timeStamp);
				timeLogs1.setUsers(users1);
				timeLogs1.setLongitude("123.4");
				timeLogs1.setLatitude("123.4");
				timeLogs1.setCreatedBy("SYSTEM");
				timeLogs1.setModifiedBy("SYSTEM");
				timeLogs1.setIsLogged(0);
				timeLogsRepository.save(timeLogs1);
			}

			long time1 = timeLogs.stream().filter(timeLogs1 -> timeLogs1.getIsLogged() == 1).mapToLong(timeLogs1 ->
			{
				punchin.add((long) timeLogs1.getCreatedAt().toLocalDateTime().toLocalTime().getHour());
				return timeLogs1.getCreatedAt().toLocalDateTime().toLocalTime().getHour();
			}).sum();
			long time2 = timeLogs.stream().filter(timeLogs1 -> timeLogs1.getIsLogged() == 0).mapToLong(timeLogs1 -> {
				if(timeLogs1.getCreatedBy().equals("SYSTEM")){
					punchout.add(0L);
					return 0;
				}else{
					punchout.add((long) timeLogs1.getCreatedAt().toLocalDateTime().toLocalTime().getHour());
					return timeLogs1.getCreatedAt().toLocalDateTime().toLocalTime().getHour();
				}
			}).sum();

			long c = 0;
			for(int k = 0; k < punchout.size(); k++) {
				if(punchout.get(k)==0L)
				{
					c = c;
				}else{
					c = c + punchout.get(k) - punchin.get(k);
				}

			}
			hours.add(c);

			System.out.println(hours.get((int) h[0]));

			Attendance attendence2 = new Attendance();
			Timestamp timeStamp = new Timestamp(System.currentTimeMillis()-24*60*60*1000);
			attendence2.setCreatedAt(timeStamp);
			attendence2.setModifiedAt(timeStamp);


			attendence2.setHours(hours.get((int) h[0]));

			if(attendence2.getHours()>6){
				attendence2.setIsPresent(Constants.IS_PRESENT);
			}else{
				attendence2.setIsPresent(Constants.IS_PRESENT_NOT);
			}
			attendence2.setUsers(users1);
			attendence2.setCreatedBy("");
			attendence2.setModifiedBy("SYSTEM");
			attendanceRepository.save(attendence2);
			h[0]++;
		});

	}

	@Override
	public String saveTimeLogs(TimeLogsDTO timeLogsDTO) {
		try {
			if (timeLogsDTO != null) {
				Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
				TimeLogs timeLogs = new TimeLogs();
				UserContextDTO userContextDTO = UserContextHolder.getUserDto();
				Optional<Users> users = usersRepository.findById(userContextDTO.getId());
				if (users.isPresent()) {
					timeLogs.setUsers(users.get());
				} else {
					throw new CustomException(Constants.ERROR_CODE, Constants.ERROR);
				}
				timeLogs.setLongitude(timeLogsDTO.getLongitude());
				timeLogs.setLatitude(timeLogsDTO.getLatitude());
				timeLogs.setIsLogged(timeLogsDTO.getIsLogged());
				timeLogs.setActive(true);
				timeLogs.setDeletedFlag(false);
				timeLogs.setCreatedAt(timeStamp);
				timeLogs.setCreatedBy("USER");
				timeLogs.setModifiedAt(timeStamp);
				timeLogs.setModifiedBy("USER");
				timeLogsRepository.save(timeLogs);
				return Constants.SUCCESS;
			} else {
				throw new CustomException(Constants.ERROR_CODE, Constants.ERROR);
			}
		} catch (CustomException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public String verifyLocation(String longitude, String latitude) {
		if (Objects.equals(longitude, Constants.LONGITUDE) && Objects.equals(latitude, Constants.LATITUDE)) {
			return Constants.IN_RANGE;
		} else {
			throw new CustomException(Constants.ERROR_CODE, Constants.NOT_IN_RANGE);
		}
	}

	@Override
	public DetailsDTO listByDate(int date) {
		List<LoggedDetailsDTO> loggedDetailsDTOList=new ArrayList<>();
		UserContextDTO userContextDTO = UserContextHolder.getUserDto();
		Attendance attendances = attendanceRepository.findByCreatedAtDate(date, userContextDTO.getId());
		DetailsDTO detailsDTO = new DetailsDTO();
		detailsDTO.setDate(date);
		detailsDTO.setHours(attendances.getHours());
		List<TimeLogs> timeLogs = timeLogsRepository.findAllByCreatedAtDate(date, userContextDTO.getId());
		timeLogs.forEach(data -> {
			LoggedDetailsDTO loggedDetailsDTO=new LoggedDetailsDTO();
			loggedDetailsDTO.setCreatedAt(data.getCreatedAt());
			loggedDetailsDTO.setCreatedBy(data.getCreatedBy());
			loggedDetailsDTO.setIsLogged(data.getIsLogged());
			loggedDetailsDTOList.add(loggedDetailsDTO);
		});
		detailsDTO.setLogs(loggedDetailsDTOList);
		return detailsDTO;
	}


	@Override
	public List<AttendenceDTO> listByDayMonth(int year, int month) {
		List<AttendenceDTO> attendenceDTOList = new ArrayList<>();
		List<Attendance> attendances = attendanceRepository.findAllByCreatedAt(year, month);
		attendances.forEach(data -> {
			AttendenceDTO attendenceDTO = new AttendenceDTO();
			attendenceDTO.setCreatedAt(data.getCreatedAt());
			attendenceDTO.setIsPresent(data.getIsPresent());
			attendenceDTOList.add(attendenceDTO);
		});
		return attendenceDTOList;
	}

}






