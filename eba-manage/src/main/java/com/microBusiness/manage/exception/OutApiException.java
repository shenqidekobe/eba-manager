package com.microBusiness.manage.exception;

import com.microBusiness.manage.entity.OutApiJsonEntity;

/**
 * 对外接口异常
 */
public class OutApiException extends RuntimeException {

	private static final long serialVersionUID = -177756908920187058L;

	private OutApiJsonEntity entity ;

    public OutApiException() {
    }

    public OutApiException(OutApiJsonEntity entity) {
        this.entity = entity;
    }

    public OutApiJsonEntity getEntity() {
        return entity;
    }

}
