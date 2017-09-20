package gov.samhsa.c2s.iexhubpixpdq.service;


import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import gov.samhsa.c2s.pixclient.client.PixManagerClientService;

public interface PixOperationService extends PixManagerClientService {

    String queryForEnterpriseId(String patientId, String patientMrnOid);
    String registerPerson(FhirPatientDto fhirPatientDto);
    String editPerson(FhirPatientDto fhirPatientDto);
}

