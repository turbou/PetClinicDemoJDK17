package org.springframework.samples.petclinic.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OwnerRepositoryCustom {

	Page<Owner> findByLastNameManual(String lastName, Pageable pageable);

}
