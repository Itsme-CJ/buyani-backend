package com.bayani.bayaniserver.mapper;

import org.dozer.CustomFieldMapper;
import org.dozer.classmap.ClassMap;
import org.dozer.fieldmap.FieldMap;
import org.hibernate.collection.internal.AbstractPersistentCollection;


public class HibernateFieldMapper implements CustomFieldMapper {

  @Override
  public boolean mapField(Object source, Object destination, Object sourceFieldValue, ClassMap classMap, 
    FieldMap fieldMapping) {
		// Check if field is a Hibernate collection proxy
		if (!(sourceFieldValue instanceof AbstractPersistentCollection)) {
			// Allow dozer to map as normal
			return false;
		}

    // Check if field is already initialized
		if (((AbstractPersistentCollection) sourceFieldValue).wasInitialized()) {
			// Allow dozer to map as normal
			return false;
		}

    // Set destination to null, and tell dozer that the field is mapped
		destination = null;
		return true;
  }
  
}
