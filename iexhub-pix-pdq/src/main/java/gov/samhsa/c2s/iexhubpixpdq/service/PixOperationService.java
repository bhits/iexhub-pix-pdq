package gov.samhsa.c2s.iexhubpixpdq.service;


import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.PatientIdentifierDto;

public interface PixOperationService {

    PatientIdentifierDto queryForEnterpriseId(String patientId, String patientMrnOid);
    String registerPerson(FhirPatientDto fhirPatientDto);
    String editPerson(FhirPatientDto fhirPatientDto);
    String searchPatientByMrn(String identifier);
}

