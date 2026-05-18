package com.buyani.buyaniserver.entity;

import java.util.Date;

public interface IEntity {

	public void setWhoAdded(Integer whoAdded);

	public Integer getWhoAdded();

	public void setWhenAdded(Date date);

	public Date getWhenAdded();

	public void setWhoUpdated(Integer whoUpdated);

	public Integer getWhoUpdated();

	public void setTs(Date date);

	public Date getTs();
}