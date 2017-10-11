package gov.samhsa.c2s.iexhubpixpdq.service;


import gov.samhsa.c2s.iexhubpixpdq.service.dto.PatientIdentifierDto;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;

public interface PixOperationService {

    PatientIdentifierDto queryForEnterpriseId(String patientId, String patientMrnOid);
    String registerPatient(FhirPatientDto fhirPatientDto);
    String editPatient(FhirPatientDto fhirPatientDto);
    String searchPatientByMrn(String identifier);
}

