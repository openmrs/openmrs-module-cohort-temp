/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cohort;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.EncounterProvider;
import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.annotation.DisableHandlers;
import org.openmrs.api.context.Context;
import org.openmrs.api.handler.VoidHandler;

@Entity
@Table(name = "cohort_encounter")
public class CohortEncounter extends BaseOpenmrsData {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_id")
	private Integer encounterId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_id", insertable = false, updatable = false)
	private CohortM cohort;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "encounter_type_id")
	private EncounterType encounterType;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private Location location;
	
	@Column(name = "encounter_datetime")
	private Date encounterDatetime;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "form_id")
	private Form form;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_visit_id")
	private CohortVisit visit;
	
	@OneToMany(mappedBy = "encounter")
	private Set<CohortObs> obs;
	
	@OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL)
	@OrderBy("provider_id")
	@DisableHandlers(handlerTypes = { VoidHandler.class })
	private Set<EncounterProvider> encounterProviders = new LinkedHashSet<>();
	
	public Integer getEncounterId() {
		return encounterId;
	}
	
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	
	public CohortM getCohort() {
		return cohort;
	}
	
	public void setCohort(CohortM cohort) {
		this.cohort = cohort;
	}
	
	public EncounterType getEncounterType() {
		return encounterType;
	}
	
	public void setEncounterType(EncounterType encounterType) {
		this.encounterType = encounterType;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Date getEncounterDatetime() {
		return encounterDatetime;
	}
	
	public void setEncounterDatetime(Date encounterDatetime) {
		this.encounterDatetime = encounterDatetime;
	}
	
	public Form getForm() {
		return form;
	}
	
	public void setForm(Form form) {
		this.form = form;
	}
	
	public CohortVisit getVisit() {
		return visit;
	}
	
	public void setVisit(CohortVisit visit) {
		this.visit = visit;
	}
	
	public Set<EncounterProvider> getEncounterProviders() {
		return encounterProviders;
	}
	
	public void setEncounterProviders(Set<EncounterProvider> encounterProviders) {
		this.encounterProviders = encounterProviders;
	}
	
	@Override
	public Integer getId() {
		return getEncounterId();
	}
	
	@Override
	public void setId(Integer id) {
		setEncounterId(id);
	}
	
	public void setProvider(EncounterRole role, Provider provider) {
		boolean hasProvider = false;
		for (Iterator<EncounterProvider> it = encounterProviders.iterator(); it.hasNext();) {
			EncounterProvider encounterProvider = it.next();
			if (encounterProvider.getEncounterRole().equals(role)) {
				if (!encounterProvider.getProvider().equals(provider)) {
					encounterProvider.setVoided(true);
					encounterProvider.setDateVoided(new Date());
					encounterProvider.setVoidedBy(Context.getAuthenticatedUser());
				} else if (!encounterProvider.isVoided()) {
					hasProvider = true;
				}
			}
		}
		
		if (!hasProvider) {
			addProvider(role, provider);
		}
	}
	
	public void addProvider(EncounterRole role, Provider provider) {
		// first, make sure the provider isn't already there
		for (EncounterProvider ep : encounterProviders) {
			if (ep.getEncounterRole().equals(role) && ep.getProvider().equals(provider) && !ep.isVoided()) {
				return;
			}
		}
		
		EncounterProvider encounterProvider = new EncounterProvider();
		//encounterProvider.setEncounter(this);
		encounterProvider.setEncounterRole(role);
		encounterProvider.setProvider(provider);
		encounterProvider.setDateCreated(new Date());
		encounterProvider.setCreator(Context.getAuthenticatedUser());
		encounterProviders.add(encounterProvider);
	}
	
	public Set<CohortObs> getObs() {
		Set<CohortObs> ret = new HashSet<CohortObs>();
		
		if (this.obs != null) {
			for (CohortObs o : this.obs) {
				ret.addAll(getObsLeaves(o));
			}
		}
		return ret;
	}
	
	private List<CohortObs> getObsLeaves(CohortObs obsParent) {
		List<CohortObs> leaves = new ArrayList<>();
		
		if (obsParent.hasGroupMembers()) {
			for (CohortObs child : obsParent.getGroupMembers()) {
				if (!child.getVoided()) {
					if (!child.isObsGrouping()) {
						leaves.add(child);
					} else {
						// recurse if this is a grouping obs
						leaves.addAll(getObsLeaves(child));
					}
				}
			}
		} else if (!obsParent.getVoided()) {
			leaves.add(obsParent);
		}
		
		return leaves;
	}
	
	public Set<CohortObs> getAllObs(boolean includeVoided) {
		if (includeVoided && obs != null) {
			return obs;
		}
		
		Set<CohortObs> ret = new HashSet<CohortObs>();
		
		if (this.obs != null) {
			for (CohortObs o : this.obs) {
				if (includeVoided) {
					ret.add(o);
				} else if (!o.getVoided()) {
					ret.add(o);
				}
			}
		}
		return ret;
	}
	
	public void setObs(Set<CohortObs> obs) {
		this.obs = obs;
	}
	
	public Set<CohortObs> getAllObs() {
		return getAllObs(false);
	}
	
	public Set<CohortObs> getObsAtTopLevel(boolean includeVoided) {
		Set<CohortObs> ret = new HashSet<CohortObs>();
		for (CohortObs o : getAllObs(includeVoided)) {
			if (o.getObsGroup() == null) {
				ret.add(o);
			}
		}
		return ret;
	}
	
	public void addObs(CohortObs observation) {
		if (obs == null) {
			obs = new HashSet<>();
		}
		
		if (observation != null) {
			obs.add(observation);
			
			//Propagate some attributes to the obs and any groupMembers
			
			// a Deque is a two-ended queue, that lets us add to the end, and fetch from the beginning
			Deque<CohortObs> obsToUpdate = new ArrayDeque<CohortObs>();
			obsToUpdate.add(observation);
			
			//prevent infinite recursion if an obs is its own group member
			Set<CohortObs> seenIt = new HashSet<CohortObs>();
			
			while (!obsToUpdate.isEmpty()) {
				CohortObs o = obsToUpdate.removeFirst();
				
				//has this obs already been processed?
				if (o == null || seenIt.contains(o)) {
					continue;
				}
				seenIt.add(o);
				
				o.setEncounterId(this);
				
				//if the attribute was already set, preserve it
				//if not, inherit the values sfrom the encounter
				if (o.getObsDateTime() == null) {
					o.setObsDateTime(getEncounterDatetime());
				}
				
				//propagate attributes to  all group members as well
				if (o.getGroupMembers(true) != null) {
					obsToUpdate.addAll(o.getGroupMembers());
				}
			}
			
		}
	}
	
	/**
	 * Remove the given observation from the list of obs for this Encounter
	 *
	 * @param observation
	 * @should remove obs successfully
	 * @should not throw error when removing null obs from empty set
	 * @should not throw error when removing null obs from non empty set
	 */
	public void removeObs(CohortObs observation) {
		if (obs != null) {
			obs.remove(observation);
		}
	}
	
	@Deprecated
	public Person getProvider() {
		if (encounterProviders == null || encounterProviders.isEmpty()) {
			return null;
		} else {
			for (EncounterProvider encounterProvider : encounterProviders) {
				// Return the first non-voided provider associated with a person in the list
				if (!encounterProvider.isVoided() && encounterProvider.getProvider().getPerson() != null) {
					return encounterProvider.getProvider().getPerson();
				}
			}
		}
		return null;
	}
	
	/**
	 * @param provider The provider to set.
	 * @deprecated use {@link #setProvider(Person)}
	 */
	@Deprecated
	public void setProvider(User provider) {
		setProvider(provider.getPerson());
	}
	
	/**
	 * @param provider The provider to set.
	 * @should set existing provider for unknown role
	 * @deprecated since 1.9, use {@link #setProvider(EncounterRole, Provider)}
	 */
	@Deprecated
	public void setProvider(Person provider) {
		EncounterRole unknownRole = Context.getEncounterService()
		        .getEncounterRoleByUuid(EncounterRole.UNKNOWN_ENCOUNTER_ROLE_UUID);
		if (unknownRole == null) {
			throw new IllegalStateException(
			        "No 'Unknown' encounter role with uuid " + EncounterRole.UNKNOWN_ENCOUNTER_ROLE_UUID + ".");
		}
		Collection<Provider> providers = Context.getProviderService().getProvidersByPerson(provider);
		if (providers == null || providers.isEmpty()) {
			throw new IllegalArgumentException("No provider with personId " + provider.getPersonId());
		}
		setProvider(unknownRole, providers.iterator().next());
	}
}
