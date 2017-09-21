package gov.samhsa.c2s.iexhubpixpdq.service;


import gov.samhsa.c2s.iexhubpixpdq.service.dto.EmpiPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;

public interface PixOperationService {

    EmpiPatientDto queryForEnterpriseId(String patientId, String patientMrnOid);
    String registerPerson(FhirPatientDto fhirPatientDto);
    String editPerson(FhirPatientDto fhirPatientDto);
}

