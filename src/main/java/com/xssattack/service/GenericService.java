package com.xssattack.service;

import com.xssattack.beans.GenericRequest;
import com.xssattack.beans.GenericResponse;

public interface GenericService {
	public GenericResponse processRequest(GenericRequest request);
}
