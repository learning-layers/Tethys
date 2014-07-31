package de.dbis.acis.cloud.i5cloud.jpa;

import java.util.List;


import de.dbis.acis.cloud.i5cloud.entity.Services;

/**
 * JPA-Interface for the Services-entity.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
public interface ServicesJpa {
	
	public boolean save(Services services);
	public boolean delete(Services services);
	public boolean update(Services services);
	
	public List<Services> getAllServices();
	public Services getServiceByName(String name);
	public Services getServiceById(int id);
	
}
