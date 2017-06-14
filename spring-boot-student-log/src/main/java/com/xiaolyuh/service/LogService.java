package com.xiaolyuh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {
	Logger logger = LoggerFactory.getLogger(LogService.class);
	
	public void log() {
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
	}
}
