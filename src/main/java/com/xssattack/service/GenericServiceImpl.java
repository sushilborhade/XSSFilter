package com.xssattack.service;

import com.xssattack.beans.GenericRequest;
import com.xssattack.beans.GenericResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GenericServiceImpl implements GenericService {

	@Override
	public GenericResponse processRequest(GenericRequest request) {

		System.out.println("Generic request "+request);
		GenericResponse bean=new GenericResponse();
		try {
			bean.setMessage("SUCCESS");
			bean.setStatus(1);
			HashMap<String, Object> payload = new HashMap<>();
			payload.put("firstname", "xssattack");
			payload.put("lastname", "borhade");
			bean.setPayload(payload);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
}
